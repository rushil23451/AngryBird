package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.badlogic.gdx.audio.Sound;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class Level_1_birds implements Screen, ContactListener {
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private static final float VIRTUAL_WIDTH = 800;
    private static final float VIRTUAL_HEIGHT = 448;
    private Main game;
    private int score=0;
    private Stage stage;
    private Texture pauseButtonTexture;
    private ShapeRenderer shapeRenderer;
    private float buttonRadius = 25f;
    private float buttonX = 40f;
    private float buttonY = 40f;
    private static final float PPM = 100f;
    private Bird activeBird;
    private Array<Bird> birdQueue;
    private static final int TOTAL_BIRDS = 3;
    private static final float BIRD_SPACING = 50f;
    private slingshot slingshot;
    private World world;
    private WoodStructure wood1;
    private WoodStructure wood3;
    private WoodStructure wood4;
    private WoodStructure wood5;
    private WoodStructureHorizontal wood2;
    private Box2DDebugRenderer debugRenderer;
    private BitmapFont font;
    private boolean victoryConditionMet = false;
    private float victoryTimer = 0f;
    private static final float VICTORY_DELAY = 3f;
    private boolean isDragging = false;
    private Vector2 dragStartPosition = new Vector2();
    private Vector2 dragEndPosition = new Vector2();
    private Vector2 dragDirection = new Vector2();

    private static final short BIRD_CATEGORY_BITS = 0x0001;
    private static final short WOOD_CATEGORY_BITS = 0x0002;
    private static final short GROUND_CATEGORY_BITS = 0x0004;

    private static final float MAX_DRAG_DISTANCE = 3f;
    private static final float MIN_DRAG_DISTANCE = 0.5f;
    private static final float LAUNCH_MULTIPLIER = 14f;
    private static final float GRAVITY = -9.8f;
    private static final float TRAJECTORY_TIME = 2f;
    private static final float TRAJECTORY_STEP = 0.1f;

    private Array<Vector2> trajectoryPoints = new Array<>();
    private List<Structure> allWoodStructures=new ArrayList<>();;
    private List<Body> bodiesToRemove = new ArrayList<>();
    private List<Pig> pigs = new ArrayList<>();
    private Sound slingshotSound;
    private boolean isSoundPlaying = false;


    private boolean birdLaunched = false;
    private float respawnTimer = 0;
    private static final float RESPAWN_DELAY = 2f;
    private static final float OFF_SCREEN_X = VIRTUAL_WIDTH + 100;
    private static final float OFF_SCREEN_Y = -100;


    public Level_1_birds(Main game) {
        this.game = game;
        this.score =0;
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("newleveel.png"));
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);



        font = new BitmapFont();
        font.setColor(Color.WHITE);
        slingshotSound = Gdx.audio.newSound(Gdx.files.internal("angry-birds-slingshot.mp3"));

        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(this);
        debugRenderer = new Box2DDebugRenderer();

        float slingX = 140f / PPM;
        float slingY = 190f / PPM;
        float slingWidth = 55f / PPM;
        float slingHeight = 120f / PPM;
        slingshot = new slingshot(world, slingX, slingY, slingWidth, slingHeight);

        // Initialize bird queue and `spawn` first bird
        birdQueue = new Array<>();
        initializeBirdQueue();

        float staticWidth = 800f / PPM;
        float staticHeight = 100f / PPM;
        float staticX = 600f / PPM;
        float staticY = staticHeight / 2;

        createStaticRectangle(staticX, staticY, staticWidth, staticHeight);

        // Initialize wood structures
        initializeWoodStructures();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        pauseButtonTexture = new Texture(Gdx.files.internal("pausebutton.png"));
        shapeRenderer = new ShapeRenderer();
        pauseButtonTexture = new Texture(Gdx.files.internal("pausebutton.png"));
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(pauseButtonTexture));
        pauseButton.setPosition(VIRTUAL_WIDTH - 100, VIRTUAL_HEIGHT - 100);
        pauseButton.setSize(100, 100);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Pause button clicked");

                game.setScreen(new PauseScreen(game,1));
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

    private void initializeBirdQueue() {
        birdQueue.clear();
        float startX = 50f / PPM;
        float startY = 110f / PPM;
        float birdRadius = 30f / PPM;

        // Create RedBird
        RedBird redBird = new RedBird(world, startX, startY, birdRadius);
        redBird.getBody().setType(BodyDef.BodyType.KinematicBody);
        birdQueue.add(redBird);

        // Create YellowBird
        YellowBird yellowBird = new YellowBird(world, startX + (BIRD_SPACING / PPM), startY, birdRadius);
        yellowBird.getBody().setType(BodyDef.BodyType.KinematicBody);
        birdQueue.add(yellowBird);

        // Create BlackBird
        Blackbird blackBird = new Blackbird(world, startX + (1/2* BIRD_SPACING / PPM), startY, birdRadius+10f/PPM);
        blackBird.getBody().setType(BodyDef.BodyType.KinematicBody);
        birdQueue.add(blackBird);

        // Spawn the first active bird
        spawnNewBird();
    }

    private void initializeWoodStructures() {
        // Calculate base height from ground
        float groundLevel = (200f / PPM) ;  // staticHeight/2 from your ground rectangle

        float woodWidth = 17.5f / PPM;
        float woodHeight = 75f / PPM;

        float wood2Width = 120f / PPM;
        float wood2Height = 17.5f / PPM;


        float pigRadius = 15f / PPM;

        float baseY = groundLevel + (woodHeight);  // Center point of the wood pieces
        wood1 = new WoodStructure(world, 540f / PPM, groundLevel, woodWidth, woodHeight, true);
        wood3 = new WoodStructure(world, 630f / PPM, groundLevel, woodWidth, woodHeight, true);

        float horizontalY = baseY + (woodHeight) + (wood2Height);
        wood2 = new WoodStructureHorizontal(world, 580f / PPM, baseY, wood2Width, wood2Height, true);

        float upperY = baseY + (woodWidth);
        wood4 = new WoodStructure(world, 550f / PPM, upperY, woodWidth, woodHeight, true);
        wood5 = new WoodStructure(world, 620f / PPM, upperY, woodWidth, woodHeight, true);
        SmallPig smallPig1 = new SmallPig(world, 590f / PPM, upperY-woodWidth/2,pigRadius);
        SmallPig smallPig2 = new SmallPig(world, 590f / PPM, groundLevel,pigRadius);
        smallPig1.getPigBody().setUserData(smallPig1);
        smallPig2.getPigBody().setUserData(smallPig2);
        pigs.add(smallPig1);
        pigs.add(smallPig2);
        allWoodStructures.add(wood1);
        allWoodStructures.add(wood3);
        allWoodStructures.add(wood2);

        allWoodStructures.add(wood4);
        allWoodStructures.add(wood5);



        setupWoodStructureCollision(wood1);
        setupWoodStructureCollision(wood2);
        setupWoodStructureCollision(wood3);
        setupWoodStructureCollision(wood4);
        setupWoodStructureCollision(wood5);

    }

    private void setupWoodStructureCollision(Object woodStructure) {
        Body body;
        if (woodStructure instanceof WoodStructure) {
            body = ((WoodStructure) woodStructure).getBody();
        } else if (woodStructure instanceof WoodStructureHorizontal) {
            body = ((WoodStructureHorizontal) woodStructure).getBody();
        } else {
            return;
        }

        // Set userData to help identify the object in collision
        body.setUserData(woodStructure);
//
//         Modify the fixture to ensure proper collision detection
//        for (Fixture fixture : body.getFixtureList()) {
//            // Set up collision filtering if needed
//            Filter filter = new Filter();
//            filter.categoryBits = WOOD_CATEGORY_BITS;  // Define this constant
//            filter.maskBits = BIRD_CATEGORY_BITS | GROUND_CATEGORY_BITS;  // Define these constants
//            fixture.setFilterData(filter);
//        }
    }

    private void spawnNewBird() {
        float slingX = 140f / PPM;
        float slingY = 195f / PPM;
        float slingHeight = 100f / PPM;
        float birdRadius = 25f / PPM;

        // Clean up previous bird
        if (activeBird != null && activeBird.getBody() != null) {
            world.destroyBody(activeBird.getBody());
            activeBird = null;
        }

        if (birdQueue.size > 0) {
            Bird nextBird = birdQueue.first();
            // Destroy the body of the queued bird before removing it
            if (nextBird.getBody() != null) {
                world.destroyBody(nextBird.getBody());
            }
            birdQueue.removeIndex(0);

            // Create new active bird at slingshot position
            if (nextBird instanceof RedBird) {
                activeBird = new RedBird(world, slingX, (slingY + slingHeight)-20f/PPM, birdRadius);
            } else if (nextBird instanceof YellowBird) {
                activeBird = new YellowBird(world, slingX, (slingY + slingHeight)-20f/PPM, birdRadius);
            } else if (nextBird instanceof Blackbird) {
                activeBird = new Blackbird(world, slingX, (slingY + slingHeight)-20f/PPM, birdRadius+5f/PPM);
            }

            birdLaunched = false;
            isDragging = false;

            float startX = 100f / PPM;
            float startY = 160f / PPM;
            for (int i = 0; i < birdQueue.size; i++) {
                Bird bird = birdQueue.get(i);
                if (bird.getBody() != null) {
                    world.destroyBody(bird.getBody());
                }
                if (bird instanceof RedBird) {
                    bird = new RedBird(world, startX - (i * BIRD_SPACING / PPM), startY, birdRadius);
                } else if (bird instanceof YellowBird) {
                    bird = new YellowBird(world, startX - (i * BIRD_SPACING / PPM), startY, birdRadius);
                } else if (bird instanceof Blackbird) {
                    bird = new Blackbird(world, startX - (i * BIRD_SPACING / PPM), startY, birdRadius+5f/PPM);
                }
                bird.getBody().setType(BodyDef.BodyType.KinematicBody);
                birdQueue.set(i, bird);
            }
        }
    }
    private void drawPigs() {
        for (Pig pig : pigs) {
            if (pig.isAlive()) {
                Vector2 pos = pig.getPosition();
                float rotation = pig.getPigBody().getAngle() * MathUtils.radiansToDegrees;

                spriteBatch.draw(
                    pig.getPigTexture(),
                    pos.x * PPM - (pig.getPigBody().getFixtureList().first().getShape().getRadius() * PPM),
                    pos.y * PPM - (pig.getPigBody().getFixtureList().first().getShape().getRadius() * PPM),
                    pig.getPigBody().getFixtureList().first().getShape().getRadius() * PPM,
                    pig.getPigBody().getFixtureList().first().getShape().getRadius() * PPM,
                    pig.getPigBody().getFixtureList().first().getShape().getRadius() * PPM * 2,
                    pig.getPigBody().getFixtureList().first().getShape().getRadius() * PPM * 2,
                    1, 1,
                    rotation,
                    0, 0,
                    pig.getPigTexture().getWidth(),
                    pig.getPigTexture().getHeight(),
                    false, false
                );
            }
        }
    }
    private void createStaticRectangle(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 100f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    private void updateBirdState(float delta) {
        if (birdLaunched && activeBird != null) {
            Vector2 position = activeBird.getPosition();
            Vector2 velocity = activeBird.getBody().getLinearVelocity();

            boolean isOffScreen = position.x * PPM > OFF_SCREEN_X || position.y * PPM < OFF_SCREEN_Y;
            boolean isStopped = Math.abs(velocity.x) <= 0.5f;

            if (isOffScreen || isStopped) {
                respawnTimer += delta;
                if (respawnTimer >= RESPAWN_DELAY) {
                    spawnNewBird();
                    respawnTimer = 0;
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        updateWorld();
        updateBirdState(delta);
        if (victoryConditionMet) {
            victoryTimer += delta;
            if (victoryTimer >= VICTORY_DELAY) {

                game.setScreen(new WinScreen(game,1,score));
                return;
            }
        }
        camera.update();

        Matrix4 debugMatrix = camera.combined.cpy();
        debugMatrix.scale(PPM, PPM, 1f);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        String scoreText = String.format("Score: %d", score);
        font.draw(spriteBatch, scoreText, 10, VIRTUAL_HEIGHT - 20);
        spriteBatch.end();
        checkGameState();
        spriteBatch.begin();

        // Draw wood structures
        drawPigs();
        drawWoodStructures();

        // Draw slingshot
        Vector2 slingPos = slingshot.getPosition();
        spriteBatch.draw(
            slingshot.getTexture(),
            slingPos.x * PPM - (slingshot.getWidth() * PPM / 2),
            slingPos.y * PPM - (slingshot.getHeight() * PPM / 2),
            slingshot.getWidth() * PPM,
            slingshot.getHeight() * PPM
        );

        // Draw waiting birds
        for (Bird bird : birdQueue) {
            Vector2 pos = bird.getPosition();
            spriteBatch.draw(
                bird.getTexture(),
                pos.x * PPM - (bird.getRadius() * PPM),
                pos.y * PPM - (bird.getRadius() * PPM),
                bird.getRadius() * PPM * 2,
                bird.getRadius() * PPM * 2
            );
        }

        // Draw active bird
        if (activeBird != null) {
            Vector2 birdPos = activeBird.getPosition();
            float birdRotation = activeBird.getBody().getAngle() * MathUtils.radiansToDegrees;
            spriteBatch.draw(
                activeBird.getTexture(),
                birdPos.x * PPM - (activeBird.getRadius() * PPM),
                birdPos.y * PPM - (activeBird.getRadius() * PPM),
                activeBird.getRadius() * PPM,
                activeBird.getRadius() * PPM,
                activeBird.getRadius() * PPM * 2,
                activeBird.getRadius() * PPM * 2,
                1, 1,
                birdRotation,
                0, 0,
                activeBird.getTexture().getWidth(),
                activeBird.getTexture().getHeight(),
                false, false
            );
        }


        // Draw drag distance
        if (isDragging) {
            float dragDistance = dragDirection.len() / PPM;

        }

        spriteBatch.end();
        checkGameState();

//        debugRenderer.render(world, debugMatrix);

        // Draw UI elements
        drawUIElements();

        handleInput();
        drawTrajectory();

        stage.act(delta);
        stage.draw();
    }

    private void checkGameState() {
        // Check if all pigs are dead or outside screen boundaries
        boolean allPigsDead = pigs.stream()
            .noneMatch(pig -> {
                boolean outsideBoundaries =
                    pig.getPosition().x * PPM < 0 ||
                        pig.getPosition().x * PPM > VIRTUAL_WIDTH ||
                        pig.getPosition().y * PPM < 0 ||
                        pig.getPosition().y * PPM > VIRTUAL_HEIGHT;

                if (outsideBoundaries && pig.isAlive()) {
                    score=score+1000;
                    pig.setAlive(false);
                }

                return pig.isAlive() && !outsideBoundaries;
            });

        // Check if all birds have been used
        boolean allBirdsUsed = birdQueue.isEmpty() && activeBird == null;

        if (allPigsDead && !victoryConditionMet) {
            victoryConditionMet = true;
            victoryTimer = 0f;

        } else if (allBirdsUsed && !allPigsDead) {
            game.setScreen(new LoseScreen1(game,1));
        }
    }


    private void drawWoodStructures() {
        for (Structure structure : allWoodStructures) {
            if (structure instanceof WoodStructure) {
                WoodStructure wood = (WoodStructure) structure;
                if (!wood.isMarkedForRemoval()) {
                    drawRotatedWoodStructure(spriteBatch, wood);
                }
            } else if (structure instanceof WoodStructureHorizontal) {
                WoodStructureHorizontal wood = (WoodStructureHorizontal) structure;
                Vector2 wood2Pos = wood.getPosition();
                float wood2Rotation = wood.getBody().getAngle() * MathUtils.radiansToDegrees;
                spriteBatch.draw(
                    wood.getTexture(),
                    wood2Pos.x * PPM - (wood.getWidth() * PPM / 2),
                    wood2Pos.y * PPM - (wood.getHeight() * PPM / 2),
                    wood.getWidth() * PPM / 2,
                    wood.getHeight() * PPM / 2,
                    wood.getWidth() * PPM,
                    wood.getHeight() * PPM,
                    1, 1,
                    wood2Rotation,
                    0, 0,
                    wood.getTexture().getWidth(),
                    wood.getTexture().getHeight(),
                    false, false
                );
            }
        }
    }


    private void drawRotatedWoodStructure(SpriteBatch batch, WoodStructure wood) {
        Vector2 pos = wood.getPosition();
        float rotation = wood.getBody().getAngle() * MathUtils.radiansToDegrees;
        batch.draw(
            wood.getTexture(),
            pos.x * PPM - (wood.getWidth() * PPM / 2),
            pos.y * PPM - (wood.getHeight() * PPM / 2),
            wood.getWidth() * PPM / 2,
            wood.getHeight() * PPM / 2,
            wood.getWidth() * PPM,
            wood.getHeight() * PPM,
            1, 1,
            rotation,
            0, 0,
            wood.getTexture().getWidth(),
            wood.getTexture().getHeight(),
            false, false
        );
    }

    private void drawUIElements() {


        if (isDragging) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.line(
                dragStartPosition.x, dragStartPosition.y,
                dragEndPosition.x, dragEndPosition.y
            );
            shapeRenderer.end();
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched() && !birdLaunched && activeBird != null) {
            Vector2 touchPosition = new Vector2(
                Gdx.input.getX() * (VIRTUAL_WIDTH / Gdx.graphics.getWidth()),
                VIRTUAL_HEIGHT - Gdx.input.getY() * (VIRTUAL_HEIGHT / Gdx.graphics.getHeight())
            );

            if (!isDragging) {
                Vector2 birdPosition = activeBird.getPosition().scl(PPM);
                if (touchPosition.dst(birdPosition) <= activeBird.getRadius() * PPM) {
                    isDragging = true;
                    dragStartPosition.set(birdPosition);
                    // Play sound when starting to drag
                    if (!isSoundPlaying) {
                        slingshotSound.play(0.5f);
                        isSoundPlaying = true;
                    }
                }
            } else {
                dragEndPosition.set(touchPosition);
                dragDirection.set(dragStartPosition).sub(dragEndPosition);
                if (dragDirection.len() > MAX_DRAG_DISTANCE * PPM) {
                    dragDirection.setLength(MAX_DRAG_DISTANCE * PPM);
                    dragEndPosition.set(dragStartPosition).sub(dragDirection);
                }
                updateTrajectory();
            }
        } else if (Gdx.input.isTouched() && birdLaunched && activeBird != null) {
            Vector2 touchPosition = new Vector2(
                Gdx.input.getX() * (VIRTUAL_WIDTH / Gdx.graphics.getWidth()),
                VIRTUAL_HEIGHT - Gdx.input.getY() * (VIRTUAL_HEIGHT / Gdx.graphics.getHeight())
            );

            // Activate special ability if click is within screen bounds
            if (touchPosition.x >= 0 && touchPosition.x <= VIRTUAL_WIDTH &&
                touchPosition.y >= 0 && touchPosition.y <= VIRTUAL_HEIGHT) {

                if (activeBird instanceof RedBird) {
                    Bird[] additionalBirds = ((RedBird) activeBird).specialAbility();
                    for (Bird newBird : additionalBirds) {
                        birdQueue.add(newBird);
                    }
                } else if (activeBird instanceof YellowBird) {
                    ((YellowBird) activeBird).specialAbility();
                } else if (activeBird instanceof Blackbird) {
                    ((Blackbird) activeBird).specialAbility(allWoodStructures, pigs, camera);
                }
            }
        } else if (isDragging) {
            isDragging = false;
            isSoundPlaying = false;

            if (dragDirection.len() >= MIN_DRAG_DISTANCE * PPM) {
                Vector2 launchVelocity = dragDirection.scl(LAUNCH_MULTIPLIER / 200);
                activeBird.launch(launchVelocity);
                birdLaunched = true;
            }
            trajectoryPoints.clear();
        }
    }


    private static final float DASH_SIZE = 5f;
    private static final float GAP_SIZE = 5f;



    private void updateTrajectory() {
        trajectoryPoints.clear();

        Vector2 initialPosition = new Vector2(activeBird.getPosition().scl(PPM));
        Vector2 velocity = new Vector2(dragStartPosition).sub(dragEndPosition).scl(LAUNCH_MULTIPLIER / 200);
        Vector2 gravity = new Vector2(0, GRAVITY * PPM);

        for (float t = 0; t < TRAJECTORY_TIME; t += TRAJECTORY_STEP) {
            trajectoryPoints.add(new Vector2(initialPosition));
            initialPosition.x += velocity.x * TRAJECTORY_STEP * PPM;
            initialPosition.y += velocity.y * TRAJECTORY_STEP * PPM;
            velocity.add(gravity.scl(TRAJECTORY_STEP));
        }
    }

    private void drawTrajectory() {
        if (trajectoryPoints.isEmpty() || !isDragging) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glLineWidth(3f);  // Increase line thickness

        // Draw a dashed line for the trajectory
        for (int i = 0; i < trajectoryPoints.size - 1; i += 2) {
            Vector2 point1 = trajectoryPoints.get(i);
            Vector2 point2 = trajectoryPoints.get(Math.min(i + 1, trajectoryPoints.size - 1));

            shapeRenderer.line(
                point1.x / PPM, point1.y / PPM,
                point2.x / PPM, point2.y / PPM
            );
        }

        shapeRenderer.end();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        wood1.dispose();
        wood2.dispose();
        wood3.dispose();
        wood4.dispose();
        wood5.dispose();
        spriteBatch.dispose();
        stage.dispose();
        pauseButtonTexture.dispose();
        shapeRenderer.dispose();
        debugRenderer.dispose();
        font.dispose();
        world.dispose();
        slingshotSound.dispose();

        // Dispose of birds
        if (activeBird != null) {
            activeBird.dispose();
        }
        for (Bird bird : birdQueue) {
            bird.dispose();
        }
        for (Pig pig : pigs) {
            pig.dispose();
        }
    }

    // Contact listener methods
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Get bodies involved in the collision
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        // Variables to track different collision types
        Bird bird = null;
        Structure woodStructure = null;
        Pig pig = null;
        Structure otherWoodStructure = null;

        // Identify collision types
        if (bodyA.getUserData() instanceof Bird && bodyB.getUserData() instanceof Pig) {
            bird = (Bird) bodyA.getUserData();
            pig = (Pig) bodyB.getUserData();
            handleBirdPigCollision(bird, pig);
        } else if (bodyB.getUserData() instanceof Bird && bodyA.getUserData() instanceof Pig) {
            bird = (Bird) bodyB.getUserData();
            pig = (Pig) bodyA.getUserData();
            handleBirdPigCollision(bird, pig);
        }

        // Bird-Wood Structure Collision
        if (bodyA.getUserData() instanceof Bird && (bodyB.getUserData() instanceof WoodStructure || bodyB.getUserData() instanceof WoodStructureHorizontal)) {
            bird = (Bird) bodyA.getUserData();
            woodStructure = (Structure) bodyB.getUserData();
            handleBirdWoodCollision(bird, woodStructure);
        } else if (bodyB.getUserData() instanceof Bird && (bodyA.getUserData() instanceof WoodStructure || bodyA.getUserData() instanceof WoodStructureHorizontal)) {
            bird = (Bird) bodyB.getUserData();
            woodStructure = (Structure) bodyA.getUserData();
            handleBirdWoodCollision(bird, woodStructure);
        }

        // Wood Structure-Pig Collision
        if ((bodyA.getUserData() instanceof WoodStructure || bodyA.getUserData() instanceof WoodStructureHorizontal)
                && bodyB.getUserData() instanceof Pig) {
            woodStructure = (Structure) bodyA.getUserData();
            pig = (Pig) bodyB.getUserData();
            handleWoodStructurePigCollision(woodStructure, pig);
        } else if ((bodyB.getUserData() instanceof WoodStructure || bodyB.getUserData() instanceof WoodStructureHorizontal)
                && bodyA.getUserData() instanceof Pig) {
            woodStructure = (Structure) bodyB.getUserData();
            pig = (Pig) bodyA.getUserData();
            handleWoodStructurePigCollision(woodStructure, pig);
        }

//        // Wood Structure-Wood Structure Collision
//        if ((bodyA.getUserData() instanceof WoodStructure || bodyA.getUserData() instanceof WoodStructureHorizontal)
//            && (bodyB.getUserData() instanceof WoodStructure || bodyB.getUserData() instanceof WoodStructureHorizontal)) {
//            woodStructure = (Structure) bodyA.getUserData();
//            otherWoodStructure = (Structure) bodyB.getUserData();
//            handleWoodStructureCollision(woodStructure, otherWoodStructure);
//        }
    }

    private void handleBirdPigCollision(Bird bird, Pig pig) {
        Vector2 birdVelocity = bird.getBody().getLinearVelocity();
        float collisionSpeed = birdVelocity.len();
        float birdMass = bird.getBody().getMass();

        System.out.println("Bird-Pig Collision Detected:");
        System.out.println("Bird Velocity: " + collisionSpeed);
        System.out.println("Bird Mass: " + birdMass);

        // Calculate damage based on momentum
        float damage = Math.max(10f, collisionSpeed * birdMass * 10f);
        System.out.println("Calculated Damage to Pig: " + damage);

        pig.takeDamage(damage);
        System.out.println("Pig Hit! Damage: " + damage + ", Remaining Health: " + pig.getHealth());
    }

    private void handleBirdWoodCollision(Bird bird, Structure woodStructure) {
        Vector2 birdVelocity = bird.getBody().getLinearVelocity();
        float collisionSpeed = birdVelocity.len();
        float birdMass = bird.getBody().getMass();

        System.out.println("Bird-Wood Structure Collision Detected:");
        System.out.println("Bird Velocity: " + collisionSpeed);
        System.out.println("Bird Mass: " + birdMass);

        // Calculate damage based on momentum
        float damage = Math.max(10f, collisionSpeed * birdMass * 6f);
        System.out.println("Calculated Damage to Wood Structure: " + damage);

        // Apply damage to the wood structure
        if (woodStructure instanceof WoodStructure) {
            ((WoodStructure) woodStructure).takeDamage(damage);
        } else if (woodStructure instanceof WoodStructureHorizontal) {
            ((WoodStructureHorizontal) woodStructure).takeDamage(damage);
        }

        if (woodStructure.getCurrentHealth() <= 0) {
            System.out.println("Wood Structure Marked for Destruction!");
        }
    }

    private void handleWoodStructurePigCollision(Structure woodStructure, Pig pig) {
        Vector2 woodVelocity = woodStructure instanceof WoodStructure ?
                ((WoodStructure) woodStructure).getBody().getLinearVelocity() :
                ((WoodStructureHorizontal) woodStructure).getBody().getLinearVelocity();

        float collisionSpeed = woodVelocity.len();
        float woodStructureMass = woodStructure instanceof WoodStructure ?
                ((WoodStructure) woodStructure).getBody().getMass() :
                ((WoodStructureHorizontal) woodStructure).getBody().getMass();

        System.out.println("Wood Structure-Pig Collision Detected:");
        System.out.println("Wood Structure Velocity: " + collisionSpeed);
        System.out.println("Wood Structure Mass: " + woodStructureMass);

        // Calculate damage based on momentum
        float damage = Math.max(5f, collisionSpeed * woodStructureMass * 2f);
        System.out.println("Calculated Damage to Pig: " + damage);

        pig.takeDamage(damage);
        System.out.println("Pig Hit by Wood Structure! Damage: " + damage + ", Remaining Health: " + pig.getHealth());
    }

    private void updateWorld() {
        List<Structure> structuresToRemove = new ArrayList<>();
        List<Pig> pigsToRemove = new ArrayList<>();

        // Remove destroyed wood structures
        for (Structure structure : allWoodStructures) {
            if (structure.isMarkedForRemoval()) {
                world.destroyBody(
                    structure instanceof WoodStructure ?
                        ((WoodStructure) structure).getBody() :
                        ((WoodStructureHorizontal) structure).getBody()
                );
                score += 100;
                structure.dispose();
                structuresToRemove.add(structure);
            }
        }

        // Remove destroyed pigs
        for (Pig pig : pigs) {
            if (!pig.isAlive()) {
                world.destroyBody(pig.getPigBody());
                score += 1000;
                pig.dispose();
                pigsToRemove.add(pig);
            }
        }

        // Remove structures and pigs
        allWoodStructures.removeAll(structuresToRemove);
        pigs.removeAll(pigsToRemove);

        // Step the physics world
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void endContact(Contact contact) {
        // Handle collision ending
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Handle pre-solve
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Handle post-solve
    }
}
