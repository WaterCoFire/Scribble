package project.UIMode;

import project.GameData.Data;
import project.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Manages the chessboard in this game. This class will not be used if the user disables UI.
 *
 * @author Team Yanyan
 * @version 1.8
 */
public class Chessboard extends JPanel {
    private boolean ifSelectedDirection = false;                    // If the input direction is already selected.
    private boolean ifReadyToInput = false;                         // If the start/end point is already selected.
    private boolean ifSelecting = false;                            // If the player has already selected a valid target tile.
    private boolean ifTileValid = true;                             // If the tile selected is valid.
    private boolean ifAbleToInput = true;                           // If the player is able to input more letters during input.
    private boolean pressStatus = true;                             // If the player has pressed down a key.
    private boolean ifChecking = false;                             // If the checking progress is ready.
    public static boolean ifChangeableDirection = true;             // If the player is able to change the input direction.
    private final Timer timer;                                      // The timer used to refresh the chessboard.
    private final int tileWidth = 30;                               // The width of the tile on the chessboard.
    private final int tileHeight = 30;                              // The height of the tile on the chessboard.
    private int mouseAtX, mouseAtY;                                 // The mouse position.
    private int targetX = 8, targetY = 8;                           // The coordinate of the target point that the user selected.
    private int endX = 0, endY = 0;                                 // The coordinate of the other end that the user selected.
    private int inputDirection;                                     // The direction of the user's input.
    private int currentInputX = 0, currentInputY = 0;               // The cursor's coordinate used when inputting/checking letters.
    private final StringBuilder lettersInput = new StringBuilder(); // Store the string that the user inputs.

    /**
     * The constructor of the <code>Chessboard</code> class. Sets a new timer to enable
     * the chessboard to constantly refresh.
     */
    public Chessboard() {
        timer = new Timer(10, e -> repaint());
    }

    /**
     * Shows all the valid input direction of the selected target point when called.
     */
    public void showPossibleDirection() {
        Data.setSelectedTileColor(targetX, targetY);        // Highlight the selected target point.

        // Check all the four direction to find out if each of them are valid.
        // If so, highlight every tile on that direction.
        if (Data.getDirectionLeft(targetX, targetY) > 0) {
            for (int i = 1; i < targetX; i++) {
                Data.setPossibleTileColor(i, targetY);
            }
        }
        if (Data.getDirectionRight(targetX, targetY) > 0) {
            for (int i = targetX + 1; i <= 15; i++) {
                Data.setPossibleTileColor(i, targetY);
            }
        }
        if (Data.getDirectionUp(targetX, targetY) > 0) {
            for (int i = 1; i < targetY; i++) {
                Data.setPossibleTileColor(targetX, i);
            }
        }
        if (Data.getDirectionDown(targetX, targetY) > 0) {
            for (int i = targetY + 1; i <= 15; i++) {
                Data.setPossibleTileColor(targetX, i);
            }
        }
    }

