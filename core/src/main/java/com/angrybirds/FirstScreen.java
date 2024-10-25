package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FirstScreen implements Screen {
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private static final float VIRTUAL_WIDTH = 1920;
    private static final float VIRTUAL_HEIGHT = 1080;
    private Main game;
    private Stage stage;
    private Music backgroundMusic;
    private Sound buttonClickSound;

    // Textures for image buttons
    private Texture playButtonTexture;
    private Texture exitButtonTexture;
    private Texture loadButtonTexture; // Texture for Load Game button

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
        // Initialize assets in the show method
        backgroundTexture = new Texture(Gdx.files.internal("Untitled design.png"));
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Load and play background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Angry-Birds-Theme-Song-Sound-Effect.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        // Load button click sound
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("289721-Select-heavy-complex-hi-tech-tone-04.mp3"));

        // Load textures for buttons
        playButtonTexture = new Texture(Gdx.files.internal("Play_final_button-removebg-preview.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("EXIT_FINAL_BUTTON-removebg-preview.png"));
        loadButtonTexture = new Texture(Gdx.files.internal("LOAD-SAVED-removebg-preview (1).png")); // Load game button texture

        // Create PLAY button
        ImageButton playButton = new ImageButton(new TextureRegionDrawable(playButtonTexture));
        playButton.setPosition(VIRTUAL_WIDTH / 2 - playButton.getWidth() / 2, VIRTUAL_HEIGHT / 2 - playButton.getHeight() / 2);

        // Play button click listener
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.play();
                System.out.println("PLAY button clicked");
                game.setScreen(new LoadingScreen(game));
            }
        });

        // Create Load Game button as ImageButton
        ImageButton loadGameButton = new ImageButton(new TextureRegionDrawable(loadButtonTexture));
        loadGameButton.setSize(playButton.getWidth() * 0.8f, playButton.getHeight() * 0.8f); // Adjust size
        loadGameButton.setPosition(VIRTUAL_WIDTH / 2 - loadGameButton.getWidth() / 2, VIRTUAL_HEIGHT / 2 - loadGameButton.getHeight() - 150); // Position below play button

        // Load game button click listener
        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.play();
                System.out.println("LOAD GAME button clicked");
                loadGame();  // Call a method to handle loading the game
            }
        });

        // Create EXIT button
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitButtonTexture));
        exitButton.setSize(playButton.getWidth() * 0.8f, playButton.getHeight() * 0.8f);
        exitButton.setPosition(VIRTUAL_WIDTH / 2 - exitButton.getWidth() / 2, VIRTUAL_HEIGHT / 2 - playButton.getHeight() - 50);

        // Exit button click listener
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.play();
                System.out.println("EXIT button clicked");
                Gdx.app.exit();
            }
        });

        stage.addActor(playButton);
        stage.addActor(loadGameButton);  // Add Load Game button to stage
        stage.addActor(exitButton);
    }

    private void loadGame() {
        // Implement the logic to load a saved game here
        // For example, you can read from a file or load from a database
        System.out.println("Loading game...");
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
        loadButtonTexture.dispose(); // Dispose the load button texture
        spriteBatch.dispose();
        stage.dispose();
        backgroundMusic.dispose();
        buttonClickSound.dispose();
    }
}
