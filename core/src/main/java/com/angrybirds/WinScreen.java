package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private Texture levelsButtonTexture;
    private Texture forwardButtonTexture;
    private Stage stage;
    private Main game;

    // Constants for button dimensions and positioning
    private static final float BUTTON_WIDTH = 150f;
    private static final float BUTTON_HEIGHT = 150f;
    private static final float BOTTOM_PADDING = 20f; // Space from bottom of screen
    private static final float SIDE_PADDING = 20f;   // Space from sides of screen

    public WinScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("victory screen.jpg"));
        victoryTexture = new Texture(Gdx.files.internal("vctory.png"));
        levelsButtonTexture = new Texture(Gdx.files.internal("LEVELS_BUTTON-Photoroom.png"));
        forwardButtonTexture = new Texture(Gdx.files.internal("forwardbutton.png"));

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Victory button (center)
        ImageButton victoryButton = new ImageButton(new TextureRegionDrawable(victoryTexture));
        victoryButton.setSize(308 * 2, 91 * 2);
        victoryButton.setPosition(
            (Gdx.graphics.getWidth() - victoryButton.getWidth()) / 2,
            (Gdx.graphics.getHeight() - victoryButton.getHeight()) / 2
        );
        victoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new FirstScreen(game));
            }
        });

        // Levels button (bottom left)
        ImageButton levelsButton = new ImageButton(new TextureRegionDrawable(levelsButtonTexture));
        levelsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        levelsButton.setPosition(SIDE_PADDING, BOTTOM_PADDING);
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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

        // Forward button (bottom right)
        ImageButton forwardButton = new ImageButton(new TextureRegionDrawable(forwardButtonTexture));
        forwardButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        forwardButton.setPosition(
            Gdx.graphics.getWidth() - BUTTON_WIDTH - SIDE_PADDING,
            BOTTOM_PADDING
        );
        forwardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("FORWARD button clicked");
                // Note: You'll need to specify the next screen here
                // game.setScreen(new NextLevelScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                forwardButton.setSize(BUTTON_WIDTH - 10, BUTTON_HEIGHT - 10);
                forwardButton.setPosition(forwardButton.getX() + 5, forwardButton.getY() + 5);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                forwardButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
                forwardButton.setPosition(forwardButton.getX() - 5, forwardButton.getY() - 5);
            }
        });

        stage.addActor(victoryButton);
        stage.addActor(levelsButton);
        stage.addActor(forwardButton);
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
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (victoryTexture != null) victoryTexture.dispose();
        if (levelsButtonTexture != null) levelsButtonTexture.dispose();
        if (forwardButtonTexture != null) forwardButtonTexture.dispose();
        stage.dispose();
    }
}
