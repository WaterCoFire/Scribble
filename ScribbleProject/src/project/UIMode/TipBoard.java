package project.UIMode;

import project.GameData.Data;

import javax.swing.*;

/**
 * Manages the tip bar in the game frame. Used only in UI mode.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class TipBoard extends JPanel {
    // Set the default text of the tip bar.
    public static JLabel gameTip = new JLabel("It is now "
            + Data.playerList.get(0).getName()
            + "'s turn. Use Arrow Key to select input direction.");
}
