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

    public LoadingScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Load the "loading.png" image, background image, and bottom-left image
        loadingImageTexture = new Texture(Gdx.files.internal("loading.png"));
        backgroundTexture = new Texture(Gdx.files.internal("wallpaperflare.com_wallpaper.jpg"));
        bottomLeftImageTexture = new Texture(Gdx.files.internal("Screenshot_2024-10-24_093545-removebg-preview (1).png"));

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

        // Draw the bottom-left image scaled down to 50% of its original size
        float scaledWidth = bottomLeftImageTexture.getWidth() * 0.5f;  // 50% of original width
        float scaledHeight = bottomLeftImageTexture.getHeight() * 0.5f;  // 50% of original height
        spriteBatch.draw(bottomLeftImageTexture, 0, 0, scaledWidth, scaledHeight);  // Draw scaled image at (0, 0)

        spriteBatch.end();

        // After 2 seconds, switch to the PlayAsScreen
        if (elapsedTime > 2.0f) {
            game.setScreen(new PlayAsScreen(game));  // Transition to PlayAsScreen
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
