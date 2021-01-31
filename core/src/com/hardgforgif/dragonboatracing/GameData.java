package com.hardgforgif.dragonboatracing;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.hardgforgif.dragonboatracing.UI.MenuUI;
import com.hardgforgif.dragonboatracing.UI.UI;

/**
 * Stores general game data which are static.
 * 
 * @since 1
 * @version 2
 * @author Team 10
 * @author Matt Tomlinson
 */
public class GameData {
    // Create the game state variables
    public static boolean mainMenuState = true;
    public static boolean choosingBoatState = false;
    public static boolean gamePlayState = false;
    public static boolean showResultsState = false;
    public static boolean resetGameState = false;
    public static boolean GameOverState = false;

    // MODIFIED: check required on first startup to delay obstacle generation until
    // difficulty has been picked
    public static boolean obstaclesGenerated = false;

    // Create the game UI and the game music
    public static UI currentUI = new MenuUI();
    public static Music music = Gdx.audio.newMusic(Gdx.files.internal("Vibing.ogg"));

    // Set the rations between the pixels, meters and tiles
    public final static float METERS_TO_PIXELS = 100f;
    public static float TILES_TO_METERS;
    public static float PIXELS_TO_TILES;

    // Create a list of possible boat stats
    // Ordered by: robustness, speed, acceleration, maneuverability
    public static float[][] boatsStats = new float[][] { { 120, 110, 100, 80 }, { 55, 110, 130, 60 },
            { 90, 110, 100, 130 }, { 65, 120, 90, 55 } };

    // Store information about each lane's boat
    // Boat's starting location
    public static float[][] startingPoints = new float[][] { { 2.3f, 4f }, { 4f, 4f }, { 7f, 4f }, { 10f, 4f } };
    // Boat's type
    public static int[] boatTypes = new int[4];
    // Boat's standing
    public static int[] standings = new int[4];
    // Boat's penalties
    public static float[] penalties = new float[4];
    // Result of the boat as a Pair<lane number, result>
    public static List<Float[]> results = new ArrayList<>();

    // MODIFIED: stores each position's best time with index corresponding to boat number
    public static float[] bests = { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE };
    
    // MODIFIED: used to determine if the game is over because the player did not
    // qualify for the final
    public static boolean dnq = false;

    // Current leg and the current timer in the leg
    public static int currentLeg = 0;
    public static float currentTimer = 0f;

    // Difficulty constants for the AI
    // MODIFIED: no longer uses set value, is instead selected in MenuUI
    public static float[] difficulty;
}
