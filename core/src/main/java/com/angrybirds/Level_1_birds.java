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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.List;
import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;

public class Level_1_birds implements Screen, ContactListener {
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private static final float VIRTUAL_WIDTH = 800;
    private static final float VIRTUAL_HEIGHT = 448;
    private Main game;
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

    private boolean isDragging = false;
    private Vector2 dragStartPosition = new Vector2();
    private Vector2 dragEndPosition = new Vector2();
    private Vector2 dragDirection = new Vector2();

    private static final short BIRD_CATEGORY_BITS = 0x0001;
    private static final short WOOD_CATEGORY_BITS = 0x0002;
    private static final short GROUND_CATEGORY_BITS = 0x0004;

    private static final float MAX_DRAG_DISTANCE = 3f;
    private static final float MIN_DRAG_DISTANCE = 0.5f;
    private static final float LAUNCH_MULTIPLIER = 7f;
    private static final float GRAVITY = -9.8f;
    private static final float TRAJECTORY_TIME = 2f;
    private static final float TRAJECTORY_STEP = 0.1f;

    private Array<Vector2> trajectoryPoints = new Array<>();
    private List<Object> allWoodStructures=new ArrayList<>();;
    private List<Body> bodiesToRemove = new ArrayList<>();
    private List<Pig> pigs = new ArrayList<>();


    private boolean birdLaunched = false;
    private float respawnTimer = 0;
    private static final float RESPAWN_DELAY = 2f;
    private static final float OFF_SCREEN_X = VIRTUAL_WIDTH + 100;
    private static final float OFF_SCREEN_Y = -100;

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
        backgroundTexture = new Texture(Gdx.files.internal("newleveel.png"));
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(this);
        debugRenderer = new Box2DDebugRenderer();

        float slingX = 140f / PPM;
        float slingY = 190f / PPM;
        float slingWidth = 55f / PPM;
        float slingHeight = 120f / PPM;
        slingshot = new slingshot(world, slingX, slingY, slingWidth, slingHeight);

        // Initialize bird queue and spawn first bird
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
        Blackbird blackBird = new Blackbird(world, startX + (1/2* BIRD_SPACING / PPM), startY, birdRadius+5f/PPM);
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

        float wood2Width = 100f / PPM;
        float wood2Height = 17.5f / PPM;


        float pigRadius = 15f / PPM;

        float baseY = groundLevel + (woodHeight);  // Center point of the wood pieces
        wood1 = new WoodStructure(world, 590f / PPM, baseY, woodWidth, woodHeight, true);
        wood3 = new WoodStructure(world, 650f / PPM, baseY, woodWidth, woodHeight, true);

        float horizontalY = baseY + (woodHeight) + (wood2Height);
        wood2 = new WoodStructureHorizontal(world, 620f / PPM, horizontalY, wood2Width, wood2Height, true);

        float upperY = horizontalY + (woodHeight);
        wood4 = new WoodStructure(world, 590f / PPM, upperY, woodWidth, woodHeight, true);
        wood5 = new WoodStructure(world, 650f / PPM, upperY, woodWidth, woodHeight, true);
        SmallPig smallPig1 = new SmallPig(world, 612.5f / PPM, baseY,pigRadius);
        SmallPig smallPig2 = new SmallPig(world, 612.5f / PPM, upperY,pigRadius);
        smallPig1.getPigBody().setUserData(smallPig1);
        smallPig2.getPigBody().setUserData(smallPig2);
        pigs.add(smallPig1);
        pigs.add(smallPig2);
        allWoodStructures.add(wood1);
        allWoodStructures.add(wood3);

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
            // We need to create the correct type of bird based on the class of nextBird
            if (nextBird instanceof RedBird) {
                activeBird = new RedBird(world, slingX, (slingY + slingHeight)-20f/PPM, birdRadius);
            } else if (nextBird instanceof YellowBird) {
                activeBird = new YellowBird(world, slingX,(slingY + slingHeight)-20f/PPM, birdRadius);
            } else if (nextBird instanceof Blackbird) {
                activeBird = new Blackbird(world, slingX, (slingY + slingHeight)-20f/PPM, birdRadius+5f/PPM);
            }

            birdLaunched = false;
            isDragging = false;

