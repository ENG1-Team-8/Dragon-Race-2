package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.BodyEditorLoader;
import com.hardgforgif.dragonboatracing.GameData;

/**
 * Class representing an obstacle.
 * 
 * @since 1
 * @version 2
 * @author Team 10
 * @author Josh Stafford
 */
public class Obstacle {

    public Sprite obstacleSprite;
    public Texture obstacleTexture;
    public Body obstacleBody;

    /**
     * Construct an obstacle object.
     * 
     * @param textureName The image path for the texture
     * 
     * @since 1
     * @version 1
     * @author Team 10
     * 
     */
    public Obstacle(String textureName) {

        obstacleTexture = new Texture(textureName);

    }

    /**
     * Creates a new obstacle body.
     * 
     * @param world    World to create the body in
     * @param posX     x location of the body, in meters
     * @param posY     y location of the body, in meters
     * @param bodyFile the name of the box2D editor json file for the body fixture
     * 
     * @since 1
     * @version 2
     * @author Team 10
     * @author Josh Stafford
     */
    public void createObstacleBody(World world, float posX, float posY, String bodyFile, float scale) {

        obstacleSprite = new Sprite(obstacleTexture);
        obstacleSprite.scale(scale);

        BodyDef bodyDef = new BodyDef();

        // MODIFIED: now changed to use dynamic bodies
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(posX, posY);
        obstacleBody = world.createBody(bodyDef);

        // MODIFIED: now gives each obstacle a set velocity downwards
        obstacleBody.setLinearVelocity(0, -0.2f);

        obstacleBody.setUserData(this);

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(bodyFile));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        scale = obstacleSprite.getWidth() / GameData.METERS_TO_PIXELS * obstacleSprite.getScaleX();
        loader.attachFixture(obstacleBody, "Name", fixtureDef, scale);

        obstacleSprite.setPosition(
                (obstacleBody.getPosition().x * GameData.METERS_TO_PIXELS) - obstacleSprite.getWidth() / 2,
                (obstacleBody.getPosition().y * GameData.METERS_TO_PIXELS) - obstacleSprite.getHeight() / 2);

    }

    /**
     * Draw the obstacle.
     * 
     * @param batch Batch to draw on
     * 
     * @since 1
     * @version 1
     * @author Team 10
     * 
     */
    public void drawObstacle(Batch batch) {

        obstacleSprite.setPosition(
                (obstacleBody.getPosition().x * GameData.METERS_TO_PIXELS) - obstacleSprite.getWidth() / 2,
                (obstacleBody.getPosition().y * GameData.METERS_TO_PIXELS) - obstacleSprite.getHeight() / 2);
        batch.begin();
        batch.draw(obstacleSprite, obstacleSprite.getX(), obstacleSprite.getY(), obstacleSprite.getOriginX(),
                obstacleSprite.getOriginY(), obstacleSprite.getWidth(), obstacleSprite.getHeight(),
                obstacleSprite.getScaleX(), obstacleSprite.getScaleY(), obstacleSprite.getRotation());
        batch.end();

    }

}