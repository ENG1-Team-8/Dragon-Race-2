package com.hardgforgif.dragonboatracing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.hardgforgif.dragonboatracing.core.Player;

public class SaveLoad {

    static Preferences prefs = Gdx.app.getPreferences("save");
    static Json json = new Json();

    public static void save(Player player) {

        prefs.putString("boatTypes", json.toJson(GameData.boatTypes));
        prefs.putString("difficulty", json.toJson(GameData.difficulty));
        prefs.putString("bests", json.toJson(GameData.bests));
        prefs.putInteger("leg", GameData.currentLeg);

        prefs.flush();
    }

    public static void load() {

        GameData.boatTypes = json.fromJson(int[].class, Gdx.app.getPreferences("save").getString("boatTypes"));
        GameData.difficulty = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("difficulty"));
        GameData.bests = json.fromJson(float[].class, Gdx.app.getPreferences("save").getString("bests"));
        GameData.currentLeg = Gdx.app.getPreferences("save").getInteger("leg");

    }

}