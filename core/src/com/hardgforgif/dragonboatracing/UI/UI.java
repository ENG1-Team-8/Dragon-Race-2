package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

/**
 * An abstract class from which to extend concrete UI classes.
 * 
 * @since 1
 * @version 1
 * @author Team 10
 * 
 */
public abstract class UI {

    /**
     * This method draws UI elements that are not related to the player boat.
     * 
     * @param batch       The batch to draw to
     * @param mousePos    The location of the mouse, necessary for buttons
     * @param screenWidth The width of the screen
     * @param delta       The time passed since the last frame
     * 
     * @since 1
     * @version 1
     * @author Team 10
     * 
     */
    public abstract void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta);

    /**
     * Draws UI elements related to the player boat.
     * 
     * @param batch      The batch to draw to
     * @param playerBoat The player boat
     * 
     * @since 1
     * @version 1
     * @author Team 10
     */
    public abstract void drawPlayerUI(Batch batch, Player playerBoat);

    /**
     * Handles input given by the user.
     * 
     * @param screenWidth The width of the screen
     * @param mousePos    the location of the mouse when it is clicked
     * 
     * @since 1
     * @version 1
     * @author Team 10
     */
    public abstract void getInput(float screenWidth, Vector2 mousePos);

    /**
     * Plays the current music available in the GameData static class.
     * 
     * @since 1
     * @version 1
     * @author Team 10
     */
    public void playMusic() {

        if (!GameData.music.isPlaying()) {
            GameData.music.play();
        }

    }

}
