package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;

public class YellowBird extends Bird {
    private static final float SPEED_MULTIPLIER = 3f;
    private boolean specialAbilityActivated = false;

    public YellowBird(World world, float x, float y, float radius) {
        super(world, x, y, radius, "bird3.png");
    }

    public void specialAbility() {
        if (!specialAbilityActivated) {
            // Get current velocity
            Vector2 currentVelocity = getBody().getLinearVelocity();

            // Increase velocity by 3x
            Vector2 boostedVelocity = new Vector2(
                currentVelocity.x * SPEED_MULTIPLIER,
                currentVelocity.y * SPEED_MULTIPLIER
            );

            // Set new velocity
            getBody().setLinearVelocity(boostedVelocity);

            specialAbilityActivated = true;
        }
    }

    public boolean isSpecialAbilityActivated() {
        return specialAbilityActivated;
    }
}
