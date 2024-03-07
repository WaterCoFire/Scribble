package project;

import project.GameData.Data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Manages the process of saving the current game.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class GameSave {
    /**
     * Save the date of the current game.
     *
     * @return if the save is successful or not
     * @see Data
     */
    public static boolean save() {
        try {
            // Obtain the current file path.
            String path = System.getProperty("user.dir");

            // Create a new text file.
            File file = new File(path + File.separator + "archive.txt");

            // Delete the text file if it already exists.
            boolean ifDelete = false;
            if (file.exists())
                ifDelete = file.delete();

            if (ifDelete)
                System.out.println("Updating the archive...");

            boolean ifCreateSuccessful;
            ifCreateSuccessful = file.createNewFile();

            // Check if the text file is successfully created.
            if (!ifCreateSuccessful)
                return false;

            // Create a print writer to write data into the text file.
            PrintWriter printWriter = new PrintWriter(file);

            // Save the necessary game data: the fields in the Data class.
            printWriter.print(Data.ifInUIMode + " ");
            printWriter.print(Data.ifFirstTime + " ");
            printWriter.print(Data.limitScore + " ");
            printWriter.print(Data.getPlayerNum() + " ");
            printWriter.print(Data.currentPlayer + " ");
            printWriter.print(Data.currentMaxPlayer + " ");
            printWriter.print(Data.currentMaxScore + " ");
            printWriter.print(Data.passCount);
            printWriter.println(" ");
            printWriter.println(" ");

            // Save the information of each player.
            for (int i = 0; i < Data.getPlayerNum(); i++) {
                printWriter.print(Data.playerList.get(i).getScore() + " " + Data.playerList.get(i).getName() + " ");
                printWriter.println(" ");
            }
            printWriter.println(" ");

            // Save the information of all the tiles on the chessboard.
            for (int row = 1; row <= 15; row++) {
                for (int column = 1; column <= 15; column++) {
                    printWriter.print(Data.playground[column][row].getTileLetter() + " ");
                    printWriter.print(Data.playground[column][row].getLetterValue() + " ");
                    printWriter.print(Data.playground[column][row].getWordValue() + " ");
                    printWriter.print(Data.playground[column][row].getIfUsedLetterValue() + " ");
                    printWriter.print(!Data.playground[column][row].getIfUnusedWordValue() + " ");
                    printWriter.print(Data.playground[column][row].getIfConfirmed() + " ");
                    printWriter.print(Data.playground[column][row].getIfLastAdded());
                    printWriter.println(" ");
                }
            }

            printWriter.close();
            return true;

        } catch (IOException e) {
            System.out.println("Game save unsuccessful!");
            throw new RuntimeException(e);
        }
    }
}
