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

public class slingshot {
    private Texture woodTexture;
    private Body woodBody;
    private PolygonShape shape;
    private float width;
    private float height;

    public slingshot(World world, float x, float y, float width, float height) {
        woodTexture = new Texture(Gdx.files.internal("slingshot.png"));
        this.width=width;
        this.height=height;


        BodyDef woodBodyDef = new BodyDef();
        woodBodyDef.type = BodyType.StaticBody;
        woodBodyDef.position.set(x, y);


        shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0.5f;
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;

        woodBody = world.createBody(woodBodyDef);
        woodBody.createFixture(fixtureDef);
        shape.dispose();
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
