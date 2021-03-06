package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.SaveLoad;
import com.hardgforgif.dragonboatracing.core.Player;

/**
 * Class representing a main menu for the game, extends UI.
 * 
 * @see UI
 * 
 * @since 1
 * @version 2
 * @author Team 10
 * @author Matt Tomlinson
 */
public class MenuUI extends UI {

    // Sets the dimensions for all the UI components

    // MODIFIED: new logo dimensions
    private static final int LOGO_WIDTH = 661;
    private static final int LOGO_HEIGHT = 298;
    private static final int LOGO_Y = 350;

    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_Y = 290;

    // MODIFIED: dimensions and y-positioning for the load button
    private static final int LOAD_BUTTON_WIDTH = 300;
    private static final int LOAD_BUTTON_HEIGHT = 120;
    private static final int LOAD_BUTTON_Y = 160;

    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;
    private static final int EXIT_BUTTON_Y = 30;

    Texture playButtonActive;
    Texture playButtonInactive;

    // MODIFIED: New textures for new buttons
    Texture playButtonActive1;
    Texture playButtonInactive1;
    Texture playButtonActive2;
    Texture playButtonInactive2;

    // MODIFIED: load button textures
    Texture loadButtonActive;
    Texture loadButtonInactive;

    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture logo;

    ScrollingBackground scrollingBackground = new ScrollingBackground();

    /**
     * Constructs a new main menu user interface.
     * 
     * @since 1
     * @version 2
     * @author Team 10
     * @author Matt Tomlinson
     */
    public MenuUI() {

        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        playButtonActive = new Texture("PlaySelected.png");
        playButtonInactive = new Texture("PlayUnselected.png");

        // MODIFIED: Load the new pngs for textures
        playButtonActive1 = new Texture("PlaySelected1.png");
        playButtonInactive1 = new Texture("PlayUnselected1.png");
        playButtonActive2 = new Texture("PlaySelected2.png");
        playButtonInactive2 = new Texture("PlayUnselected2.png");

        // MODIFIED: create the button textures from new asset pngs
        loadButtonActive = new Texture("LoadSelected.png");
        loadButtonInactive = new Texture("LoadUnselected.png");

        exitButtonActive = new Texture("ExitSelected.png");
        exitButtonInactive = new Texture("ExitUnselected.png");
        logo = new Texture("Title.png");

    }

