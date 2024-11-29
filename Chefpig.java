package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.Texture;

public class Chefpig extends Pig {
    private static final float TEXTURE_CHANGE_THRESHOLD = 40f;
    private final Texture damagedTexture;
    private final Texture normalTexture;
    private boolean isDamagedTextureActive = false;
    private final float maxHealth = 75f;

    public Chefpig(World world, float x, float y, float radius) {
        super(world, x, y, "helmetpig.png", radius);

        this.normalTexture = new Texture("helmetpig.png");
        this.damagedTexture = new Texture("helmetpigdamaged.png");

        this.health = maxHealth;
    }

    @Override
    public void takeDamage(float damage) {

        super.takeDamage(damage);
        updateTexture();
    }

    private void updateTexture() {

        if (health <= maxHealth * 0.6) {
            if (!isDamagedTextureActive) {
                super.setTexture(damagedTexture);
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
