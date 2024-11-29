package com.angrybirds;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JUnitTest {
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private FirstScreen firstScreen;
    private Main game;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        game = new Main();
        firstScreen = new FirstScreen(game);
    }

    @Test
    public void testPlayButtonClick() {
        firstScreen.playButtonClicked();

        String output = outContent.toString();
        assertTrue(output.contains("Play button clicked"),
                "Play button click should output confirmation message");
    }

    @Test
    public void testPauseButtonClick() {
        Level_2_Birds levelScreen = new Level_2_Birds(game);
        levelScreen.pauseButtonClicked();

        String output = outContent.toString();
        assertTrue(output.contains("Game Paused"),
                "Pause button click should print 'Game Paused'");

        assertTrue(levelScreen.isPaused(),
                "Game should be in paused state after clicking pause button");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }
}
