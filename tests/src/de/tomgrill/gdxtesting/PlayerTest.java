package de.tomgrill.gdxtesting;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.core.Map;
import com.hardgforgif.dragonboatracing.core.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;


@RunWith(GdxTestRunner.class)
public class PlayerTest {

    World testWorld = new World(new Vector2(0f, 0f), true);
    Map testMap = new Map("Map1/Map1.tmx", 1000);
    Player testPlayer;


    @Before
    public void initializeVariables()
    {
        testMap.createLanes(testWorld,1);
        testPlayer = new Player(100f, 100f, 100f, 100f, 1, testMap.lanes[1]);
        testPlayer.createBoatBody(testWorld,2,0,"boat1.json");
    }

    //test if pressing A moves the player to the left

    @Test
    public void testPlayerInputsA()
    {
        boolean [] pressedA = new boolean[]{false,true,false,false};
        testPlayer.updatePlayer(pressedA,1/60f);
        assertTrue(testPlayer.boatBody.getAngle()>0);

    }

    //test if pressing D moves the player to the right

    @Test
    public void testPlayerInputsD()
    {
        boolean [] pressedD = new boolean[]{false,false,false,true};
        testPlayer.updatePlayer(pressedD,1/60f);
        assertTrue(testPlayer.boatBody.getAngle()<0);
    }

    //test if pressing both keys at the same time keeps the player at the same rotation

    @Test
    public void testPlayerInputsBoth ()
    {
        boolean [] pressedBoth = new boolean[]{true,true,true,true};
        testPlayer.updatePlayer(pressedBoth,1/60f);
        System.out.println(testPlayer.boatBody.getAngle());
        assertTrue(testPlayer.boatBody.getAngle()==0);
    }

    //test if pressing no keys keeps the player at the same rotation

    @Test
    public void testPlayerInputsNone ()
    {
        boolean [] pressedNone = new boolean[]{false,false,false,false};
        testPlayer.updatePlayer(pressedNone,1/60f);
        assertTrue(testPlayer.boatBody.getAngle()==0);
    }

}
