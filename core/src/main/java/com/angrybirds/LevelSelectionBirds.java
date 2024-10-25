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

public class LevelSelectionBirds implements Screen {
    private Texture backgroundTexture;
    private Texture levelButtonTexture;
    private Texture level2ButtonTexture;
    private Texture level3ButtonTexture;
    private Texture lockedlevelButtonTexture;
    private Texture chuckBirdTexture; // Load the chuckbird texture
    private Texture backButtonTexture; // Texture for back button
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private static final float VIRTUAL_WIDTH = 800;
    private static final float VIRTUAL_HEIGHT = 448;
    private Main game;

    public LevelSelectionBirds(Main game) {
        this.game = game;
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void show() {
        // Load textures
        backgroundTexture = new Texture(Gdx.files.internal("Untitled design (1).png"));
        levelButtonTexture = new Texture(Gdx.files.internal("1.png"));
        level2ButtonTexture = new Texture(Gdx.files.internal("Screenshot_2024-10-24_092517-removebg-preview.png"));
        level3ButtonTexture = new Texture(Gdx.files.internal("Screenshot_2024-10-24_092530-removebg-preview.png"));
        lockedlevelButtonTexture = new Texture(Gdx.files.internal("image.png"));
        backButtonTexture = new Texture(Gdx.files.internal("backbuttonremoved.png")); // Load back button texture
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Level button size (unchanged)
        float buttonWidth = 60;  // Size of levelButton1
        float buttonHeight = 60; // Size of levelButton1

        // Button 1: Level button 1
        ImageButton levelButton1 = new ImageButton(new TextureRegionDrawable(levelButtonTexture));
        levelButton1.setPosition(335, 30);
        levelButton1.setSize(buttonWidth, buttonHeight);
        levelButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level 1 selected");
                game.setScreen(new Level_1_birds(game)); // Redirect to Level_1
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Increase the size of levelButton1
                levelButton1.setSize(buttonWidth + 15, buttonHeight + 15); // Increase size by 15 pixels
                levelButton1.setPosition(levelButton1.getX() - 7.5f, levelButton1.getY() - 7.5f); // Center the button
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Reset the size of levelButton1
                levelButton1.setSize(buttonWidth, buttonHeight); // Reset size
                levelButton1.setPosition(levelButton1.getX() + 7.5f, levelButton1.getY() + 7.5f); // Center the button back
            }
        });

        // Button 2: Level button 2
        ImageButton levelButton2 = new ImageButton(new TextureRegionDrawable(level2ButtonTexture));
        levelButton2.setPosition(510, 80);
        levelButton2.setSize(buttonWidth, buttonHeight);
        levelButton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level 2 selected");
                // Implement redirection to Level 2
            }
        });

        // Button 3: Level button 3
        ImageButton levelButton3 = new ImageButton(new TextureRegionDrawable(level3ButtonTexture));
        levelButton3.setPosition(340, 190);
        levelButton3.setSize(buttonWidth, buttonHeight);
        levelButton3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level 3 selected");
                // Implement redirection to Level 3
            }
        });

        // Create BACK button as an ImageButton
        final ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setPosition(20, 20); // Bottom left position
        backButton.setSize(115, 65); // Original size increased by 15 pixels
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Back button clicked");
                game.setScreen(new PlayAsScreen(game)); // Change to your main menu screen
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                backButton.setSize(backButton.getWidth() - 15, backButton.getHeight() - 15); // Reduce size by 15 pixels
                backButton.setPosition(backButton.getX() + 7.5f, backButton.getY() + 7.5f); // Center the button
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                backButton.setSize(backButton.getWidth() + 15, backButton.getHeight() + 15); // Reset size back to original
                backButton.setPosition(backButton.getX() - 7.5f, backButton.getY() - 7.5f); // Center the button back
            }
        });


        // Add buttons to the stage
        stage.addActor(levelButton1);
        stage.addActor(levelButton2);
        stage.addActor(levelButton3);
        stage.addActor(backButton); // Add back button to the stage
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        // Draw background
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
        chuckBirdTexture.dispose(); // Dispose of chuckbird texture
        backButtonTexture.dispose(); // Dispose of back button texture
        spriteBatch.dispose();
        stage.dispose();
    }
}
