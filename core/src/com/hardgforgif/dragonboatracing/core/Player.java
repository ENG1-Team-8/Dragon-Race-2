package com.hardgforgif.dragonboatracing.core;

import com.hardgforgif.dragonboatracing.GameData;

/**
 * Class representing the player's boat, extending from Boat.
 * 
 * @see Boat
 * 
 * @since 1
 * @version 1
 * @author Team 10
 * 
 */
public class Player extends Boat {

    /**
     * Construct a player object.
     * 
     * @param robustness      The maximum boat 'health'
     * @param speed           The boat's maximum speed
     * @param acceleration    The boat's acceleration
     * @param maneuverability The boat's turning speed
     * @param boatType        The array position for boat stats
     * @param lane            The lane the boat is racing in
     * 
     * @since 1
     * @version 1
     * @author Team 10
     * 
     */
    public Player(float robustness, float speed, float acceleration, float maneuverability, int boatType, Lane lane) {

        // Construct a boat with the player boat's parameters
        super(robustness, speed, acceleration, maneuverability, boatType, lane);

    }

    /**
     * Updates the player based on the input given.
     * 
     * @param pressedKeys W, A, S, D pressed status
     * @param delta       Time since last frame
     * 
     * @since 1
     * @version 1
     * @author Team 10
     * 
     */
    public void updatePlayer(boolean[] pressedKeys, float delta) {

        // Check which angle you need to rotate to, then apply the roation
        if (pressedKeys[1])
            targetAngle = 90f;
        else if (pressedKeys[3])
            targetAngle = -90f;
        else
            targetAngle = 0f;
        rotateBoat(targetAngle);

        // Move the boat
        moveBoat();

        // Update the sprite location to match the body
        boatSprite.setRotation((float) Math.toDegrees(boatBody.getAngle()));
        boatSprite.setPosition((boatBody.getPosition().x * GameData.METERS_TO_PIXELS) - boatSprite.getWidth() / 2,
                (boatBody.getPosition().y * GameData.METERS_TO_PIXELS) - boatSprite.getHeight() / 2);

        // Update the lane limits
        updateLimits();
        if (stamina > 30f)
            stamina -= 1.5 * delta;

    }

}
