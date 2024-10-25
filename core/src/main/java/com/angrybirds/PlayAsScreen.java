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

public class PlayAsScreen implements Screen {
    private Texture backgroundTexture;
    private Texture chuckbirdTexture;
    private Texture slypigTexture;
    private Texture backButtonTexture; // Add texture for the back button
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private static final float VIRTUAL_WIDTH = 800;
    private static final float VIRTUAL_HEIGHT = 448;
    private Main game;

    public PlayAsScreen(Main game) {
        this.game = game;
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void show() {
        // Load textures
        backgroundTexture = new Texture(Gdx.files.internal("background2.png"));
        chuckbirdTexture = new Texture(Gdx.files.internal("chuckbird.png"));
        slypigTexture = new Texture(Gdx.files.internal("slypig.png"));
        backButtonTexture = new Texture(Gdx.files.internal("backbuttonremoved.png")); // Load back button texture
        spriteBatch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Chuckbird button
        final ImageButton chuckbirdButton = new ImageButton(new TextureRegionDrawable(chuckbirdTexture));
        chuckbirdButton.setPosition(200, 200);
        chuckbirdButton.setSize(150, 150);
        chuckbirdButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Chuckbird selected");
                game.setScreen(new LevelSelectionBirds(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                chuckbirdButton.setSize(165, 165); // Increase size by 15 pixels
                chuckbirdButton.setPosition(chuckbirdButton.getX() - 7.5f, chuckbirdButton.getY() - 7.5f); // Center the button
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                chuckbirdButton.setSize(150, 150); // Reset size
                chuckbirdButton.setPosition(chuckbirdButton.getX() + 7.5f, chuckbirdButton.getY() + 7.5f); // Center the button back
            }
        });

        // Slypig button
        final ImageButton slypigButton = new ImageButton(new TextureRegionDrawable(slypigTexture));
        slypigButton.setPosition(450, 200);
        slypigButton.setSize(150, 150);
        slypigButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Slypig selected");
                game.setScreen(new LevelSelectionPigs(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                slypigButton.setSize(165, 165); // Increase size by 15 pixels
                slypigButton.setPosition(slypigButton.getX() - 7.5f, slypigButton.getY() - 7.5f); // Center the button
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                slypigButton.setSize(150, 150); // Reset size
                slypigButton.setPosition(slypigButton.getX() + 7.5f, slypigButton.getY() + 7.5f); // Center the button back
            }
        });

        // Back button
        // Back button
        final ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setPosition(20, 20); // Bottom left position
        backButton.setSize(115, 65); // Original size increased by 15 pixels
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Back button clicked");
                game.setScreen(new FirstScreen(game)); // Change to your main menu screen
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


        stage.addActor(chuckbirdButton);
        stage.addActor(slypigButton);
        stage.addActor(backButton); // Add the back button to the stage
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
        chuckbirdTexture.dispose();
        slypigTexture.dispose();
        backButtonTexture.dispose(); // Dispose of the back button texture
        spriteBatch.dispose();
        stage.dispose();
    }
}
