package project.GameData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsible for checking if the word that the player inputs is valid.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class Dictionary {
    /**
     * Check if the word assigned is in the dictionary.
     *
     * @param word the word to be checked
     * @return whether the word is in the dictionary or not
     */

    public boolean isValid(String word) {
        // Load the dictionary file.
        String file = "dictionary.txt";
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            // Read and store the file content.
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
            String fileContent = stringBuilder.toString();

            // Create the pattern (the exact word) to be searched for.
            Pattern pattern = Pattern.compile("\\b" + word.toLowerCase() + "\\b");
            Matcher matcher = pattern.matcher(fileContent);

            // Return whether the word is found or not.
            return matcher.find();
        } catch (IOException e) {
            // If the search failed.
            System.out.println("An error occurred!");
            System.err.format("IOException: %s%n", e);
            return false;
        }
    }

}
