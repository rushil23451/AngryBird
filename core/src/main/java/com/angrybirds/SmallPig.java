package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;

public class SmallPig extends Pig {
    // Constructor that calls the parent constructor
    public SmallPig(World world, float x, float y,float radius) {
        super(world, x, y, "pig3.png",radius);

        // Modify attributes specific to SmallPig
        this.health = 40f;
        // Small pigs have less health
    }




    @Override
    public void takeDamage(float damage) {
        // Override takeDamage to add a small pig-specific behavior
        super.takeDamage(damage);

        // If damaged, try to use special ability
    }
}
