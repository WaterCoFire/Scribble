package project.GameData;

/**
 * Stores the data of every player.
 *
 * @author Siyu Yan, Jason Cai
 * @version Team Yanyan
 */
public class Player {
    private int score;      // The score of the player.
    private String name;    // The name of the player.

    /**
     * The constructor of the <code>Player</code> class. Set the
     * default score and name of the player.
     */
    public Player() {
        score = 0;
        name = "Player";
    }

    /**
     * Sets all the fields to expected value. Used when continuing the saved game.
     *
     * @param score the expected score of the player
     * @param name  the expected name of the player
     */
    public void setAll(int score, String name) {
        this.score = score;
        setName(name);
    }

    /**
     * To obtain the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * To set the name of the player.
     *
     * @param name the expected name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * To obtain the current score of the player.
     *
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * To add score for the player.
     *
     * @param score the score to be added to the player
     */
    public void addScore(int score) {
        this.score += score;
    }
}
