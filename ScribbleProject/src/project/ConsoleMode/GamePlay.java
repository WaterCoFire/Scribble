package project.ConsoleMode;

import project.AI;
import project.GameData.Data;
import project.Main;

import java.util.Scanner;

/**
 * Manages the process of the game in console mode.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class GamePlay {
    /**
     * Manages the process of the game in console mode.
     *
     * @param ifNewGame to start a new game or not:<br>
     *                  <code>true</code> - start a new game<br>
     *                  <code>false</code> - continue the saved game
     */
    public void init(boolean ifNewGame) {
        System.out.println(" ");
        Scanner sc = new Scanner(System.in);
        boolean ifValid;                // A boolean status judging whether the user's input is valid.
        boolean checkStatus = false;    // If the player's word has been checked invalid in a turn once.
        boolean ifFirstEnd = true;      // If the first input period has already finished.
        boolean ifAi = Data.ifAi;
        AI ai = new AI();
        String[] aiWord = null;
        String endInput, targetInput;
        String endRowInput, endColumnInput;

        String targetRowInput, targetColumnInput;
        String lettersInput;
        String wordInput = "";
        StringBuilder word = new StringBuilder();

        int endRow = 0, endColumn = 0;
        int targetRow, targetColumn;
        int inputLength = 0;
        int inputDirection = 0;
        int checkResult;
        int finalScoreCount;
        int lettersBefore;

        if (Data.ifFirstTime) {
            // Instructions for the first input.
            System.out.println(Data.playerList.get(Data.currentPlayer - 1).getName() + ", it is your turn.");
            Data.displayChessboard();
            System.out.println("For it is the first time, you can only enter a word that starts or ends at the center (Row 8, Column 8).");
            System.out.println("Now, you should enter the other end of the word you want to input on the chessboard.");
            System.out.println("The format is ROW,COLUMN, e.g, '8,8'.(Do not input the inverted commas.)");
            System.out.println(" ");
            System.out.println("For example:");
            System.out.println("If you want to input the word HELLO and you want to enter to the right from the center point,");
            System.out.println("You should input '8,12', for it will occupy Row 8, from Column 8 to Column 12.");
            System.out.println(" ");
        }

        ifValid = false;    // Set the default value for ifValid.

        firstInput:
        // The period of the first input.
        while (Data.ifFirstTime) {
            // The first input - Stage 1: Input the coordinate of the other end (The target point is 8,8 for the first input).
            ifFirstEnd = false;
            while (!ifValid) {
                // Remind the user.
                System.out.println(Data.playerList.get(Data.currentPlayer - 1).getName()
                        + ", please enter the coordinate of the other end.");
                System.out.println(" ");
                System.out.println("Input as required.");
                System.out.println("OR: Input 0 -> Quit the game.");

                // Use try catch to judge if the input is valid.
                try {
                    endInput = sc.nextLine();
                    // Check if the user wants to quit the game.
                    if (endInput.equals("0")) {
                        Main.endGame(false);
                    } else if (endInput.equals("1")) {
                        // Do not allow the player to pass the first input.
                        System.out.println("You are not allowed to pass the first input.");
                        continue;
                    }
                    endRowInput = endInput.substring(0, endInput.indexOf(","));
                    endColumnInput = endInput.substring(endInput.indexOf(",") + 1);
                    endRow = Integer.parseInt(endRowInput);
                    endColumn = Integer.parseInt(endColumnInput);
                } catch (Exception e) {
                    // If the input is invalid, remind the user and continue the loop to enable the user to input again.
                    System.out.println("Invalid format, please input again!");
                    continue;
                }

                if (endRow != 8 && endColumn != 8) {
                    // If the coordinate is not on the same row or same column as the center point.
                    System.out.println("Invalid coordinate input: It is not on the same row or same column as the center point. Please input again.");
                    System.out.println(" ");
                } else if (endRow == 8 && endColumn == 8) {
                    // If the coordinate is the same as the center point.
                    System.out.println("Invalid coordinate input: The end point you enter is the center point. Please input again.");
                    System.out.println(" ");
                } else if (endRow < 1 || endRow > 15 || endColumn < 1 || endColumn > 15) {
                    // If the coordinate is out of bounds.
                    System.out.println("Invalid coordinate input: It is out of the chessboard (15 x 15). Please input again.");
                    System.out.println(" ");
                } else {
                    // If no errors are detected.
                    ifValid = true;
                    // Judge the input direction.
                    if (endRow == 8) {
                        if (endColumn < 8) {
                            inputDirection = 3;             // Left direction.
                            inputLength = 9 - endColumn;    // The length of the word that the player needs to input.
                        } else {
                            inputDirection = 4;             // Right direction.
                            inputLength = endColumn - 7;    // The length of the word that the player needs to input.
                        }
                    } else {
                        if (endRow < 8) {
                            inputDirection = 1;             // Up direction.
                            inputLength = 9 - endRow;       // The length of the word that the player needs to input.
                        } else {
                            inputDirection = 2;             // Down direction.
                            inputLength = endRow - 7;       // The length of the word that the player needs to input.
                        }
                    }
                }
            }

            ifValid = false;    // Set the default value of ifValid.

            // The first input - Stage 2: Input the word the first player prefers.
            while (true) {
                System.out.println("Please enter a word that has " + inputLength + " letters.");
                System.out.println("Both upper and lower case letters are accepted.");
                System.out.println(" ");
                System.out.println("Input as required.");
                System.out.println("OR: Input -1 -> Re-select the point of the other end.");
                System.out.println("OR: Input 0 -> Quit the game.");
                lettersInput = sc.nextLine();
                // Check if the player wants to quit the game or re-select the point of the other end.
                if (lettersInput.equals("0")) {
                    Main.endGame(false);
                } else if (lettersInput.equals("-1")) {
                    continue firstInput;
                }
                // Change all lowercase letters in the input to uppercase.
                wordInput = lettersInput.toUpperCase();

                // Check if the word is valid.
                checkResult = checkValidity(wordInput, inputLength, true);

                switch (checkResult) {
                    case 0: // Check result is valid.
                        Data.ifFirstTime = false;
                        break firstInput;
                    case 1: // Incorrect length.
                        System.out.println("Invalid input. Please enter a word that has the correct length.");
                        System.out.println(" ");
                        break;
                    case 2: // Invalid characters detected.
                        System.out.println("Invalid input. Please make sure all the characters are valid. Please input again.");
                        System.out.println(" ");
                        break;
                    case 3: // The word is not in the dictionary.
                        System.out.println("Invalid input. " + wordInput + " is not a valid word. Please input again.");
                        System.out.println(" ");
                        break;
                }
            }
        }

        int wordScoreCount = 0;
        int maxWordValue = 1;

        // Update the chessboard and calculate the score.
        switch (inputDirection) {
            case 1 -> { // Up direction.
                int x = endRow;
                for (int i = 0; i < inputLength; i++) {
                    Data.playground[8][x].setTileLetter(wordInput.charAt(i));

                    // Set them as last added.
                    Data.playground[8][x].setIfLastAdded(true);

                    // Decide the maximum word value (Double or Triple).
                    if (Data.playground[8][x].getWordValue() > maxWordValue && Data.playground[8][x].getIfUnusedWordValue())
                        maxWordValue = Data.playground[8][x].getWordValue();
                    wordScoreCount += Data.toScore(wordInput.charAt(i), x, 8);
                    x++;
                }
            }
            case 2 -> { // Down direction.
                int x = 8;
                for (int i = 0; i < inputLength; i++) {
                    Data.playground[8][x].setTileLetter(wordInput.charAt(i));

                    // Set them as last added.
                    Data.playground[8][x].setIfLastAdded(true);

                    // Decide the maximum word value (Double or Triple).
                    if (Data.playground[8][x].getWordValue() > maxWordValue && Data.playground[8][x].getIfUnusedWordValue())
                        maxWordValue = Data.playground[8][x].getWordValue();
                    wordScoreCount += Data.toScore(wordInput.charAt(i), x, 8);
                    x++;
                }
            }
            case 3 -> { // Left direction.
                int x = endColumn;
                for (int i = 0; i < inputLength; i++) {
                    Data.playground[x][8].setTileLetter(wordInput.charAt(i));

                    // Set them as last added.
                    Data.playground[x][8].setIfLastAdded(true);

                    // Decide the maximum word value (Double or Triple).
                    if (Data.playground[x][8].getWordValue() > maxWordValue && Data.playground[8][x].getIfUnusedWordValue())
                        maxWordValue = Data.playground[x][8].getWordValue();
                    wordScoreCount += Data.toScore(wordInput.charAt(i), 8, x);
                    x++;
                }
            }
            case 4 -> { // Right direction.
                int x = 8;
                for (int i = 0; i < inputLength; i++) {
                    Data.playground[x][8].setTileLetter(wordInput.charAt(i));

                    // Set them as last added.
                    Data.playground[x][8].setIfLastAdded(true);

                    // Decide the maximum word value (Double or Triple).
                    if (Data.playground[x][8].getWordValue() > maxWordValue && Data.playground[8][x].getIfUnusedWordValue())
                        maxWordValue = Data.playground[x][8].getWordValue();
                    wordScoreCount += Data.toScore(wordInput.charAt(i), 8, x);
                    x++;
                }
            }
        }

        // The following can only be reached if it is a new game or the first input period is not finished.
        if (ifNewGame || !ifFirstEnd) {
            // Add the final score to the first player.
            finalScoreCount = wordScoreCount * maxWordValue;
            Data.playerList.get(0).addScore(finalScoreCount);

            // Remind the player.
            System.out.println(Data.playerList.get(Data.currentPlayer - 1).getName() + ", you get " + finalScoreCount + " points for the word '" + wordInput + "'!");

            // Set the first player as the player with the highest mark.
            System.out.println("Congrats! Currently you have the highest score.");
            System.out.println(" ");
            Data.currentMaxScore = Data.playerList.get(0).getScore();
            Data.currentMaxPlayer = 1;

            // Check if the current highest score has reached the limit score.
            if (Data.limitScore != 0 && Data.currentMaxScore >= Data.limitScore) {
                // If so, the game is over.
                System.out.println("Game Over! " + Data.playerList.get(Data.currentPlayer - 1).getName() + " has reached the limit score!");
                Main.endGame(true);
            }

            // Switch to the second player and refresh the chessboard.
            Data.currentPlayer++;
        }

        // Display the current score and chessboard.
        Data.displayScore();
        Data.displayChessboard();

        normalInput:
        // The period of every normal input (except from the first one).
        while (true) {
            ifValid = false;    // Set the default value of ifValid.

            targetRow = 0;
            targetColumn = 0;

            if (!ifAi || Data.currentPlayer != Data.getPlayerNum()) {
                // Remind the next player.
                System.out.println(" ");
                System.out.println(Data.playerList.get(Data.currentPlayer - 1).getName() + ", it is your turn.");
                System.out.println(" ");

                // Normal input - Stage 1: Input the target point that the player prefers. (There must be a letter on it.)
                while (!ifValid) {
                    System.out.println("Please enter the coordinate of the target point as a base point for the new word.");
                    System.out.println("Note that the target point should already have a letter.");
                    System.out.println("The format is ROW,COLUMN, e.g, '8,8'. (Do not input the inverted commas.)");
                    System.out.println(" ");
                    System.out.println("Input as required.");
                    System.out.println("OR: Input 1 -> Pass this turn.");
                    System.out.println("OR: Input / -> See the chessboard again.");
                    System.out.println("OR: Input 0 -> Quit the game.");

                    // Use try catch to judge if the input is valid.
                    try {
                        targetInput = sc.nextLine();
                        //Check if the user wants to quit the game, see the chessboard or pass this turn.
                        switch (targetInput) {
                            case "0" ->
                                // End the game.
                                    Main.endGame(false);
                            case "1" -> {
                                // Pass this turn.
                                // Switch to the next player.
                                Data.currentPlayer++;
                                if (Data.currentPlayer > Data.getPlayerNum())
                                    Data.currentPlayer = 1;
                                Data.passCount++;
                                if (Data.passCount == Data.getPlayerNum()) {
                                    // If all players have skipped a full turn in a row, end the game.
                                    System.out.println("Players have skipped a full turn in a row, the game is over!");
                                    Main.endGame(true);
                                }
                                // Continue the normal input process from the beginning.
                                System.out.println("The last player passes their turn.");
                                continue normalInput;
                            }
                            case "/" -> {
                                // Display the chessboard again.
                                Data.displayChessboard();
                                continue;
                            }
                        }
                        targetRowInput = targetInput.substring(0, targetInput.indexOf(","));
                        targetColumnInput = targetInput.substring(targetInput.indexOf(",") + 1);
                        targetRow = Integer.parseInt(targetRowInput);
                        targetColumn = Integer.parseInt(targetColumnInput);
                    } catch (Exception e) {
                        // If the input is invalid, remind the user and continue the loop to enable the user to input again.
                        System.out.println("Invalid format, please input again!");
                        System.out.println(" ");
                        continue;
                    }

                    if (targetRow < 1 || targetRow > 15 || targetColumn < 1 || targetColumn > 15) {
                        // If the coordinate is out of bounds.
                        System.out.println("Invalid coordinate input: It is not on the chessboard (15 x 15). Please input again.");
                        System.out.println(" ");
                    } else if (Data.playground[targetColumn][targetRow].getTileLetter() == '0') {
                        // If there is no letter at the selected target points.
                        System.out.println("Invalid coordinate input: There is no letter existing in this coordinate. Please input again.");
                        System.out.println(" ");
                    } else if (Data.getDirectionLeft(targetColumn, targetRow) == 0
                            && Data.getDirectionRight(targetColumn, targetRow) == 0
                            && Data.getDirectionUp(targetColumn, targetRow) == 0
                            && Data.getDirectionDown(targetColumn, targetRow) == 0) {
                        // If all the four directions from the target point is invalid.
                        System.out.println("Invalid coordinate input: You can't input letters from this target point for all four directions are occupied.");
                        System.out.println("Please input again.");
                        System.out.println(" ");
                    } else {
                        // If no errors are detected.
                        ifValid = true;
                    }
                }
            } else {
                // If AI is on, and it is now AI's turn.
                System.out.println("It's AI's turn!");
                aiWord = ai.getWord();
                if (aiWord == null) {
                    Data.currentPlayer = 1;
                    Data.passCount++;
                    if (Data.passCount == Data.getPlayerNum()) {
                        // If all players have skipped a full turn in a row, end the game.
                        System.out.println("Players have skipped a full turn in a row, the game is over!");
                        Main.endGame(true);
                    }
                    // Continue the normal input process from the beginning.
                    System.out.println("AI passes its turn.");
                    continue;
                } else {
                    targetRow = Integer.parseInt(aiWord[1]);
                    targetColumn = Integer.parseInt(aiWord[2]);
                }
            }

            ifValid = false;    // Set the default value of ifValid.

            // Normal input - Stage 2: Input the coordinate of the other end.
            while (!ifValid) {
                if (!ifAi || Data.currentPlayer != Data.getPlayerNum()) {
                    System.out.println("Please enter the coordinate of the other end.");
                    System.out.println("It can either be the start point or the end point of the new word, depending on the specific direction.");
                    System.out.println("The format is ROW,COLUMN, e.g, '8,8'. (Do not input the inverted commas.)");
                    System.out.println(" ");
                    System.out.println("Input as required.");
                    System.out.println("OR: Input -1 -> Re-select the target point.");
                    System.out.println("OR: Input / -> See the chessboard again.");
                    System.out.println("OR: Input 0 -> Quit the game.");

                    // Use try catch to judge if the input is valid.
                    try {
                        endInput = sc.nextLine();
                        // Check if the user wants to quit the game, see the chessboard or re-select the target point.
                        switch (endInput) {
                            case "0" ->
                                // End the game.
                                    Main.endGame(false);
                            case "-1" -> {
                                // Re-select the target point.
                                continue normalInput;
                            }
                            case "/" -> {
                                // Display the chessboard again.
                                Data.displayChessboard();
                                continue;
                            }
                        }
                        endRowInput = endInput.substring(0, endInput.indexOf(","));
                        endColumnInput = endInput.substring(endInput.indexOf(",") + 1);
                        endRow = Integer.parseInt(endRowInput);
                        endColumn = Integer.parseInt(endColumnInput);
                    } catch (Exception e) {
                        // If the input is invalid, remind the user and continue the loop to enable the user to input again.
                        System.out.println("Invalid format, please input again!");
                        System.out.println(" ");
                        continue;
                    }
                } else {
                    endRow = Integer.parseInt(aiWord[3]);
                    endColumn = Integer.parseInt(aiWord[4]);

                }

                if (endRow != targetRow && endColumn != targetColumn) {
                    // If the coordinate is not on the same row or same column as the target point.
                    System.out.println("Invalid coordinate input: It is not on the same row or same column as the target point. Please input again.");
                    System.out.println(" ");
                } else if (endRow == targetRow && endColumn == targetColumn) {
                    // If the coordinate is the same as the target point.
                    System.out.println("Invalid coordinate input: The end point you enter is the target point you choose. Please input again.");
                    System.out.println(" ");
                } else if (endRow < 1 || endRow > 15 || endColumn < 1 || endColumn > 15) {
                    // If the coordinate is out of bounds.
                    System.out.println("Invalid coordinate input: It is not on the chessboard (15 x 15). Please input again.");
                    System.out.println(" ");
                } else {
                    // If no errors are detected.

                    // Judge the direction of the input.
                    if (endRow == targetRow) {
                        if (endColumn < targetColumn) {
                            inputDirection = 3;                             // Left direction.
                            inputLength = targetColumn - endColumn + 1;     // The length of the word that the player needs to input.
                        } else {
                            inputDirection = 4;                             // Right direction.
                            inputLength = endColumn - targetColumn + 1;     // The length of the word that the player needs to input.
                        }
                    } else {
                        if (endRow < targetRow) {
                            inputDirection = 1;                             // Up direction.
                            inputLength = targetRow - endRow + 1;           // The length of the word that the player needs to input.
                        } else {
                            inputDirection = 2;                             // Down direction.
                            inputLength = endRow - targetRow + 1;           // The length of the word that the player needs to input.
                        }
                    }

                    // Check if it is a valid direction.
                    if (Data.getDirection(inputDirection, targetColumn, targetRow) == 0) {
                        // If it is invalid, remind the user and continue the loop to enable the user to input again.
                        System.out.println("Invalid coordinate input: The direction you choose is invalid. Please input again.");
                        System.out.println(" ");
                        continue;
                    }
                    ifValid = true;
                }
            }

            // Prepare for the detecting if the user is adding prefix or suffix.
            int checkAdditionDirection = switch (inputDirection) {
                case 1, 2 -> 3 - inputDirection;
                case 3, 4 -> 7 - inputDirection;
                default -> throw new IllegalStateException("Unexpected value: " + inputDirection);
            };

            // Normal input - Stage 3: Input the word the current player prefers.
            while (true) {
                if (!ifAi || Data.currentPlayer != Data.getPlayerNum()) {
                    // Remind the current player the length of letters to be inputted.
                    // Check if the player is adding a new word.
                    if (Data.getDirection(checkAdditionDirection, targetColumn, targetRow) != 0) {
                        System.out.println("Please enter a word that has " + inputLength + " letters.");
                        switch (inputDirection) {
                            case 1, 3 -> {
                                System.out.println("The word should end with the letter " + Data.playground[targetColumn][targetRow].getTileLetter() + ".");
                                System.out.println("Both upper and lower case letters are accepted.");
                            }
                            case 2, 4 -> {
                                System.out.println("The word should start with the letter " + Data.playground[targetColumn][targetRow].getTileLetter() + ".");
                                System.out.println("Both upper and lower case letters are accepted.");
                            }
                        }
                    } else {
                        // Judge whether the player is adding a prefix or suffix.
                        switch (checkAdditionDirection) {
                            case 1, 3 -> { // Suffix.
                                System.out.println("It seems that you are adding a suffix to a word that is already on the chessboard.");
                                System.out.println("Please simply type the suffix that starts with the letter on the target point.");
                                System.out.println("For example, if you want to turn SAY into SAYING, you should input YING.");
                                System.out.println(" ");
                                System.out.println("This means you should type a string that has " + inputLength + " letters.");
                                System.out.println("The string should start with the letter " + Data.playground[targetColumn][targetRow].getTileLetter() + ".");
                                System.out.println("Both upper and lower case letters are accepted.");
                            }
                            case 2, 4 -> { // Prefix.
                                System.out.println("It seems that you are adding a prefix to a word that is already on the chessboard.");
                                System.out.println("Please simply type the prefix that ends with the letter on the target point.");
                                System.out.println("For example, if you want to turn LIKE into UNLIKE, you should input UNL.");
                                System.out.println(" ");
                                System.out.println("This means you should type a string that has " + inputLength + " letters.");
                                System.out.println("The string should end with the letter " + Data.playground[targetColumn][targetRow].getTileLetter() + ".");
                                System.out.println("Both upper and lower case letters are accepted.");
                            }
                        }
                    }
                    System.out.println(" ");
                    System.out.println("Input as required.");
                    System.out.println("OR: Input -1 -> Re-select the target point.");
                    System.out.println("OR: Input / -> See the chessboard again.");
                    System.out.println("OR: Input 0 -> Quit the game.");

                    lettersInput = sc.nextLine();

                    // Check if the user wants to quit the game, see the chessboard or re-select the target point.
                    switch (lettersInput) {
                        case "0" ->
                            // End the game.
                                Main.endGame(false);
                        case "-1" -> {
                            // Re-select the target point.
                            continue normalInput;
                        }
                        case "/" -> {
                            // Display the chessboard again.
                            Data.displayChessboard();
                            continue;
                        }
                    }
                } else {
                    lettersInput = aiWord[0];
                }

                // Change all lowercase letters in the input to uppercase.
                wordInput = lettersInput.toUpperCase();

                // Check if the input is valid.
                checkResult = checkValidity(wordInput, inputLength, false);

                // If the input is invalid, remind the user and continue the loop to enable the user to input again.
                switch (checkResult) {
                    case 1 -> { // Incorrect length.
                        System.out.println("Invalid input. Please enter a word that has the correct length.");
                        System.out.println(" ");
                        continue;
                    }
                    case 2 -> { // Invalid characters detected.
                        System.out.println("Invalid input. Please make sure all the characters are valid. Please input again.");
                        System.out.println(" ");
                        continue;
                    }
                }

                // Check if what the user inputs is conflict with any letter that is already on the chessboard.
                int startRow = 0, startColumn = 0;

                switch (inputDirection) {
                    case 1 -> {
                        startRow = endRow;
                        startColumn = targetColumn;
                    }
                    case 2, 4 -> {
                        startRow = targetRow;
                        startColumn = targetColumn;
                    }
                    case 3 -> {
                        startRow = targetRow;
                        startColumn = endColumn;
                    }
                }

                int conflictResult = checkConflict(wordInput, inputDirection, startRow, startColumn);

                // If any conflict is detected, remind the user and continue the loop to enable the user to input again.
                if (conflictResult != 0) {
                    System.out.println("Invalid input: The letter '"
                            + wordInput.charAt(conflictResult - 1)
                            + "' at No." + conflictResult
                            + " you input is conflict with the existing letter on the chessboard.");
                    System.out.println("Please input again.");
                    System.out.println(" ");
                    continue;
                }

                // Since the user may have entered a prefix or suffix of an already existing word,
                // The entire word needs to be fetched.
                int checkX, checkY;
                word.delete(0, word.length());
                word.append(wordInput);
                lettersBefore = 0;

                if (!checkStatus) {
                    switch (inputDirection) {
                        case 1 -> { // Up direction.

                            // Obtain all the letters BEFORE the input part (if there is any).
                            checkY = endRow - 1;
                            if (checkY != 0) {
                                while (Data.playground[targetColumn][checkY].getTileLetter() != '0') {
                                    word.insert(0, Data.playground[targetColumn][checkY].getTileLetter());
                                    checkY--;
                                    lettersBefore++;
                                    if (checkY == 0) break;
                                }
                            }

                            // Obtain all the letters AFTER the input part (if there is any).
                            checkY = targetRow + 1;
                            if (checkY != 16) {
                                while (Data.playground[targetColumn][checkY].getTileLetter() != '0') {
                                    word.append(Data.playground[targetColumn][checkY].getTileLetter());
                                    checkY++;
                                    if (checkY == 16) break;
                                }
                            }
                        }
                        case 2 -> { // Down direction.

                            // Obtain all the letters BEFORE the input part (if there is any).
                            checkY = targetRow - 1;
                            if (checkY != 0) {
                                while (Data.playground[targetColumn][checkY].getTileLetter() != '0') {
                                    word.insert(0, Data.playground[targetColumn][checkY].getTileLetter());
                                    checkY--;
                                    lettersBefore++;
                                    if (checkY == 0) break;
                                }
                            }

                            // Obtain all the letters AFTER the input part (if there is any).
                            checkY = endRow + 1;
                            if (checkY != 16) {
                                while (Data.playground[targetColumn][checkY].getTileLetter() != '0') {
                                    word.append(Data.playground[targetColumn][checkY].getTileLetter());
                                    checkY++;
                                    if (checkY == 16) break;
                                }
                            }
                        }
                        case 3 -> { // Left direction.

                            // Obtain all the letters BEFORE the input part (if there is any).
                            checkX = endColumn - 1;
                            if (checkX != 0) {
                                while (Data.playground[checkX][targetRow].getTileLetter() != '0') {
                                    word.insert(0, Data.playground[checkX][targetRow].getTileLetter());
                                    checkX--;
                                    lettersBefore++;
                                    if (checkX == 0) break;
                                }
                            }

                            // Obtain all the letters AFTER the input part (if there is any).
                            checkX = targetColumn + 1;
                            if (checkX != 16) {
                                while (Data.playground[checkX][targetRow].getTileLetter() != '0') {
                                    word.append(Data.playground[checkX][targetRow].getTileLetter());
                                    checkX++;
                                    if (checkX == 16) break;
                                }
                            }
                        }
                        case 4 -> { // Right direction.

                            // Obtain all the letters BEFORE the input part (if there is any).
                            checkX = targetColumn - 1;
                            if (checkX != 0) {
                                while (Data.playground[checkX][targetRow].getTileLetter() != '0') {
                                    word.insert(0, Data.playground[checkX][targetRow].getTileLetter());
                                    checkX--;
                                    lettersBefore++;
                                    if (checkX == 0) break;
                                }
                            }

                            // Obtain all the letters AFTER the input part (if there is any).
                            checkX = endColumn + 1;
                            if (checkX != 16) {
                                while (Data.playground[checkX][targetRow].getTileLetter() != '0') {
                                    word.append(Data.playground[checkX][targetRow].getTileLetter());
                                    checkX++;
                                    if (checkX == 16) break;
                                }
                            }
                        }
                    }
                }

                // Check if the final word is in the dictionary.
                checkResult = checkValidity(String.valueOf(word), word.length(), true);
                if (checkResult == 3) {
                    // The word is not in the dictionary.
                    System.out.println("Invalid input. " + word + " is not a valid word. Please input again.");
                    System.out.println(" ");
                    checkStatus = true;
                } else if (checkResult == 0) {
                    // The word is valid.
                    checkStatus = false;
                    break;
                }
            }

            // Refresh all the letters on the chessboard, since all of them are not last-added now.
            for (int i = 1; i <= 15; i++) {
                for (int j = 1; j <= 15; j++) {
                    Data.playground[i][j].setIfLastAdded(false);
                }
            }

            // Calculate the score.
            wordScoreCount = 0;
            maxWordValue = 1;
            switch (inputDirection) {
                case 1 -> { // Up direction.
                    int x = endRow;
                    for (int i = 0; i < word.length(); i++) {
                        // Update the letters on the chessboard.
                        Data.playground[targetColumn][x].setTileLetter(word.charAt(i));

                        // Set them as last added.
                        Data.playground[targetColumn][x].setIfLastAdded(true);

                        // Decide the maximum word value (Double or Triple).
                        if (Data.playground[targetColumn][x].getWordValue() > maxWordValue && Data.playground[targetColumn][x].getIfUnusedWordValue()) {
                            maxWordValue = Data.playground[targetColumn][x].getWordValue();
                        }
                        wordScoreCount += Data.toScore(Data.playground[targetColumn][x].getTileLetter(), targetColumn, x);
                        x++;
                    }
                }
                case 2 -> { // Down direction.
                    int x = targetRow - lettersBefore;
                    for (int i = 0; i < word.length(); i++) {
                        // Update the letters on the chessboard.
                        Data.playground[targetColumn][x].setTileLetter(word.charAt(i));

                        // Set them as last added.
                        Data.playground[targetColumn][x].setIfLastAdded(true);

                        // Decide the maximum word value (Double or Triple).
                        if (Data.playground[targetColumn][x].getWordValue() > maxWordValue && Data.playground[targetColumn][x].getIfUnusedWordValue()) {
                            maxWordValue = Data.playground[targetColumn][x].getWordValue();
                        }
                        wordScoreCount += Data.toScore(Data.playground[targetColumn][x].getTileLetter(), targetColumn, x);
                        x++;
                    }
                }
                case 3 -> { // Left direction.
                    int x = endColumn;
                    for (int i = 0; i < word.length(); i++) {
                        // Update the letters on the chessboard.
                        Data.playground[x][targetRow].setTileLetter(word.charAt(i));

                        // Set them as last added.
                        Data.playground[x][targetRow].setIfLastAdded(true);

                        // Decide the maximum word value (Double or Triple).
                        if (Data.playground[x][targetRow].getWordValue() > maxWordValue && Data.playground[x][targetRow].getIfUnusedWordValue()) {
                            maxWordValue = Data.playground[x][targetRow].getWordValue();
                        }
                        wordScoreCount += Data.toScore(Data.playground[x][targetRow].getTileLetter(), x, targetRow);
                        x++;
                    }
                }
                case 4 -> { // Right direction.
                    int x = targetColumn - lettersBefore;
                    for (int i = 0; i < word.length(); i++) {
                        // Update the letters on the chessboard.
                        Data.playground[x][targetRow].setTileLetter(word.charAt(i));

                        // Set them as last added.
                        Data.playground[x][targetRow].setIfLastAdded(true);

                        // Decide the maximum word value (Double or Triple).
                        if (Data.playground[x][targetRow].getWordValue() > maxWordValue && Data.playground[x][targetRow].getIfUnusedWordValue()) {
                            maxWordValue = Data.playground[x][targetRow].getWordValue();
                        }
                        wordScoreCount += Data.toScore(Data.playground[x][targetRow].getTileLetter(), x, targetRow);
                        x++;
                    }
                }
            }

            // Calculate the final score and add it to the current player.
            finalScoreCount = wordScoreCount * maxWordValue;
            Data.playerList.get(Data.currentPlayer - 1).addScore(finalScoreCount);

            // Remind the user.
            System.out.println(Data.playerList.get(Data.currentPlayer - 1).getName()
                    + ", you get " + finalScoreCount
                    + " points for the word '" + word + "'!");
            System.out.println(" ");
            Data.displayScore();
            System.out.println(" ");

            //If it's AI's turn, remind the user the target point it chose.
            if (ifAi && Data.currentPlayer == Data.getPlayerNum()) {
                System.out.println("The target point AI chose is " + aiWord[1] + "," + aiWord[2] + ".\n");
            }

            // Check if the current player has the highest score, if so, congratulate the player.
            if (Data.playerList.get(Data.currentPlayer - 1).getScore() > Data.currentMaxScore) {
                System.out.println("Congrats! Currently you have the highest score.");
                Data.currentMaxScore = Data.playerList.get(Data.currentPlayer - 1).getScore();
                Data.currentMaxPlayer = Data.currentPlayer;
            }

            // Check if the current highest score has reached the limit score.
            if (Data.limitScore != 0 && Data.currentMaxScore >= Data.limitScore) {
                // If so, the game is over.
                System.out.println("Game Over! "
                        + Data.playerList.get(Data.currentPlayer - 1).getName()
                        + " has reached the limit score!");
                Main.endGame(true);
                break;
            }

            // Switch to the next player.
            Data.currentPlayer++;
            if (Data.currentPlayer > Data.getPlayerNum())
                Data.currentPlayer = 1;

            word.delete(0, word.length());
            Data.passCount = 0;

            // Refresh the chessboard.
            Data.displayChessboard();
        }
    }

    /**
     * Checks the validity of the assigned word. Used only in console mode.
     *
     * @param wordInput         the word to be checked
     * @param length            the word's expected length
     * @param ifCheckDictionary whether to check if it is in the dictionary or not
     * @return <code>0</code> - if the word is valid<br>
     * <code>1</code> - if the word does not have the expected length<br>
     * <code>2</code> - if the word has at least one invalid character<br>
     * <code>3</code> - if the word is not found in the dictionary
     * @see Data
     */
    public int checkValidity(String wordInput, int length, boolean ifCheckDictionary) {
        if (wordInput.length() != length)
            // If the word does not have the expected length.
            return 1;
        for (int i = 0; i < length; i++) {
            if (wordInput.charAt(i) < 'A' || wordInput.charAt(i) > 'Z')
                // If any invalid character is found.
                return 2;
        }
        if (ifCheckDictionary && !Data.checkDictionary(wordInput)) {
            // If the word cannot be found in the dictionary.
            return 3;
        }
        // Return 0 if the word is valid.
        return 0;
    }

    /**
     * Checks if the letters that the player inputs is conflict with any letter that is
     * already on the chessboard. Used only in console mode.
     *
     * @param wordInput   the string of letters to be checked
     * @param direction   the current input direction (1-Up, 2-Down, 3-Left, 4-Right)
     * @param startRow    the row number where this string of letters begins
     * @param startColumn the column number where this string of letter begins
     * @return <code>0</code> - if no conflict is detected<br>
     * an integer more than <code>0</code> - the index (starts with <code>1</code>) of
     * the character in the string to be checked where a conflict is detected
     */
    public int checkConflict(String wordInput, int direction, int startRow, int startColumn) {
        switch (direction) {
            case 1, 2 -> { // Up and Down direction.
                for (int i = startRow; i <= startRow + wordInput.length() - 1; i++) {
                    if (Data.playground[startColumn][i].getTileLetter() != '0'
                            && Data.playground[startColumn][i].getTileLetter() != wordInput.charAt(i - startRow))
                        // If a conflict is found.
                        return i - startRow + 1;
                }
            }
            case 3, 4 -> { // Left and Right direction.
                for (int i = startColumn; i <= startColumn + wordInput.length() - 1; i++) {
                    if (Data.playground[i][startRow].getTileLetter() != '0'
                            && Data.playground[i][startRow].getTileLetter() != wordInput.charAt(i - startColumn))
                        // If a conflict is found.
                        return i - startColumn + 1;
                }
            }
        }
        // Return 0 if no conflict is found.
        return 0;
    }
}
