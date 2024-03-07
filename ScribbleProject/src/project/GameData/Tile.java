package project.GameData;

import java.awt.*;

/**
 * Stores the data of every tile on the Scribble chessboard.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class Tile {
    private char tileLetter;            // The letter stored in the tile.
    private int letterValue;            // The letter value of this tile. (Default/Double/Triple)
    private int wordValue;              // The word value of this tile. (Default/Double/Triple)
    private boolean ifUsedLetterValue;  // If the letter value of this tile is already used.
    private boolean ifUsedWordValue;    // If the word value of this tile is already used.
    private Color backgroundColor;      // (Used only in UI mode) The current background color of this tile.
    private boolean ifConfirmed;        // (Used only in UI mode) If the letter put on the chessboard is already confirmed.
    private boolean ifLastAdded;        // (Used only in UI mode) If the letter is last added.

    /**
     * The constructor of the <code>Tile</code> class. Sets the default value of all the fields of each member.
     *
     * @param x the column number of the tile
     * @param y the row number of the tile
     */
    public Tile(int x, int y) {
        tileLetter = '0';
        letterValue = 1;
        wordValue = 1;
        ifUsedLetterValue = false;
        ifUsedWordValue = false;
        if (x == 8 && y == 8)
            backgroundColor = Color.GREEN;
        else
            backgroundColor = Color.LIGHT_GRAY;
        ifConfirmed = false;
    }

    /**
     * Sets all the fields except for background color to expected value. Used when continuing the saved game.
     *
     * @param tileLetter        the letter in this tile expected
     * @param letterValue       the letter value in this tile expected
     * @param wordValue         the word value in this tile expected
     * @param ifUsedLetterValue the status telling if the letter value is used expected
     * @param ifUsedWordValue   the status telling if the word value is used expected
     * @param ifConfirmed       the status telling if the letter in this tile is confirmed expected
     * @param ifLastAdded       the status telling if the letter in this tile is last added expected
     */
    public void setAll(char tileLetter,
                       int letterValue,
                       int wordValue,
                       boolean ifUsedLetterValue,
                       boolean ifUsedWordValue,
                       boolean ifConfirmed,
                       boolean ifLastAdded) {
        setTileLetter(tileLetter);
        setLetterValue(letterValue);
        setWordValue(wordValue);
        setIfUsedLetterValue(ifUsedLetterValue);
        setIfUsedWordValue(ifUsedWordValue);
        setIfConfirmed(ifConfirmed);
        setIfLastAdded(ifLastAdded);
        Data.setAllDefaultColor();  // Set the default color for all the tiles.
    }

    /**
     * To obtain the tile letter.
     *
     * @return the tile letter stored
     */
    public char getTileLetter() {
        return tileLetter;
    }

    /**
     * To obtain whether the tile letter is confirmed or not.
     *
     * @return the status telling whether the tile letter is confirmed or not
     */
    public boolean getIfConfirmed() {
        return ifConfirmed;
    }

    /**
     * To set the status telling whether the tile letter is confirmed or not.
     *
     * @param ifConfirmed the expected status
     */
    public void setIfConfirmed(boolean ifConfirmed) {
        this.ifConfirmed = ifConfirmed;
    }

    /**
     * To obtain the letter value of the tile.
     *
     * @return the letter value stored
     */
    public int getLetterValue() {
        return letterValue;
    }

    /**
     * To obtain the word value of the tile.
     *
     * @return the word value stored
     */
    public int getWordValue() {
        return wordValue;
    }

    /**
     * To obtain the background color of the tile. Used only in UI mode.
     *
     * @return the current background color stored
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * To set the status telling whether the letter value of the tile is used or not.
     *
     * @param ifUsedLetterValue the expected status
     */
    public void setIfUsedLetterValue(boolean ifUsedLetterValue) {
        this.ifUsedLetterValue = ifUsedLetterValue;
    }

    /**
     * To obtain whether the letter value of the tile is used or not.
     *
     * @return the status telling whether the letter value of the tile is used or not
     */
    public boolean getIfUsedLetterValue() {
        return ifUsedLetterValue;
    }

    /**
     * To set the status telling whether the word value of the tile is used or not.
     *
     * @param ifUsedWordValue the expected status
     */
    public void setIfUsedWordValue(boolean ifUsedWordValue) {
        this.ifUsedWordValue = ifUsedWordValue;
    }

    /**
     * To obtain whether the word value of the tile is used or not.
     *
     * @return the status telling whether the word value of the tile is used or not
     */
    public boolean getIfUnusedWordValue() {
        return !ifUsedWordValue;
    }

    /**
     * To set the tile letter.
     *
     * @param tileLetter the expected letter
     */
    public void setTileLetter(char tileLetter) {
        this.tileLetter = tileLetter;
        ifConfirmed = false;
    }

    /**
     * To set the background color of the tile.
     *
     * @param backgroundColor the expected background color
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * To set the letter value of the tile.
     *
     * @param letterValue the expected letter value
     */
    public void setLetterValue(int letterValue) {
        this.letterValue = letterValue;
    }

    /**
     * To set the word value of the tile.
     *
     * @param wordValue the expected word value
     */
    public void setWordValue(int wordValue) {
        this.wordValue = wordValue;
    }

    /**
     * To obtain whether the tile letter is last added or not.
     *
     * @return the status telling whether the tile letter is last added or not
     */
    public boolean getIfLastAdded() {
        return ifLastAdded;
    }

    /**
     * To set the status telling whether the tile letter is last added or not.
     *
     * @param ifLastAdded the expected status
     */
    public void setIfLastAdded(boolean ifLastAdded) {
        this.ifLastAdded = ifLastAdded;
    }
}
