package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Pig {
    // Protected fields to allow access in child classes
    protected Texture pigTexture;
    protected Body pigBody;
    protected CircleShape pigShape;
    protected float health;
    protected float maxHealth;  // Added to track maximum health
    protected boolean isAlive;

    // Constructor with default implementation
    public Pig(World world, float x, float y, String texturePath, float radius) {
        // Initialize texture
        pigTexture = new Texture(Gdx.files.internal(texturePath));

        // Create body definition
        BodyDef pigBodyDef = new BodyDef();
        pigBodyDef.type = BodyDef.BodyType.DynamicBody;
        pigBodyDef.position.set(x, y);

        // Create shape
        pigShape = new CircleShape();
        pigShape.setRadius(radius);

        // Create fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 3f;
        fixtureDef.shape = pigShape;
        fixtureDef.friction = 100f;
        fixtureDef.restitution = 0f;

        // Create body and fixture
        pigBody = world.createBody(pigBodyDef);
        pigBody.createFixture(fixtureDef);
        pigBody.setUserData(this);  // Set the user data to this pig instance

        pigShape.dispose();

        // Initialize default health and alive status
        this.maxHealth = 60f;
        this.health = this.maxHealth;
        this.isAlive = true;
    }

    public Texture getPigTexture() {
        return pigTexture;
    }

    public void setPigTexture(Texture pigTexture) {
        this.pigTexture = pigTexture;
    }

    public Body getPigBody() {
        return pigBody;
    }

    public Body getBody() {  // Added alias method for consistency
        return pigBody;
    }

    public void setPigBody(Body pigBody) {
        this.pigBody = pigBody;
    }

    public Vector2 getPosition() {
        return pigBody.getPosition();
    }

    public float getHealth() {
        return health;
    }

    public float getCurrentHealth() {  // Added method for current health
        return health;
    }

    public float getMaxHealth() {  // Added method for maximum health
        return maxHealth;
    }

    public void setHealth(float health) {
        this.health = Math.max(0, Math.min(health, maxHealth));  // Clamp health between 0 and maxHealth
        this.isAlive = this.health > 0;
    }

    public Texture getTexture() {
        return pigTexture;
    }

    public void takeDamage(float damage) {
        if (!isAlive) return;  // Don't process damage if already dead

        float newHealth = Math.max(0, this.health - damage);
        setHealth(newHealth);

        // Debug output for damage taken
        System.out.println("Pig took " + damage + " damage. Health: " + getCurrentHealth() + "/" + getMaxHealth());
    }

    public boolean isAlive() {
        return this.isAlive;
    }
    public void setTexture(Texture newTexture) {
        // Dispose of the old texture to prevent memory leaks
        if (this.pigTexture != null) {
            this.pigTexture.dispose();
        }
        this.pigTexture = newTexture;
    }
    // Cleanup method
    public void dispose() {
        if (pigTexture != null) {
            pigTexture.dispose();
        }
    }
}
