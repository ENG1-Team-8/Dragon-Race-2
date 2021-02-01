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

public class Powerup extends Obstacle {

    public Sprite obstacleSprite;
    private Texture obstacleTexture;
    public Body obstacleBody;
    private int type;
    public static int typesNo = 3;

    Powerup(String textureName) {

        super(textureName);
        this.randomiseType();

    }

    Powerup(String textureName, int type) {

        super(textureName);
        this.type = type;

    }

    public void createObstacleBody(World world, float posX, float posY) {

    }

    public void randomiseType() {

        Random random = new Random();
        this.type = random.nextInt(Powerup.typesNo);

    }

    public void setType(int t) {

        this.type = t;

    }

    public int getType() {

        return this.type;

    }

}
