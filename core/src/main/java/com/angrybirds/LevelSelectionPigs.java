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
    private Texture level2ButtonTexture;
    private Texture level3ButtonTexture;
    private Texture lockedlevelButtonTexture;
    private Texture chuckBirdTexture;
    private Texture backButtonTexture;
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
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void show() {
        // Load textures
        backgroundTexture = new Texture(Gdx.files.internal("Angry_village1.png"));
        levelButtonTexture = new Texture(Gdx.files.internal("1pigsbg.png"));
        level2ButtonTexture = new Texture(Gdx.files.internal("2pigsbg.png"));
        level3ButtonTexture = new Texture(Gdx.files.internal("3pigsbg.png"));
        backButtonTexture = new Texture(Gdx.files.internal("backbuttonremoved.png"));
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        float buttonWidth = 60;
        float buttonHeight = 60;

        ImageButton levelButton1 = new ImageButton(new TextureRegionDrawable(levelButtonTexture));
        levelButton1.setPosition(300, -10);
        levelButton1.setSize(buttonWidth, buttonHeight);
        levelButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level 1 selected");
                game.setScreen(new Level_1_pigs(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                levelButton1.setSize(buttonWidth + 15, buttonHeight + 15);
                levelButton1.setPosition(levelButton1.getX() - 7.5f, levelButton1.getY() - 7.5f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                levelButton1.setSize(buttonWidth, buttonHeight);
                levelButton1.setPosition(levelButton1.getX() + 7.5f, levelButton1.getY() + 7.5f);
            }
        });
        ImageButton levelButton2 = new ImageButton(new TextureRegionDrawable(level2ButtonTexture));
        levelButton2.setPosition(400, 60);
        levelButton2.setSize(buttonWidth, buttonHeight);
        levelButton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level 2 selected");
            }
        });

        float button3Width = buttonWidth - 15;
        float button3Height = buttonHeight - 15;

        ImageButton levelButton3 = new ImageButton(new TextureRegionDrawable(level3ButtonTexture));
        levelButton3.setPosition(340, 100);
        levelButton3.setSize(button3Width, button3Height);
        levelButton3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level 3 selected");

            }
        });

        final ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setPosition(20, 20);
        backButton.setSize(115, 65);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Back button clicked");
                game.setScreen(new PlayAsScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                backButton.setSize(backButton.getWidth() - 15, backButton.getHeight() - 15);
                backButton.setPosition(backButton.getX() + 7.5f, backButton.getY() + 7.5f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                backButton.setSize(backButton.getWidth() + 15, backButton.getHeight() + 15);
                backButton.setPosition(backButton.getX() - 7.5f, backButton.getY() - 7.5f);
            }
        });

        stage.addActor(levelButton1);
        stage.addActor(levelButton2);
        stage.addActor(levelButton3);
        stage.addActor(backButton);
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
    public void pause() {}

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        levelButtonTexture.dispose();
        level2ButtonTexture.dispose();
        level3ButtonTexture.dispose();
        lockedlevelButtonTexture.dispose();
        chuckBirdTexture.dispose();
        backButtonTexture.dispose();
        spriteBatch.dispose();
        stage.dispose();
    }
}
