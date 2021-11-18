package game;

import acm.graphics.GObject;
import acm.program.GraphicsProgram;
import acm.program.ProgramMenuBar;
import game.players.HumanPlayer;
import game.util.Button;
import game.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;
import java.util.Map;

import static game.Constants.GUI.*;
import static game.Constants.Game.*;

/**
 * A singleton display for a Connect4 Game
 * Includes various options for game play
 * Controls game flow
 */
public class ConnectDisplay extends GraphicsProgram implements MouseListener, ActionListener {
    private static ConnectDisplay instance;
    private final Timer aiTimer;
    private Column[] frame;
    private Slot[][] positions;
    private Text title;
    private PercentBar[] winRateDisplays;
    private ConnectGame game;
    private GameType gameType;
    private boolean showDatabase;
    private boolean autoReset;

    /**
     * Creates a new ConnectDisplay
     * Game type is 2 human players by default
     */
    private ConnectDisplay() {
        addMouseListeners();
        gameType = new GameType(GameType.HUMAN, GameType.HUMAN);
        aiTimer = new Timer(AI_DELAY, this);
        showDatabase = false;
    }

    /**
     * Gets the instance of ConnectDisplay or makes one if it has not been instantiated
     *
     * @return the instance of ConnectDisplay
     */
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

        title = new Text("Connect 4", Color.BLUE);
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
        for (int i = 0; i < COLS; i++) {
            winRateDisplays[i] = new PercentBar(BOARD_WIDTH + BUTTON_PADDING,
                    TEXT_MARGIN + BUTTON_HEIGHT + BUTTON_PADDING + i * (PERCENT_BAR_HEIGHT + PERCENT_BAR_PADDING));
            add(winRateDisplays[i]);
        }
    }

    /**
     * Creates the menu bar with options for different game modes
     */
    private void initMenuBar() {
        ProgramMenuBar menuBar = getMenuBar();
        JMenu gameOptions = new JMenu("Game Options");
        menuBar.add(gameOptions);

        // AI Delay button
        JCheckBoxMenuItem delayButton = new JCheckBoxMenuItem("AI Delay");
        delayButton.setSelected(true);
        delayButton.addActionListener(e -> aiTimer.setDelay(AI_DELAY - aiTimer.getDelay()));
        gameOptions.add(delayButton);

        // Use Database button
        JCheckBoxMenuItem databaseButton = new JCheckBoxMenuItem("Use Database");
        databaseButton.addActionListener(e -> showDatabase = !showDatabase);
        gameOptions.add(databaseButton);

        // Auto Reset button
        JCheckBoxMenuItem resetButton = new JCheckBoxMenuItem("Automatic Reset");
        resetButton.addActionListener(e -> autoReset = !autoReset);
        gameOptions.add(resetButton);

        // Game types buttons
        gameOptions.addSeparator();
        ButtonGroup gameTypes = new ButtonGroup();

        Map<String, GameType> options = new LinkedHashMap<>();
        options.put("2 Players", new GameType(GameType.HUMAN, GameType.HUMAN));
        options.put("1 Player (vs Algo)", new GameType(GameType.HUMAN, GameType.ALGORITHM));
        options.put("1 Player (vs QLearn)", new GameType(GameType.HUMAN, GameType.Q_LEARN));
        options.put("Algo v Algo", new GameType(GameType.ALGORITHM, GameType.ALGORITHM));
        options.put("Algo v QLearn", new GameType(GameType.ALGORITHM, GameType.Q_LEARN));
        options.put("QLearn v QLearn", new GameType(GameType.Q_LEARN, GameType.Q_LEARN));
        options.put("Exploratory Training", new GameType(GameType.Q_LEARN_NEW, GameType.Q_LEARN_NEW));
        options.put("Random Training", new GameType(GameType.Q_LEARN_RAND, GameType.Q_LEARN_RAND));

        boolean first = true;
        for (Map.Entry<String, GameType> entry : options.entrySet()) {
            JRadioButtonMenuItem button = new JRadioButtonMenuItem(entry.getKey());
            if (first) {
                button.setSelected(true);
                first = false;
            }

            button.addActionListener(e -> gameType = entry.getValue());
            gameTypes.add(button);
            gameOptions.add(button);
        }

        // QLearn file menu
        JMenu qLearnMenu = new JMenu("Q-Learn Options");
        menuBar.add(qLearnMenu);
        JMenuItem chooseFile = new JMenuItem("Select Qualities File");
        qLearnMenu.add(chooseFile);
        chooseFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                DatabaseIO.setQualitiesFile(fileChooser.getSelectedFile());
            }
        });
    }

    /**
     * Handles mouse clicks
     * Drops a piece in the current column on click
     *
     * @param mouseEvent the mouse click
     */
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
     * Runs the AI loop when AITimer expires
     *
     * @param e the AITimer event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        runAI();
    }

    /**
     * Runs the AI loop depending on the number of AIPLayers in the game
     */
    private void runAI() {
        if (!(game.getCurrentPlayer() instanceof HumanPlayer)) {
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

    /**
     * Ends the game and changes the display to match the winner
     */
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

    /**
     * Updates the screen to match the current board state
     */
    private void updateScreen() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                positions[i][j].updateColor(game.getBoard()[i][j]);
            }
        }

        double[][] winRates = game.getWinRates();
        for (int i = 0; i < COLS; i++) {
            if (showDatabase) {
                winRateDisplays[i].update(winRates[i][0], (int) winRates[i][1]);
            } else {
                winRateDisplays[i].update(-1, 0);
            }
        }
    }

    /**
     * Updates the title to match the current player.
     */
    private void updatePlayerText() {
        title.setLabel("Player " + (game.currentPlayerNum() + 1) + "'s Turn");
        switch (game.currentPlayerNum()) {
            case PLAYER_1 -> title.setColor(Color.YELLOW);
            case PLAYER_2 -> title.setColor(Color.RED);
        }
    }

    /**
     * Updates the title to match the winning player
     */
    private void updateWinText() {
        title.setColor(Color.BLUE);
        if (game.checkWin() % 1 == 0.5) {
            title.setLabel("It's a tie!!");
        } else {
            String playerType = game.getOtherPlayer().getClass().toString().split("\\.")[2];

            switch ((int) game.checkWin()) {
                case PLAYER_1 -> title.setLabel(String.format("Player 1 Wins! (%s)", playerType.charAt(0)));
                case PLAYER_2 -> title.setLabel(String.format("Player 2 Wins! (%s)", playerType.charAt(0)));
            }
        }
    }

    /**
     * Adds the components of an Addable to the display
     *
     * @param obj an Addable display element
     */
    private void add(Addable obj) {
        for (GObject component : obj.getComponents()) {
            add(component);
        }
    }

    /**
     * A Button to start a new game with the current settings
     */
    private class PlayButton extends Button {
        /**
         * Creates a new PlayButton at the given location
         *
         * @param x the x coordinate of the PlayButton
         * @param y the y coordinate of the PlayButton
         */
        public PlayButton(int x, int y) {
            super(x, y, "Play Game", Color.GREEN);
        }

        /**
         * Initiates a new game when the button is pressed
         */
        protected void buttonAction() {
            game = new ConnectGame(gameType.getPlayers());
            updateScreen();
            updatePlayerText();
            aiTimer.start();
        }
    }
}