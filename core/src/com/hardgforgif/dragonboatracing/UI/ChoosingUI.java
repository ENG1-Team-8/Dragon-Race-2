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

import java.util.ArrayList;
import java.util.Collections;

public class ChoosingUI extends UI{
    private Texture background;
    private Sprite background_sprite;

    private Texture bar;
    private Sprite[] barSprites = new Sprite[4];
    private Texture boatTexture;
    private Sprite[] boatSprites = new Sprite[4];

    private BitmapFont label;

    private float[] currentStats = new float[4];

    ScrollingBackground scrollingBackground = new ScrollingBackground();


    public ChoosingUI() {
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        background = new Texture(Gdx.files.internal("Background.png"));
        background_sprite = new Sprite(background);
        background_sprite.setPosition(200,410);
        background_sprite.setSize(900,260);


        label = new BitmapFont();
        label.getData().setScale(1.4f);
        label.setColor(Color.BLACK);



        bar = new Texture("Robustness_bar.png");
        barSprites[0] = new Sprite(bar);
        barSprites[0].setPosition(430,600);

        bar = new Texture("Speed_bar.png");
        barSprites[1] = new Sprite(bar);
        barSprites[1].setPosition(430,550);

        bar = new Texture("Acceleration_bar.png");
        barSprites[2] = new Sprite(bar);
        barSprites[2].setPosition(430,500);

        bar = new Texture("Maneuverability_bar.png");
        barSprites[3] = new Sprite(bar);
        barSprites[3].setPosition(430,450);

        for (int i = 1; i <= 4; i++){
            boatTexture = new Texture("Boat" + i + ".png");
            boatSprites[i - 1] = new Sprite(boatTexture);
            boatSprites[i - 1].scale(-0.6f);
            boatSprites[i - 1].setPosition(150 + 300f * (i - 1), -200f);
        }
    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        //Check if the mouse is hovering over a boat, and update the bars displayed
        for (int i = 0; i < 4; i++){
            // Get the position of the boat
            float boatX = boatSprites[i].getX() + boatSprites[i].getWidth() / 2 -
                    boatSprites[i].getWidth() / 2 * boatSprites[i].getScaleX();
            float boatY = boatSprites[i].getY() + boatSprites[i].getHeight() / 2 -
                    boatSprites[i].getHeight() / 2 * boatSprites[i].getScaleY();
            float boatWidth = boatSprites[i].getWidth() * boatSprites[i].getScaleX();
            float boatHeight = boatSprites[i].getHeight() * boatSprites[i].getScaleY();

            // Check if the mouse is hovered over it
            if (mousePos.x > boatX && mousePos.x < boatX + boatWidth &&
                    mousePos.y > boatY && mousePos.y < boatY + boatHeight){
                currentStats[0] = GameData.boatsStats[i][0];
                currentStats[1] = GameData.boatsStats[i][1];
                currentStats[2] = GameData.boatsStats[i][2];
                currentStats[3] = GameData.boatsStats[i][3];
            }
        }

        float full_bar = screenWidth / 3;
        batch.begin();
        scrollingBackground.updateAndRender(delta, batch);
        background_sprite.draw(batch);

        // Display the bars based on the selected boat
        for (int i = 0; i < 4; i++){
            barSprites[i].setSize(full_bar * (currentStats[i]/100), 30);
            barSprites[i].draw(batch);
        }

        label.draw(batch, "Robustness:", 260, 625);
        label.draw(batch, "Speed:", 260, 575);
        label.draw(batch, "Acceleration:", 260, 525);
        label.draw(batch, "Maneuverability:", 260, 475);


        // Display the boats
        for (int i = 0; i < 4; i++){
            batch.draw(boatSprites[i], boatSprites[i].getX(), boatSprites[i].getY(), boatSprites[i].getOriginX(),
                    boatSprites[i].getOriginY(),
                    boatSprites[i].getWidth(), boatSprites[i].getHeight(), boatSprites[i].getScaleX(),
                    boatSprites[i].getScaleY(), boatSprites[i].getRotation());
        }
        batch.end();

        playMusic();
    }

    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {
        // Check which of the boat was pressed
        for (int i = 0; i < 4; i++){
            float boatX = boatSprites[i].getX() + boatSprites[i].getWidth() / 2 -
                    boatSprites[i].getWidth() / 2 * boatSprites[i].getScaleX();
            float boatY = boatSprites[i].getY() + boatSprites[i].getHeight() / 2 -
                    boatSprites[i].getHeight() / 2 * boatSprites[i].getScaleY();
            float boatWidth = boatSprites[i].getWidth() * boatSprites[i].getScaleX();
            float boatHeight = boatSprites[i].getHeight() * boatSprites[i].getScaleY();

            if (mousePos.x > boatX && mousePos.x < boatX + boatWidth &&
                    mousePos.y > boatY && mousePos.y < boatY + boatHeight){
                // Set the player's boat type based on the clicked boat
                GameData.boatTypes[0] = i;

                // Randomise the AI boats
                ArrayList<Integer> intList = new ArrayList<Integer>(){{add(0); add(1); add(2); add(3);}};
                intList.remove(new Integer(i));
                Collections.shuffle(intList);
                GameData.boatTypes[1] = intList.get(0);
                GameData.boatTypes[2] = intList.get(1);
                GameData.boatTypes[3] = intList.get(2);

                // Change the music
                GameData.music.stop();
                GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Love_Drama.ogg"));

                // Set the game state to the game play state
                GameData.choosingBoatState = false;
                GameData.gamePlayState = true;
                GameData.currentUI = new GamePlayUI();
            }
        }

    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {
    }
}