    /**
     * Sets all the listeners for key events and mouse events. Enable the program to
     * response to the player's operation correctly at the suitable time, therefore
     * process the data and give corresponding reaction on the user interface.
     * @see Data
     */
    public void init() {

        // The mouse listener:
        // Left Click: Select the target point.
        // Right Click: Select the other end.
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                // When the left button clicked (Select the tile) and it is not the first input.
                if (e.getButton() == MouseEvent.BUTTON1 && !Data.ifFirstTime && ifChangeableDirection) {

                    // Clear all the current highlighted background.
                    Data.setAllDefaultColor();

                    // Reset the ready-to-input status.
                    ifReadyToInput = false;

                    // Get the position of the mouse point.
                    Point mousePoint = getMousePosition();
                    mouseAtX = mousePoint.x;
                    mouseAtY = mousePoint.y;
                    targetX = 0;
                    targetY = 0;
                    while (mouseAtX > tileHeight) {
                        mouseAtX -= tileHeight;
                        targetX++;
                    }
                    while (mouseAtY > tileWidth) {
                        mouseAtY -= tileWidth;
                        targetY++;
                    }

                    // If the mouse position is out of the chessboard, set the selected coordinate to 0,0 to avoid errors.
                    if (targetX < 1 || targetX > 15 || targetY < 1 || targetY > 15) {
                        targetX = 0;
                        targetY = 0;
                    }

                    // Judge whether the target point selected is valid.
                    // If so, give background color feedback and allow the player to choose direction.
                    if (Data.playground[targetX][targetY].getTileLetter() != '0') {
                        ifTileValid = Data.getDirectionLeft(targetX, targetY) != 0
                                || Data.getDirectionRight(targetX, targetY) != 0
                                || Data.getDirectionUp(targetX, targetY) != 0
                                || Data.getDirectionDown(targetX, targetY) != 0;
                        if (ifTileValid) {
                            showPossibleDirection();
                            // Set the tip.
                            TipBoard.gameTip.setText("This target point is valid. Please use Arrow Key to select input direction.");
                            ifChangeableDirection = true;
                            ifSelecting = true;
                        } else {
                            // If all four directions are unusable.
                            Data.setInvalidTileColor(targetX, targetY);
                            // Set the tip.
                            TipBoard.gameTip.setText("This target point is invalid for all its four directions are invalid.");
                            ifSelecting = false;
                        }
                    } else {
                        // If the selected target point has no letter.
                        // Set the tip.
                        TipBoard.gameTip.setText("This target point is invalid for it has no letter.");
                        Data.setInvalidTileColor(targetX, targetY);
                        ifSelecting = false;
                    }

                }

                // After a valid direction is chosen: Choose the other end point by Right Click.
                if (e.getButton() == MouseEvent.BUTTON3 && ifSelectedDirection && ifChangeableDirection) {

                    // Get the position of the mouse point.
                    Point mousePoint = getMousePosition();
                    mouseAtX = mousePoint.x;
                    mouseAtY = mousePoint.y;

                    endX = 0;
                    endY = 0;

                    while (mouseAtX > tileHeight) {
                        mouseAtX -= tileHeight;
                        endX++;
                    }
                    while (mouseAtY > tileWidth) {
                        mouseAtY -= tileWidth;
                        endY++;
                    }

                    // If the mouse position is out of the chessboard, set the selected coordinate to 0,0 to avoid errors.
                    if (endX < 1 || endX > 15 || endY < 1 || endY > 15) {
                        endX = 0;
                        endY = 0;
                    }

                    // Check if the selected coordinate point is valid.
                    // If so, prepare for the letter input.
                    switch (inputDirection) {
                        case 1 -> { // Up direction.
                            if (endX == targetX && endY < targetY && Data.playground[endX][endY].getTileLetter() == '0') {
                                // Set the tip.
                                TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");
                                currentInputX = endX;
                                currentInputY = endY;
                                // Highlight all the tiles in the up direction.
                                for (int i = 1; i < endY; i++) {
                                    Data.setDefaultTileColor(targetX, i);
                                }
                                Data.setInputTileColor(endX, endY);
                                if (Data.ifFirstTime) {
                                    for (int i = endY + 1; i <= 8; i++) {
                                        Data.setSelectedTileColor(targetX, i);
                                    }
                                } else {
                                    for (int i = endY + 1; i <= targetY; i++) {
                                        Data.setSelectedTileColor(targetX, i);
                                    }
                                }
                                ifReadyToInput = true;
                            }
                        }
                        case 2 -> { // Down direction.
                            if (endX == targetX && endY > targetY && Data.playground[endX][endY].getTileLetter() == '0') {
                                // Set the tip.
                                TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");
                                currentInputX = endX;
                                if (Data.ifFirstTime)
                                    currentInputY = 8;
                                else
                                    currentInputY = targetY + 1;

                                if (Data.ifFirstTime) {
                                    Data.setInputTileColor(8, 8);
                                } else {
                                    Data.setInputTileColor(targetX, targetY + 1);
                                }
                                // Highlight all the tiles in the up direction.
                                for (int i = endY + 1; i <= 15; i++) {
                                    Data.setDefaultTileColor(targetX, i);
                                }
                                for (int i = targetY + 2; i <= endY; i++) {
                                    Data.setSelectedTileColor(targetX, i);
                                }
                                ifReadyToInput = true;
                            }
                        }
                        case 3 -> { // Left direction.
                            if (endX < targetX && endY == targetY && Data.playground[endX][endY].getTileLetter() == '0') {
                                // Set the tip.
                                TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");
                                currentInputX = endX;
                                currentInputY = endY;
                                // Highlight all the tiles in the up direction.
                                for (int i = 1; i < endX; i++) {
                                    Data.setDefaultTileColor(i, targetY);
                                }
                                Data.setInputTileColor(endX, endY);
                                if (Data.ifFirstTime) {
                                    for (int i = endX + 1; i <= 8; i++) {
                                        Data.setSelectedTileColor(i, targetY);
                                    }
                                } else {
                                    for (int i = endX + 1; i <= targetX; i++) {
                                        Data.setSelectedTileColor(i, targetY);
                                    }
                                }
                                ifReadyToInput = true;
                            }
                        }
                        case 4 -> { // Right direction.
                            if (endX > targetX && endY == targetY && Data.playground[endX][endY].getTileLetter() == '0') {
                                // Set the tip.
                                TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");
                                if (Data.ifFirstTime)
                                    currentInputX = 8;
                                else
                                    currentInputX = targetX + 1;

                                currentInputY = endY;
                                if (Data.ifFirstTime) {
                                    Data.setInputTileColor(8, 8);
                                } else {
                                    Data.setInputTileColor(targetX + 1, targetY);
                                }
                                // Highlight all the tiles in the up direction.
                                for (int i = endX + 1; i <= 15; i++) {
                                    Data.setDefaultTileColor(i, targetY);
                                }
                                for (int i = targetX + 2; i <= endX; i++) {
                                    Data.setSelectedTileColor(i, targetY);
                                }
                                ifReadyToInput = true;
                            }
                        }
                    }
                }
            }
        });

        // The key listener:
        // Arrow keys: Select the input direction.
        // A-Z: Input letters.
        // Backspace: Delete the last letter.
        // Enter: Confirm an input.
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // When a key is pressed.
                pressStatus = true;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Obtain the current caps lock status.
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                boolean capsLockOn = toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK);

                // After a valid target point is selected: Choose the direction by pressing arrow keys.
                // For the first input:
                if (Data.ifFirstTime && (!ifReadyToInput || ifChangeableDirection)) {
                    ifSelecting = true;

                    // Obtain the direction that the user chooses.
                    // For it is the first input, there is no need to check the validity of the direction chosen.
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        ifReadyToInput = false;
                        showPossibleDirection();

                        // Set the input direction.
                        inputDirection = 1;
                        ifSelectedDirection = true;

                        // Set the tip.
                        TipBoard.gameTip.setText("Use Right Click to select the other end point, or use Arrow Key to re-select direction.");

                        // Highlight all the tile on the selected direction.
                        for (int i = 1; i < targetY; i++) {
                            Data.setSelectedTileColor(targetX, i);
                        }

                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        ifReadyToInput = false;
                        showPossibleDirection();

                        // Set the input direction.
                        inputDirection = 2;
                        ifSelectedDirection = true;

                        // Set the tip.
                        TipBoard.gameTip.setText("Use Right Click to select the other end point, or use Arrow Key to re-select direction.");

                        // Highlight all the tile on the selected direction.
                        for (int i = targetY + 1; i <= 15; i++) {
                            Data.setSelectedTileColor(targetX, i);
                        }

                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        ifReadyToInput = false;
                        showPossibleDirection();

                        // Set the input direction.
                        inputDirection = 3;
                        ifSelectedDirection = true;

                        // Set the tip.
                        TipBoard.gameTip.setText("Use Right Click to select the other end point, or use Arrow Key to re-select direction.");

                        // Highlight all the tile on the selected direction.
                        for (int i = 1; i < targetX; i++) {
                            Data.setSelectedTileColor(i, targetY);
                        }

                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        ifReadyToInput = false;
                        showPossibleDirection();

                        // Set the input direction.
                        inputDirection = 4;
                        ifSelectedDirection = true;

                        // Set the tip.
                        TipBoard.gameTip.setText("Use Right Click to select the other end point, or use Arrow Key to re-select direction.");

                        // Highlight all the tile on the selected direction.
                        for (int i = targetX + 1; i <= 15; i++) {
                            Data.setSelectedTileColor(i, targetY);
                        }
                    }
                }

                // For every normal input (after the first input is over):
                if (ifSelecting && !Data.ifFirstTime && (!ifReadyToInput || ifChangeableDirection)) {

                    // Obtain the direction that the user chooses.
                    // This time the direction that the user chooses can be invalid.
                    // The reaction is done only if the direction is valid.
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        // Check if the direction is valid.
                        if (Data.getDirectionUp(targetX, targetY) == 0) {
                            // Set the tip.
                            TipBoard.gameTip.setText("This is an invalid direction.");
                        } else {
                            ifReadyToInput = false;
                            showPossibleDirection();

                            // Set the tip.
                            TipBoard.gameTip.setText("Use Right Click to select the other end point, or use Arrow Key to re-select direction.");

                            // Set the input direction.
                            inputDirection = 1;

                            // Highlight all the tile on the selected direction.
                            for (int i = 1; i < targetY; i++) {
                                Data.setSelectedTileColor(targetX, i);
                            }
                            ifSelectedDirection = true;
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        // Check if the direction is valid.
                        if (Data.getDirectionDown(targetX, targetY) == 0) {
                            // Set the tip.
                            TipBoard.gameTip.setText("This is an invalid direction.");
                        } else {
                            ifReadyToInput = false;
                            showPossibleDirection();

                            // Set the tip.
                            TipBoard.gameTip.setText("Use Right Click to select the other end point, or use Arrow Key to re-select direction.");

                            // Set the input direction.
                            inputDirection = 2;

                            // Highlight all the tile on the selected direction.
                            for (int i = targetY + 1; i <= 15; i++) {
                                Data.setSelectedTileColor(targetX, i);
                            }
                            ifSelectedDirection = true;
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        // Check if the direction is valid.
                        if (Data.getDirectionLeft(targetX, targetY) == 0) {
                            // Set the tip.
                            TipBoard.gameTip.setText("This is an invalid direction.");
                        } else {
                            ifReadyToInput = false;
                            showPossibleDirection();

                            // Set the tip.
                            TipBoard.gameTip.setText("Use Right Click to select the other end point, or use Arrow Key to re-select direction.");

                            // Set the input direction.
                            inputDirection = 3;

                            // Highlight all the tile on the selected direction.
                            for (int i = 1; i < targetX; i++) {
                                Data.setSelectedTileColor(i, targetY);
                            }
                            ifSelectedDirection = true;
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        // Check if the direction is valid.
                        if (Data.getDirectionRight(targetX, targetY) == 0) {
                            // Set the tip.
                            TipBoard.gameTip.setText("This is an invalid direction.");
                        } else {
                            ifReadyToInput = false;
                            showPossibleDirection();

                            // Set the tip.
                            TipBoard.gameTip.setText("Use Right Click to select the other end point, or use Arrow Key to re-select direction.");

                            // Set the input direction.
                            inputDirection = 4;

                            // Highlight all the tile on the selected direction.
                            for (int i = targetX + 1; i <= 15; i++) {
                                Data.setSelectedTileColor(i, targetY);
                            }
                            ifSelectedDirection = true;
                        }
                    }
                }

                // When the other end is selected, and it is time for input: Input using key A-Z.
                // Reject the input while caps lock is on.
                if ((e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z) && !capsLockOn && ifReadyToInput && pressStatus) {

                    switch (inputDirection) {
                        case 1 -> { // Up direction.
                            //checkStatus = true;

                            if (Data.ifFirstTime) {
                                // If it is the first time.
                                if (currentInputY <= 8 && ifAbleToInput) {
                                    Data.playground[currentInputX][currentInputY].setTileLetter((char) (e.getKeyChar() - 32));
                                    Data.setSelectedTileColor(currentInputX, currentInputY);
                                    lettersInput.append(e.getKeyChar());

                                    // Judge whether the input is finished or not.
                                    if (currentInputY < 8) {
                                        ifAbleToInput = true;
                                        currentInputY++;
                                    } else {
                                        // The last letter required is inputted.
                                        ifAbleToInput = false;
                                        ifChecking = true;
                                    }

                                    // If at least one letter is inputted, do not allow the user to change direction.
                                    if (currentInputY != endY && currentInputY <= 8) {
                                        ifChangeableDirection = false;
                                        // Set the tip.
                                        TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");
                                    }

                                    // Update the tip if the input is finished to remind the user to confirm.
                                    if (currentInputY == 8)
                                        TipBoard.gameTip.setText("Press Enter to confirm, or use Backspace to delete.");

                                    // Set the cursor color for the tile that is ready to receive the next letter.
                                    Data.setInputTileColor(currentInputX, currentInputY);
                                }
                            } else {
                                // If it is a normal input.
                                if (currentInputY <= targetY - 1 && ifAbleToInput) {
                                    Data.setSelectedTileColor(currentInputX, currentInputY);
                                    Data.playground[currentInputX][currentInputY].setTileLetter((char) (e.getKeyChar() - 32));
                                    lettersInput.append(e.getKeyChar());

                                    // Judge whether the input is finished or not.
                                    if (currentInputY < targetY - 1) {
                                        ifAbleToInput = true;
                                        currentInputY++;

                                        // If the next tile already has a letter, skip it.
                                        while (Data.playground[currentInputX][currentInputY].getTileLetter() != '0') {
                                            lettersInput.append(Data.playground[currentInputX][currentInputY].getTileLetter());
                                            currentInputY++;
                                        }
                                    } else {
                                        // The last letter required is inputted.
                                        ifAbleToInput = false;
                                        ifChecking = true;
                                    }

                                    // If at least one letter is inputted, do not allow the user to change direction.
                                    if ((Data.playground[currentInputX][currentInputY].getTileLetter() != '0' || currentInputY != endY)
                                            && currentInputY <= targetY - 1) {
                                        ifChangeableDirection = false;
                                        // Set the tip.
                                        TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");
                                    }

                                    // Update the tip if the input is finished to remind the user to confirm.
                                    if (currentInputY == targetY - 1)
                                        TipBoard.gameTip.setText("Press Enter to confirm, or use Backspace to delete.");

                                    // Set the cursor color for the tile that is ready to receive the next letter.
                                    Data.setInputTileColor(currentInputX, currentInputY);

                                }
                            }
                        }
                        case 2 -> { // Down direction.
                            if (currentInputY <= endY && ifAbleToInput) {
                                Data.setSelectedTileColor(currentInputX, currentInputY);
                                Data.playground[currentInputX][currentInputY].setTileLetter((char) (e.getKeyChar() - 32));
                                lettersInput.append(e.getKeyChar());

                                // Judge whether the input is finished or not.
                                if (currentInputY < endY) {
                                    ifAbleToInput = true;
                                    currentInputY++;

                                    // If the next tile already has a letter, skip it.
                                    while (Data.playground[currentInputX][currentInputY].getTileLetter() != '0') {
                                        lettersInput.append(Data.playground[currentInputX][currentInputY].getTileLetter());
                                        currentInputY++;
                                    }
                                } else {
                                    // The last letter required is inputted.
                                    ifAbleToInput = false;
                                    ifChecking = true;
                                }

                                // If at least one letter is inputted, do not allow the user to change direction.
                                if ((Data.playground[currentInputX][currentInputY].getTileLetter() != '0' || currentInputY != targetY)
                                        && currentInputY <= endY) {
                                    ifChangeableDirection = false;
                                    // Set the tip.
                                    TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");
                                }

                                // Update the tip if the input is finished to remind the user to confirm.
                                if (currentInputY == endY)
                                    TipBoard.gameTip.setText("Press Enter to confirm, or use Backspace to delete.");

                                // Set the cursor color for the tile that is ready to receive the next letter.
                                Data.setInputTileColor(currentInputX, currentInputY);

                            }
                        }
                        case 3 -> { // Left direction.
                            if (Data.ifFirstTime) {
                                // If it is the first time.
                                if (currentInputX <= 8 && ifAbleToInput) {
                                    Data.setSelectedTileColor(currentInputX, currentInputY);
                                    Data.playground[currentInputX][currentInputY].setTileLetter((char) (e.getKeyChar() - 32));
                                    lettersInput.append(e.getKeyChar());

                                    // Judge whether the input is finished or not.
                                    if (currentInputX < 8) {
                                        ifAbleToInput = true;
                                        currentInputX++;

                                        // If the next tile already has a letter, skip it.
                                        while (Data.playground[currentInputX][currentInputY].getTileLetter() != '0') {
                                            lettersInput.append(Data.playground[currentInputX][currentInputY].getTileLetter());
                                            currentInputX++;
                                        }

                                    } else {
                                        // The last letter required is inputted.
                                        ifAbleToInput = false;
                                        ifChecking = true;
                                    }

                                    // If at least one letter is inputted, do not allow the user to change direction.
                                    if (currentInputX != endX && currentInputX <= 8) {
                                        ifChangeableDirection = false;
                                        // Set the tip.
                                        TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");
                                    }

                                    // Update the tip if the input is finished to remind the user to confirm.
                                    if (currentInputX == 8)
                                        TipBoard.gameTip.setText("Press Enter to confirm, or use Backspace to delete.");

                                    // Set the cursor color for the tile that is ready to receive the next letter.
                                    Data.setInputTileColor(currentInputX, currentInputY);
                                }
                            } else {
                                // If it is a normal input.
                                if (currentInputX <= targetX - 1 && ifAbleToInput) {
                                    Data.setSelectedTileColor(currentInputX, currentInputY);
                                    Data.playground[currentInputX][currentInputY].setTileLetter((char) (e.getKeyChar() - 32));
                                    lettersInput.append(e.getKeyChar());

                                    // Judge whether the input is finished or not.
                                    if (currentInputX < targetX - 1) {
                                        ifAbleToInput = true;
                                        currentInputX++;

                                        // If the next tile already has a letter, skip it.
                                        while (Data.playground[currentInputX][currentInputY].getTileLetter() != '0') {
                                            lettersInput.append(Data.playground[currentInputX][currentInputY].getTileLetter());
                                            currentInputX++;
                                        }
                                    } else {
                                        // The last letter required is inputted.
                                        ifAbleToInput = false;
                                        ifChecking = true;
                                    }

                                    // If at least one letter is inputted, do not allow the user to change direction.
                                    if ((Data.playground[currentInputX][currentInputY].getTileLetter() != '0' || currentInputX != endX)
                                            && currentInputX <= targetX - 1) {
                                        ifChangeableDirection = false;
                                        // Set the tip.
                                        TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");
                                    }

                                    // Update the tip if the input is finished to remind the user to confirm.
                                    if (currentInputX == targetX - 1)
                                        TipBoard.gameTip.setText("Press Enter to confirm, or use Backspace to delete.");

                                    // Set the cursor color for the tile that is ready to receive the next letter.
                                    Data.setInputTileColor(currentInputX, currentInputY);
                                }
                            }
                        }
                        case 4 -> { // Right direction.
                            if (currentInputX <= endX && ifAbleToInput) {
                                Data.setSelectedTileColor(currentInputX, currentInputY);
                                Data.playground[currentInputX][currentInputY].setTileLetter((char) (e.getKeyChar() - 32));
                                lettersInput.append(e.getKeyChar());

                                // Judge whether the input is finished or not.
                                if (currentInputX < endX) {
                                    ifAbleToInput = true;
                                    currentInputX++;

                                    // If the next tile already has a letter, skip it.
                                    while (Data.playground[currentInputX][currentInputY].getTileLetter() != '0') {
                                        lettersInput.append(Data.playground[currentInputX][currentInputY].getTileLetter());
                                        currentInputX++;
                                    }
                                } else {
                                    // The last letter required is inputted.
                                    ifAbleToInput = false;
                                    ifChecking = true;
                                }

                                // If at least one letter is inputted, do not allow the user to change direction.
                                if ((Data.playground[currentInputX][currentInputY].getTileLetter() != '0' || currentInputX != targetX)
                                        && currentInputX <= endX) {
                                    ifChangeableDirection = false;
                                    // Set the tip.
                                    TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");
                                }

                                // Update the tip if the input is finished to remind the user to confirm.
                                if (currentInputX == endX)
                                    TipBoard.gameTip.setText("Press Enter to confirm, or use Backspace to delete.");

                                // Set the cursor color for the tile that is ready to receive the next letter.
                                Data.setInputTileColor(currentInputX, currentInputY);
                            }
                        }
                    }
                    pressStatus = false;
                }

                // During input: Use Backspace key to delete the last inputted letter.
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && ifReadyToInput && pressStatus) {
                    switch (inputDirection) {
                        case 1 -> { // Up direction.
                            if (currentInputY > endY && ifAbleToInput) {
                                Data.setSelectedTileColor(currentInputX, currentInputY);
                                currentInputY--;

                                // If the last letter is already inputted before this turn, skip it.
                                while (Data.playground[currentInputX][currentInputY].getIfConfirmed()) {
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    currentInputY--;
                                }

                                Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                lettersInput.deleteCharAt(lettersInput.length() - 1);
                                Data.setInputTileColor(currentInputX, currentInputY);
                            }

                            // When the input is already finished before.
                            if (currentInputY == targetY - 1 && !ifAbleToInput) {
                                // Set the tip.
                                TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");

                                ifChecking = false;
                                Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                lettersInput.deleteCharAt(lettersInput.length() - 1);
                                ifAbleToInput = true;
                            }

                            // When the user deleted all the inputted letters.
                            if (currentInputY == endY && Data.playground[currentInputX][currentInputY].getTileLetter() == '0') {
                                ifChangeableDirection = true;
                                // Set the tip.
                                TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");

                                lettersInput.delete(0, lettersInput.length());
                            }
                        }
                        case 2 -> { // Down position.
                            if (Data.ifFirstTime) {
                                // If it is the first input.
                                if (currentInputY > 8 && ifAbleToInput) {
                                    Data.setSelectedTileColor(currentInputX, currentInputY);
                                    currentInputY--;
                                    Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    Data.setInputTileColor(currentInputX, currentInputY);
                                }

                                // When the input is already finished before.
                                if (currentInputY == endY && !ifAbleToInput) {
                                    // Set the tip.
                                    TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");

                                    ifChecking = false;
                                    Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    ifAbleToInput = true;
                                }

                                // When the user deleted all the inputted letters.
                                if (currentInputY == 8 && Data.playground[8][8].getTileLetter() == '0') {
                                    ifChangeableDirection = true;
                                    // Set the tip.
                                    TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");

                                    lettersInput.delete(0, lettersInput.length());
                                }

                            } else {
                                // If it is a normal input.
                                if (currentInputY > targetY + 1 && ifAbleToInput) {
                                    Data.setSelectedTileColor(currentInputX, currentInputY);
                                    currentInputY--;

                                    // If the last letter is already inputted before this turn, skip it.
                                    while (Data.playground[currentInputX][currentInputY].getIfConfirmed()) {
                                        lettersInput.deleteCharAt(lettersInput.length() - 1);
                                        currentInputY--;
                                    }

                                    Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    Data.setInputTileColor(currentInputX, currentInputY);
                                }

                                // When the input is already finished before.
                                if (currentInputY == endY && !ifAbleToInput) {
                                    // Set the tip.
                                    TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");

                                    ifChecking = false;
                                    Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    ifAbleToInput = true;
                                }

                                // When the user deleted all the inputted letters.
                                if (currentInputY == targetY + 1 && Data.playground[currentInputX][currentInputY].getTileLetter() == '0') {
                                    ifChangeableDirection = true;
                                    // Set the tip.
                                    TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");

                                    lettersInput.delete(0, lettersInput.length());
                                }
                            }
                        }
                        case 3 -> { // Left position.
                            if (currentInputX > endX && ifAbleToInput) {
                                Data.setSelectedTileColor(currentInputX, currentInputY);
                                currentInputX--;

                                // If the last letter is already inputted before this turn, skip it.
                                while (Data.playground[currentInputX][currentInputY].getIfConfirmed()) {
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    currentInputX--;
                                }

                                Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                lettersInput.deleteCharAt(lettersInput.length() - 1);
                                Data.setInputTileColor(currentInputX, currentInputY);
                            }

                            // When the input is already finished before.
                            if (currentInputX == targetX - 1 && !ifAbleToInput) {
                                // Set the tip.
                                TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");

                                ifChecking = false;
                                Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                lettersInput.deleteCharAt(lettersInput.length() - 1);
                                ifAbleToInput = true;
                            }

                            // When the user deleted all the inputted letters.
                            if (currentInputX == endX /*&& Data.playground[currentInputX][currentInputY].getTileLetter() == '0'*/) {
                                ifChangeableDirection = true;
                                // Set the tip.
                                TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");

                                lettersInput.delete(0, lettersInput.length());
                            }
                        }
                        case 4 -> { // Right position.
                            if (Data.ifFirstTime) {
                                // If it is the first input.
                                if (currentInputX > 8 && ifAbleToInput) {
                                    Data.setSelectedTileColor(currentInputX, currentInputY);
                                    currentInputX--;
                                    Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    Data.setInputTileColor(currentInputX, currentInputY);
                                }

                                // When the input is already finished before.
                                if (currentInputX == endX && !ifAbleToInput) {
                                    // Set the tip.
                                    TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");

                                    ifChecking = false;
                                    Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    ifAbleToInput = true;
                                }

                                // When the user deleted all the inputted letters.
                                if (currentInputX == 8 && Data.playground[8][8].getTileLetter() == '0') {
                                    ifChangeableDirection = true;
                                    // Set the tip.
                                    TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");

                                    lettersInput.delete(0, lettersInput.length());
                                }

                            } else {
                                // If it is a normal input.
                                if (currentInputX > targetX + 1 && ifAbleToInput) {
                                    Data.setSelectedTileColor(currentInputX, currentInputY);
                                    currentInputX--;

                                    // If the last letter is already inputted before this turn, skip it.
                                    while (Data.playground[currentInputX][currentInputY].getIfConfirmed()) {
                                        lettersInput.deleteCharAt(lettersInput.length() - 1);
                                        currentInputX--;
                                    }

                                    Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    Data.setInputTileColor(currentInputX, currentInputY);
                                }

                                // When the input is already finished before.
                                if (currentInputX == endX && !ifAbleToInput) {
                                    // Set the tip.
                                    TipBoard.gameTip.setText("Keep inputting, or use Backspace to delete.");

                                    ifChecking = false;
                                    Data.playground[currentInputX][currentInputY].setTileLetter('0');
                                    lettersInput.deleteCharAt(lettersInput.length() - 1);
                                    ifAbleToInput = true;
                                }

                                // When the user deleted all the inputted letters.
                                if (currentInputX == targetX + 1 && Data.playground[currentInputX][currentInputY].getTileLetter() == '0') {
                                    ifChangeableDirection = true;
                                    // Set the tip.
                                    TipBoard.gameTip.setText("You can start inputting, or use Arrow Key to re-select the direction.");

                                    lettersInput.delete(0, lettersInput.length());
                                }

                            }
                        }
                    }
                    pressStatus = false;
                }

                // After the user inputs all the letters: Press Enter to confirm.
                if (e.getKeyCode() == KeyEvent.VK_ENTER && ifChecking) {
                    int lettersBefore = 0;
                    StringBuilder word = new StringBuilder();
                    word.append(lettersInput);

                    // Check if it is the first input.
                    // If not, the user may have entered a prefix or suffix of an already existing word.
                    // So the entire word needs to be fetched.
                    if (!Data.ifFirstTime) {
                        int checkX, checkY;
                        switch (inputDirection) {
                            case 1 -> { // Up direction.

                                // Obtain all the letters BEFORE the input part (if there is any).
                                checkY = endY - 1;
                                if (endY != 1) {
                                    while (Data.playground[endX][checkY].getTileLetter() != '0') {
                                        word.insert(0, Data.playground[endX][checkY].getTileLetter());
                                        checkY--;
                                        lettersBefore++;
                                        if (checkY == 0) break;
                                    }
                                }

                                // Obtain all the letters AFTER the input part (if there is any).
                                checkY = targetY;
                                while (Data.playground[endX][checkY].getTileLetter() != '0') {
                                    word.append(Data.playground[endX][checkY].getTileLetter());
                                    checkY++;
                                    if (checkY == 16) break;
                                }
                            }
                            case 2 -> { // Down direction.

                                // Obtain all the letters BEFORE the input part (if there is any).
                                checkY = targetY;
                                while (Data.playground[endX][checkY].getTileLetter() != '0') {
                                    word.insert(0, Data.playground[endX][checkY].getTileLetter());
                                    checkY--;
                                    lettersBefore++;
                                    if (checkY == 0) break;
                                }

                                // Obtain all the letters AFTER the input part (if there is any).
                                checkY = endY + 1;
                                if (checkY != 16) {
                                    while (Data.playground[endX][checkY].getTileLetter() != '0') {
                                        word.append(Data.playground[endX][checkY].getTileLetter());
                                        checkY++;
                                        if (checkY == 16) break;
                                    }
                                }
                            }
                            case 3 -> { // Left direction.

                                // Obtain all the letters BEFORE the input part (if there is any).
                                checkX = endX - 1;
                                if (endX != 1) {
                                    while (Data.playground[checkX][endY].getTileLetter() != '0') {
                                        word.insert(0, Data.playground[checkX][endY].getTileLetter());
                                        checkX--;
                                        lettersBefore++;
                                        if (checkX == 0) break;
                                    }
                                }

                                // Obtain all the letters AFTER the input part (if there is any).
                                checkX = targetX;
                                while (Data.playground[checkX][endY].getTileLetter() != '0') {
                                    word.append(Data.playground[checkX][endY].getTileLetter());
                                    checkX++;
                                    if (checkX == 16) break;
                                }

                            }
                            case 4 -> { // Right direction.

                                // Obtain all the letters BEFORE the input part (if there is any).
                                checkX = targetX;
                                while (Data.playground[checkX][endY].getTileLetter() != '0') {
                                    word.insert(0, Data.playground[checkX][endY].getTileLetter());
                                    checkX--;
                                    lettersBefore++;
                                    if (checkX == 0) break;
                                }

                                // Obtain all the letters AFTER the input part (if there is any).
                                checkX = endX + 1;
                                if (checkX != 16) {
                                    while (Data.playground[checkX][endY].getTileLetter() != '0') {
                                        word.append(Data.playground[checkX][endY].getTileLetter());
                                        checkX++;
                                        if (checkX == 16) break;
                                    }
                                }
                            }
                        }
                    }

                    // Check if the word is valid.
                    String wordCheck = String.valueOf(word).toUpperCase();
                    boolean charCheckValid = Data.checkDictionary(wordCheck);

                    if (charCheckValid) {
                        // If the entire word is checked valid:
                        // Refresh all the letters.
                        for (int i = 1; i <= 15; i++) {
                            for (int j = 1; j <= 15; j++) {
                                Data.playground[i][j].setIfLastAdded(false);
                            }
                        }

                        // Prepare for printing the letters.
                        int x = 0, y = 0;
                        switch (inputDirection) {
                            case 1 -> {
                                x = targetX;
                                y = endY - lettersBefore;
                            }
                            case 2 -> {
                                if (Data.ifFirstTime) {
                                    x = 8;
                                    y = 8;
                                } else {
                                    x = targetX;
                                    y = targetY - lettersBefore + 1;
                                }
                            }
                            case 3 -> {
                                x = endX - lettersBefore;
                                y = targetY;
                            }
                            case 4 -> {
                                if (Data.ifFirstTime) {
                                    x = 8;
                                    y = 8;
                                } else {
                                    x = targetX - lettersBefore + 1;
                                    y = targetY;
                                }
                            }
                        }

                        int wordScoreCount = 0;
                        int finalScoreCount;
                        int maxWordValue = 1;

                        for (int i = 0; i < word.length(); i++) {
                            // Set the tile letter.
                            if (word.charAt(i) >= 'a')
                                Data.playground[x][y].setTileLetter((char) (word.charAt(i) - 32));
                            else Data.playground[x][y].setTileLetter(word.charAt(i));

                            // Decide the maximum word value (Double or Triple).
                            if (Data.playground[x][y].getWordValue() > maxWordValue && Data.playground[x][y].getIfUnusedWordValue()) {
                                maxWordValue = Data.playground[x][y].getWordValue();
                            }

                            wordScoreCount += Data.toScore(Data.playground[x][y].getTileLetter(), x, y);

                            // Set the last-added color of entire word to remind all the players.
                            Data.playground[x][y].setIfLastAdded(true);

                            switch (inputDirection) {
                                case 1, 2 -> y++;
                                case 3, 4 -> x++;
                            }
                        }

                        // Set the default tile background color for all tiles.
                        Data.setAllDefaultColor();

                        // Set all tile letters as confirmed.
                        Data.setAllConfirmed();

                        // Count the final score and add it to the player.
                        finalScoreCount = wordScoreCount * maxWordValue;
                        Data.playerList.get(Data.currentPlayer - 1).addScore(finalScoreCount);
                        System.out.println(Data.playerList.get(Data.currentPlayer - 1).getName()
                                + ", you get " + finalScoreCount
                                + " points for the word '" + wordCheck + "'!");

                        // Congratulate the current player if he/she gets the highest score after this turn.
                        if (Data.playerList.get(Data.currentPlayer - 1).getScore() > Data.currentMaxScore) {
                            System.out.println("Congrats! Currently you have the highest score.");
                            Data.currentMaxScore = Data.playerList.get(Data.currentPlayer - 1).getScore();
                            Data.currentMaxPlayer = Data.currentPlayer;
                        }

                        // Check if the current player reaches the limit score. If so, the game will be over.
                        if (Data.limitScore != 0 && Data.currentMaxScore >= Data.limitScore) {
                            System.out.println(" ");
                            System.out.println("Game Over! "
                                    + Data.playerList.get(Data.currentPlayer - 1).getName()
                                    + " has reached the limit score!");
                            Main.endGame(true);
                        }

                        // Turn to the next player.
                        Data.currentPlayer++;
                        if (Data.currentPlayer > Data.getPlayerNum())
                            Data.currentPlayer = 1;

                        // Reset all the status to default value.
                        ifReadyToInput = false;
                        ifAbleToInput = true;
                        Data.ifFirstTime = false;
                        ifSelecting = false;
                        ifChecking = false;
                        ifChangeableDirection = true;
                        ifSelectedDirection = false;
                        lettersInput.delete(0, lettersInput.length());
                        Data.passCount = 0;

                        // Display the chessboard in the console.
                        Data.displayChessboard();

                        // Update the score board.
                        ScoreBoard.updateScore();

                        // Remind the next player, both in the frame and in the console.
                        TipBoard.gameTip.setText("It is now "
                                + Data.playerList.get(Data.currentPlayer - 1).getName()
                                + "'s turn. Use Left Click to select the target point.");

                        System.out.println(" ");
                        System.out.println("Now it is "
                                + Data.playerList.get(Data.currentPlayer - 1).getName()
                                + "'s turn. Please input on the chessboard.");
                        System.out.println(" ");
                    } else {
                        // If the word cannot be found in the dictionary.
                        TipBoard.gameTip.setText(wordCheck + " is not a valid word. Use Backspace to delete.");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * Refreshes and displays the chessboard.
     *
     * @param gr a member of the <code>Graphics</code> class
     */
    @Override
    public void paintComponent(Graphics gr) {
        timer.start();
        super.paintComponent(gr);
        setFocusable(true);
        requestFocusInWindow();
        Graphics2D g2d = (Graphics2D) gr;

        // Draw the default frame for each tile.
        for (int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                g2d.setColor(Data.playground[i][j].getBackgroundColor());
                g2d.fillRect(i * tileWidth, j * tileHeight, tileWidth, tileHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(i * tileWidth, j * tileHeight, tileWidth, tileHeight);
            }
        }

        // Draw the frame for tiles with special values (e.g, Double Letter, Triple Word).
        for (int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                if (Data.playground[i][j].getLetterValue() == 2) {
                    g2d.setColor(Color.CYAN);
                    g2d.drawRect(i * tileWidth + 1, j * tileHeight + 1, tileWidth - 2, tileHeight - 2);
                } else if (Data.playground[i][j].getLetterValue() == 3) {
                    g2d.setColor(Color.BLUE);
                    g2d.drawRect(i * tileWidth + 1, j * tileHeight + 1, tileWidth - 2, tileHeight - 2);
                } else if (Data.playground[i][j].getWordValue() == 2) {
                    g2d.setColor(Color.ORANGE);
                    g2d.drawRect(i * tileWidth + 1, j * tileHeight + 1, tileWidth - 2, tileHeight - 2);
                } else if (Data.playground[i][j].getWordValue() == 3) {
                    g2d.setColor(Color.RED);
                    g2d.drawRect(i * tileWidth + 1, j * tileHeight + 1, tileWidth - 2, tileHeight - 2);
                }
            }
        }

        // Draw the letters for each tile (if it has a letter).
        g2d.setFont(new Font("Arial", Font.PLAIN, 15));
        for (int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                if (Data.playground[i][j].getTileLetter() != '0') {
                    if (Data.playground[i][j].getIfLastAdded())  //If the letter is last added
                        g2d.setColor(Color.MAGENTA);
                    else
                        g2d.setColor(Color.BLACK);
                    g2d.drawString(String.valueOf(Data.playground[i][j].getTileLetter()), i * tileWidth + 8, j * tileHeight + tileWidth - 6);
                }
            }
        }
    }
}