    /**
     * @since 1
     * @version 2
     * @author Team 10
     * @author Matt Tomlinson
     */
    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {

        batch.begin();
        scrollingBackground.updateAndRender(delta, batch);
        batch.draw(logo, screenWidth / 2 - LOGO_WIDTH / 2, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);

        // If the mouse is not hovered over the buttons, draw the unselected buttons
        float x = screenWidth / 2 - PLAY_BUTTON_WIDTH / 2;
        if (mousePos.x < x + PLAY_BUTTON_WIDTH && mousePos.x > x &&
        // cur pos < top_height
                mousePos.y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && mousePos.y > PLAY_BUTTON_Y) {
            batch.draw(playButtonActive1, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        } else {
            batch.draw(playButtonInactive1, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        // MODIFIED: The two if conditions directly below draw new buttons for differing
        // difficulties
        x = screenWidth / 2 - (3 * PLAY_BUTTON_WIDTH + 20) / 2;
        if (mousePos.x < x + PLAY_BUTTON_WIDTH && mousePos.x > x &&
        // cur pos < top_height
                mousePos.y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && mousePos.y > PLAY_BUTTON_Y) {
            batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        } else {
            batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        x = screenWidth / 2 + (PLAY_BUTTON_WIDTH + 20) / 2;
        if (mousePos.x < x + PLAY_BUTTON_WIDTH && mousePos.x > x &&
        // cur pos < top_height
                mousePos.y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && mousePos.y > PLAY_BUTTON_Y) {
            batch.draw(playButtonActive2, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        } else {
            batch.draw(playButtonInactive2, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        // MODIFIED: draw the selected/unselected load button
        x = screenWidth / 2 - LOAD_BUTTON_WIDTH / 2;
        if (mousePos.x < x + LOAD_BUTTON_WIDTH && mousePos.x > x &&
        // cur pos < top_height
                mousePos.y < LOAD_BUTTON_Y + LOAD_BUTTON_HEIGHT && mousePos.y > LOAD_BUTTON_Y) {
            batch.draw(loadButtonActive, x, LOAD_BUTTON_Y, LOAD_BUTTON_WIDTH, LOAD_BUTTON_HEIGHT);
        } else {
            batch.draw(loadButtonInactive, x, LOAD_BUTTON_Y, LOAD_BUTTON_WIDTH, LOAD_BUTTON_HEIGHT);
        }

        // Otherwise draw the selected buttons
        x = screenWidth / 2 - EXIT_BUTTON_WIDTH / 2;
        if (mousePos.x < x + EXIT_BUTTON_WIDTH && mousePos.x > x && mousePos.y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT
                && mousePos.y > EXIT_BUTTON_Y) {
            batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        } else {
            batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }
        batch.end();

        playMusic();

    }

    /**
     * @since 1
     * @version 1
     * @author Team 10
     */
    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

    }

    /**
     * @since 1
     * @version 2
     * @author Team 10
     * @author Matt Tomlinson
     */
    @Override
    public void getInput(float screenWidth, Vector2 clickPos) {

        // If the play button is clicked
        float x = screenWidth / 2 - PLAY_BUTTON_WIDTH / 2;
        if (clickPos.x < x + PLAY_BUTTON_WIDTH && clickPos.x > x &&
        // cur pos < top_height
                clickPos.y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && clickPos.y > PLAY_BUTTON_Y) {
            // Switch to the choosing state
            GameData.mainMenuState = false;
            GameData.choosingBoatState = true;
            // MODIFIED: Selecting button now sets the game difficulty
            GameData.difficulty = new float[] { 1f, 1.01f, 1.02f, 1.04f };
            GameData.currentUI = new ChoosingUI();
        }

        // MODIFIED: The two if conditions below check for clicks on new added buttons
        // above
        x = screenWidth / 2 - (3 * PLAY_BUTTON_WIDTH + 10) / 2;
        if (clickPos.x < x + PLAY_BUTTON_WIDTH && clickPos.x > x &&
        // cur pos < top_height
                clickPos.y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && clickPos.y > PLAY_BUTTON_Y) {
            // Switch to the choosing state
            GameData.mainMenuState = false;
            GameData.choosingBoatState = true;
            GameData.difficulty = new float[] { 0.9f, 0.91f, 0.92f, 0.94f };
            GameData.currentUI = new ChoosingUI();
        }

        x = screenWidth / 2 + (PLAY_BUTTON_WIDTH + 10) / 2;
        if (clickPos.x < x + PLAY_BUTTON_WIDTH && clickPos.x > x &&
        // cur pos < top_height
                clickPos.y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && clickPos.y > PLAY_BUTTON_Y) {
            // Switch to the choosing state
            GameData.mainMenuState = false;
            GameData.choosingBoatState = true;
            GameData.difficulty = new float[] { 1.1f, 1.11f, 1.12f, 1.14f };
            GameData.currentUI = new ChoosingUI();
        }

        // MODIFIED: check for click on load button
        x = screenWidth / 2 - LOAD_BUTTON_WIDTH / 2;
        if (clickPos.x < x + LOAD_BUTTON_WIDTH && clickPos.x > x &&
        // cur pos < top_height
                clickPos.y < LOAD_BUTTON_Y + LOAD_BUTTON_HEIGHT && clickPos.y > LOAD_BUTTON_Y) {
            // MODIFIED: go straight to gameplay after loading
            GameData.mainMenuState = false;
            GameData.gamePlayState = true;
            SaveLoad.load();
            GameData.music.stop();
            GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Love_Drama.ogg"));
            GameData.currentUI = new GamePlayUI();
        }

        // If the exit button is clicked, close the game
        x = screenWidth / 2 - EXIT_BUTTON_WIDTH / 2;
        if (clickPos.x < x + EXIT_BUTTON_WIDTH && clickPos.x > x && clickPos.y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT
                && clickPos.y > EXIT_BUTTON_Y) {
            Gdx.app.exit();
        }

    }

}
