package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class FirstScreen implements Screen {
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private static final float VIRTUAL_WIDTH = 1920;
    private static final float VIRTUAL_HEIGHT = 1080;
    private Main game;
    private Stage stage;
    private Sound buttonClickSound;

    private Texture playButtonTexture;
    private Texture exitButtonTexture;
    private Texture loadButtonTexture;

    public FirstScreen(Main game) {
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
        backgroundTexture = new Texture(Gdx.files.internal("Untitled design.png"));
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("289721-Select-heavy-complex-hi-tech-tone-04.mp3"));

        playButtonTexture = new Texture(Gdx.files.internal("Play_final_button-removebg-preview.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("EXIT_FINAL_BUTTON-removebg-preview.png"));
        loadButtonTexture = new Texture(Gdx.files.internal("LOAD-SAVED-removebg-preview (1).png"));

        // Set up Play button with hover effect
        ImageButton playButton = new ImageButton(new TextureRegionDrawable(playButtonTexture));
        playButton.setPosition(VIRTUAL_WIDTH / 2 - playButton.getWidth() / 2, VIRTUAL_HEIGHT / 2 + playButton.getHeight() - 200);

        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonClickSound.play();
                System.out.println("PLAY button clicked");
                game.setScreen(new LoadingScreen(game, false));
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                playButton.setSize(playButton.getWidth() + 10, playButton.getHeight() + 20); // Increase size
                playButton.setPosition(playButton.getX() - 5, playButton.getY() - 5); // Adjust position
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                playButton.setSize(playButton.getWidth() - 10, playButton.getHeight() - 20); // Reset size
                playButton.setPosition(playButton.getX() + 5, playButton.getY() + 5); // Adjust position
            }
        });

        // Load Game button with hover effect
        ImageButton loadGameButton = new ImageButton(new TextureRegionDrawable(loadButtonTexture));
        loadGameButton.setSize(playButton.getWidth() * 0.8f, playButton.getHeight() * 0.8f);
        loadGameButton.setPosition(VIRTUAL_WIDTH / 2 - loadGameButton.getWidth() / 2 - 65, playButton.getY() - loadGameButton.getHeight() + 30);

        loadGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonClickSound.play();
                System.out.println("LOAD GAME button clicked");
                game.setScreen(new LoadingScreen(game, true));
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                loadGameButton.setSize(loadGameButton.getWidth() + 10, loadGameButton.getHeight() + 10); // Increase size
                loadGameButton.setPosition(loadGameButton.getX() - 5, loadGameButton.getY() - 5); // Adjust position
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                loadGameButton.setSize(loadGameButton.getWidth() - 10, loadGameButton.getHeight() - 10); // Reset size
                loadGameButton.setPosition(loadGameButton.getX() + 5, loadGameButton.getY() + 5); // Adjust position
            }
        });

        // Exit button with hover effect
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitButtonTexture));
        exitButton.setSize(playButton.getWidth() * 0.8f, playButton.getHeight() * 0.8f);
        exitButton.setPosition(VIRTUAL_WIDTH / 2 - exitButton.getWidth() / 2 - 10, loadGameButton.getY() - exitButton.getHeight() - 10);

        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonClickSound.play();
                System.out.println("EXIT button clicked");
                Gdx.app.exit();
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitButton.setSize(exitButton.getWidth() + 10, exitButton.getHeight() + 10); // Increase size
                exitButton.setPosition(exitButton.getX() - 5, exitButton.getY() - 5); // Adjust position
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitButton.setSize(exitButton.getWidth() - 10, exitButton.getHeight() - 10); // Reset size
                exitButton.setPosition(exitButton.getX() + 5, exitButton.getY() + 5); // Adjust position
            }
        });

        stage.addActor(playButton);
        stage.addActor(loadGameButton);
        stage.addActor(exitButton);
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
        playButtonTexture.dispose();
        exitButtonTexture.dispose();
        loadButtonTexture.dispose();
        spriteBatch.dispose();
        stage.dispose();
        buttonClickSound.dispose();
    }
}
