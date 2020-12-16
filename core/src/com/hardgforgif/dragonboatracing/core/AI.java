package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;

public class AI extends Boat{

    public Vector2 laneChecker;
    public Vector2 objectChecker;
    private boolean isDodging = false;
    private boolean isTurning = false;
    private float detectedObstacleYPos;

    public AI(float robustness, float stamina, float handling, float speed, int boatType, Lane lane) {
        super(robustness, stamina, handling, speed, boatType, lane);
        this.robustness *= GameData.difficulty[GameData.currentLeg];
        this.stamina *= GameData.difficulty[GameData.currentLeg];
        this.maneuverability *= GameData.difficulty[GameData.currentLeg];
        this.speed *= GameData.difficulty[GameData.currentLeg];
        this.acceleration *= GameData.difficulty[GameData.currentLeg];
    }

    /**
     * Get a point at a given distance in front of the boat
     * @param distance The distance to the new point
     * @return
     */
    private Vector2 getAIPredictionVector(float distance) {
        // Get the coordinates of the center of the boat
        float originX = boatBody.getPosition().x * GameData.METERS_TO_PIXELS;
        float originY = boatBody.getPosition().y * GameData.METERS_TO_PIXELS;

        // First we need to calculate the position of the boat's head (the front of the boat)
        Vector2 boatHeadPos = new Vector2();
        float radius = (boatSprite.getHeight() * boatSprite.getScaleY())/2;
        boatHeadPos.set(originX + radius * MathUtils.cosDeg(boatSprite.getRotation() + 90),
                originY + radius * MathUtils.sinDeg(boatSprite.getRotation() + 90));

        // Calculate the x and y positions of the direction vector, based on the rotation of the boat
        double auxAngle = boatSprite.getRotation() % 90;
        if (boatSprite.getRotation() < 90 || boatSprite.getRotation() >= 180 && boatSprite.getRotation() < 270)
            auxAngle = 90 - auxAngle;
        auxAngle = auxAngle * MathUtils.degRad;
        float x = (float) (Math.cos(auxAngle) * distance);
        float y = (float) (Math.sin(auxAngle) * distance);

        // Build the target vector based on the position of the player's head
        Vector2 target = new Vector2();
        if (boatSprite.getRotation() < 90)
            target.set(boatHeadPos.x - x, boatHeadPos.y + y);
        else if (boatSprite.getRotation() < 180)
            target.set(boatHeadPos.x - x, boatHeadPos.y - y);
        else if (boatSprite.getRotation() < 270)
            target.set(boatHeadPos.x + x, boatHeadPos.y - y);
        else
            target.set(boatHeadPos.x + x, boatHeadPos.y + y);

        return target;
    }

    /**
     * Sets the target angle attribute to keep the boat in lane, based on the limits at the predicted location
     * @param predictLimits the limits of the lane at the predicted location
     */
    private void stayInLane(float[] predictLimits){
        float laneWidth = predictLimits[1] - predictLimits[0];
        float middleOfLane = predictLimits[0] + laneWidth / 2;

        // If the predicted location is outside the lane, rotate the boat
        if (laneChecker.x < predictLimits[0] && boatSprite.getRotation() == 0){
            targetAngle = -15f;
            isTurning = true;
        }

        else if (laneChecker.x > predictLimits[1] && boatSprite.getRotation() == 0){
            targetAngle = 15f;
            isTurning = true;
        }

        // If the predicted location is far enough into the lane, straighten the boat
        else if (laneChecker.x < middleOfLane - laneWidth / 4 && boatSprite.getRotation() > 0){
            targetAngle = 0f;
            isTurning = false;
        }

        else if (laneChecker.x > middleOfLane + laneWidth / 4  && boatSprite.getRotation() < 0){
            targetAngle = 0f;
            isTurning = false;
        }

        // Apply the rotation
        rotateBoat(targetAngle);
        boatSprite.setRotation((float)Math.toDegrees(boatBody.getAngle()));
    }

    /**
     * Checks for obstacles in range of the AI
     * @return True if there's an obstacle in range, false otherwise
     */
    private boolean obstaclesInRange(){
        for (Obstacle obstacle : this.lane.obstacles){
            // Get the obstacles attributes
            float width = obstacle.obstacleSprite.getWidth() * obstacle.obstacleSprite.getScaleX();
            float height = obstacle.obstacleSprite.getHeight() * obstacle.obstacleSprite.getScaleY();
            float posX = obstacle.obstacleSprite.getX() + obstacle.obstacleSprite.getWidth() / 2 - width / 2;
            float posY = obstacle.obstacleSprite.getY() + obstacle.obstacleSprite.getHeight() / 2 - height / 2;

            // Get the boat  attributes
            float boatLeftX = objectChecker.x - boatSprite.getWidth() / 2 * boatSprite.getScaleX();
            float boatRightX = objectChecker.x + boatSprite.getWidth() / 2 * boatSprite.getScaleX();

            // Check for obstacles
            if (boatRightX >= posX && boatLeftX <= posX + width &&
                    objectChecker.y >= posY && boatSprite.getY() + boatSprite.getHeight() / 2 <= posY){
                detectedObstacleYPos = posY;
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the target angle attribute to keep the boat from hitting an obstacle
     */
    private void dodgeObstacles(){
        if (obstaclesInRange()){
            float boatPosX = boatSprite.getX() + boatSprite.getWidth() / 2;

            // If the boat is turning into an object, stop turning
            if (isTurning){
                targetAngle = 0f;
                isTurning = false;
            }
            // Otherwise check which way is better to dodge, then set the rotation
            else if (rightLimit - boatPosX < boatPosX - leftLimit){
                targetAngle = 15f;
            }
            else
                targetAngle = -15f;

            // Mark that the AI is currently dodging an obstacle
            isDodging = true;

            //Apply the roation
            rotateBoat(targetAngle);
            boatSprite.setRotation((float)Math.toDegrees(boatBody.getAngle()));
        }
    }

    /**
     * Updates the AI to apply appropriate movement and rotation
     * @param delta Time since last frame
     */
    public void updateAI(float delta) {
        // Start by matching the location of the sprite with the location of the sprite
        boatSprite.setPosition((boatBody.getPosition().x * GameData.METERS_TO_PIXELS) - boatSprite.getWidth() / 2,
                (boatBody.getPosition().y * GameData.METERS_TO_PIXELS) - boatSprite.getHeight() / 2);

        // Create the prediction vectors
        laneChecker = getAIPredictionVector(400f);
        objectChecker = getAIPredictionVector(300f);

        // Store the limits of the lane at the laneChecker prediction vector
        float[] predictLimits = getLimitsAt(laneChecker.y);

        // If the Ai is dodging an obstacle
        if (isDodging){
            // Apply rotation
            rotateBoat(targetAngle);
            boatSprite.setRotation((float)Math.toDegrees(boatBody.getAngle()));

            float boatFrontLocation = boatSprite.getY() + boatSprite.getHeight() / 2 +
                    boatSprite.getHeight() / 2 * boatSprite.getScaleY();

            // If the front of the boat passed the obstacle, stop dodging
            if (boatFrontLocation >= detectedObstacleYPos)
                isDodging = false;

        }
        // Otherwise look for obstacles to dodge and try to stay in th lane
        else{
            dodgeObstacles();
            stayInLane(predictLimits);
        }

        // Apply the movement
        moveBoat();

        // Update the limits
        updateLimits();

        if (stamina > 30f)
            stamina -= 1.5 * delta;

    }
}
