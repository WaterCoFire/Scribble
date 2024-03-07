package project;

import project.ConsoleMode.GamePlay;
import project.GameData.Data;
import project.GameData.Player;
import project.GameData.Tile;
import project.UIMode.GamePlayUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Manages the initialization of the game, both in UI mode and in console mode.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class GameInit {
    /**
     * Initializes a new game or continues the saved game, both in console mode and in UI mode.
     *
     * @param ifNewGame to start a new game or not:<br>
     *                  <code>true</code> - start a new game<br>
     *                  <code>false</code> - continue the saved game
     * @throws IOException an I/O exception has occurred
     * @see GamePlay
     * @see GamePlayUI
     */
    public static void init(boolean ifNewGame) throws IOException {
        if (ifNewGame) {
            // If start a new game.
            System.out.println("A new game now starts! Good luck!");
            System.out.println(" ");

            // Judge whether to enable UI or not.
            if (Data.ifInUIMode) {
                // Start a new game in UI mode.
                GamePlayUI gamePlayUI = new GamePlayUI();
                gamePlayUI.init(true);
            } else {
                // Start a new game in console mode.
                GamePlay gamePlayConsole = new GamePlay();
                gamePlayConsole.init(true);
            }
        } else {
            // If continue the saved game:
            // Load the text file that stores the game progress.
            String path = System.getProperty("user.dir");
            File file = new File(path + File.separator + "archive.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String nextLine;
            StringBuilder lineCut = new StringBuilder();
            String lineCutResult;
            int cutCount = 0;

            // Load the fields in the Data class.
            boolean ifInUIMode = false;
            boolean ifFirstTime = false;
            int limitScore = 0;
            int playerNum = 0;
            int currentPlayer = 0;
            int currentMaxPlayer = 0;
            int currentMaxScore = 0;
            int passCount = 0;

            nextLine = bufferedReader.readLine();
            for (int i = 0; i < nextLine.length(); i++) {
                if (nextLine.charAt(i) == ' ') {
                    lineCutResult = String.valueOf(lineCut);
                    lineCut.delete(0, lineCut.length());
                    cutCount++;
                    switch (cutCount) {
                        case 1 -> ifInUIMode = Boolean.parseBoolean(lineCutResult);
                        case 2 -> ifFirstTime = Boolean.parseBoolean(lineCutResult);
                        case 3 -> limitScore = Integer.parseInt(lineCutResult);
                        case 4 -> playerNum = Integer.parseInt(lineCutResult);
                        case 5 -> currentPlayer = Integer.parseInt(lineCutResult);
                        case 6 -> currentMaxPlayer = Integer.parseInt(lineCutResult);
                        case 7 -> currentMaxScore = Integer.parseInt(lineCutResult);
                        case 8 -> passCount = Integer.parseInt(lineCutResult);
                    }
                } else {
                    lineCut.append(nextLine.charAt(i));
                }
            }
            bufferedReader.readLine();

            // Load the information of each player.
            int[] score = new int[playerNum];
            String[] name = new String[playerNum];

            for (int i = 0; i < playerNum; i++) {

                lineCut.delete(0, lineCut.length());
                nextLine = bufferedReader.readLine();
                cutCount = 0;

                for (int j = 0; j < nextLine.length(); j++) {
                    if (nextLine.charAt(j) == ' ') {
                        lineCutResult = String.valueOf(lineCut);
                        lineCut.delete(0, lineCut.length());
                        cutCount++;
                        switch (cutCount) {
                            case 1 -> score[i] = Integer.parseInt(lineCutResult);
                            case 2 -> name[i] = lineCutResult;
                        }
                    } else {
                        lineCut.append(nextLine.charAt(j));
                    }
                }
            }
            bufferedReader.readLine();

            // Load the information of all the tiles on the chessboard.
            char[][] tileLetter = new char[16][16];
            int[][] letterValue = new int[16][16];
            int[][] wordValue = new int[16][16];
            boolean[][] ifUsedLetterValue = new boolean[16][16];
            boolean[][] ifUsedWordValue = new boolean[16][16];
            boolean[][] ifConfirmed = new boolean[16][16];
            boolean[][] ifLastAdded = new boolean[16][16];

            for (int row = 1; row <= 15; row++) {
                for (int column = 1; column <= 15; column++) {

                    lineCut.delete(0, lineCut.length());
                    nextLine = bufferedReader.readLine();
                    cutCount = 0;

                    for (int i = 0; i < nextLine.length(); i++) {
                        if (nextLine.charAt(i) == ' ') {
                            lineCutResult = String.valueOf(lineCut);
                            lineCut.delete(0, lineCut.length());
                            cutCount++;
                            switch (cutCount) {
                                case 1 -> tileLetter[column][row] = lineCutResult.charAt(0);
                                case 2 -> letterValue[column][row] = Integer.parseInt(lineCutResult);
                                case 3 -> wordValue[column][row] = Integer.parseInt(lineCutResult);
                                case 4 -> ifUsedLetterValue[column][row] = Boolean.parseBoolean(lineCutResult);
                                case 5 -> ifUsedWordValue[column][row] = Boolean.parseBoolean(lineCutResult);
                                case 6 -> ifConfirmed[column][row] = Boolean.parseBoolean(lineCutResult);
                                case 7 -> ifLastAdded[column][row] = Boolean.parseBoolean(lineCutResult);
                            }
                        } else {
                            lineCut.append(nextLine.charAt(i));
                        }
                    }
                }
            }
            bufferedReader.close();

            // Initialize all the data.
            Data.ifInUIMode = ifInUIMode;
            Data.ifFirstTime = ifFirstTime;
            Data.limitScore = limitScore;
            Data.setPlayerNum(playerNum);
            Data.currentPlayer = currentPlayer;
            Data.currentMaxPlayer = currentMaxPlayer;
            Data.currentMaxScore = currentMaxScore;
            Data.passCount = passCount;

            for (int i = 0; i <= 15; i++) {
                for (int j = 0; j <= 15; j++) {
                    Data.playground[i][j] = new Tile(i, j);
                }
            }

            for (int row = 1; row <= 15; row++) {
                for (int column = 1; column <= 15; column++) {
                    Data.playground[column][row].setAll(
                            tileLetter[column][row],
                            letterValue[column][row],
                            wordValue[column][row],
                            ifUsedLetterValue[column][row],
                            ifUsedWordValue[column][row],
                            ifConfirmed[column][row],
                            ifLastAdded[column][row]);
                }
            }

            for (int i = 1; i <= playerNum; i++) {
                Data.playerList.add(new Player());
            }

            for (int i = 0; i < playerNum; i++) {
                Data.playerList.get(i).setAll(score[i], name[i]);
            }

            // Ask if the user wants to switch game mode (UI/console).
            if (Data.ifInUIMode) {
                System.out.println("Do you want to remain in UI mode, or switch to console mode?");
                System.out.println("Input 0 -> Switch to console mode.");
                System.out.println("Input any other key -> Remain in UI mode.");
            } else {
                System.out.println("Do you want to remain in console mode, or switch to UI mode?");
                System.out.println("Input 0 -> Switch to UI mode.");
                System.out.println("Input any other key -> Remain in console mode.");
            }

            Scanner sc = new Scanner(System.in);
            String switchDecision = sc.nextLine();

            while (switchDecision.isEmpty()) {
                System.out.println("Please input any key.");
                switchDecision = sc.nextLine();
            }

            // Change the current mode if the player wants.
            if (switchDecision.equals("0")) {
                Data.ifInUIMode = !Data.ifInUIMode;
            }

            // Ask whether to switch on the AI or not.
            System.out.println("Do you wish to turn on the AI?");
            System.out.println("Note: AI will play as the last player of a turn and only works in console mode.");
            System.out.println("For example, if there are three players, AI will be the third player.");
            System.out.println("Input 0 -> Enable AI.");
            System.out.println("Input any other number -> Disable AI.");
            boolean flag = false;
            int AIInput;
            while (!flag) {
                try {
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
            // If AI is on, decide the difficulty.
            if (Data.ifAi) {
                System.out.println("Please input the difficulty you prefer AI to be.");
                System.out.println("The difficulty should be between 1-100");
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

            // Decide whether to enable UI or not.
            if (Data.ifInUIMode) {
                // Reload the game in UI mode.
                GamePlayUI gamePlayUI = new GamePlayUI();
                gamePlayUI.init(false);
            } else {
                // Reload the game in console mode.
                GamePlay gamePlayConsole = new GamePlay();
                gamePlayConsole.init(false);
            }
        }

    }
}
