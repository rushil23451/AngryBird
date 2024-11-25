//package com.angrybirds;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.FixtureDef;
//import com.badlogic.gdx.physics.box2d.PolygonShape;
//import com.badlogic.gdx.physics.box2d.World;
//
//public abstract class Structure extends GameObject {
//    protected float width;
//    protected float height;
//    protected Texture structTexture;
//    protected int health; // Health of the structure
//    private boolean isDestroyed = false; // Flag to track if the structure is destroyed
//    public Sprite objectSprite;
//    // Constructor for Structure class
//    public Structure(World world, float x, float y, float width, float height, String texturePath, boolean isDynamic, int initialHealth) {
//        super(world, x, y, texturePath, initialHealth); // Call the GameObject constructor
//
//        this.width = width;
//        this.height = height;
//        this.health = initialHealth;
//        this.objectSprite = new Sprite();
//        this.structTexture = new Texture("wood.png");
//        // Define the body type
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = isDynamic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
//        bodyDef.position.set(x, y);
//
//        // Create the shape for the structure
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(width / 2, height / 2); // Adjusted to match expected size
//
//        // Create the fixture
//        FixtureDef fixtureDef = new FixtureDef();
//        configureFixture(fixtureDef); // Configurable by subclasses
//        fixtureDef.shape = shape;
//
//        // Create the body and attach the fixture
//        this.body = world.createBody(bodyDef);
//        this.body.createFixture(fixtureDef);
//
//        // Dispose of the shape after use
//        shape.dispose();
//    }
//
//    // Abstract method for child classes to configure physics properties
//    protected abstract void configureFixture(FixtureDef fixtureDef);
//
//    // Reduces health and destroys the structure if health reaches zero
//    public void takeDamage(int damage) {
//        health -= damage;
//        if (health <= 0 && !isDestroyed) {
//            destroy();
//        }
//    }
//
//
//    // Accessors for position, width, height, and texture
//    public Vector2 getPosition() {
//        return body.getPosition();
//    }
//
//    public float getWidth() {
//        return this.width;
//    }
//
//    public float getHeight() {
//        return this.height;
//    }
//
//    public Texture getTexture() {
//        return this.texture;
//    }
//
//    // Frees up resources
//    public void dispose() {
//        texture.dispose();
//    }
//
//    // Retrieves the current health
//    public int getHealth() {
//        return health;
//    }
//
//    // Update method to manage game logic each frame, including deltaTime
//    public void update(float deltaTime) {
//        if (health <= 0 && !isDestroyed) {
//            isDestroyed = true;
//            // Optionally change texture to represent destroyed state
//            texture = new Texture(Gdx.files.internal("structure_destroyed.png"));
//            destroy(); // Destroy the structure if it's dead
//        }
//
//        // Add other update logic here (e.g., physics changes, animations, etc.)
//        // Example: If the structure has physics or animations, you could use deltaTime
//        // to scale them properly.
//
//        // Example of using deltaTime to scale any time-based behavior, such as physics updates:
//        // body.applyForceToCenter(new Vector2(0, -9.8f * deltaTime), true);  // Gravity or force adjustments
//    }
//
//    // Get the Body object of the structure
//    public Body getStructureBody() {
//        return body; // Return the body of the structure
//    }
//}

