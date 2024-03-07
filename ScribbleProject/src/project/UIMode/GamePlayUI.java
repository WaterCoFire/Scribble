package project.UIMode;

import project.GameData.Data;
import project.Main;

import javax.swing.*;
import java.awt.*;
/**
 * Manages the user interface of the game in UI mode.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class GamePlayUI {
    // The size of the frame.
    private final Dimension size = new Dimension(740, 580);

    // The following are the components of the frame.
    private final JFrame gameFrame = new JFrame("Scribble Game");
    private final JMenuBar gameMenuBar = new JMenuBar();
    private final JMenu gameMenuQuit = new JMenu("Quit");
    private final JMenuItem gameMenuGameEnd = new JMenuItem("Quit Game");
    private final JMenu gameMenuRules = new JMenu("Rules");
    private final JMenuItem gameMenuViewRules = new JMenuItem("View Rules");
    private final Chessboard gameChessboard = new Chessboard();
    private final Box gameRightBox = Box.createVerticalBox();

    /**
     * Initializes the user interface of the game. Assembles all the components.
     *
     * @param ifNewGame to start a new game or not:<br>
     *                  <code>true</code> - start a new game<br>
     *                  <code>false</code> - continue the saved game
     * @see Chessboard
     * @see ScoreBoard
     * @see TipBoard
     */
    public void init(boolean ifNewGame) {
        // Set the attributes of the frame.
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setFocusable(true);
        gameFrame.setResizable(false);
        gameFrame.setSize(size.width, size.height);

        // The top menu bar.
        gameMenuBar.add(gameMenuQuit);
        gameMenuBar.add(gameMenuRules);
        gameMenuQuit.add(gameMenuGameEnd);
        gameMenuRules.add(gameMenuViewRules);
        gameFrame.add(gameMenuBar, BorderLayout.NORTH);
        gameRightBox.add(Box.createVerticalStrut(20));

        // Set action listeners for the buttons:
        // The End Game button: Close the game and remind the user.
        gameMenuGameEnd.addActionListener(actionEvent -> {
            JOptionPane.showMessageDialog(null,
                    "Game Over. Please proceed in the console!",
                    "End Game",
                    JOptionPane.PLAIN_MESSAGE);
            gameFrame.setVisible(false);
            Main.endGame(false);
        });

        // The Rules button: Show the user the rules of Scribble.
        gameMenuViewRules.addActionListener(actionEvent -> Data.showRules());

        // Add the chessboard to the left side of the frame.
        gameChessboard.init();
        gameFrame.add(gameChessboard);

        // Add the score board to the right side of the frame.
        ScoreBoard.assemble(ifNewGame);
        gameFrame.add(ScoreBoard.scoreBox, BorderLayout.EAST);

        // Add the tip bar to the bottom of the frame.
        TipBoard.gameTip.setFont(new Font("Arial", Font.PLAIN, 17));
        TipBoard.gameTip.setHorizontalAlignment(SwingConstants.CENTER);
        TipBoard.gameTip.setForeground(Color.BLUE);
        gameFrame.add(TipBoard.gameTip, BorderLayout.SOUTH);

        // If the frame is initialized for continuing the saved game:
        // Update the tip bar to remind the current player at turn.
        if (!ifNewGame) {
            // Check if it is the first input.
            if (Data.ifFirstTime) {
                // If so, remind the first player to use Arrow Key to select direction.
                TipBoard.gameTip.setText("It is now "
                        + Data.playerList.get(0).getName()
                        + "'s turn. Use Arrow Key to select input direction.");

                // Highlight the center point on the chessboard.
                Data.setSelectedTileColor(8,8);
            } else {
                // If not, remind the current player to use Left Click to select the target point.
                TipBoard.gameTip.setText("It is now "
                        + Data.playerList.get(Data.currentPlayer - 1).getName()
                        + "'s turn. Use Left Click to select the target point.");
            }
        }

        // Make the frame visible.
        gameFrame.setVisible(true);
    }
}