            // Shift remaining birds forward and update their bodies
            float startX = 100f / PPM;
            float startY = 160f / PPM;
            for (int i = 0; i < birdQueue.size; i++) {
                Bird bird = birdQueue.get(i);
                // Destroy old body
                if (bird.getBody() != null) {
                    world.destroyBody(bird.getBody());
                }
                // Create new body at updated position
                if (bird instanceof RedBird) {
                    bird = new RedBird(world, startX - (i * BIRD_SPACING / PPM), startY, birdRadius);
                } else if (bird instanceof YellowBird) {
                    bird = new YellowBird(world, startX - (i * BIRD_SPACING / PPM), startY, birdRadius);
                } else if (bird instanceof Blackbird) {
                    bird = new Blackbird(world, startX - (i * BIRD_SPACING / PPM), startY, birdRadius+5/PPM);
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

        camera.update();

        Matrix4 debugMatrix = camera.combined.cpy();
        debugMatrix.scale(PPM, PPM, 1f);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        spriteBatch.end();

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
            String dragText = String.format("Drag: %.1fm", dragDistance);
            font.draw(spriteBatch, dragText, 10, VIRTUAL_HEIGHT - 10);
        }

        spriteBatch.end();
        checkGameState();

        debugRenderer.render(world, debugMatrix);

        // Draw UI elements
        drawUIElements();

        handleInput();
        drawTrajectory();

        stage.act(delta);
        stage.draw();
    }
    private void checkGameState() {
        // Check if all pigs are dead
        boolean allPigsDead = pigs.stream().noneMatch(Pig::isAlive);

        // Check if all birds have been used
        boolean allBirdsUsed = birdQueue.isEmpty() && activeBird == null;

        if (allPigsDead) {
            game.setScreen(new WinScreen(game));
        } else if (allBirdsUsed && !allPigsDead) {
            game.setScreen(new LoseScreen(game));
        }
    }


