package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadingScreen implements Screen {
    private Main game;
    private Texture loadingImageTexture;
    private Texture backgroundTexture;
    private Texture bottomLeftImageTexture;
    private SpriteBatch spriteBatch;
    private float elapsedTime;
    private boolean loadGame; // Flag to check if load game was pressed

    public LoadingScreen(Main game, boolean loadGame) {
        this.game = game;
        this.loadGame = loadGame; // Store the button pressed information
    }

    @Override
    public void show() {
        // Load the "loading.png" image, background image, and bottom-left image
        loadingImageTexture = new Texture(Gdx.files.internal("loadingnew.png"));
        backgroundTexture = new Texture(Gdx.files.internal("wallpaperflare.com_wallpaper.jpg"));
        bottomLeftImageTexture = new Texture(Gdx.files.internal("tipnew.png"));

        spriteBatch = new SpriteBatch();
        elapsedTime = 0f;  // Reset the timer
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);  // Clear the screen with black
        elapsedTime += delta;

        spriteBatch.begin();

        // Draw the background image, stretched to fill the screen
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Get screen dimensions
        float screenWidth = Gdx.graphics.getWidth();

        // Get dimensions of the loading image
        float loadingImageWidth = loadingImageTexture.getWidth();

        // Draw the "loading.png" image in the bottom-right corner
        spriteBatch.draw(loadingImageTexture, screenWidth - loadingImageWidth, 0);  // Offset image to bottom-right corner

        // Draw the bottom-left image scaled down to 40% of its original size
        float scaledWidth = bottomLeftImageTexture.getWidth() * 1.6f;  // Adjust scaling factor to 40%
        float scaledHeight = bottomLeftImageTexture.getHeight() * 1.6f;  // Adjust scaling factor to 40%
        spriteBatch.draw(bottomLeftImageTexture, 0, 0, scaledWidth, scaledHeight);  // Draw scaled image at (0, 0)

        spriteBatch.end();

        // After 2 seconds, switch to the appropriate screen
        if (elapsedTime > 2.0f) {
            if (loadGame) {
                game.setScreen(new Level_1_birds(game));  // Transition to Level_1 if load game was pressed
            } else {
                game.setScreen(new PlayAsScreen(game));  // Transition to PlayAsScreen if play was pressed
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        loadingImageTexture.dispose();
        backgroundTexture.dispose();
        bottomLeftImageTexture.dispose();
        spriteBatch.dispose();
    }
}
