package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class WoodStructureHorizontal {
    private float maxHealth;
    private float currentHealth;
    private Texture woodTexture;
    private Texture damagedTexture;
    private Texture brokenTexture;
    private Body woodBody;
    private float width;
    private float height;

    public WoodStructureHorizontal(World world, float x, float y, float width, float height, boolean isDynamic) {
        woodTexture = new Texture(Gdx.files.internal("woodhor.png"));
        damagedTexture = new Texture(Gdx.files.internal("woodhor_damaged.png"));


        this.width = width;
        this.height = height;
        this.maxHealth = 15f;  // Base health
        this.currentHealth = maxHealth;


        BodyDef woodBodyDef = new BodyDef();
        woodBodyDef.type = isDynamic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
        woodBodyDef.position.set(x, y);
        woodBodyDef.linearDamping = 0.4f;
        // Add angular damping to prevent excessive rotation
        woodBodyDef.angularDamping = 0.3f;


        // Create the shape for the wood structure
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width /2, height / 2);

        // Define the fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 12f;
        fixtureDef.shape = shape;
        fixtureDef.friction = 50f;
        fixtureDef.restitution = 0f;

        // Create the body and attach the fixture
        woodBody = world.createBody(woodBodyDef);
        woodBody.createFixture(fixtureDef);

        // Dispose of the shape after use
        shape.dispose();
    }

    public float getWidth() {
        return this.width;
    }
    public Body getBody() {
        return woodBody;
    }
    public float getHeight() {
        return this.height;
    }

    public Vector2 getPosition() {
        return woodBody.getPosition();
    }
    public void takeDamage(float damage) {
        currentHealth -= damage;

        // Update texture based on health
        if (currentHealth <= maxHealth * 0.6f) {
            woodTexture = damagedTexture;
        }
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public Texture getTexture() {
        return woodTexture;
    }

    public void dispose() {
        woodTexture.dispose();
        damagedTexture.dispose();
        brokenTexture.dispose();
    }

}
