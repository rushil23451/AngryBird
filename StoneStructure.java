package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;
public class StoneStructure extends Structure {
    public StoneStructure(
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
            "stonelongblock.png",         // Normal texture
            "damagedstonblock.png", // Damaged texture
            100f                 // Health
        );
        this.density = 13f;
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
