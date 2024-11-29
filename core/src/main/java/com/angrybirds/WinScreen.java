package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class WinScreen implements Screen {

    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture levelsButtonTexture;
    private Texture nextLevelButtonTexture;
    private Stage stage;
    private Main game;
    private int currentLevel;
    private int score;
    private Music backgroundMusic;
    private BitmapFont scoreFont;

    private float scoreX;
    private float scoreY;

    // Constants for button dimensions and positioning
    private static final float BUTTON_WIDTH = 150f;
    private static final float BUTTON_HEIGHT = 150f;
    private static final float BOTTOM_PADDING = 20f; // Space from bottom of screen
    private static final float SIDE_PADDING = 20f;   // Space from sides of screen
    private static final int MAX_LEVELS = 3; // Maximum number of levels

    public WinScreen(Main game, int completedLevel, int score) {
        this.game = game;
        this.currentLevel = completedLevel;
        this.score = score;
        batch = new SpriteBatch();

        // Initialize font
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.WHITE);
        scoreFont.getData().setScale(2f); // Adjust size as needed
    }

    // Method to set custom score display coordinates
    public void setScoreDisplayCoordinates(float x, float y) {
        this.scoreX = x;
        this.scoreY = y;
    }

    @Override
    public void show() {
        // Choose background and music based on score
        if (score > 2300) {
            backgroundTexture = new Texture(Gdx.files.internal("victory_silver_screen.jpeg"));
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("myinstants.mp3"));

            // Default coordinates for gold screen
            setScoreDisplayCoordinates(
                Gdx.graphics.getWidth() * 0.45f,
                Gdx.graphics.getHeight() * 0.7f
            );
        } else if (score > 2100) {
            backgroundTexture = new Texture(Gdx.files.internal("victory_gold_screen.jpeg"));
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("myinstants.mp3"));

            // Default coordinates for silver screen
            setScoreDisplayCoordinates(
                Gdx.graphics.getWidth() * 0.45f,
                Gdx.graphics.getHeight() * 0.7f
            );
        } else {
            backgroundTexture = new Texture(Gdx.files.internal("victory_bronze_screen.jpeg"));
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("myinstants.mp3"));

            // Default coordinates for bronze screen
            setScoreDisplayCoordinates(
                Gdx.graphics.getWidth() * 0.45f,
                Gdx.graphics.getHeight() * 0.7f
            );
        }

        // Configure music playback
        backgroundMusic.setLooping(false);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

        levelsButtonTexture = new Texture(Gdx.files.internal("backbuttonremoved.png"));
        nextLevelButtonTexture = new Texture(Gdx.files.internal("nextbutton.png"));

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Levels button (bottom left)
        ImageButton levelsButton = new ImageButton(new TextureRegionDrawable(levelsButtonTexture));
        levelsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        levelsButton.setPosition(SIDE_PADDING, BOTTOM_PADDING);
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusic();
                System.out.println("LEVELS button clicked");
                game.setScreen(new LevelSelectionBirds(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                levelsButton.setSize(BUTTON_WIDTH - 10, BUTTON_HEIGHT - 10);
                levelsButton.setPosition(levelsButton.getX() + 5, levelsButton.getY() + 5);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                levelsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
                levelsButton.setPosition(levelsButton.getX() - 5, levelsButton.getY() - 5);
            }
        });
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = null;
        style.down = null;
        style.checked = null;

        // Next Level button (centered)
        ImageButton nextLevelButton = new ImageButton(style); // Remove the texture
        nextLevelButton.setSize(BUTTON_WIDTH+50, BUTTON_HEIGHT+50);
        nextLevelButton.setPosition(
            (Gdx.graphics.getWidth() - BUTTON_WIDTH) / 2f,
            BOTTOM_PADDING
        );
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusic();
                System.out.println("NEXT LEVEL button clicked");

                // Navigate to the next level
                Screen nextLevelScreen;
                switch (currentLevel) {
                    case 1:
                        nextLevelScreen = new Level_2_Birds(game);
                        break;
                    case 2:
                        nextLevelScreen = new Level_3_birds(game);
                        break;
                    default:
                        nextLevelScreen = new Level_1_birds(game);
                        break;
                }
                game.setScreen(nextLevelScreen);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                nextLevelButton.setSize(BUTTON_WIDTH - 10, BUTTON_HEIGHT - 10);
                nextLevelButton.setPosition(
                    (Gdx.graphics.getWidth() - (BUTTON_WIDTH - 10)) / 2f,
                    BOTTOM_PADDING + 5
                );
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                nextLevelButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
                nextLevelButton.setPosition(
                    (Gdx.graphics.getWidth() - BUTTON_WIDTH) / 2f,
                    BOTTOM_PADDING
                );
            }
        });

        stage.addActor(levelsButton);
        stage.addActor(nextLevelButton);
    }

    // Method to stop music
    private void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        // Draw background
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw score
        scoreFont.draw(batch, "Score: " + score, scoreX, scoreY);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        stopMusic();
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stopMusic();
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stopMusic(); // Ensure music is stopped and resources are freed
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (levelsButtonTexture != null) levelsButtonTexture.dispose();
        if (nextLevelButtonTexture != null) nextLevelButtonTexture.dispose();
        if (scoreFont != null) scoreFont.dispose();
        stage.dispose();
    }
}
