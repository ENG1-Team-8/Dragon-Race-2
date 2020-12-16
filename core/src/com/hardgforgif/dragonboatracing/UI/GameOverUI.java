package com.hardgforgif.dragonboatracing.UI;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;


public class GameOverUI extends UI{
    private Texture gameOverTexture;
    private Sprite gameOverSprite;
    private Texture victoryTexture;
    private Sprite victorySprite;

    private ScrollingBackground scrollingBackground = new ScrollingBackground();


    public GameOverUI(){
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        gameOverTexture = new Texture(Gdx.files.internal("gameOver.png"));
        victoryTexture = new Texture(Gdx.files.internal("victory.png"));

        gameOverSprite = new Sprite(gameOverTexture);
        victorySprite = new Sprite(victoryTexture);

        gameOverSprite.setPosition(400, 200);
        gameOverSprite.setSize(500, 500);

        victorySprite.setPosition(400, 200);
        victorySprite.setSize(500, 500);

    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();
        scrollingBackground.updateAndRender(delta, batch);
        // If this was the last leg and the player won, show the victory screen
        if (GameData.currentLeg == 2 && GameData.standings[0] == 1)
            victorySprite.draw(batch);
        // Otherwise, the game is over with a loss
        else
            gameOverSprite.draw(batch);
        batch.end();
        playMusic();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

    }

    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {
        // When the user clicks on the screen
        if(mousePos.x != 0f && mousePos.y != 0f) {
            // Reset the game, after which the game will return to the main menu state
            GameData.GameOverState = false;
            GameData.resetGameState = true;

            // Switch the music to the main menu music
            GameData.music.stop();
            GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Vibing.ogg"));
        }
    }
}
