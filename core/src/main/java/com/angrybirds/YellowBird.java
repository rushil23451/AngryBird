package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;

public class YellowBird extends Bird {
    public YellowBird(World world, float x, float y, float radius) {
        super(world, x, y, radius, "bird3.png"); // Pass texture path specific to YellowBird
    }
}
