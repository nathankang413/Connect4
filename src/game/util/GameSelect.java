package game.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameSelect extends JFrame {

    public GameSelect() {
        super("Game Select");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // TODO: remove magic numbers
        setLayout(new GridLayout(3, 3));

        // add buttons
        String[] playerTypes = new String[] {"Human", "AI", "QLearner"}; // TODO: remove magic array
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                JButton button = new JButton(playerTypes[i] + " vs " + playerTypes[j]);
                ActionListener actionListener = event -> {
                    // TODO: BUTTON LOGIC
                    String str = event.getActionCommand();
                    System.out.println("Clicked = " + str);
                };
                button.addActionListener(actionListener);
                add(button);
            }
        }

        // TODO: remove magic numbers
        setPreferredSize(new Dimension(500, 300));

        pack();
        setVisible(true);
    }

}
