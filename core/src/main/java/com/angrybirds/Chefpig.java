package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;

public class Chefpig extends Pig {
    // Constructor that calls the parent constructor
    public Chefpig(World world, float x, float y,float radius) {
        super(world, x, y, "pig1.png",radius);

        // Modify attributes specific to SmallPig
        this.health = 50f;  // Small pigs have less health
    }




    @Override
    public void takeDamage(float damage) {
        // Override takeDamage to add a small pig-specific behavior
        super.takeDamage(damage);



    }
}
