package com.angrybirds;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class MissilePig {
    private Texture birdTexture;
    private Body birdBody;
    private CircleShape shape;

    public MissilePig(World world, float x, float y) {
        birdTexture = new Texture(Gdx.files.internal("pig1.png"));

        // Define the body
        BodyDef birdBodyDef = new BodyDef();
        birdBodyDef.type = BodyType.StaticBody;
        birdBodyDef.position.set(x, y);

        // Create the shape and fixture
        shape = new CircleShape();
        shape.setRadius(5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 2.5f;
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.25f;
        fixtureDef.restitution = 0.75f;

        // Create the bird's physics body in the world
        birdBody = world.createBody(birdBodyDef);
        birdBody.createFixture(fixtureDef);
        shape.dispose();  // Dispose the shape after using it
    }

    public Vector2 getPosition() {
        return birdBody.getPosition();
    }

    public Texture getTexture() {
        return birdTexture;
    }

    public void dispose() {
        birdTexture.dispose();
    }
}
