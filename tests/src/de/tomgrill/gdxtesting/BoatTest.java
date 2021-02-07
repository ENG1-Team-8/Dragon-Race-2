package de.tomgrill.gdxtesting;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(GdxTestRunner.class)
public class BoatTest {

    World testWorld = new World(new Vector2(0f, 0f), true);
    Map testMap = new Map("Map1/Map1.tmx", 1000);
    Boat testBoat;


    @Before
    public void initializeVariables()
    {
        testMap.createLanes(testWorld,1);
        testBoat = new Boat(100f, 100f, 100f, 100f, 1, testMap.lanes[1]);
        testBoat.createBoatBody(testWorld,2,0,"boat1.json");
    }

    //test if the boat limits in the lane are created successfully

    @Test
    public void testLimits()
    {
        float expectedLeftLimit = testBoat.lane.leftBoundary[0][1];
        float expectedRightLimit = testBoat.lane.rightBoundary[0][1];
        testBoat.updateLimits();
        assertEquals(expectedLeftLimit,testBoat.getLimitsAt(0)[0],0);
        assertEquals(expectedRightLimit,testBoat.getLimitsAt(0)[1],0);
    }

    //test if the boat has its at a finishing position

    @Test
    public void testFinished()
    {
        testBoat.boatSprite.setCenterY(900);
        assertFalse(testBoat.hasFinished());

        testBoat.boatSprite.setCenterY(900000);
        assertTrue(testBoat.hasFinished());
    }

    //test if the boat gains speed in the right direction while updating its movement

    @Test
    public void testLinearMovement()
    {
        float initialSpeed = testBoat.current_speed;
        float i;
        for (i=0;i<10000;i++)
        {
            testBoat.moveBoat(1);
        }
        float expectedSpeed = initialSpeed+(0.15f*(testBoat.acceleration/90)*(testBoat.stamina/100)*i);


        //check that the speed does not exceed maximum speed
        assertTrue(testBoat.current_speed<=testBoat.speed);

        if (expectedSpeed>this.testBoat.speed)
        {
            expectedSpeed = testBoat.speed;
        }


        // as our boat moves at 0 degree angle, we test that the speed is added correctly in relation to the number of frames i,
        // time delta, and boat acceleration and stamina

        assertEquals(expectedSpeed,testBoat.current_speed,0.1);

    }

    //test if the boat rotates in the right direction

    @Test
    public void testRotation ()
    {
        assertTrue(testBoat.rotateBoat(90)>0);
        assertTrue(testBoat.rotateBoat(-90)<0);
    }

    //test stamina boost at max stamina and less than max robustness

    @Test
    public void testPowerUpRegen ()
    {
        float initialRobustness = testBoat.robustness;
        testBoat.applyPowerup(1);
        assertEquals(initialRobustness,testBoat.robustness,0);

        testBoat.robustness = 50;
        float expectedRobustness = testBoat.robustness + initialRobustness*0.2f;
        testBoat.applyPowerup(1);
        assertEquals(expectedRobustness,testBoat.robustness,0);
    }

    //test stamina boost at max stamina and less than max stamina

    @Test
    public void testPowerUpStamina ()
    {
        float initialStamina = testBoat.stamina;
        testBoat.applyPowerup(2);
        assertEquals(initialStamina,testBoat.stamina,0);

        testBoat.stamina = 50;
        float expectedStamina = testBoat.stamina+ initialStamina*0.2f;
        testBoat.applyPowerup(2);
        assertEquals(expectedStamina,testBoat.stamina,0);
    }


    //test speed boost

    @Test
    public void testPowerUpSpeed()
    {
        float initialSpeed = testBoat.current_speed;
        testBoat.applyPowerup(3);
        assertEquals(initialSpeed,testBoat.current_speed,0);
    }


}
