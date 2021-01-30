package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public class GameOverUI extends UI {

    // MODIFIED: new distinct game over textures and sprites
    private Texture gameOverFinalTexture;
    private Texture gameOverDNQTexture;

    private Sprite gameOverFinalSprite;
    private Sprite gameOverDNQSprite;

    private Texture victoryTexture;
    private Sprite victorySprite;

    private ScrollingBackground scrollingBackground = new ScrollingBackground();

    public GameOverUI() {
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        // MODIFIED: load the new game over images
        gameOverFinalTexture = new Texture(Gdx.files.internal("gameOverFinal.png"));
        gameOverDNQTexture = new Texture(Gdx.files.internal("gameOverDNQ.png"));

        victoryTexture = new Texture(Gdx.files.internal("victory.png"));

        // MODIFIED: create the new sprites from the new textures
        gameOverFinalSprite = new Sprite(gameOverFinalTexture);
        gameOverDNQSprite = new Sprite(gameOverDNQTexture);

        victorySprite = new Sprite(victoryTexture);

        // MODIFIED: position and size the new sprites
        gameOverFinalSprite.setPosition(400, 200);
        gameOverFinalSprite.setSize(500, 500);
        gameOverDNQSprite.setPosition(400, 200);
        gameOverDNQSprite.setSize(500, 500);

        victorySprite.setPosition(400, 200);
        victorySprite.setSize(500, 500);

    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();
        scrollingBackground.updateAndRender(delta, batch);
        // If this was the last leg and the player won, show the victory screen
        if (GameData.currentLeg == 3 && GameData.standings[0] == 1) {
            victorySprite.draw(batch);
        }

        // MODIFIED: if the user has not won, but the game is over, show either new DNQ
        // or Final game over
        else if (GameData.dnq) {
            gameOverDNQSprite.draw(batch);
        }
        // Otherwise, the game is over with a loss
        else {
            gameOverFinalSprite.draw(batch);
            // TO DO: "You came second/third"
        }
        batch.end();
        playMusic();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

    }

    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {
        // When the user clicks on the screen
        if (mousePos.x != 0f && mousePos.y != 0f) {
            // Reset the game, after which the game will return to the main menu state
            GameData.GameOverState = false;
            GameData.resetGameState = true;

            // Switch the music to the main menu music
            GameData.music.stop();
            GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Vibing.ogg"));
        }
    }
}
