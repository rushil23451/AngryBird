package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

public class WinScreen implements Screen {

    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture victoryTexture;
    private Stage stage;
    private Main game;

    public WinScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("victory screen.jpg"));
        victoryTexture = new Texture(Gdx.files.internal("vctory.png"));

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        ImageButton victoryButton = new ImageButton(new TextureRegionDrawable(victoryTexture));
        victoryButton.setSize(308 * 2, 91 * 2); // Adjust the size as needed


        victoryButton.setPosition((Gdx.graphics.getWidth() - victoryButton.getWidth()) / 2,
            (Gdx.graphics.getHeight() - victoryButton.getHeight()) / 2);

        victoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new FirstScreen(game));
            }
        });

        stage.addActor(victoryButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (victoryTexture != null) victoryTexture.dispose();
        stage.dispose();
    }
}
