package com.angrybirds;

import com.badlogic.gdx.physics.box2d.World;

public class Blackbird extends Bird {
    public Blackbird(World world, float x, float y, float radius) {
        super(world, x, y, radius, "Bomb.png"); // Pass texture path specific to YellowBird
    }
}
