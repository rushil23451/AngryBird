package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class WoodStructure {
    private Texture woodTexture;
    private Body woodBody;
    private PolygonShape shape;
    private float width;
    private float height;

    public WoodStructure(World world, float x, float y, float width, float height) {
        woodTexture = new Texture(Gdx.files.internal("wood.png"));
        this.width=width;
        this.height=height;

        // Define the body for the wood structure
        BodyDef woodBodyDef = new BodyDef();
        woodBodyDef.type = BodyType.StaticBody;  // Static body since the wood is not movable
        woodBodyDef.position.set(x, y);          // Position the wood piece at (x, y)

        // Create the shape and fixture for the wood
        shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);   // Define the width and height of the wood structure

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0.5f;               // Wood can have a lower density
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;           // Less bouncy for wood

        // Create the wood's physics body in the world
        woodBody = world.createBody(woodBodyDef);
        woodBody.createFixture(fixtureDef);
        shape.dispose();  // Dispose of the shape after using it
    }
    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public Vector2 getPosition() {
        return woodBody.getPosition();
    }

    public Texture getTexture() {
        return woodTexture;
    }

    public void dispose() {
        woodTexture.dispose();
    }
}
