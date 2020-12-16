package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public class GamePlayUI extends UI{
    private BitmapFont positionLabel;
    private BitmapFont robustnessLabel;
    private BitmapFont staminaLabel;
    private BitmapFont timerLabel;
    private BitmapFont legLabel;
    private Texture stamina;
    private Texture robustness;
    private Sprite rBar;
    private Sprite sBar;

    public GamePlayUI() {
        positionLabel = new BitmapFont();
        positionLabel.getData().setScale(1.4f);
        positionLabel.setColor(Color.BLACK);

        robustnessLabel = new BitmapFont();
        staminaLabel = new BitmapFont();

        timerLabel = new BitmapFont();
        timerLabel.getData().setScale(1.4f);
        timerLabel.setColor(Color.BLACK);

        legLabel = new BitmapFont();
        legLabel.getData().setScale(1.4f);
        legLabel.setColor(Color.BLACK);

        stamina = new Texture(Gdx.files.internal("Stamina_bar.png"));
        robustness  = new Texture(Gdx.files.internal("Robustness_bar.png"));
        rBar = new Sprite(robustness);
        sBar = new Sprite(stamina);
        sBar.setPosition(10 ,120);
        rBar.setPosition(10,60);

    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {

    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {
        // Set the robustness and stamina bars size based on the player boat
        sBar.setSize(playerBoat.stamina, 30);
        rBar.setSize(playerBoat.robustness,30);

        batch.begin();
        // Draw the robustness and stamina bars
        sBar.draw(batch);
        rBar.draw(batch);
        robustnessLabel.draw(batch, "Robustness", 10, 110);
        staminaLabel.draw(batch, "Stamina", 10,170);

        // Draw the position label, the timer and the leg label
        positionLabel.draw(batch, GameData.standings[0] + "/4", 1225, 700);
        timerLabel.draw(batch, String.valueOf(Math.round(GameData.currentTimer * 10.0) / 10.0), 10, 700);
        legLabel.draw(batch, "Leg: " + (GameData.currentLeg + 1), 10, 650);
        batch.end();

        playMusic();
    }
    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {

    }
}
