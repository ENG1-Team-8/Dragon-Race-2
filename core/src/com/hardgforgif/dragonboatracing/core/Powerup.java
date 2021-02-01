package com.hardgforgif.dragonboatracing.core;

import java.util.Random;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Powerup extends Obstacle {

    private int type;
    public static int typesNo = 3;

    Powerup(int type) {

        super("powerup" + String.valueOf(type) + ".png");
        this.type = type;

    }

    public void createObstacleBody(World world, int posX, int posY, int width, int height) {

        this.obstacleBody = createBox(posX, posY, width, height, false, world);
        this.obstacleBody.setLinearVelocity(0, -0.2f);

        obstacleBody.setUserData(this);

    }

    private Body createBox(int x, int y, int width, int height, boolean isStatic, World world) {

        // Creates a body and body definition (properties for a body)
        Body body;
        BodyDef def = new BodyDef();

        // Allows the programmer to define whether a body is static or not
        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }

        // Sets the position of the body according to the scale of the game
        def.position.set(x / GameData.TILES_TO_METERS, y / GameData.TILES_TO_METERS);

        // Fixes the rotation of the object
        def.fixedRotation = true;

        // Adds the body to the game world
        body = world.createBody(def);

        // Sets the shape of the body to be a box polygon
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f / GameData.TILES_TO_METERS, height / 2f / GameData.TILES_TO_METERS);

        // Fixes the box to the body
        body.createFixture(shape, 1.0f);

        // Disposes of the used shape
        shape.dispose();
        return body;
    }

    public int getType() {

        return this.type;

    }

}
