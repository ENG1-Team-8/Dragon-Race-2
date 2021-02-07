package com.hardgforgif.dragonboatracing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

/**
 * Methods for saving and loading the game.
 * 
 * @since 2
 * @version 2
 * @author Team 8
 * @author Matt Tomlinson
 */
public class SaveLoad {

    // MODIFIED: get the save preference and create new json object
    static Preferences prefs = Gdx.app.getPreferences("save");
    static Json json = new Json();

    /**
     * Save game information.
     * 
     * @since 2
     * @version 2
     * @author Team 8
     * @author Matt Tomlinson
     */
    public static void save() {

        // MODIFIED: store serialised game information in preferences
        prefs.putString("boatTypes", json.toJson(GameData.boatTypes));
        prefs.putString("difficulty", json.toJson(GameData.difficulty));
        prefs.putString("bests", json.toJson(GameData.bests));
        prefs.putInteger("leg", GameData.currentLeg);
        prefs.putString("timer",json.toJson(GameData.currentTimer));
        prefs.putString("penalties",json.toJson(GameData.penalties));
        prefs.putString("hitPoints",json.toJson(GameData.currenHPs));
        prefs.putString("stamina",json.toJson(GameData.currentStamina));
        prefs.putString("speeds",json.toJson(GameData.currentSpeeds));
        prefs.putString("boat1Position",json.toJson(GameData.currentPositions[0]));
        prefs.putString("boat2Position",json.toJson(GameData.currentPositions[1]));
        prefs.putString("boat3Position",json.toJson(GameData.currentPositions[2]));
        prefs.putString("boat4Position",json.toJson(GameData.currentPositions[3]));

        // MODIFIED: save the changes
        prefs.flush();

    }

    /**
     * Load game information.
     * 
     * @since 2
     * @version 2
     * @author Team 8
     * @author Matt Tomlinson
     */
    public static void load() {

        GameData.loadedState=true;

        // MODIFIED: retrieve serialised information from preferences and load into game
        GameData.boatTypes = json.fromJson(int[].class, Gdx.app.getPreferences("save").getString("boatTypes"));
        GameData.difficulty = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("difficulty"));
        GameData.bests = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("bests"));
        GameData.currentTimer = json.fromJson(float.class, Gdx.app.getPreferences("save").getString("timer"));
        GameData.penalties = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("penalties"));
        GameData.currenHPs = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("hitPoints"));
        GameData.currentSpeeds = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("speeds"));
        GameData.currentStamina = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("stamina"));
        GameData.currentLeg = Gdx.app.getPreferences("save").getInteger("leg");
        GameData.startingPoints[0] = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("boat1Position"));
        GameData.startingPoints[1] = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("boat2Position"));
        GameData.startingPoints[2] = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("boat3Position"));
        GameData.startingPoints[3] = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("boat4Position"));

    }

}