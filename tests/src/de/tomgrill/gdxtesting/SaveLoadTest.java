package de.tomgrill.gdxtesting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.SaveLoad;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;


@RunWith(GdxTestRunner.class)
public class SaveLoadTest {


    int expectedLeg = 3;
    String  expectedDifficulty = "[1,1,1,1]";
    String  expectedBests = "[40,41,42,43]";
    String  expectedBoatTypes = "[3,1,2,4]";
    float [] expectedDifficultyArray = new float[]{1,1,1,1};
    float [] expectedBestsArray = new float[]{40,41,42,43};
    int [] expectedBoatTypesArray = new int[]{3,1,2,4};

    @Before
    public void createVariable ()
    {
        GameData.currentLeg=3;
        GameData.difficulty = new float[]{1,1,1,1};
        GameData.bests = new float[]{40,41,42,43};
        GameData.boatTypes = new int[] {3,1,2,4};
        SaveLoad.save();
    }


    // test if the save function is able to save the serialized objects
    @Test
    public void testSave()
    {
        assertEquals(expectedLeg,SaveLoad.prefs.getInteger("leg"),0);
        assertEquals(expectedBests,SaveLoad.prefs.getString("bests"));
        assertEquals(expectedDifficulty,SaveLoad.prefs.getString("difficulty"));
        assertEquals(expectedBoatTypes,SaveLoad.prefs.getString("boatTypes"));
    }

    //test if the load function is able to retrieve the saved values
    @Test
    public void testLoad()
    {
        // first off, reset the values inside GameData
        GameData.currentLeg=0;
        GameData.difficulty = new float[]{0,0,0,0};
        GameData.bests = new float[]{0,0,0,0};
        GameData.boatTypes = new int[] {0,0,0,0};
        SaveLoad.load();
        assertEquals(expectedLeg,GameData.currentLeg,0);
        assertArrayEquals(expectedBestsArray,GameData.bests,0);
        assertArrayEquals(expectedDifficultyArray,GameData.difficulty,0);
        assertArrayEquals(expectedBoatTypesArray,GameData.boatTypes);
    }
}
