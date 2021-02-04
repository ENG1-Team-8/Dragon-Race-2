package com.hardgforgif.dragonboatracing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

/**
 * Methods for saving and loading the game.
 * 
 * @since 2
 * @version 2
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
     * @author Matt Tomlinsons
     */
    public static void save() {

        // MODIFIED: store serialised game information in preferences
        prefs.putString("boatTypes", json.toJson(GameData.boatTypes));
        prefs.putString("difficulty", json.toJson(GameData.difficulty));
        prefs.putString("bests", json.toJson(GameData.bests));
        prefs.putInteger("leg", GameData.currentLeg);

        // MODIFIED: keep the changes
        prefs.flush();
    }

    /**
     * Load game information.
     * 
     * @since 2
     * @version 2
     * @author Matt Tomlinson
     */
    public static void load() {

        // MODIFIED: retrieve serialised information from preferences and load into game
        GameData.boatTypes = json.fromJson(int[].class, Gdx.app.getPreferences("save").getString("boatTypes"));
        GameData.difficulty = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("difficulty"));
        GameData.bests = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("bests"));
        GameData.currentLeg = Gdx.app.getPreferences("save").getInteger("leg");

    }

}