package project;

import project.GameData.Data;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Manages the process of the beginning and the ending of the program.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class Main {
    /**
     * The <code>main()</code> method of this program. Enables the user to set opinions of the game
     * before game initialization.
     *
     * @param args String parameter required here
     * @throws IOException an I/O exception has occurred
     * @see GameInit
     */
    public static void main(String[] args) throws IOException {
        System.out.println(" ");
        System.out.println("WELCOME TO THE SCRIBBLE GAME!");

        // Obtain and display the current date.
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        Month dateMonth = date.getMonth();
        String month = dateMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int day = date.getDayOfMonth();
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        String week = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        System.out.println("Today is " + week + ", " + day + " " + month + ", " + year + ".");
        System.out.println(" ");

        Scanner sc = new Scanner(System.in);

        // Show the main menu.
        System.out.println("---------- Main Menu ----------");
        System.out.println("Input 0 -> Reload and continue the saved game.");
        System.out.println("Input 1 -> Learn the rules of Scribble.");
        System.out.println("Input any other key -> Start a new game.");
        String startOption = sc.nextLine();

        // If the user does not input anything.
        while (startOption.isEmpty()) {
            System.out.println("Please enter any key.");
            startOption = sc.nextLine();
        }

        // Allow the user to view the rules of Scribble.
        while (startOption.equals("1")) {
            Data.showRules();
            System.out.println("---------- Main Menu ----------");
            System.out.println("Input 0 -> Reload and continue the saved game.");
            System.out.println("Input 1 -> Learn the rules of Scribble.");
            System.out.println("Input any other key -> Start a new game.");
            startOption = sc.nextLine();
        }

        // Set whether to start a new game or not.
        boolean ifNewGame;
        ifNewGame = !startOption.equals("0");

        // Check the user wants to continue the saved game process.
        if (!ifNewGame) {
            // If so, try to obtain the text file storing the game data.
            String path = System.getProperty("user.dir");
            File file = new File(path + File.separator + "archive.txt");
            if (file.exists()) {
                // If it exists, continue the saved game.
                GameInit.init(false);
                return;
            } else {
                // If it does not exist, remind the user and automatically start a new game.
                System.out.println("There is no game process saved.");
                System.out.println("Now we will start a new game.");
            }
        }

        // It is a new game.
        // Ask the user the number of players.
        System.out.println("Input the number of players (2-4):");
        boolean ifValid = true;
        int playerNum = 0;

        // Check if what the user inputs is invalid.
        try {
            playerNum = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Please input a valid number.");
            ifValid = false;
        }

        if ((playerNum != 2 && playerNum != 3 && playerNum != 4) && ifValid) {
            System.out.println("Please input a valid number.");
            ifValid = false;
        }

        while (!ifValid) {
            try {
                sc.nextLine();
                playerNum = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please input a valid number.");
                continue;
            }
            if (playerNum != 2 && playerNum != 3 && playerNum != 4) {
                System.out.println("Please input a valid number.");
                continue;
            }
            ifValid = true;
        }

        // Set the number of players.
        Data.setPlayerNum(playerNum);
        sc.nextLine();

        // Ask the players for their names.
        for (int i = 0; i < Data.getPlayerNum(); i++) {
            System.out.println("Player " + (i + 1) + ", may I have your name please?");
            String nameInput = sc.nextLine();

            // Check if what the user inputs is valid.
            if (nameInput.isEmpty()) {
                System.out.println("Your name should contain at least one character. Please enter your name again.");
                ifValid = false;

            }
            for (int j = 0; j < nameInput.length(); j++) {
                if (nameInput.charAt(j) == ' ') {
                    System.out.println("Your name cannot contain spaces. Please enter your name again.");
                    ifValid = false;
                    break;
                }
            }

            while (!ifValid) {
                nameInput = sc.nextLine();
                if (nameInput.isEmpty()) {
                    System.out.println("Your name should contain at least one character. Please enter your name again.");
                    continue;
                }
                boolean ifContainSpace = false;
                for (int j = 0; j < nameInput.length(); j++) {
                    if (nameInput.charAt(i) == ' ') {
                        System.out.println("Your name cannot contain spaces. Please enter your name again.");
                        ifContainSpace = true;
                        break;
                    }
                }
                if (ifContainSpace)
                    continue;

                ifValid = true;
            }
            // Set the name for each player.
            Data.playerList.get(i).setName(nameInput);
        }

        // Ask the user whether to play in UI mode or in console mode.
        System.out.println(" ");
        System.out.println("Do you want to enable UI or just play it in the console here?");
        System.out.println("Input 0 -> Enable UI mode.");
        System.out.println("Input any other key -> Use console mode.");
        String decisionUI = sc.nextLine();
        while (decisionUI.isEmpty()) {
            System.out.println("Please enter any key.");
            decisionUI = sc.nextLine();
        }

        // Set whether to enable UI or not.
        Data.ifInUIMode = decisionUI.equals("0");

        // Ask the user to set a limit score.
        System.out.println("Note that if you want to change the displaying mode after the game starts, ");
        System.out.println("You can save & quit the game, then reopen the program and choose to continue the game.");
        System.out.println("And you have a chance to change the displaying mode when reloading.");
        System.out.println(" ");
        System.out.println("Ok, now, please input the limit score. When one of the players reaches it, the game will be over.");
        System.out.println("To ensure a good experience for all players, it has to at least 50.");
        System.out.println("If you wish not to set a limit score, you can input 0.");
        int limitScore = 0;

        // Check if what the user inputs is valid.
        try {
            limitScore = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Please input a valid score.");
            ifValid = false;
        }
        if (limitScore < 50 && limitScore != 0) {
            System.out.println("The score you enter is too small. Please input again.");
            ifValid = false;
        }
        while (!ifValid) {
            try {
                sc.nextLine();
                limitScore = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please input a valid score.");
                continue;
            }
            if (limitScore < 50 && limitScore != 0) {
                System.out.println("The score you enter is too small. Please input again.");
                continue;
            }
            ifValid = true;
        }

        // Set the limit score.
        Data.limitScore = limitScore;

        // The AI can only be enabled in console mode.
        // If in console mode, Ask if the player wants to turn on AI or not.
        if (!Data.ifInUIMode) {
            System.out.println("Do you wish to turn on the AI?");
            System.out.println("Note: AI will play as the last player of a turn and only works in console mode.");
            System.out.println("For example, if there are three players, AI will be the third player.");
            System.out.println("Input 0 -> Enable AI.");
            System.out.println("Input any other number -> Disable AI.");
            boolean flag = false;
            int AIInput;
            while (!flag) {
                try {
                    sc.nextLine();
                    AIInput = sc.nextInt();
                    if (AIInput == 0) {
                        Data.ifAi = true;
                        System.out.println("AI is on.");
                    } else
                        System.out.println("AI is off.");
                    flag = true;
                } catch (InputMismatchException e) {
                    System.out.println("Please input a valid value;");
                }
            }

            if (Data.ifAi) {
                System.out.println("Please input the difficulty you prefer AI to be.");
                System.out.println("The difficulty should be between 1-100.");
                System.out.println("For your reference: 1-25 to be easy, 26-50 to be moderate, 51-75 to be hard, 76-100 to be very hard.");
                int num;
                boolean valid = false;
                while (!valid) {
                    try {
                        sc.nextLine();
                        num = sc.nextInt();
                        if (num > 0 && num <= 100) {
                            AI.setDifficulty(num);
                            valid = true;
                        } else {
                            System.out.println("Invalid input, difficulty should be between 1 to 100");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input, please input an integer.");
                    }
                }
            }
        }


        // Initialize a new game.
        Data.setValue();
        GameInit.init(true);
    }

    /**
     * Manages the end process of the game.
     *
     * @param ifNormalEnd if the end of the game is caused by normal reason or not:<br>
     *                    <code>true</code> - one player reaches the limit score<br>
     *                    <code>false</code> - the player inputs instruction to manually end the game
     * @see Data
     */
    public static void endGame(boolean ifNormalEnd) {
        if (ifNormalEnd) {
            // If one player reaches the limit score.
            // Announce the winner.
            System.out.println("The winner is: " + Data.playerList.get(Data.currentMaxPlayer - 1).getName() + "!");
            System.out.println(" ");

            // Displays the score of all the players.
            for (int i = 0; i < Data.getPlayerNum(); i++) {
                System.out.println(Data.playerList.get(i).getName() + " has got " + Data.playerList.get(i).getScore() + " points!");
            }
        } else {
            // If the player inputs the instruction to manually end the game.
            Scanner sc = new Scanner(System.in);

            // Ask if the player wants to end the game, or just save the game and quit.
            System.out.println(" ");
            System.out.println("Do you wish to end the game directly, or save the game progress so that you can continue next time?");
            System.out.println("Input 0 -> Directly end this game.");
            System.out.println("Input any other key -> Save this game.");

            String endChoice = sc.nextLine();
            while (endChoice.isEmpty()) {
                System.out.println("Please enter any key.");
                endChoice = sc.nextLine();
            }

            boolean ifEnd = endChoice.equals("0");
            if (ifEnd) {
                // If the player wants to end the game, call this method again with parameter 'true' to display the winner and scores.
                endGame(true);
            } else {
                // If the player wants to save process and quit.
                if (GameSave.save()) {
                    // If the progress is saved successfully.
                    System.out.println("Game progress is stored. You can choose to continue this game next time.");
                } else {
                    // If the progress fails to be saved.
                    System.out.println("Sorry, failed to store the game progress!");
                }
            }
        }
        // Quit the program.
        System.out.println(" ");
        System.out.println("GOODBYE!");
        System.exit(0);
    }
}