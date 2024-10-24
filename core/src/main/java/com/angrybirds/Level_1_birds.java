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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;

public class Level_1_birds implements Screen {
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private static final float VIRTUAL_WIDTH = 800;
    private static final float VIRTUAL_HEIGHT = 448;
    private Main game;
    private Stage stage;
    private Texture pauseButtonTexture;
    private World world;
    private RedBird bird1;
    private YellowBird bird2;
    private Blackbird bird3;
    private WoodStructure wood1;
    private WoodStructureHorizontal wood2;
    private WoodStructure wood3;
    private slingshot slingshot;




    private Smallpig pig1;// Bird instance

    public Level_1_birds(Main game) {
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
        // Load background and initialize camera
        backgroundTexture = new Texture(Gdx.files.internal("birdbackground.png"));
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        // Initialize Box2D world and Bird object
        world = new World(new Vector2(0, -9.8f), true);
        slingshot=new slingshot(world,1.45f,1.5f,0.75f,1f);
        bird1 = new RedBird(world, 0.75f, 1.5f);
        bird2= new YellowBird(world,1.5f,1.75f);
        pig1= new Smallpig(world,6.35f,1.4f);
        wood1= new WoodStructure(world,6.01f,1.21f,0.2f,0.6f);
        wood2= new WoodStructureHorizontal(world,6.13f,1.7f,0.55f,0.2f);
        wood3= new WoodStructure(world,6.55f,1.21f,0.2f,0.6f);






        // Create the stage and UI elements
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        pauseButtonTexture = new Texture(Gdx.files.internal("pausebutton.png"));
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(pauseButtonTexture));
        pauseButton.setPosition(VIRTUAL_WIDTH - 100, VIRTUAL_HEIGHT - 100);
        pauseButton.setSize(100, 100);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Pause button clicked");
                game.setScreen(new PauseScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                pauseButton.setSize(115, 115);
                pauseButton.setPosition(pauseButton.getX() - 7.5f, pauseButton.getY() - 7.5f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                pauseButton.setSize(100, 100);
                pauseButton.setPosition(pauseButton.getX() + 7.5f, pauseButton.getY() + 7.5f);
            }
        });
        stage.addActor(pauseButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        // Step the physics simulation
        world.step(1 / 60f, 6, 2);

        // Update camera and SpriteBatch
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Draw the background
        spriteBatch.draw(backgroundTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // Draw the bird and pig textures at the correct positions
        Vector2 RedbirdPosition = bird1.getPosition();
        Vector2 YellowbirdPosition = bird2.getPosition();
        Vector2 SmallpigPosition = pig1.getPosition();

        // Dynamically draw birds and pig
        spriteBatch.draw(bird1.getTexture(), RedbirdPosition.x * 100 - 25, RedbirdPosition.y * 100 - 25, 50 ,50);
        spriteBatch.draw(bird2.getTexture(), YellowbirdPosition.x * 100 - 25, YellowbirdPosition.y * 100 - 25, 50, 50);
        spriteBatch.draw(pig1.getTexture(), SmallpigPosition.x * 100 - 25, SmallpigPosition.y * 100 - 25,50, 50);

        // Draw the wood structures with dynamic sizes
        Vector2 wood1Position = wood1.getPosition();
        Vector2 wood2Position = wood2.getPosition();
        Vector2 wood3Position = wood3.getPosition();
        Vector2 slingshotPosition = slingshot.getPosition();


        spriteBatch.draw(slingshot.getTexture(), slingshotPosition.x * 100 - (slingshot.getWidth() / 2), slingshotPosition.y * 100 - (slingshot.getHeight() / 2), slingshot.getWidth() * 100, slingshot.getHeight()* 100);
        spriteBatch.draw(wood1.getTexture(), wood1Position.x * 100 - (wood1.getWidth() / 2), wood1Position.y * 100 - (wood1.getHeight() / 2), wood1.getWidth() * 100, wood1.getHeight() * 100);
        spriteBatch.draw(wood2.getTexture(), wood2Position.x * 100 - (wood2.getWidth() / 2), wood2Position.y * 100 - (wood2.getHeight() / 2), wood2.getWidth() * 100, wood2.getHeight() * 100);
        spriteBatch.draw(wood3.getTexture(), wood3Position.x * 100 - (wood3.getWidth() / 2), wood3Position.y * 100 - (wood3.getHeight() / 2), wood3.getWidth() * 100, wood3.getHeight() * 100);

        spriteBatch.end();

        // Render the stage (UI elements)
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
        bird1.dispose();
        bird2.dispose();
        pig1.dispose();
        wood1.dispose();
        wood2.dispose();
        wood3.dispose();
        slingshot.dispose();
        spriteBatch.dispose();
        stage.dispose();
        pauseButtonTexture.dispose();
        world.dispose();
    }
}
