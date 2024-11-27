package com.angrybirds;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
public class IceSquareStructure extends Structure {
    public IceSquareStructure(
        World world,
        float x,
        float y,
        float width,
        float height,
        boolean isDynamic
    ) {
        super(
            world,
            x,
            y,
            width,
            height,
            isDynamic,
            "icesquareblock.png",         // Normal texture
            "damagedicesquareblock.png", // Damaged texture
            50f                 // Health
        );
        this.density = 4f;
        this.friction = 50f;
        this.restitution = 0f;

        this.linearDamping = 0.1f;  // Less linear damping
        this.angularDamping = 0.2f;  // Less angular damping

        updateBodyProperties();
    }
    @Override
    protected void updateBodyProperties() {
        // Set damping
        structureBody.setLinearDamping(this.linearDamping);
        structureBody.setAngularDamping(this.angularDamping);

        // Create shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width , height);

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

    public void takeDamage(float damage) {
        currentHealth -= damage;

        if (currentHealth <= maxHealth * 0.5f && currentHealth>0) {
            normalTexture = damagedTexture;
        }
        else if (currentHealth <= 0) {
            this.markedForRemoval = true;
        }
    }


}
