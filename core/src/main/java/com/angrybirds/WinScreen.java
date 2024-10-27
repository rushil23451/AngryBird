package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WinScreen implements Screen {

    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture victoryTexture;
    private TextureRegion victoryRegion;

    public WinScreen() {
        batch = new SpriteBatch();

        // Load the background image
        backgroundTexture = new Texture(Gdx.files.internal("victory screen.jpg"));

        // Load the victory icon and set its size to 150x150
        victoryTexture = new Texture(Gdx.files.internal("victory.png"));
        victoryRegion = new TextureRegion(victoryTexture, 0, 0, 150, 150);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        batch.begin();


        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        float xPosition = (Gdx.graphics.getWidth() - 150) / 2;
        float yPosition = Gdx.graphics.getHeight() - 100 - 150;
        batch.draw(victoryRegion, xPosition, yPosition, 150, 150);

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
        victoryTexture.dispose();
    }
}
