package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LevelSelectionPigs implements Screen {
    private Texture backgroundTexture;
    private Texture levelButtonTexture;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private static final float VIRTUAL_WIDTH = 800;
    private static final float VIRTUAL_HEIGHT = 448;
    private Main game;

    public LevelSelectionPigs(Main game) {
        this.game = game;
    }

    @Override
    public void resume() {
        // Code to handle resume can go here if needed
    }

    @Override
    public void hide() {
        // Code to handle hiding this screen can go here if needed
    }

    @Override
    public void show() {
        // Load textures
        backgroundTexture = new Texture(Gdx.files.internal("pigslevel.png"));
        levelButtonTexture = new Texture(Gdx.files.internal("levelbutton.png"));

        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Level button
        final ImageButton levelButton = new ImageButton(new TextureRegionDrawable(levelButtonTexture));
        levelButton.setPosition(200, 200);
        levelButton.setSize(150, 150);
        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Chuckbird level selected");
                // Add code to proceed to the selected level here
                // e.g., game.setScreen(new GameScreen(game, selectedLevel));
            }
        });

        // Add the button to the stage
        stage.addActor(levelButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        spriteBatch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        // Code to handle pause can go here if needed
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        levelButtonTexture.dispose();
        spriteBatch.dispose();
        stage.dispose();
    }
}
