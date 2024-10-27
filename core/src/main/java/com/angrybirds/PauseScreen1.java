package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PauseScreen1 implements Screen {
    private Texture backgroundTexture;
    private Texture resumeButtonTexture;
    private Texture retryButtonTexture;
    private Texture levelsButtonTexture;
    private Texture saveButtonTexture; // Added for SAVE button
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private static final float VIRTUAL_WIDTH = 800;
    private static final float VIRTUAL_HEIGHT = 448;
    private Main game;
    private Stage stage;

    public PauseScreen1(Main game) {
        this.game = game;
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void show() {
        // Load textures for buttons (unchanged)
        backgroundTexture = new Texture(Gdx.files.internal("Untitled design (2).png"));
        resumeButtonTexture = new Texture(Gdx.files.internal("RESUME_BUTTON-Photoroom.png"));
        retryButtonTexture = new Texture(Gdx.files.internal("RETRY_BUTTON_FINAL-removebg-preview.png"));
        levelsButtonTexture = new Texture(Gdx.files.internal("LEVELS_BUTTON-Photoroom.png"));
        saveButtonTexture = new Texture(Gdx.files.internal("SAVE_BUTTON_FINAL.png")); // Load SAVE button texture

        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        float buttonWidth = 165;
        float buttonHeight = 90;


        float topOffset = 100;
        float spacing = 2;


        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(resumeButtonTexture));
        resumeButton.setSize(buttonWidth, buttonHeight);
        resumeButton.setPosition(VIRTUAL_WIDTH / 2 - buttonWidth / 2, VIRTUAL_HEIGHT - topOffset);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("RESUME button clicked");
                game.setScreen(new Level_1_pigs(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                resumeButton.setSize(buttonWidth - 10, buttonHeight - 10);
                resumeButton.setPosition(resumeButton.getX() + 5, resumeButton.getY() + 5);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                resumeButton.setSize(buttonWidth, buttonHeight);
                resumeButton.setPosition(resumeButton.getX() - 5, resumeButton.getY() - 5);
            }
        });


        ImageButton retryButton = new ImageButton(new TextureRegionDrawable(retryButtonTexture));
        retryButton.setSize(buttonWidth, buttonHeight);
        retryButton.setPosition(VIRTUAL_WIDTH / 2 - buttonWidth / 2, VIRTUAL_HEIGHT - topOffset - buttonHeight - spacing);
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("RETRY button clicked");
                game.setScreen(new LoseScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                retryButton.setSize(buttonWidth - 10, buttonHeight - 10);
                retryButton.setPosition(retryButton.getX() + 5, retryButton.getY() + 5);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                retryButton.setSize(buttonWidth, buttonHeight);
                retryButton.setPosition(retryButton.getX() - 5, retryButton.getY() - 5);
            }
        });

        ImageButton saveButton = new ImageButton(new TextureRegionDrawable(saveButtonTexture));
        saveButton.setSize(buttonWidth, buttonHeight);
        saveButton.setPosition(VIRTUAL_WIDTH / 2 - buttonWidth / 2, VIRTUAL_HEIGHT - topOffset - 2 * (buttonHeight + spacing));
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("SAVE button clicked");

                game.setScreen(new FirstScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                saveButton.setSize(buttonWidth - 10, buttonHeight - 10);
                saveButton.setPosition(saveButton.getX() + 5, saveButton.getY() + 5);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                saveButton.setSize(buttonWidth, buttonHeight);
                saveButton.setPosition(saveButton.getX() - 5, saveButton.getY() - 5);
            }
        });


        ImageButton levelsButton = new ImageButton(new TextureRegionDrawable(levelsButtonTexture));
        levelsButton.setSize(buttonWidth, buttonHeight);
        levelsButton.setPosition(VIRTUAL_WIDTH / 2 - buttonWidth / 2, VIRTUAL_HEIGHT - topOffset - 3 * (buttonHeight + spacing));
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("LEVELS button clicked");
                game.setScreen(new LevelSelectionPigs(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                levelsButton.setSize(buttonWidth - 10, buttonHeight - 10);
                levelsButton.setPosition(levelsButton.getX() + 5, levelsButton.getY() + 5);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                levelsButton.setSize(buttonWidth, buttonHeight);
                levelsButton.setPosition(levelsButton.getX() - 5, levelsButton.getY() - 5);
            }
        });


        stage.addActor(resumeButton);
        stage.addActor(retryButton);
        stage.addActor(saveButton);
        stage.addActor(levelsButton);
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
    public void dispose() {
        backgroundTexture.dispose();
        resumeButtonTexture.dispose();
        retryButtonTexture.dispose();
        levelsButtonTexture.dispose();
        saveButtonTexture.dispose();
        spriteBatch.dispose();
        stage.dispose();
    }
}
