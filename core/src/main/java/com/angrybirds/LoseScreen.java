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
        this.game = game;

        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("wp3830719-angry-bird-red-wallpapers.jpg"));

        // Stop background music if it's playing

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new Level_1_pigs(game));
            }
        }, 1.5f);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
    }
}
