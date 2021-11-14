package game;

import acm.graphics.GObject;
import acm.program.GraphicsProgram;
import acm.program.ProgramMenuBar;
import game.players.HumanPlayer;
import game.util.*;
import game.util.Button;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;

import static game.Constants.GUI.*;
import static game.Constants.Game.*;

public class ConnectDisplay extends GraphicsProgram implements MouseListener, ActionListener {
    private static ConnectDisplay instance;
    private Column[] frame;
    private Slot[][] positions;
    private TextDisplay title;
    private PercentBar[] winRateDisplays;
    private ConnectGame game;
    private final Timer aiTimer;
    private GameType gameType;
    private boolean showDatabase;
    private boolean autoReset;

    private ConnectDisplay() {
        addMouseListeners();
        gameType = new GameType(0, 0);
        aiTimer = new Timer(AI_DELAY, this);
        showDatabase = false;
    }

    public static ConnectDisplay getInstance() {
        if (instance == null) {
            instance = new ConnectDisplay();
        }
        return instance;
    }

    /**
     * Initiates Connect4 display and game
     */
    @Override
    public void run() {

        initMenuBar();

        this.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        pause(WINDOW_RESIZE_WAIT);

        title = new TextDisplay("Connect 4", Color.BLUE);
        add(title);

        // set up frame and positions for display
        frame = new Column[COLS];
        for (int j = 0; j < COLS; j++) {
            frame[j] = new Column(j * SPACING, TEXT_MARGIN);
            add(frame[j]);
        }

        positions = new Slot[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j] = new Slot(j * SPACING + POS_MARGIN, i * SPACING + POS_MARGIN + TEXT_MARGIN, EMPTY);
                add(positions[i][j]);
            }
        }

        add(new PlayButton(BOARD_WIDTH + BUTTON_PADDING, TEXT_MARGIN));

        // win rate bars underneath the buttons
        winRateDisplays = new PercentBar[7];
        for (int i=0; i<COLS; i++) {
            winRateDisplays[i] = new PercentBar(BOARD_WIDTH + BUTTON_PADDING,
                    TEXT_MARGIN + BUTTON_HEIGHT + BUTTON_PADDING + i * (PERCENT_BAR_HEIGHT + PERCENT_BAR_PADDING));
            add(winRateDisplays[i]);
        }

        // TODO: back button for database reader

    }

    private void initMenuBar() {
        ProgramMenuBar menuBar = getMenuBar();
        JMenu menu = new JMenu("Game Settings");
        menuBar.add(menu);

        // AI Delay button
        JCheckBoxMenuItem delayButton = new JCheckBoxMenuItem("AI Delay");
        delayButton.setSelected(true);
        delayButton.addActionListener(e -> aiTimer.setDelay(AI_DELAY - aiTimer.getDelay()));
        menu.add(delayButton);

        // Use Database button
        JCheckBoxMenuItem databaseButton = new JCheckBoxMenuItem("Use Database");
        databaseButton.addActionListener(e -> showDatabase = !showDatabase);
        menu.add(databaseButton);

        // Auto Reset button
        JCheckBoxMenuItem resetButton = new JCheckBoxMenuItem("Automatic Reset");
        resetButton.addActionListener(e -> autoReset = !autoReset);
        menu.add(resetButton);

        // Game types buttons
        menu.addSeparator();
        ButtonGroup gameTypes = new ButtonGroup();

        Map<String, GameType> options = new LinkedHashMap<>();
        options.put("2 Players", new GameType(GameType.HUMAN, GameType.HUMAN));
        options.put("1 Player (vs Algo)", new GameType(GameType.HUMAN, GameType.ALGORITHM));
        options.put("1 Player (vs QLearn)", new GameType(GameType.HUMAN, GameType.Q_LEARN));
        options.put("Algo v Algo", new GameType(GameType.ALGORITHM, GameType.ALGORITHM));
        options.put("Algo v QLearn", new GameType(GameType.ALGORITHM, GameType.Q_LEARN));
        options.put("QLearn v QLearn", new GameType(GameType.Q_LEARN, GameType.Q_LEARN));

        boolean first = true;
        for (Map.Entry<String, GameType> entry : options.entrySet()) {
            JRadioButtonMenuItem button = new JRadioButtonMenuItem(entry.getKey());
            if (first) {
                button.setSelected(true);
                first = false;
            }

            button.addActionListener(e -> gameType = entry.getValue());
            gameTypes.add(button);
            menu.add(button);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (game != null && game.checkWin() == EMPTY) {
            // determine the chosen column
            int col = -1;
            for (int j = 0; j < COLS; j++) {
                if (frame[j].contains(mouseEvent.getX(), mouseEvent.getY())) {
                    col = j;
                }
            }

            if (col == -1) return; // if click not on board

            // drop the piece
            game.runHumanTurn(col);

            // update screen
            updatePlayerText();
            updateScreen();

            // handle win
            if (game.checkWin() != EMPTY) {
                handleWin();
            }

            // reset ai timer
            aiTimer.restart();
        }
    }

    /**
     * Runs on AITimer
     * @param e AITimer ends? TODO: docs
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        runAI();
    }

    private void runAI() {
        if (!(game.currentPlayer() instanceof HumanPlayer)) {
            if (game.checkWin() == EMPTY) {
                game.runAITurn();
                updateScreen();
                updatePlayerText();

                if (game.checkWin() != EMPTY) {
                    handleWin();
                }
            }
        }
    }

    private void handleWin() {
        if (game.checkWin() == EMPTY) throw new RuntimeException("handleWin was called when no player has won yet.");

        aiTimer.stop();
        updateWinText();
        game.updateHistory();

        if (autoReset) {
            game = new ConnectGame(gameType.getPlayers());
            updateScreen();
            updatePlayerText();
            aiTimer.start();
        }
    }

    private void updateScreen() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j].changeColor(game.getBoard()[i][j]);
            }
        }

        double[][] winRates = game.getWinRates();
        for (int i=0; i<COLS; i++) {
            if (showDatabase) {
                winRateDisplays[i].update(winRates[i][0], (int) winRates[i][1]);
            } else {
                winRateDisplays[i].update(-1, 0);
            }
        }
    }

    private void updatePlayerText() {
        title.setLabel("Player " + (game.currentPlayerNum() + 1) + "'s Turn");
        switch (game.currentPlayerNum()) {
            case PLAYER_1 -> title.setColor(Color.YELLOW);
            case PLAYER_2 -> title.setColor(Color.RED);
        }
    }

    private void updateWinText() {
        // TODO: some way to show the type of player than won (human, algo, qlearn)

        title.setColor(Color.BLUE);
        if (game.checkWin() % 1 == 0.5) {
            title.setLabel("It's a tie!!");
        } else {
            switch ((int) game.checkWin()) {
                case PLAYER_1 -> title.setLabel("Player 1 Wins!");
                case PLAYER_2 -> title.setLabel("Player 2 Wins!");
            }
        }
    }

    private void add(Addable obj) {
        for (GObject component : obj.getComponents()) {
            add(component);
        }
    }

    private class PlayButton extends Button {

        public PlayButton(int x, int y) {
            super(x, y, "Play Game", Color.GREEN);
        }

        protected void buttonAction() {
            game = new ConnectGame(gameType.getPlayers());
            updateScreen();
            updatePlayerText();
            aiTimer.start();
        }
    }
}