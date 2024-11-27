package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends Bird {
    public RedBird(World world, float x, float y, float radius) {
        super(world, x, y, radius, "bird1.png"); // Pass texture path specific to RedBird
    }
}
