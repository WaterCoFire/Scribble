package project.GameData;

import java.awt.*;
import java.util.ArrayList;

/**
 * Manages all the necessary data for the game, both in UI mode and in console mode.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class Data {
    public static boolean ifInUIMode;                                   // If the game is in UI mode.
    public static boolean ifFirstTime = true;                           // If it is the first input.
    public static int limitScore = 0;                                   // The limit score (Game over when one player reaches).
    private static int playerNum;                                       // The number of players.
    public static int currentPlayer = 1;                                // The player at turn.
    public static int currentMaxPlayer = 1;                             // The player that has the highest score.
    public static int currentMaxScore = 0;                              // The highest score among all players currently.
    public static int passCount = 0;                                    // Used to count the number of consecutive turn skips.
    public static Tile[][] playground = new Tile[16][16];               // The array of the Tile class's members. Store the information of each tile.
    public static ArrayList<Player> playerList = new ArrayList<>();     // The arraylist that stores the information of players.
    public static boolean ifAi = false;                                 // If AI is on.

    /**
     * Sets the value for all the tiles with special values, e.g, Double Letter,
     * Triple Word, according to rules.
     *
     * @see Tile
     */
    public static void setValue() {
        for (int i = 0; i <= 15; i++) {
            for (int j = 0; j <= 15; j++) {
                playground[i][j] = new Tile(i, j);
            }
        }

        // Triple Word.
        playground[1][1].setWordValue(3);
        playground[1][8].setWordValue(3);
        playground[1][15].setWordValue(3);
        playground[8][1].setWordValue(3);
        playground[8][15].setWordValue(3);
        playground[15][1].setWordValue(3);
        playground[15][8].setWordValue(3);
        playground[15][15].setWordValue(3);

        // Double Word.
        playground[2][2].setWordValue(2);
        playground[2][14].setWordValue(2);
        playground[3][3].setWordValue(2);
        playground[3][13].setWordValue(2);
        playground[4][4].setWordValue(2);
        playground[4][12].setWordValue(2);
        playground[5][5].setWordValue(2);
        playground[5][11].setWordValue(2);
        playground[11][5].setWordValue(2);
        playground[11][11].setWordValue(2);
        playground[12][4].setWordValue(2);
        playground[12][12].setWordValue(2);
        playground[13][3].setWordValue(2);
        playground[13][13].setWordValue(2);
        playground[14][2].setWordValue(2);
        playground[14][14].setWordValue(2);

        // Triple Letter.
        playground[2][6].setLetterValue(3);
        playground[2][10].setLetterValue(3);
        playground[6][2].setLetterValue(3);
        playground[6][6].setLetterValue(3);
        playground[6][10].setLetterValue(3);
        playground[6][14].setLetterValue(3);
        playground[10][2].setLetterValue(3);
        playground[10][6].setLetterValue(3);
        playground[10][10].setLetterValue(3);
        playground[10][14].setLetterValue(3);
        playground[14][6].setLetterValue(3);
        playground[14][10].setLetterValue(3);

        // Double Letter.
        playground[1][4].setLetterValue(2);
        playground[1][12].setLetterValue(2);
        playground[3][7].setLetterValue(2);
        playground[3][9].setLetterValue(2);
        playground[4][1].setLetterValue(2);
        playground[4][8].setLetterValue(2);
        playground[4][15].setLetterValue(2);
        playground[7][3].setLetterValue(2);
        playground[7][7].setLetterValue(2);
        playground[7][9].setLetterValue(2);
        playground[7][13].setLetterValue(2);
        playground[8][4].setLetterValue(2);
        playground[8][12].setLetterValue(2);
        playground[9][3].setLetterValue(2);
        playground[9][7].setLetterValue(2);
        playground[9][9].setLetterValue(2);
        playground[9][13].setLetterValue(2);
        playground[12][1].setLetterValue(2);
        playground[12][8].setLetterValue(2);
        playground[12][15].setLetterValue(2);
        playground[13][7].setLetterValue(2);
        playground[13][9].setLetterValue(2);
        playground[15][4].setLetterValue(2);
        playground[15][12].setLetterValue(2);
    }

    /**
     * To obtain the number of players.
     *
     * @return the number of players
     */
    public static int getPlayerNum() {
        return playerNum;
    }

    /**
     * To set the number of players.
     *
     * @param playerNum the expected number of players
     */
    public static void setPlayerNum(int playerNum) {
        Data.playerNum = playerNum;
        for (int i = 1; i <= playerNum; i++) {
            playerList.add(new Player());
        }
    }

    /**
     * Sets the background color of the tile when it is selected. Used only in UI mode.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     */
    public static void setSelectedTileColor(int X, int Y) {
        playground[X][Y].setBackgroundColor(Color.GREEN);
    }

    /**
     * Sets the background color of the tile when the player tries to set it as the target
     * point while it is invalid. Used only in UI mode.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     */
    public static void setInvalidTileColor(int X, int Y) {
        playground[X][Y].setBackgroundColor(Color.RED);
    }

    /**
     * Sets the default background color of the tile. Used only in UI mode.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     */
    public static void setDefaultTileColor(int X, int Y) {
        playground[X][Y].setBackgroundColor(Color.LIGHT_GRAY);
    }

    /**
     * Sets the background color of the tile when it is in the possible direction when the
     * player selects a valid target point. Used only in UI mode.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     */
    public static void setPossibleTileColor(int X, int Y) {
        playground[X][Y].setBackgroundColor(new Color(180, 255, 130));
    }

    /**
     * Sets the background color of the tile when the cursor is currently at this tile.
     * This happens when the next letter that the player inputs is going to be here.
     * Used only in UI mode.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     */
    public static void setInputTileColor(int X, int Y) {
        playground[X][Y].setBackgroundColor(Color.YELLOW);
    }

    /**
     * To obtain how many valid tiles are available for input above the target point
     * when the player selects a valid target point. Used only in UI mode.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     * @return the valid length above the target point
     */
    public static int getDirectionUp(int X, int Y) {
        if (Y == 1) return 0;
        if (playground[X][Y - 1].getTileLetter() != '0') return 0;
        return Y - 1;
    }

    /**
     * To obtain how many valid tiles are available for input below the target point
     * when the player selects a valid target point. Used only in UI mode.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     * @return the valid length below the target point
     */
    public static int getDirectionDown(int X, int Y) {
        if (Y == 15) return 0;
        if (playground[X][Y + 1].getTileLetter() != '0') return 0;
        return 15 - Y;
    }

    /**
     * To obtain how many valid tiles are available for input to the left of the target
     * point when the player selects a valid target point. Used only in UI mode.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     * @return the valid length to the left of the target point
     */
    public static int getDirectionLeft(int X, int Y) {
        if (X == 1) return 0;
        if (playground[X - 1][Y].getTileLetter() != '0') return 0;
        return X - 1;
    }

    /**
     * To obtain how many valid tiles are available for input to the right of the target
     * point when the player selects a valid target point. Used only in UI mode.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     * @return the valid length to the right of the target point
     */
    public static int getDirectionRight(int X, int Y) {
        if (X == 15) return 0;
        if (playground[X + 1][Y].getTileLetter() != '0') return 0;
        return 15 - X;
    }

    /**
     * To obtain how many valid tiles are available for input around the target point
     * when the player selects a valid target point. Needs to be assigned a specific
     * direction. Used only in UI mode.
     *
     * @param direction the expected direction (1-Up, 2-Down, 3-Left, 4-Right)
     * @param X         the column number of the expected tile
     * @param Y         the row number of the expected tile
     * @return the valid length
     */
    public static int getDirection(int direction, int X, int Y) {
        return switch (direction) {
            case 1 -> getDirectionUp(X, Y);
            case 2 -> getDirectionDown(X, Y);
            case 3 -> getDirectionLeft(X, Y);
            case 4 -> getDirectionRight(X, Y);
            default -> -1;
        };
    }

    /**
     * Sets the default background color for all the tiles.
     */
    public static void setAllDefaultColor() {
        for (int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                setDefaultTileColor(i, j);
            }
        }
    }

    /**
     * Set the assigned tile to confirmed.
     *
     * @param X the column number of the expected tile
     * @param Y the row number of the expected tile
     */
    public static void setConfirmed(int X, int Y) {
        Data.playground[X][Y].setIfConfirmed(true);
    }

    /**
     * Set all the tiles to confirmed.
     */
    public static void setAllConfirmed() {
        for (int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                setConfirmed(i, j);
            }
        }
    }

    /**
     * Calculate the points earned on the assigned tile, based on the letter on each tile,
     * the letter value of this tile, and whether the value of this tile has been used.
     *
     * @param ch the letter on this tile
     * @param X  the column number of the expected tile
     * @param Y  the row number of the expected tile
     * @return the score earned on this tile
     */
    public static int toScore(char ch, int X, int Y) {
        // Obtain the basic score on this tile according to the rules of Scribble.
        int currentScoreCount = switch (ch) {
            case 'A', 'E', 'I', 'O', 'U', 'N', 'R', 'T', 'L', 'S' -> 1;
            case 'D', 'G' -> 2;
            case 'B', 'C', 'M', 'P' -> 3;
            case 'F', 'H', 'V', 'W', 'Y' -> 4;
            case 'K' -> 5;
            case 'J', 'X' -> 8;
            case 'Q', 'Z' -> 10;
            default -> 0;
        };
        // Check if the letter value has been used.
        if (!Data.playground[X][Y].getIfUsedLetterValue()) {
            // If not, multiply the basic score with the letter value.
            currentScoreCount *= Data.playground[X][Y].getLetterValue();
        }

        // Set the tile's status telling if the letter and word value is used to true.
        Data.playground[X][Y].setIfUsedLetterValue(true);
        Data.playground[X][Y].setIfUsedWordValue(true);

        // Return the score earned on this tile.
        return currentScoreCount;
    }

    /**
     * Displays the chessboard in the console. Used only in console mode.
     */
    public static void displayChessboard() {
        System.out.println(" ");
        System.out.println("The current chessboard:");
        System.out.println(" ");

        System.out.println("┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓");
        System.out.println("┃ 0 ┃ 1 ┃ 2 ┃ 3 ┃ 4 ┃ 5 ┃ 6 ┃ 7 ┃ 8 ┃ 9 ┃10 ┃11 ┃12 ┃13 ┃14 ┃15 ┃");
        System.out.println("┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫");
        for (int row = 1; row <= 15; row++) {
            System.out.print("┃");
            if (row < 10)
                System.out.print(" " + row + " ┃");
            else
                System.out.print(row + " ┃");
            for (int column = 1; column <= 15; column++) {
                if (Data.playground[column][row].getTileLetter() != '0')
                    System.out.print(" " + Data.playground[column][row].getTileLetter() + " ┃");
                else if (row == 8 && column == 8)
                    System.out.print(" * ┃");
                else
                    System.out.print("   ┃");
            }
            System.out.println(" ");
            if (row < 15)
                System.out.println("┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫");
            else
                System.out.println("┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛");
        }
    }

    /**
     * Displays the scores of all the players. Used both in UI mode and in console mode.
     */
    public static void displayScore() {
        System.out.println("The current score:");
        for (int i = 0; i < Data.getPlayerNum(); i++) {
            System.out.println(Data.playerList.get(i).getName() + " has got " + Data.playerList.get(i).getScore() + " points.");
        }
    }

    /**
     * Check if the word assigned is in the dictionary. Used both in UI mode and in console mode.
     *
     * @param word the word to be checked
     * @return whether the word is in the dictionary or not
     */
    public static boolean checkDictionary(String word) {
        Dictionary dictionary = new Dictionary();
        return dictionary.isValid(word);
    }

    /**
     * Displays the rules of the Scribble. Used both in UI mode and in console mode.
     */
    public static void showRules() {
        String rules = """
                
                Scribble Game Rules:

                [1]  This game is played by 2-4 players. Players will take turns to place letters on a 15 x 15 chessboard to get scores.

                [2]  Players must make sure that each time the letters they place can form a word on the chessboard.
                     The validity of the letters inputted will be checked when a player confirms their turn.

                [3]  Words can be added horizontally or vertically, but the alphabetical order must be from top to bottom or from left to right.

                [4]  The first player should input a word that starts or ends at the center point.

                [5]  After the first input, each time the current player must select a target point as the start or end of a new word first.
                     The target point should already contain a letter and have at least one valid direction to store a new word.

                [6]  Players can add a prefix or suffix to a word that is already on the chessboard.
                     But they must make sure that the new word is still valid.

                [7]  Each letter has a corresponding basic score:
                     1  point  : A, E, I, O, U, N, R, T, L, S;
                     2  points : D, G;
                     3  points : B, C, M, P;
                     4  points : F, H, V, W, Y;
                     5  points : K;
                     8  points : J, X;
                     10 points : Q, Z;

                [8]  Some points on the chessboard has special values, such as Double Letter or Triple Word.
                     Note that the special value on each tile can only be used once.

                [9]  Players can set a limit score that is at least 50. If one of the players' score reaches it, the game will be over.
                     There is also a unlimited mode that allows players to play to their heart's content.

                [10] Players can choose to pass their current turn if they have no idea what to input.
                     But if all players skip a full turn in a row, the game will be over.

                """;
        // Display the rules in the console.
        System.out.println(rules);
    }
}
