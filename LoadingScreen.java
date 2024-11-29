package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadingScreen implements Screen {
    private Main game;
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    private float elapsedTime;
    private boolean loadGame;
    private int currentLevel;

    public LoadingScreen(Main game, boolean loadGame) {
        this(game, loadGame, 1);
    }

    public LoadingScreen(Main game, boolean loadGame, int currentLevel) {
        this.game = game;
        this.loadGame = loadGame;
        this.currentLevel = currentLevel;
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("loadingnew.png"));
        spriteBatch = new SpriteBatch();
        elapsedTime = 0f;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        elapsedTime += delta;

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        if (elapsedTime > 2.0f) {
            Screen nextScreen;
            switch (currentLevel) {
                case 1:
                    nextScreen = new Level_1_birds(game);
                    break;
                case 2:
                    nextScreen = new Level_2_Birds(game);
                    break;
                case 3:
                    nextScreen = new Level_3_birds(game);
                    break;
                default:
                    nextScreen = new Level_1_birds(game);
                    break;
            }

            if (!loadGame) {
                nextScreen = new PlayAsScreen(game);
            }

            game.setScreen(nextScreen);
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
        backgroundTexture.dispose();
        spriteBatch.dispose();
    }
}
