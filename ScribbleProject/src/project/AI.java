package project;

import project.GameData.Data;
import project.GameData.Dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages the function of AI if it's turned on.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class AI {
    private final String file = "dictionary.txt";   // The dictionary that AI refers to.
    private static int difficulty = 0;              // The difficulty that user prefers.
    Random random = new Random();
    Dictionary dictionary = new Dictionary();       // Create a dictionary object to detect whether a word is valid.

    /**
     * Set the difficulty of AI according to user preference.
     *
     * @param num The parameter of difficulty that user prefers.
     */
    public static void setDifficulty(int num) {
        difficulty = num * 10;
    }

    /**
     * Manage the whole process: AI fetches a word automatically and return specific information (word content and position) to the caller.
     *
     * @return Information of the exact word, including word content, target point and end point.
     */

    public String[] getWord() {
        String[] word;
        for (int i = 0; i < difficulty; i++) {
            char[][] wordInfo = findCap();                              // Fetch all available starting points and store them.
            char[] temp = wordInfo[random.nextInt(wordInfo.length)];    // Get one of the available points.
            int n = random.nextInt(4);                           // Choose the direction to input the word.
            switch (n) {
                case 0 -> {
                    word = getUp(temp);                     // Get a possible word with the information of starting point.
                    if (word != null && upValid(word))      // If the word is valid, return its information.
                        return word;
                }
                case 1 -> {
                    word = getDown(temp);
                    if (word != null && downValid(word))    // Same as case 0.
                        return word;
                }
                case 2 -> {
                    word = getLeft(temp);
                    if (word != null && leftValid(word))    // Same as case 0.
                        return word;
                }
                case 3 -> {
                    word = getRight(temp);
                    if (word != null && rightValid(word))   // Same as case 0.
                        return word;
                }
                default -> {
                }
            }
        }
        return null;
    }

    /**
     * Get a possible word in the upward direction.
     *
     * @param wordInfo Information of the starting point.
     * @return Information of the exact word, including starting point, end point and letters to be added on.
     */
    private String[] getUp(char[] wordInfo) {
        int length = detectUp(wordInfo);        // Detect the available room in the upward direction.
        int row = wordInfo[1];                  // Get the row of starting point.
        int column = wordInfo[2];               // Get the column of starting point.
        if (length == 0)
            return null;                        // If there's no room for input.
        else {
            int n = random.nextInt(length) + 1;                         // Get a possible length of the word to be inputted.
            String suffix = completeDown(wordInfo).toLowerCase();       // Complete the suffix if there's any.
            String word = newWordSuf(suffix, n);                        // Get a new word with the existing suffix.
            if (word == null)
                return null;                    // If there's no valid word.
            else {
                String addition = word.substring(0, word.length() - suffix.length() + 1);     // Settle the letters to be inputted.
                return new String[]{addition, Integer.toString(row), Integer.toString(column), Integer.toString(row - n), Integer.toString(column)};    //Return information of the exact word.
            }
        }
    }

    /**
     * Get a possible word in the downward direction.
     *
     * @param wordInfo Information of the starting point.
     * @return Information of the exact word, including starting point, end point and letters to be added on.
     */
    private String[] getDown(char[] wordInfo) {
        int length = detectDown(wordInfo);  // Detect the available room in the downward direction.
        int row = wordInfo[1];              // Get the row of starting point.
        int column = wordInfo[2];           // Get the column of starting point.
        if (length == 0)
            return null;                    // If there's no room for input.
        else {
            int n = random.nextInt(length) + 1;                     // Get a possible length of the word to be inputted.
            String prefix = completeUp(wordInfo).toLowerCase();     // Complete the prefix if there's any.
            String word = newWordPre(prefix, n);                    // Get a new word with the existing prefix.
            if (word == null)
                return null;       // If there's no valid word.
            else {
                String addition = word.substring(prefix.length() - 1);      // Settle the letters to be inputted.
                return new String[]{addition, Integer.toString(row), Integer.toString(column), Integer.toString(row + n), Integer.toString(column)};    //Return information of the exact word.
            }
        }
    }

    /**
     * Get a possible word in the left direction.
     *
     * @param wordInfo Information of the starting point.
     * @return Information of the exact word, including starting point, end point and letters to be added on.
     */
    private String[] getLeft(char[] wordInfo) {
        int length = detectLeft(wordInfo);              // Detect the available room in the left direction.
        int row = wordInfo[1];                          // Get the row of starting point.
        int column = wordInfo[2];                       // Get the column of starting point.
        if (length == 0)
            return null;                                // If there's no room for input.
        else {
            int n = random.nextInt(length) + 1;                         // Get a possible length of the word to be inputted.
            String suffix = completeRight(wordInfo).toLowerCase();      // Complete the suffix if there's any.
            String word = newWordSuf(suffix, n);                        // Get a new word with the existing suffix.
            if (word == null)
                return null;                            // If there's no valid word.
            else {
                String addition = word.substring(0, word.length() - suffix.length() + 1);     // Settle the letters to be inputted.
                return new String[]{addition, Integer.toString(row), Integer.toString(column), Integer.toString(row), Integer.toString(column - n)};    //Return information of the exact word.
            }
        }
    }

    /**
     * Get a possible word in the right direction.
     *
     * @param wordInfo Information of the starting point.
     * @return Information of the exact word, including starting point, end point and letters to be added on.
     */
    private String[] getRight(char[] wordInfo) {
        int length = detectRight(wordInfo); // Detect the available room in the right direction.
        int row = wordInfo[1];              // Get the row of starting point.
        int column = wordInfo[2];           // Get the column of starting point.
        if (length == 0)
            return null;                    // If there's no room for input.
        else {
            int n = random.nextInt(length) + 1;                     // Get a possible length of the word to be inputted.
            String prefix = completeLeft(wordInfo).toLowerCase();   // Complete the prefix if there's any.
            String word = newWordPre(prefix, n);                    // Get a new word with the existing prefix.
            if (word == null)
                return null;                // If there's no valid word.
            else {
                String addition = word.substring(prefix.length() - 1);      // Settle the letters to be inputted.
                return new String[]{addition, Integer.toString(row), Integer.toString(column), Integer.toString(row), Integer.toString(column + n)};    //Return information of the exact word.
            }
        }
    }

    /**
     * Find all the available capitals in current chessboard.
     *
     * @return Information of all capitals. Including the letter and starting point.
     */
    private char[][] findCap() {
        char[][] wordInfo;              // The array to store capital information.
        int count = 0, i = 0;
        for (int row = 1; row <= 15; row++) {
            for (int column = 1; column <= 15; column++) {
                if (Data.playground[column][row].getTileLetter() != '0')     // Count the number of available points.
                    count++;
            }
        }
        wordInfo = new char[count][3];
        for (int row = 1; row <= 15; row++) {
            for (int column = 1; column <= 15; column++) {
                if (Data.playground[column][row].getTileLetter() != '0') {
                    wordInfo[i++] = new char[]{Data.playground[column][row].getTileLetter(), (char) (row), (char) (column)};    //Store the information of available points.
                }
            }
        }
        return wordInfo;    // Capital data to be returned.
    }

    /**
     * Check if the word to be returned is valid in upward direction.
     *
     * @param word Information of the word to be checked.
     * @return If the word is valid, return true. Otherwise, return false.
     */
    private boolean upValid(String[] word) {
        int row = Integer.parseInt(word[3]);
        int column = Integer.parseInt(word[4]);
        char cap = Data.playground[column][row].getTileLetter();    // Get all the information of the point.
        char[] wordInfo = new char[]{cap, (char) row, (char) column};
        String former = completeUp(wordInfo);                       // Complete the former part of the word.
        String latter = word[0];                                    // Get the latter part of the word.
        String prefix = former.substring(0, former.length() - 1);
        String newWord = prefix + latter;                           // Combine the word.
        return dictionary.isValid(newWord);                         // Check the word validity.
    }

    /**
     * Check if the word to be returned is valid in downward direction.
     *
     * @param word Information of the word to be checked.
     * @return If the word is valid, return true. Otherwise, return false.
     */
    private boolean downValid(String[] word) {
        int row = Integer.parseInt(word[3]);
        int column = Integer.parseInt(word[4]);
        char cap = Data.playground[column][row].getTileLetter();    // Get all the information of the point.
        char[] wordInfo = new char[]{cap, (char) row, (char) column};
        String former = word[0];                                    // Get the former part of the word.
        String latter = completeDown(wordInfo);                     // Complete the latter part of the word.
        String suffix = latter.substring(1);
        String newWord = former + suffix;                           // Combine the word.
        return dictionary.isValid(newWord);                         // Check the word's validity.
    }

    /**
     * Check if the word to be returned is valid in left direction.
     *
     * @param word Information of the word to be checked.
     * @return If the word is valid, return true. Otherwise, return false.
     */
    private boolean leftValid(String[] word) {
        int row = Integer.parseInt(word[3]);
        int column = Integer.parseInt(word[4]);
        char cap = Data.playground[column][row].getTileLetter();    // Get all the information of the point.
        char[] wordInfo = new char[]{cap, (char) row, (char) column};
        String former = completeLeft(wordInfo);                     // Complete the former part of the word.
        String latter = word[0];                                    // Get the latter part of the word.
        String prefix = former.substring(0, former.length() - 1);
        String newWord = prefix + latter;                           // Combine the word.
        return dictionary.isValid(newWord);                         // Check the word's validity.
    }

    /**
     * Check if the word to be returned is valid in right direction.
     *
     * @param word Information of the word to be checked.
     * @return If the word is valid, return true. Otherwise, return false.
     */
    private boolean rightValid(String[] word) {
        int row = Integer.parseInt(word[3]);
        int column = Integer.parseInt(word[4]);
        char cap = Data.playground[column][row].getTileLetter();    // Get all the information of the point.
        char[] wordInfo = new char[]{cap, (char) row, (char) column};
        String former = word[0];                                    // Get the former part of the word.
        String latter = completeRight(wordInfo);                    // Complete the latter part of the word.
        String suffix = latter.substring(1);
        String newWord = former + suffix;                           // Combine the word.
        return dictionary.isValid(newWord);                         // Check the word validity.
    }

    /**
     * Check the available room above the capital.
     *
     * @param wordInfo Information of the capital.
     * @return The available length above.
     */

    private int detectUp(char[] wordInfo) {
        int row = wordInfo[1];
        int column = wordInfo[2];   // Get information of the capital.
        int length = 0;
        int checkY = row - 1;
        if (checkY != 0) {
            while (Data.playground[column][checkY].getTileLetter() == '0') {
                checkY--;
                length++;   // Count the available length.
                if (checkY == 0) break;
            }
        }
        return length;
    }

    /**
     * Collect all letters above if there's any.
     *
     * @param wordInfo The information of targeted capital.
     * @return All letters above.
     */
    private String completeUp(char[] wordInfo) {
        char cap = wordInfo[0];
        StringBuilder word = new StringBuilder();
        word.append(cap);
        int row = wordInfo[1];
        int column = wordInfo[2];   // Get information of the capital.
        int checkY = row - 1;
        if (checkY != 0) {
            while (Data.playground[column][checkY].getTileLetter() != '0') {
                word.insert(0, Data.playground[column][checkY].getTileLetter());    // Insert all letters above.
                checkY--;
                if (checkY == 0) break;
            }
        }
        return word.toString();
    }

    /**
     * Check the available room below the capital.
     *
     * @param wordInfo Information of the capital.
     * @return The available length above.
     */
    private int detectDown(char[] wordInfo) {
        int row = wordInfo[1];
        int column = wordInfo[2];       // Get information of the capital.
        int length = 0;
        int checkY = row + 1;
        if (checkY != 16) {
            while (Data.playground[column][checkY].getTileLetter() == '0') {
                checkY++;
                length++;   // Count the available length.
                if (checkY == 16) break;
            }
        }
        return length;
    }

    /**
     * Collect all letters below if there's any.
     *
     * @param wordInfo The information of targeted capital.
     * @return All letters below.
     */
    private String completeDown(char[] wordInfo) {
        char cap = wordInfo[0];
        StringBuilder word = new StringBuilder();
        word.append(cap);
        int row = wordInfo[1];
        int column = wordInfo[2];   // Get information of the capital.
        int checkY = row + 1;
        if (checkY != 16) {
            while (Data.playground[column][checkY].getTileLetter() != '0') {
                word.append(Data.playground[column][checkY].getTileLetter());    // Insert all letters below.
                checkY++;
                if (checkY == 16) break;
            }
        }
        return word.toString();
    }

    /**
     * Check the available room on the left of the capital.
     *
     * @param wordInfo Information of the capital.
     * @return The available length above.
     */
    private int detectLeft(char[] wordInfo) {
        int row = wordInfo[1];
        int column = wordInfo[2];       // Get information of the capital.
        int length = 0;
        int checkX = column - 1;
        if (checkX != 0) {
            while (Data.playground[checkX][row].getTileLetter() == '0') {
                checkX--;
                length++;   // Count the available length.
                if (checkX == 0) break;
            }
        }
        return length;
    }

    /**
     * Collect all letters on the left if there's any.
     *
     * @param wordInfo The information of targeted capital.
     * @return All letters on the left.
     */
    private String completeLeft(char[] wordInfo) {
        char cap = wordInfo[0];
        StringBuilder word = new StringBuilder();
        word.append(cap);
        int row = wordInfo[1];
        int column = wordInfo[2];   // Get information of the capital.
        int checkX = column - 1;
        if (checkX != 0) {
            while (Data.playground[checkX][row].getTileLetter() != '0') {
                word.insert(0, Data.playground[checkX][row].getTileLetter());    // Insert all letters on the left.
                checkX--;
                if (checkX == 0) break;
            }
        }
        return word.toString();
    }

    /**
     * Check the available room on the right of the capital.
     *
     * @param wordInfo Information of the capital.
     * @return The available length above.
     */
    private int detectRight(char[] wordInfo) {
        int row = wordInfo[1];
        int column = wordInfo[2];       // Get information of the capital.
        int length = 0;
        int checkX = column + 1;
        if (checkX != 16) {
            while (Data.playground[checkX][row].getTileLetter() == '0') {
                checkX++;
                length++;   // Count the available length.
                if (checkX == 16) break;
            }
        }
        return length;
    }

    /**
     * Collect all letters on the right if there's any.
     *
     * @param wordInfo The information of targeted capital.
     * @return All letters on the right.
     */
    private String completeRight(char[] wordInfo) {
        char cap = wordInfo[0];
        StringBuilder word = new StringBuilder();
        word.append(cap);
        int row = wordInfo[1];
        int column = wordInfo[2];       // Get information of the capital.
        int checkX = column + 1;
        if (checkX != 16) {
            while (Data.playground[checkX][row].getTileLetter() != '0') {
                word.append(Data.playground[checkX][row].getTileLetter());    // Insert all letters on the right.
                checkX++;
                if (checkX == 16) break;
            }
        }
        return word.toString();
    }

    /**
     * Get a new word with the given suffix in the dictionary.
     *
     * @param suffix The given suffix.
     * @param length The length of letters to be added in front of the suffix.
     * @return The new word satisfying the requirement.
     */
    private String newWordSuf(String suffix, int length) {
        String word;
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(inputStreamReader)) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            // Read and store the file content.
            String fileContent = sb.toString();
            // Create the pattern to be searched for.
            Pattern pattern = Pattern.compile("\\b\\w*" + length + "}" + suffix + "\\b");
            Matcher matcher = pattern.matcher(fileContent);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            String[] matches = new String[count];
            int i = 0;
            matcher.reset();
            while (matcher.find()) {
                matches[i] = matcher.group();   // Store all words that match the pattern.
                i++;
            }
            if (count > 0) {
                word = matches[random.nextInt(count)];
                return word.toLowerCase();      // Return a word that satisfies the requirement.
            } else
                return null;
        } catch (IOException e) {
            //If the search failed.
            System.out.println("An error occurred!");
            System.err.format("IOException: %s%n", e);
            return null;
        }
    }

    /**
     * Get a new word with the given prefix in the dictionary.
     *
     * @param prefix The given prefix.
     * @param length The length of letters to be added behind the prefix.
     * @return The new word satisfying the requirement.
     */
    private String newWordPre(String prefix, int length) {
        String word;
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(inputStreamReader)) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            // Read and store the file content.
            String fileContent = sb.toString();
            // Create the pattern to be searched for.
            Pattern pattern = Pattern.compile("\\b" + prefix + "\\w*" + length + "}\\b");
            Matcher matcher = pattern.matcher(fileContent);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            String[] matches = new String[count];
            int i = 0;
            matcher.reset();
            while (matcher.find()) {
                matches[i] = matcher.group();   // Store all words that match the pattern.
                i++;
            }
            if (count > 0) {
                word = matches[random.nextInt(count)];
                return word.toLowerCase();      // Return a word that satisfies the requirement.
            } else
                return null;
        } catch (IOException e) {
            // If the search failed.
            System.out.println("An error occurred!");
            System.err.format("IOException: %s%n", e);
            return null;
        }
    }
}
