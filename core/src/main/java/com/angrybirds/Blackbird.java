package com.angrybirds;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class Blackbird extends Bird {
    private static final float EXPLOSION_RADIUS = 10f; // 60 units radius
    private static final float EXPLOSION_FORCE = 5f; // Adjust as needed
    private boolean specialAbilityActivated = false;

    public Blackbird(World world, float x, float y, float radius) {
        super(world, x, y, radius, "Bomb.png");
    }

    public void specialAbility(List<Structure> structures, List<Pig> pigs, OrthographicCamera camera) {
        if (!specialAbilityActivated) {
            // Get current bird position
            Vector2 explosionCenter = getPosition();

            // Apply explosive force to structures
            for (Structure structure : structures) {
                Body body = structure.getBody();
                if (body != null) {
                    applyExplosiveForce(body, explosionCenter);
                }
            }

            // Apply explosive force to pigs
            for (Pig pig : pigs) {
                Body body = pig.getBody();
                if (body != null) {
                    applyExplosiveForce(body, explosionCenter);
                }
            }

            // Attempt to shake camera
            try {
                camera.translate(
                    (float) (Math.random() * 5 - 2.5),
                    (float) (Math.random() * 5 - 2.5)
                );
            } catch (Exception e) {
                // Fallback if specific shake method not available
                System.out.println("Camera shake not supported");
            }

            specialAbilityActivated = true;
        }
    }

    private void applyExplosiveForce(Body body, Vector2 explosionCenter) {
        Vector2 objPosition = body.getPosition();
        float distance = explosionCenter.dst(objPosition);

        // Check if object is within explosion radius
        if (distance <= EXPLOSION_RADIUS) {
            // Calculate direction away from explosion center
            Vector2 forceDirection = objPosition.cpy().sub(explosionCenter).nor();

            // Calculate force based on distance (closer objects get more force)
            float force = EXPLOSION_FORCE * (1 - distance / EXPLOSION_RADIUS);

            // Apply impulse to the object
            body.applyLinearImpulse(
                forceDirection.scl(force),
                body.getWorldCenter(),
                true
            );
        }
    }
    public boolean isSpecialAbilityActivated() {
        return specialAbilityActivated;
    }
}
