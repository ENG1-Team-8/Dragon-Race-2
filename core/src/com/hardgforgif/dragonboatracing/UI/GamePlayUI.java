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

/**
 * The UI during gameplay (racing), extends UI.
 * <p>
 * Responsible for displaying stamina/health bars, leg number, timer and
 * controls.
 * 
 * @see UI
 * 
 * @since 1
 * @version 2
 * @author Team 10
 * @author Matt Tomlinson
 */
public class GamePlayUI extends UI {

    private BitmapFont positionLabel;
    private BitmapFont robustnessLabel;
    private BitmapFont staminaLabel;
    private BitmapFont timerLabel;
    private BitmapFont legLabel;
    private Texture stamina;
    private Texture robustness;
    private Sprite rBar;
    private Sprite sBar;

    // MODIFIED: new texture and sprite for on-screen controls
    private Texture controls;
    private Sprite controlsDisplay;

    // MODIFIED: variable to store the text to display the leg
    private String legText;

    /**
     * Constructs a new UI for gameplay.
     * 
     * @since 1
     * @version 2
     * @author Team 10
     * @author Matt Tomlinson
     */
    public GamePlayUI() {

        positionLabel = new BitmapFont();
        positionLabel.getData().setScale(1.4f);

        // MODIFIED: white text colour for better readability
        positionLabel.setColor(Color.WHITE);

        robustnessLabel = new BitmapFont();
        staminaLabel = new BitmapFont();

        timerLabel = new BitmapFont();
        timerLabel.getData().setScale(1.4f);

        // MODIFIED: white text colour for better readability
        timerLabel.setColor(Color.WHITE);

        legLabel = new BitmapFont();
        legLabel.getData().setScale(1.4f);

        // MODIFIED: white text colour for better readability
        legLabel.setColor(Color.WHITE);

        stamina = new Texture(Gdx.files.internal("Stamina_bar.png"));
        robustness = new Texture(Gdx.files.internal("Robustness_bar.png"));

        rBar = new Sprite(robustness);
        sBar = new Sprite(stamina);
        sBar.setPosition(10, 120);
        rBar.setPosition(10, 60);

        // MODIFIED: load texture and create, scale and position sprite for controls
        controls = new Texture(Gdx.files.internal("controls.png"));
        controlsDisplay = new Sprite(controls);
        controlsDisplay.scale(-0.74f);
        controlsDisplay.setPosition(-276, -54);

    }

    /**
     * @since 1
     * @version 1
     * @author Team 10
     */
    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {

    }

    /**
     * @since 1
     * @version 2
     * @author Team 10
     * @author Matt Tomlinson
     */
    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

        // Set the robustness and stamina bars size based on the player boat
        sBar.setSize(playerBoat.stamina, 30);
        rBar.setSize(playerBoat.robustness, 30);

        // MODIFIED: Used to display more descriptive text for key legs rather than just
        // numbers
        if (GameData.currentLeg == 0)
            legText = "Practice";
        else if (GameData.currentLeg == 3)
            legText = "Final";
        else
            legText = Integer.toString(GameData.currentLeg + 1);

        batch.begin();
        // Draw the robustness and stamina bars
        sBar.draw(batch);
        rBar.draw(batch);

        // MODIFIED: draw the on-screen controls
        controlsDisplay.draw(batch);

        // MODIFIED: Displays "Boat health" instead of "Robustness" for clarity
        robustnessLabel.draw(batch, "Boat health", 10, 110);
        staminaLabel.draw(batch, "Stamina", 10, 170);

        // Draw the position label, the timer and the leg label
        // MODIFIED: Labels for each UI element for more clarity
        positionLabel.draw(batch, "Position: " + GameData.standings[0] + "/4", 1140, 700);
        timerLabel.draw(batch, "Timer: " + String.valueOf(Math.round(GameData.currentTimer * 10.0) / 10.0), 10, 700);
        legLabel.draw(batch, "Leg: " + legText, 10, 650);
        batch.end();

        playMusic();

    }

    /**
     * @since 1
     * @version 1
     * @author Team 10
     */
    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {

    }

}
