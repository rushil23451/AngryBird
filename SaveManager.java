package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class SaveManager {
    private static final String SAVE_FILE_1 = "save_slot_1.json";
    private static final String SAVE_FILE_2 = "save_slot_2.json";

    private Json json;

    public SaveManager() {
        json = new Json();
    }

    // Save game to a specific slot
    public void saveGame(SaveData saveData, boolean useSlot1) {
        try {
            String filename = useSlot1 ? SAVE_FILE_1 : SAVE_FILE_2;
            FileHandle file = Gdx.files.local(filename);
            file.writeString(json.toJson(saveData), false);
            System.out.println("Game saved successfully to " + filename);
        } catch (Exception e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    // Load game from a specific slot
    public SaveData loadGame(boolean useSlot1) {
        try {
            String filename = useSlot1 ? SAVE_FILE_1 : SAVE_FILE_2;
            FileHandle file = Gdx.files.local(filename);

            if (file.exists()) {
                return json.fromJson(SaveData.class, file.readString());
            } else {
                System.out.println("No saved game found in " + filename);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    // Check if a save slot has a saved game
    public boolean hasSavedGame(boolean useSlot1) {
        String filename = useSlot1 ? SAVE_FILE_1 : SAVE_FILE_2;
        return Gdx.files.local(filename).exists();
    }
}
