package com.angrybirds;

import com.badlogic.gdx.Game;

public class Main extends Game {
    private int saveLevel = -1; // Variable to store the saved level

    @Override
    public void create() {
        loadSavedLevel();
    }

    public void saveLevel(int level) {
        this.saveLevel = level;
    }

    private void loadSavedLevel() {
        if (saveLevel != -1) {
            // Load the saved level based on saveLevel
            switch (saveLevel) {
                case 1:
                    setScreen(new Level_1(this)); // Load Level 1
                    break;
                // Add more cases if you have more levels
                default:
                    setScreen(new FirstScreen(this)); // Fallback to main menu or any other screen
            }
        } else {
            setScreen(new FirstScreen(this)); // No saved level, go to the main menu
        }
    }
}
