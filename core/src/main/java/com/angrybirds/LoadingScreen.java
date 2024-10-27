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
    private boolean loadGame;

    public LoadingScreen(Main game, boolean loadGame) {
        this.game = game;
        this.loadGame = loadGame;
    }

    @Override
    public void show() {

        loadingImageTexture = new Texture(Gdx.files.internal("loadingnew.png"));
        backgroundTexture = new Texture(Gdx.files.internal("wallpaperflare.com_wallpaper.jpg"));
        bottomLeftImageTexture = new Texture(Gdx.files.internal("tipnew.png"));

        spriteBatch = new SpriteBatch();
        elapsedTime = 0f;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        elapsedTime += delta;

        spriteBatch.begin();

        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float screenWidth = Gdx.graphics.getWidth();

        float loadingImageWidth = loadingImageTexture.getWidth();

        spriteBatch.draw(loadingImageTexture, screenWidth - loadingImageWidth, 0);

        float scaledWidth = bottomLeftImageTexture.getWidth() * 1.6f;
        float scaledHeight = bottomLeftImageTexture.getHeight() * 1.6f;
        spriteBatch.draw(bottomLeftImageTexture, 0, 0, scaledWidth, scaledHeight);
        spriteBatch.end();

        if (elapsedTime > 2.0f) {
            if (loadGame) {
                game.setScreen(new Level_1_birds(game));
            } else {
                game.setScreen(new PlayAsScreen(game));
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
