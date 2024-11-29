package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends Bird {
    private boolean specialAbilityActivated = false;

    public RedBird(World world, float x, float y, float radius) {
        super(world, x, y, radius, "bird1.png");
    }

    public Bird[] specialAbility() {
        // Placeholder method that does nothing
        specialAbilityActivated = true;
        return new Bird[0];
    }
}
