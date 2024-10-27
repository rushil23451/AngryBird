package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class LoseScreen implements Screen {

    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Main game;

    public LoseScreen(Main game) {
        this.game = game; // Reference to the main game instance
        // Initialize SpriteBatch and load the background texture
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("wp3830719-angry-bird-red-wallpapers.jpg"));

        // Schedule the transition back to Level_1 after 1.5 seconds
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new Level_1_pigs(game)); // Redirect to Level_1
            }
        }, 1.5f); // 1.5 seconds delay
    }

    @Override
    public void show() {
        // Called when the screen is displayed
    }

    @Override
    public void render(float delta) {
        // Begin the SpriteBatch
        batch.begin();

        // Draw the background image stretched to cover the entire screen
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // End the SpriteBatch
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Handle resizing if necessary
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Dispose of resources when no longer needed
        batch.dispose();
        backgroundTexture.dispose();
    }
}
