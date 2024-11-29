package com.angrybirds;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentLevel;

    // Constructor
    public SaveData(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    // Getter for current level
    public int getCurrentLevel() {
        return currentLevel;
    }

    // Setter for current level
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    // Method to save game data
    public static void saveGame(SaveData data) {
        try (FileOutputStream fileOut = new FileOutputStream("game_save.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(data);
            System.out.println("Game progress saved successfully.");
        } catch (IOException i) {
            System.err.println("Error saving game progress: " + i.getMessage());
        }
    }

    // Method to load game data
    public static SaveData loadGame() {
        try (FileInputStream fileIn = new FileInputStream("game_save.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            SaveData data = (SaveData) in.readObject();
            System.out.println("Game progress loaded successfully.");
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("No saved game found or error loading: " + e.getMessage());
            // Return a default save data if no save exists
            return new SaveData(1); // Default to first level
        }
    }
}