    private void drawWoodStructures() {
        for (Object woodObj : allWoodStructures) {

            WoodStructure wood = (WoodStructure) woodObj;
            if (!wood.isMarkedForRemoval()) {
                drawRotatedWoodStructure(spriteBatch, wood);
            }

        }

        Vector2 wood2Pos = wood2.getPosition();
        float wood2Rotation = wood2.getBody().getAngle() * MathUtils.radiansToDegrees;
        spriteBatch.draw(
            wood2.getTexture(),
            wood2Pos.x * PPM - (wood2.getWidth() * PPM / 2),
            wood2Pos.y * PPM - (wood2.getHeight() * PPM / 2),
            wood2.getWidth() * PPM / 2,
            wood2.getHeight() * PPM / 2,
            wood2.getWidth() * PPM,
            wood2.getHeight() * PPM,
            1, 1,
            wood2Rotation,
            0, 0,
            wood2.getTexture().getWidth(),
            wood2.getTexture().getHeight(),
            false, false
        );
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
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(buttonX, buttonY, buttonRadius);
        shapeRenderer.end();

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
        } else if (isDragging) {
            isDragging = false;

            if (dragDirection.len() >= MIN_DRAG_DISTANCE * PPM) {
                Vector2 launchVelocity = dragDirection.scl(LAUNCH_MULTIPLIER / 200);
                activeBird.launch(launchVelocity);
                birdLaunched = true;
            }

            trajectoryPoints.clear();
        }
    }

    private void updateTrajectory() {
        trajectoryPoints.clear();

        Vector2 position = new Vector2(activeBird.getPosition().scl(PPM));
        Vector2 velocity = new Vector2(dragStartPosition).sub(dragEndPosition).scl(LAUNCH_MULTIPLIER / 200);
        Vector2 tempVelocity = new Vector2(velocity);

        for (float t = 0; t < TRAJECTORY_TIME; t += TRAJECTORY_STEP) {
            trajectoryPoints.add(new Vector2(position));
            position.x += tempVelocity.x * TRAJECTORY_STEP;
            position.y += tempVelocity.y * TRAJECTORY_STEP;
            tempVelocity.y += GRAVITY * TRAJECTORY_STEP;
        }
    }

    private void drawTrajectory() {
        if (trajectoryPoints.size < 2) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        for (int i = 0; i < trajectoryPoints.size - 1; i++) {
            Vector2 point1 = trajectoryPoints.get(i);
            Vector2 point2 = trajectoryPoints.get(i + 1);

            shapeRenderer.line(
                point1.x, point1.y,
                point2.x, point2.y
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


            // Check if the collision involves a bird and a wood structure
            boolean isBirdPigCollision = false;
            boolean isBirdWoodCollision = false;
            Bird bird = null;
            WoodStructure woodStructure = null;
            Pig pig = null;

            if (bodyA.getUserData() instanceof Bird && bodyB.getUserData() instanceof Pig) {
                bird = (Bird) bodyA.getUserData();
                pig = (Pig) bodyB.getUserData();
                isBirdPigCollision = true;
            } else if (bodyB.getUserData() instanceof Bird && bodyA.getUserData() instanceof Pig) {
                bird = (Bird) bodyB.getUserData();
                pig = (Pig) bodyA.getUserData();
                isBirdPigCollision = true;
            }


        if (bodyA.getUserData() instanceof Bird && bodyB.getUserData() instanceof WoodStructure) {
                bird = (Bird) bodyA.getUserData();
                woodStructure = (WoodStructure) bodyB.getUserData();
                isBirdWoodCollision = true;
            } else if (bodyB.getUserData() instanceof Bird && bodyA.getUserData() instanceof WoodStructure) {
                bird = (Bird) bodyB.getUserData();
                woodStructure = (WoodStructure) bodyA.getUserData();
                isBirdWoodCollision = true;
            }
            if (isBirdPigCollision && bird != null && pig != null) {
                Vector2 birdVelocity = bird.getBody().getLinearVelocity();
                float collisionSpeed = birdVelocity.len();
                float birdMass = bird.getBody().getMass();
                System.out.println("Collision Detected:");
                System.out.println("Bird Velocity: " + collisionSpeed);
                System.out.println("Bird Mass: " + birdMass);

                // Calculate damage based on momentum
                float damage = Math.max(10f, collisionSpeed * birdMass * 10f);
                System.out.println("Calculated Damage: " + damage);
                pig.takeDamage(damage);
                System.out.println("Pig Hit! Damage: " + damage + ", Remaining Health: " + pig.getHealth());
            }

            // If it's a bird-wood collision, calculate and apply damage
            if (isBirdWoodCollision && bird != null && woodStructure != null) {
                // Calculate collision velocity and damage
                Vector2 birdVelocity = bird.getBody().getLinearVelocity();
                float collisionSpeed = birdVelocity.len();
                float birdMass = bird.getBody().getMass();
                System.out.println("Collision Detected:");
                System.out.println("Bird Velocity: " + collisionSpeed);
                System.out.println("Bird Mass: " + birdMass);

                // Calculate damage based on momentum (velocity * mass)
                // Adjust these multipliers based on game balance
                float damage = Math.max(10f, collisionSpeed * birdMass * 10f);
                System.out.println("Calculated Damage: " + damage);


                // Apply damage to the wood structure
                woodStructure.takeDamage(damage);

                // Optional: Add a visual or sound effect for collision
                // You might want to add methods in WoodStructure class for this
                if (woodStructure.getCurrentHealth() <= 0) {
                    System.out.println("Wood Structure Marked for Destruction!");
                }
            }
        }
//    private void destroyWoodStructure(WoodStructure woodStructure) {
//        // Remove from physics world
//        if (woodStructure.getBody() != null) {
//            world.destroyBody(woodStructure.getBody());
//        }
//
//        // Remove from game objects list if you're maintaining one
//        if (allWoodStructures != null) {
//            allWoodStructures.remove(woodStructure);
//        }
//
//
//    }

    private void updateWorld() {
        List<Object> structuresToRemove = new ArrayList<>();
        List<Pig> removepig=new ArrayList();

        for (Object woodObj : allWoodStructures) {

                WoodStructure structure = (WoodStructure) woodObj;
                if (structure.isMarkedForRemoval()) {
                    world.destroyBody(structure.getBody());
                    structure.dispose();
                    structuresToRemove.add(structure);
                }
            }

        allWoodStructures.removeAll(structuresToRemove);
        for(Pig pig:removepig){
            Pig rem_pig = (Pig) pig;
            if (!rem_pig.isAlive()) {
                world.destroyBody(rem_pig.getPigBody());
                rem_pig.dispose();
                removepig.add(rem_pig);
            }
        }
        pigs.removeAll(removepig);




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
