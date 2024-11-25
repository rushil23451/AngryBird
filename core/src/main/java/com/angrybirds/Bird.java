package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Bird {
    protected Texture birdTexture;
    protected Body birdBody;
    protected boolean isDragged = false; // To track if the bird is being dragged
    protected float radius; // Bird's radius
    protected Vector2 slingshotPosition; // Initial slingshot position

    // Constructor to create the bird in the world
    public Bird(World world, float x, float y, float radius, String texturePath) {
        if (world == null) {
            System.out.println("Error: World is null");
            return;
        }

        this.radius = radius;
        this.slingshotPosition = new Vector2(x, y);
        birdTexture = new Texture(Gdx.files.internal(texturePath));

        // Define the bird's body
        BodyDef birdBodyDef = new BodyDef();
        birdBodyDef.type = BodyDef.BodyType.DynamicBody;
        birdBodyDef.position.set(x, y);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 2.5f;
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.4f;

        birdBody = world.createBody(birdBodyDef);
        Fixture birdFixture = birdBody.createFixture(fixtureDef);
        birdFixture.setUserData(this);  // Set userData on the fixture
        birdBody.setUserData(this);
        shape.dispose();
    }

    public Body getBody() {
        return birdBody;
    }

    public boolean contains(float touchX, float touchY) {
        if (birdBody == null) {
            return false;
        }
        Vector2 birdPosition = birdBody.getPosition();
        return Vector2.dst(birdPosition.x, birdPosition.y, touchX, touchY) <= radius;
    }

    public void launch(Vector2 velocity) {
        if (birdBody != null) {
            birdBody.setLinearVelocity(velocity);
        }
    }

    public void setPosition(float touchX, float touchY) {
        if (birdBody != null) {
            birdBody.setTransform(touchX, touchY, birdBody.getAngle());
        }
    }

    public void applyLinearImpulse(Vector2 launchVector) {
        if (birdBody != null) {
            birdBody.setAwake(true);
            birdBody.applyLinearImpulse(launchVector, birdBody.getWorldCenter(), true);
        }
    }

    public boolean isInside(Vector2 point) {
        return point.dst(getPosition()) <= radius;
    }

    public void setDragged(boolean isDragged) {
        this.isDragged = isDragged;
    }

    public Vector2 getPosition() {
        return birdBody != null ? birdBody.getPosition() : new Vector2(0, 0);
    }

    public Texture getTexture() {
        return birdTexture;
    }

    public void dispose() {
        if (birdTexture != null) {
            birdTexture.dispose();
        }
    }

    public float getRadius() {
        return radius;
    }

    public void resetToSlingshotPosition() {
        if (birdBody != null) {
            birdBody.setTransform(slingshotPosition, 0);
            birdBody.setLinearVelocity(0, 0);
            birdBody.setAngularVelocity(0);
        }
    }
}
