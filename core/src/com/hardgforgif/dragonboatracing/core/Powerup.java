package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.GameData;

/**
 * Class representing a powerup, extending from Obstacle.
 * 
 * @see Obstacle
 * 
 * @since 2
 * @version 2
 * @author Team 8
 * @author Josh Stafford
 * 
 */
public class Powerup extends Obstacle {

    private int type;
    public static int typesNo = 3;

    /**
     * Construct a powerup with given type.
     * 
     * @param type The powerup type, 1 Health, 2 Stamina, 3 Speed
     * 
     * @since 2
     * @version 2
     * @author Team 8
     * @author Josh Stafford
     * 
     */
    Powerup(int type) {

        super("Powerups/powerup" + String.valueOf(type) + ".png");
        this.type = type;

    }

    /**
     * Create a body for the obstacle (powerup).
     * 
     * @param world  The world to create the body in
     * @param posX   The x position to create the body at
     * @param posY   The y position to create the body at
     * @param width  The width of the body
     * @param height The height of the body
     * 
     * @since 2
     * @version 2
     * @author Team 8
     * @author Josh Stafford
     */
    public void createObstacleBody(World world, float posX, float posY, float width, float height) {

        // MODIFIED: set the sprite, body, velocity and user data
        obstacleSprite = new Sprite(obstacleTexture);
        this.obstacleBody = createBox(posX, posY, width, height, world);
        this.obstacleBody.setLinearVelocity(0, -0.2f);
        obstacleBody.setUserData(this);

        // MODIFIED: set the initial position of the sprite
        obstacleSprite.setPosition(
                (obstacleBody.getPosition().x * GameData.METERS_TO_PIXELS) - obstacleSprite.getWidth() / 2,
                (obstacleBody.getPosition().y * GameData.METERS_TO_PIXELS) - obstacleSprite.getHeight() / 2);

    }

    /**
     * Create a box fixture for collisions.
     * 
     * @param x      The x position
     * @param y      The y position
     * @param width  The width of the box
     * @param height The height of the box
     * @param world  The world to create the box in
     * @return A Body with a box fixture
     * 
     * @since 2
     * @version 2
     * @author Team 8
     * @author Josh Stafford
     */
    private Body createBox(float x, float y, float width, float height, World world) {

        // MODIFIED: Creates a body and body definition (properties for a body)
        Body body;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        // MODIFIED: Sets the position of the body according to the scale of the game
        def.position.set(x, y);

        // MODIFIED: Fixes the rotation of the object
        def.fixedRotation = true;

        // MODIFIED: Adds the body to the game world
        body = world.createBody(def);

        // Sets the shape of the body to be a box polygon
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        // MODIFIED: Fixes the box to the body
        body.createFixture(shape, 1.0f);

        // MODIFIED: Disposes of the used shape
        shape.dispose();

        return body;

    }

    /**
     * Get the type number for the powerup
     * 
     * @return The powerup type
     * 
     * @since 2
     * @version 2
     * @author Team 8
     * @author Josh Stafford
     * 
     */
    public int getType() {

        return this.type;

    }

}
