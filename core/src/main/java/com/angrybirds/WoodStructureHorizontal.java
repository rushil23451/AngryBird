package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;
public class WoodStructureHorizontal extends Structure {
    public WoodStructureHorizontal(
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
            "woodhor.png",         // Normal texture
            "woodhor_damaged.png", // Damaged texture
            50f                 // Health
        );
        this.density = 10f;
        this.friction = 50f;
        this.restitution = 0f;

        this.linearDamping = 0.1f;  // Less linear damping
        this.angularDamping = 0.2f;  // Less angular damping

        updateBodyProperties();
    }


    public void takeDamage(float damage) {
        currentHealth -= damage;

        if (currentHealth <= maxHealth * 0.6f && currentHealth>0) {
            normalTexture = damagedTexture;
        }
        else if (currentHealth <= 0) {
            this.markedForRemoval = true;
        }
    }


}
