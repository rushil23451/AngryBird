package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class WoodStructure {
    private float maxHealth;
    private float currentHealth;
    private Texture woodTexture;
    private Texture damagedTexture;
    private Texture brokenTexture;
    private Body woodBody;
    private float width;
    private float height;
    private boolean markedForRemoval=false;
    private boolean isDestroyed=false;

    public WoodStructure(World world, float x, float y, float width, float height, boolean isDynamic) {
        woodTexture = new Texture(Gdx.files.internal("wood.png"));
        damagedTexture = new Texture(Gdx.files.internal("wood_damaged.png"));


        this.width = width;
        this.height = height;
        this.maxHealth = 50f;  // Base health
        this.currentHealth = maxHealth;


        BodyDef woodBodyDef = new BodyDef();
        woodBodyDef.type = isDynamic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
        woodBodyDef.position.set(x, y);
        woodBodyDef.linearDamping = 0.1f;
        woodBodyDef.angularDamping = 0.2f;


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 10f;
        fixtureDef.shape = shape;
        fixtureDef.friction = 50f;
        fixtureDef.restitution = 0f;

        woodBody = world.createBody(woodBodyDef);
        woodBody.createFixture(fixtureDef);

        Fixture woodFixture = woodBody.createFixture(fixtureDef);
        woodFixture.setUserData(this);
        woodBody.setUserData(this);

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

        if (currentHealth <= maxHealth * 0.6f && currentHealth>0) {
            woodTexture = damagedTexture;
        }
        else if (currentHealth <= 0) {
            this.markedForRemoval = true;

        }
    }



    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public Texture getTexture() {
        if (currentHealth > maxHealth * 0.6) {
            return woodTexture;
        }
        else {
            return damagedTexture;
        }
    }

    public void dispose(){
        woodTexture.dispose();
        damagedTexture.dispose();


    }

}
