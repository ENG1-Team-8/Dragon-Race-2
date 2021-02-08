package de.tomgrill.gdxtesting;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.core.AI;
import com.hardgforgif.dragonboatracing.core.Map;
import com.hardgforgif.dragonboatracing.core.Obstacle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(GdxTestRunner.class)
public class AITest {

    World testWorld = new World(new Vector2(0f, 0f), true);
    Map testMap = new Map("Map1/Map1.tmx", 1000);
    AI testAI;
    Obstacle testObstacle;

    @Before
    public void initializeVariables()
    {
        testMap.createLanes(testWorld,1);
        testAI = new AI(100f, 100f, 100f, 100f, 1, testMap.lanes[0],1f);
        testAI.createBoatBody(testWorld,2,0,"boat1.json");
        testObstacle = testMap.lanes[0].obstacles[0];
    }

    //test the capability of the AI to spot an obstacle in front of it
    @Test
    public void testAwareness()
    {
        // test for no obstacle in front of the AI
        testObstacle.obstacleSprite.setPosition(900,300);
        testAI.updateAI(0.01f);
        assertFalse(testAI.obstaclesInRange());

        // test for an actual obstacle in range
        testObstacle.obstacleSprite.setPosition(200,300);
        testAI.updateAI(0.01f);
        assertTrue(testAI.obstaclesInRange());
    }

    //test the ability of the AI to avoid an obstacle in its path
    @Test
    public void testDodgingRight()
    {
        //place an obstacle in front and to the right of the AI
        testAI.boatBody.setTransform(3.4f,2,0);
        testObstacle.obstacleSprite.setPosition(320,500);
        testAI.updateAI(0.01f);
        float obstacleSpriteX = testObstacle.obstacleSprite.getX();
        assertTrue(testAI.objectChecker.x>obstacleSpriteX);
    }

    @Test
    public void testDodgingLeft()
    {
        //place an obstacle in front and to the left of the AI
        testAI.boatBody.setTransform(3.3f,2,-10);
        testObstacle.obstacleSprite.setPosition(370,500);
        testAI.updateAI(0.01f);
        float obstacleSpriteX = testObstacle.obstacleSprite.getX();
        assertTrue(testAI.objectChecker.x<obstacleSpriteX);
    }

    //test the ability of the AI to stay in lane by checking its target for movement against the limit of its lane
    @Test
    public void testGoingOutOfLaneLeft()
    {
        //move the ai close to the left boundary of the lane
        testAI.boatBody.setTransform(2.3f,1,0);
        testAI.updateAI(0.1f);
        float laneLimitX = testAI.lane.leftBoundary[1][1];
        assertTrue(testAI.laneChecker.x>laneLimitX);
    }


    @Test
    public void testGoingOutOfLaneRight()
    {
        //move the ai close to the right boundary of the lane
        testAI.boatBody.setTransform(3.7f,1,0);
        testAI.updateAI(0.1f);
        float laneLimitX = testAI.lane.rightBoundary[1][1];
        assertTrue(testAI.laneChecker.x < laneLimitX);
    }


    @Test
    public void testStayingInLane()
    {
        //move the ai close to the center of the lane
        testAI.boatBody.setTransform(3f,1,0);
        testAI.updateAI(0.1f);
        float laneLimitXLeft = testAI.lane.leftBoundary[1][1];
        float laneLimitXRight = testAI.lane.rightBoundary[1][1];
        assertTrue(testAI.laneChecker.x > laneLimitXLeft);
        assertTrue(testAI.laneChecker.x < laneLimitXRight);
    }

}
