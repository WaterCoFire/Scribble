package project.UIMode;

import project.GameData.Data;
import project.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Manages the score board and the "Pass" button in the game frame. Used only in UI mode.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class ScoreBoard extends JPanel {

    // The Box to be used in the game frame.
    // It contains all the components declared below.
    public static final Box scoreBox = Box.createVerticalBox();

    // The following are all the necessary components.
    private static final JLabel player1Name = new JLabel();
    private static final JLabel player2Name = new JLabel();
    private static final JLabel player3Name = new JLabel();
    private static final JLabel player4Name = new JLabel();
    private static final JLabel player1Score = new JLabel("0");
    private static final JLabel player2Score = new JLabel("0");
    private static final JLabel player3Score = new JLabel("0");
    private static final JLabel player4Score = new JLabel("0");
    private static final JLabel playerTitle = new JLabel("Player");
    private static final JLabel scoreTitle = new JLabel("Score");
    private static final Font contentFont = new Font("Arial", Font.PLAIN, 15);
    private static final Font titleFont = new Font("Arial", Font.PLAIN, 20);
    private static final ImageIcon legendPicture = new ImageIcon("Legend.jpg");
    private static final JLabel legend = new JLabel(legendPicture);
    private static final JButton passTurnButton = new JButton("Pass");

    /**
     * Assembles all the components of the score board.
     *
     * @param ifNewGame if the game frame is assembled for a new game or not:<br>
     *                  <code>true</code> - for a new game<br>
     *                  <code>false</code> - for continuing the saved game
     * @see Data
     */
    public static void assemble(boolean ifNewGame) {
        // Set the default text colors.
        player1Name.setForeground(Color.BLACK);
        player2Name.setForeground(Color.BLACK);
        player3Name.setForeground(Color.BLACK);
        player4Name.setForeground(Color.BLACK);

        player1Score.setForeground(Color.BLACK);
        player2Score.setForeground(Color.BLACK);
        player3Score.setForeground(Color.BLACK);
        player4Score.setForeground(Color.BLACK);

        playerTitle.setForeground(Color.BLACK);
        scoreTitle.setForeground(Color.BLACK);

        // Set the players' names.
        player1Name.setText(Data.playerList.get(0).getName());
        player2Name.setText(Data.playerList.get(1).getName());
        if (Data.getPlayerNum() >= 3) {
            player3Name.setText(Data.playerList.get(2).getName());
            if (Data.getPlayerNum() == 4)
                player4Name.setText(Data.playerList.get(3).getName());
        }

        // Set the default fonts.
        player1Name.setFont(contentFont);
        player2Name.setFont(contentFont);
        player3Name.setFont(contentFont);
        player4Name.setFont(contentFont);

        player1Score.setFont(contentFont);
        player2Score.setFont(contentFont);
        player3Score.setFont(contentFont);
        player4Score.setFont(contentFont);

        playerTitle.setFont(titleFont);
        scoreTitle.setFont(titleFont);

        // Set the action listener for the Pass Turn button:
        passTurnButton.addActionListener(actionEvent -> {
            // Judge if it is the first turn. If so, do not allow the player to pass.
            if (Data.ifFirstTime) {
                TipBoard.gameTip.setText("You are not allowed to pass for it is the first input.");
                return;
            }

            // If the current player has already inputted some unconfirmed letters on the chessboard,
            // Do not allow the user to pass the turn.
            if (!Chessboard.ifChangeableDirection) {
                TipBoard.gameTip.setText("Please delete the letters you input before passing your turn.");
                return;
            }

            // Switch to the next player.
            Data.currentPlayer++;
            if (Data.currentPlayer > Data.getPlayerNum())
                Data.currentPlayer = 1;

            Data.passCount++;
            if (Data.passCount == Data.getPlayerNum()) {
                // If all players have skipped a full turn in a row, end the game.
                System.out.println("Players have skipped a full turn in a row, the game is over!");
                JOptionPane.showMessageDialog(null,
                        "Players have skipped a full turn in a row! Game over!",
                        "End Game",
                        JOptionPane.PLAIN_MESSAGE);
                Main.endGame(true);
            }
            // Refresh the chessboard:
            // Set the default tile background color for all tiles.
            Data.setAllDefaultColor();

            // Display the chessboard in the console.
            Data.displayChessboard();

            // Update the score board.
            ScoreBoard.updateScore();

            // Update the tip bar.
            TipBoard.gameTip.setText("It is now "
                    + Data.playerList.get(Data.currentPlayer - 1).getName()
                    + "'s turn. Use Left Click to select the target point.");

            // Update the information in the console.
            System.out.println(" ");
            System.out.println("The last player passes their turn.");
            System.out.println("Now it is "
                    + Data.playerList.get(Data.currentPlayer - 1).getName()
                    + "'s turn. Please input on the chessboard.");
            System.out.println(" ");
        });

        // Put them into the final box.
        Box playerVBox = Box.createVerticalBox();
        Box scoreVBox = Box.createVerticalBox();
        playerVBox.add(Box.createVerticalStrut(20));
        playerVBox.add(playerTitle);
        playerVBox.add(Box.createVerticalStrut(10));
        playerVBox.add(player1Name);
        playerVBox.add(Box.createVerticalStrut(10));
        playerVBox.add(player2Name);
        playerVBox.add(Box.createVerticalStrut(10));
        playerVBox.add(player3Name);
        playerVBox.add(Box.createVerticalStrut(10));
        playerVBox.add(player4Name);
        playerVBox.add(Box.createVerticalStrut(30));

        scoreVBox.add(Box.createVerticalStrut(20));
        scoreVBox.add(scoreTitle);
        scoreVBox.add(Box.createVerticalStrut(10));
        scoreVBox.add(player1Score);
        scoreVBox.add(Box.createVerticalStrut(10));
        scoreVBox.add(player2Score);
        scoreVBox.add(Box.createVerticalStrut(10));
        scoreVBox.add(player3Score);
        scoreVBox.add(Box.createVerticalStrut(10));
        scoreVBox.add(player4Score);
        scoreVBox.add(Box.createVerticalStrut(30));

        Box scoreBoardBox = Box.createHorizontalBox();

        scoreBoardBox.add(Box.createHorizontalStrut(10));
        scoreBoardBox.add(playerVBox);
        scoreBoardBox.add(Box.createHorizontalStrut(30));
        scoreBoardBox.add(scoreVBox);
        scoreBoardBox.add(Box.createHorizontalStrut(50));

        Box legendBox = Box.createHorizontalBox();
        legendBox.add(Box.createHorizontalGlue());
        legendBox.add(legend);
        legendBox.add(Box.createHorizontalGlue());

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(passTurnButton);
        buttonBox.add(Box.createHorizontalGlue());

        scoreBox.add(scoreBoardBox);
        scoreBox.add(legendBox);
        scoreBox.add(Box.createVerticalStrut(15));
        scoreBox.add(buttonBox);
        scoreBox.add(Box.createVerticalStrut(300));

        // Hide some components according to the number of players.
        if (Data.getPlayerNum() == 2) {
            player3Name.setVisible(false);
            player3Score.setVisible(false);
            player4Name.setVisible(false);
            player4Score.setVisible(false);
        } else if (Data.getPlayerNum() == 3) {
            player4Name.setVisible(false);
            player4Score.setVisible(false);
        }

        // Check if the score board is assembled for continuing the saved game.
        if (!ifNewGame)
            // If it is, update the information about the scores and current player.
            updateScore();
        else
            // Set the player 1's name label to red since player 1 is the current player.
            player1Name.setForeground(Color.RED);
    }

    /**
     * Refreshes the score board. The scores and the current player will be updated.
     */
    public static void updateScore() {
        // Displays the scores in the console.
        Data.displayScore();

        // Update the scores in the score board.
        player1Score.setText(String.valueOf(Data.playerList.get(0).getScore()));
        player2Score.setText(String.valueOf(Data.playerList.get(1).getScore()));
        if (Data.getPlayerNum() >= 3) {
            player3Score.setText(String.valueOf(Data.playerList.get(2).getScore()));
            if (Data.getPlayerNum() == 4) {
                player4Score.setText(String.valueOf(Data.playerList.get(3).getScore()));
            }
        }

        // Update the current player.
        switch (Data.currentPlayer) {
            case 1 -> {
                switch (Data.getPlayerNum()) {
                    case 2 -> player2Name.setForeground(Color.BLACK);
                    case 3 -> player3Name.setForeground(Color.BLACK);
                    case 4 -> player4Name.setForeground(Color.BLACK);
                }
                player1Name.setForeground(Color.RED);
            }
            case 2 -> {
                player1Name.setForeground(Color.BLACK);
                player2Name.setForeground(Color.RED);
            }
            case 3 -> {
                player2Name.setForeground(Color.BLACK);
                player3Name.setForeground(Color.RED);
            }
            case 4 -> {
                player3Name.setForeground(Color.BLACK);
                player4Name.setForeground(Color.RED);
            }
        }
    }
}
