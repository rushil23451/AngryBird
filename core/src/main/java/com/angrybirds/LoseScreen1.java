package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class LoseScreen1 implements Screen {

    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Main game;

    public LoseScreen1(Main game) {
        this.game = game;

        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("pig-happy-angry-birds-28211886-1920-1200.jpg"));


        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new Level_1_birds(game));
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
