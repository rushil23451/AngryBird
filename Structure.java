package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Structure {
    // Physical properties
    protected Body structureBody;
    protected float width;
    protected float height;

    // Physics properties
    protected float density;
    protected float friction;
    protected float restitution;
    protected float linearDamping;
    protected float angularDamping;

    // Texture management
    protected Texture normalTexture;
    protected Texture damagedTexture;


    // Health management
    protected float maxHealth;
    protected float currentHealth;

    // State flags
    protected boolean markedForRemoval = false;

    // Simplified constructor without physics details
    public Structure(
        World world,
        float x,
        float y,
        float width,
        float height,
        boolean isDynamic,
        String normalTexturePath,
        String damagedTexturePath,
        float health
    ) {
        // Load textures
        this.normalTexture = new Texture(Gdx.files.internal(normalTexturePath));
        this.damagedTexture = new Texture(Gdx.files.internal(damagedTexturePath));

        // Set dimensions
        this.width = width;
        this.height = height;

        // Set health
        this.maxHealth = health;
        this.currentHealth = maxHealth;

        // Default damping values
        this.linearDamping = 0.1f;
        this.angularDamping = 0.2f;

        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isDynamic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        // Create body
        structureBody = world.createBody(bodyDef);
    }

    // Getters for physics properties
    public float getDensity() { return density; }
    public float getFriction() { return friction; }
    public float getRestitution() { return restitution; }
    public float getLinearDamping() { return linearDamping; }
    public float getAngularDamping() { return angularDamping; }

    // Method to update body properties after setting custom values
    protected void updateBodyProperties() {
        // Set damping
        structureBody.setLinearDamping(this.linearDamping);
        structureBody.setAngularDamping(this.angularDamping);

        // Create shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        // Create fixture definition with configurable properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = this.density;
        fixtureDef.shape = shape;
        fixtureDef.friction = this.friction;
        fixtureDef.restitution = this.restitution;

        // Remove existing fixtures
        while (structureBody.getFixtureList().size > 0) {
            structureBody.destroyFixture(structureBody.getFixtureList().first());
        }
        structureBody.createFixture(fixtureDef);

        // Create new fixture
        Fixture structureFixture = structureBody.createFixture(fixtureDef);
        structureFixture.setUserData(this);
        structureBody.setUserData(this);

        // Dispose of shape
        shape.dispose();
    }
    // Common methods for all structures
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Body getBody() {
        return structureBody;
    }

    public Vector2 getPosition() {
        return structureBody.getPosition();
    }

    // Damage and health management

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }


    public Texture getTexture() {
        if (currentHealth > maxHealth * 0.6) {
            return normalTexture;
        }
        else {
            return damagedTexture;
        }
    }

    public void dispose() {
        normalTexture.dispose();
        damagedTexture.dispose();


    }
    public void takeDamage(float damage) {
        currentHealth -= damage;

        if (currentHealth <= maxHealth * 0.6f && currentHealth>0) {
            normalTexture = damagedTexture;
        }
        else if (currentHealth <= 0) {
            this.markedForRemoval = true;
        }
    }
}

