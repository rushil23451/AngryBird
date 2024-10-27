package com.angrybirds;

import com.badlogic.gdx.Game;

public class Main extends Game {
    private int saveLevel = -1;

    @Override
    public void create() {
        loadSavedLevel();
    }

    public void saveLevel(int level) {
        this.saveLevel = level;
    }

    private void loadSavedLevel() {
        if (saveLevel != -1) {

            switch (saveLevel) {
                case 1:
                    setScreen(new Level_1(this));
                    break;

                default:
                    setScreen(new FirstScreen(this));
            }
        } else {
            setScreen(new FirstScreen(this));
        }
    }
}
