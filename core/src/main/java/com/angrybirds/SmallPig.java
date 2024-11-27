package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.Texture;

public class SmallPig extends Pig {
    private static final float TEXTURE_CHANGE_THRESHOLD = 40f;
    private final Texture damagedTexture;
    private final Texture normalTexture;
    private boolean isDamagedTextureActive = false;
    private final float maxHealth = 75f;  // Define maxHealth

    // Constructor that calls the parent constructor
    public SmallPig(World world, float x, float y, float radius) {
        super(world, x, y, "minionpig.png", radius);

        // Load textures
        this.normalTexture = new Texture("minionpig.png");
        this.damagedTexture = new Texture("damagedminionpig.png");

        // Modify attributes specific to SmallPig
        this.health = maxHealth;
    }

    @Override
    public void takeDamage(float damage) {
        // Override takeDamage to add a small pig-specific behavior
        super.takeDamage(damage);
        updateTexture();
    }

    private void updateTexture() {
        // Use consistent logic for texture changes
        if (health <= maxHealth * 0.6) {  // This is equivalent to 45 health (60% of 75)
            if (!isDamagedTextureActive) {
                super.setTexture(damagedTexture);  // Call parent's setTexture method
                isDamagedTextureActive = true;
            }
        } else {
            if (isDamagedTextureActive) {
                super.setTexture(normalTexture);
                isDamagedTextureActive = false;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        normalTexture.dispose();
        damagedTexture.dispose();
    }
}
