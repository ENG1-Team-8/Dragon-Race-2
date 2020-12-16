package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.BodyEditorLoader;

public class Boat {
    // Boat specs
    public float robustness;
    public float stamina = 120f;
    public float maneuverability;
    public float speed;
    public float acceleration;

    public float current_speed = 20f;
    public float turningSpeed = 0.25f;
    public float targetAngle = 0f;

    public Sprite boatSprite;
    public Texture boatTexture;
    public Body boatBody;

    private TextureAtlas textureAtlas;
    private Animation animation;

    public Lane lane;
    public float leftLimit;
    public float rightLimit;


    public Boat(float robustness, float speed, float acceleration, float maneuverability, int boatType, Lane lane) {
        this.robustness = robustness;
        this.speed = speed;
        this.acceleration = acceleration;
        this.maneuverability = maneuverability;
        turningSpeed *= this.maneuverability / 100;


        boatTexture = new Texture("Boat" + (boatType + 1) + ".png");

        textureAtlas = new TextureAtlas(Gdx.files.internal("Boats/Boat" + (boatType + 1) +  ".atlas"));
        animation = new Animation(1/15f, textureAtlas.getRegions());

        this.lane = lane;
    }

    /**
     * Creates a boat body
     * @param world World to create the body in
     * @param posX x location of the body, in meters
     * @param posY y location of the body, in meters
     * @param bodyFile the name of the box2D editor json file for the body fixture
     */
    public void createBoatBody(World world, float posX, float posY, String bodyFile){
        boatSprite = new Sprite(boatTexture);
        boatSprite.scale(-0.8f);

        // Define the body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX, posY);

        // Create the body
        boatBody = world.createBody(bodyDef);
        // Mark the body as a boat's body
        boatBody.setUserData(this);

        // Load the body fixture from box2D editor
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(bodyFile));
        FixtureDef fixtureDef = new FixtureDef();

        // Set the physical properties of the body
        fixtureDef.density = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        // Attach the fixture to the body
        float scale = boatSprite.getWidth() / GameData.METERS_TO_PIXELS * boatSprite.getScaleX();
        loader.attachFixture(boatBody, "Name", fixtureDef, scale);
    }

    /**
     * Draws the boat on the batch, with animations
     * @param batch The batch to draw on
     */
    public void drawBoat(Batch batch){
        batch.begin();
        batch.draw((TextureRegion) animation.getKeyFrame(GameData.currentTimer, true), boatSprite.getX(), boatSprite.getY(), boatSprite.getOriginX(),
                boatSprite.getOriginY(),
                boatSprite.getWidth(), boatSprite.getHeight(), boatSprite.getScaleX(), boatSprite.
                        getScaleY(), boatSprite.getRotation());
        batch.end();
    }

    /**
     * Updates the boat's limits in the lane based on it's location
     */
    public void updateLimits(){
        int i;
        for (i = 1; i < lane.leftIterator; i++){
            if (lane.leftBoundry[i][0] > boatSprite.getY() + (boatSprite.getHeight() / 2)) {
                break;
            }
        }
        leftLimit = lane.leftBoundry[i - 1][1];

        for (i = 1; i < lane.rightIterator; i++){
            if (lane.rightBoundry[i][0] > boatSprite.getY() + (boatSprite.getHeight() / 2)) {
                break;
            }
        }
        rightLimit = lane.rightBoundry[i - 1][1];
    }

    public float[] getLimitsAt(float yPosition){
        float[] lst = new float[2];
        int i;
        for (i = 1; i < lane.leftIterator; i++){
            if (lane.leftBoundry[i][0] > yPosition) {
                break;
            }
        }
        lst[0] = lane.leftBoundry[i - 1][1];

        for (i = 1; i < lane.rightIterator; i++){
            if (lane.rightBoundry[i][0] > yPosition) {
                break;
            }
        }
        lst[1] = lane.rightBoundry[i - 1][1];
        return lst;
    }

    /**
     * Checks if the boat finished the race
     * @return True if the boat passed the finish line, false otherwise
     */
    public boolean hasFinished(){
        if (boatSprite.getY() + boatSprite.getHeight() / 2 > 9000f)
            return true;
        return false;
    }

    /**
     * Moves the boat forward, based i=on it's rotation
     */
    public void moveBoat(){
        current_speed += 0.15f * (acceleration/90)  * (stamina/100);
        if (current_speed > speed)
            current_speed = speed;
        if (stamina < 70f && current_speed > speed * 0.8f)
            current_speed = speed * 0.8f;
        if (current_speed < 0)
            current_speed = 0;


        // Get the coordinates of the center of the boat
        float originX = boatBody.getPosition().x * GameData.METERS_TO_PIXELS;
        float originY = boatBody.getPosition().y * GameData.METERS_TO_PIXELS;

        // First we need to calculate the position of the player's head (the front of the boat)
        // So we can move him based on this and not the center of the boat
        Vector2 boatHeadPos = new Vector2();
        float radius = boatSprite.getHeight()/2;
        boatHeadPos.set(originX + radius * MathUtils.cosDeg(boatSprite.getRotation() + 90),
                originY + radius * MathUtils.sinDeg(boatSprite.getRotation() + 90));

        // Create the vector that shows which way we need to move
        Vector2 target = new Vector2();

        // Calculate the x and y positions of the direction vector, based on the rotation of the boat
        double auxAngle = boatSprite.getRotation() % 90;
        if (boatSprite.getRotation() < 90 || boatSprite.getRotation() >= 180 && boatSprite.getRotation() < 270)
            auxAngle = 90 - auxAngle;
        auxAngle = auxAngle * MathUtils.degRad;
        float x = (float) (Math.cos(auxAngle) * speed);
        float y = (float) (Math.sin(auxAngle) * speed);

        // Build the direction vector based on the position of the player's head
        if (boatSprite.getRotation() < 90)
            target.set(boatHeadPos.x - x, boatHeadPos.y + y);
        else if (boatSprite.getRotation() < 180)
            target.set(boatHeadPos.x - x, boatHeadPos.y - y);
        else if (boatSprite.getRotation() < 270)
            target.set(boatHeadPos.x + x, boatHeadPos.y - y);
        else
            target.set(boatHeadPos.x + x, boatHeadPos.y + y);

        Vector2 direction = new Vector2();
        Vector2 velocity = new Vector2();
        Vector2 movement = new Vector2();

        direction.set(target).sub(boatHeadPos).nor();
        velocity.set(direction).scl(current_speed);
        movement.set(velocity).scl(Gdx.graphics.getDeltaTime());

        boatBody.setLinearVelocity(movement);
    }

    /**
     * Rotate the boat until it reaches the given angle, based on it's turning speed and stamina
     * @param angle angle to rotate to
     */
    public void rotateBoat(float angle){
        // Calculate the difference between the target angle and the current rotation of the boat
        float angleDifference = angle - boatBody.getAngle() * MathUtils.radDeg;

        if (Math.abs(angleDifference) < turningSpeed) {
            boatBody.setTransform(boatBody.getPosition(), angle * MathUtils.degRad);
            return;
        }

        // Create the new angle we want the player to be rotated to every frame, based on the turning speed and stamina
        float newAngle = boatSprite.getRotation();

        if (angleDifference < 0)
            newAngle += turningSpeed * (-1) * (this.stamina / 70);
        else if (angleDifference > 0)
            newAngle += turningSpeed * (this.stamina / 70);

        boatBody.setTransform(boatBody.getPosition(), newAngle * MathUtils.degRad);
    }
}
