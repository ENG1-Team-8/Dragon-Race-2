package com.hardgforgif.dragonboatracing.core;

import java.util.Random;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.GameData;

/**
 * Class representing a lane to race in.
 * 
 * @since 1
 * @version 2
 * @author Team 10
 * @author Matt Tomlinson
 */
public class Lane {

    public float[][] leftBoundry;
    public int leftIterator = 0;
    public float[][] rightBoundry;
    public int rightIterator = 0;
    private MapLayer leftLayer;
    private MapLayer rightLayer;

    public Obstacle[] obstacles;

    // MODIFIED: array for powerups
    public Powerup[] powerups;

    /**
     * Constructs a Lane object on which boats race.
     * 
     * @param mapHeight  The height of the map
     * @param left       The left map layer
     * @param right      The right map layer
     * @param difficulty The difficulty for the lane
     * 
     * @since 1
     * @version 2
     * @author Team 10
     * @author Matt Tomlinson
     */
    public Lane(int mapHeight, MapLayer left, MapLayer right, float difficulty) {

        leftBoundry = new float[mapHeight][2];
        rightBoundry = new float[mapHeight][2];

        leftLayer = left;
        rightLayer = right;

        // MODIFIED: number of obstacles is weighted by difficulty now
        obstacles = new Obstacle[Math.round((difficulty * 1.5f) * 30)];

        // MODIFIED: set number of powerups on each lane
        powerups = new Powerup[20];

    }

    /**
     * Construct bodies that match the lane separators.
     * 
     * @param unitScale The size of a tile in pixels
     * 
     * @since 1
     * @version 1
     * @author Team 10
     */
    public void constructBoundries(float unitScale) {

        MapObjects objects = leftLayer.getObjects();

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            float height = rectangle.getY() * unitScale;
            float limit = (rectangle.getX() * unitScale) + (rectangle.getWidth() * unitScale);
            leftBoundry[leftIterator][0] = height;
            leftBoundry[leftIterator++][1] = limit;
        }

        objects = rightLayer.getObjects();

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            float height = rectangle.getY() * unitScale;
            float limit = rectangle.getX() * unitScale;
            rightBoundry[rightIterator][0] = height;
            rightBoundry[rightIterator++][1] = limit;
        }

    }

    /**
     * Gets the left and right limits of a lane at a given y-position.
     * 
     * @param yPosition The y-position to check left and right limits at.
     * @return A list of length 2, 0 index left boundary, 1 index right boundary
     * 
     * @since 1
     * @version 1
     * @author Team 10
     */
    public float[] getLimitsAt(float yPosition) {

        float[] lst = new float[2];
        int i;
        for (i = 1; i < leftIterator; i++) {
            if (leftBoundry[i][0] > yPosition) {
                break;
            }
        }
        lst[0] = leftBoundry[i - 1][1];

        for (i = 1; i < rightIterator; i++) {
            if (rightBoundry[i][0] > yPosition) {
                break;
            }
        }
        lst[1] = rightBoundry[i - 1][1];
        return lst;

    }

    /**
     * Spawn obstacles randomly on the lane.
     * 
     * @param world     World to spawn obstacles in
     * @param mapHeight Height of the map to draw on
     * 
     * @since 1
     * @version 1
     * @author Team 10
     */
    public void spawnObstacles(World world, float mapHeight) {

        int nrObstacles = obstacles.length;
        float segmentLength = mapHeight / nrObstacles;
        for (int i = 0; i < nrObstacles; i++) {
            int randomIndex = new Random().nextInt(6);
            float scale = 0f;
            if (randomIndex == 0 || randomIndex == 5)
                scale = -0.8f;
            obstacles[i] = new Obstacle("Obstacles/Obstacle" + (randomIndex + 1) + ".png");
            float segmentStart = i * segmentLength;
            float yPos = (float) (600f + (segmentStart + Math.random() * segmentLength));

            float[] limits = this.getLimitsAt(yPos);
            float leftLimit = limits[0] + 50;
            float rightLimit = limits[1];
            float xPos = (float) (leftLimit + Math.random() * (rightLimit - leftLimit));

            obstacles[i].createObstacleBody(world, xPos / GameData.METERS_TO_PIXELS, yPos / GameData.METERS_TO_PIXELS,
                    "Obstacles/Obstacle" + (randomIndex + 1) + ".json", scale);
        }

    }

    /**
     * Spawns powerups in the given world for this lane.
     * 
     * @param world     The world to spawn the powerups in
     * @param mapHeight The map height
     * 
     * @since 2
     * @version 2
     * @author Team 8
     * @author Matt Tomlinson
     */
    public void spawnPowerups(World world, float mapHeight) {

        int nrPowerups = powerups.length;
        float segmentLength = mapHeight / nrPowerups;
        for (int i = 0; i < nrPowerups; i++) {
            int randomIndex = new Random().nextInt(3);

            powerups[i] = new Powerup(randomIndex + 1);
            float segmentStart = i * segmentLength;
            float yPos = (float) (600f + (segmentStart + Math.random() * segmentLength));

            float[] limits = this.getLimitsAt(yPos);
            float leftLimit = limits[0] + 50;
            float rightLimit = limits[1];
            float xPos = (float) (leftLimit + Math.random() * (rightLimit - leftLimit));

            powerups[i].createObstacleBody(world, xPos / GameData.METERS_TO_PIXELS, yPos / GameData.METERS_TO_PIXELS,
                    35f / GameData.METERS_TO_PIXELS, 20f / GameData.METERS_TO_PIXELS);
        }

    }

}
