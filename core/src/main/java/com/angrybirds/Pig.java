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
    protected boolean isAlive;

    // Constructor with default implementation
    public Pig(World world, float x, float y, String texturePath,float radius) {
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
        fixtureDef.friction = 5f;
        fixtureDef.restitution = 0.1f;

        // Create body and fixture
        pigBody = world.createBody(pigBodyDef);
        pigBody.createFixture(fixtureDef);

        pigShape.dispose();

        // Initialize default health and alive status
        this.health = 60f;
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

    public void setPigBody(Body pigBody) {
        this.pigBody = pigBody;
    }

    // Common methods that can be overridden by child classes
    public Vector2 getPosition() {
        return pigBody.getPosition();
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public Texture getTexture() {
        return pigTexture;
    }

    public void takeDamage(float damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.isAlive = false;
        }
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    // Abstract method to be implemented by child classes


    // Cleanup method
    public void dispose() {
        if (pigTexture != null) {
            pigTexture.dispose();
        }
    }
}
