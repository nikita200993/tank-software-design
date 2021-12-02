package ru.mipt.bit.platformer.driver.initalizers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.mipt.bit.platformer.driver.GameLevelInitializer;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Point2D;

/**
 * Input adapter.
 */
public class FileConfigurationGameLevelInitializer implements GameLevelInitializer {

    private final String resourceDescriptor;

    public FileConfigurationGameLevelInitializer(final String resourceDescriptor) {
        this.resourceDescriptor = resourceDescriptor;
    }

    @Override
    public Level init(final int width, final int height) {
        final List<Point2D> obstacles = new ArrayList<>();
        Point2D playerPoint = null;
        final String content = loadContent();
        final List<String> lines;
        if (content.contains("\r\n")) {
            lines = Arrays.asList(content.split("\r\n"));
        } else {
            lines = Arrays.asList(content.split("\n"));
        }
        checkContentCorrespondsToHeigth(height, lines);
        for (int i = 0; i < lines.size(); i++) {
            final var line = lines.get(i);
            checkLineCorrespondToWidth(width, line);
            for (int j = 0; j < line.length(); j++) {
                final char aChar = line.charAt(j);
                switch (aChar) {
                    case 'T':
                        obstacles.add(new Point2D(j, height - 1 - i));
                        break;
                    case 'X': {
                        if (playerPoint != null) {
                            throw new IllegalStateException("there must be one player in the game");
                        }
                        playerPoint = new Point2D(j, height - 1 - i);
                        break;
                    }
                }
            }
        }
        if (playerPoint == null) {
            throw new IllegalStateException("resource at path '" + resourceDescriptor + "' doesn't contain player");
        }
        return new Level(playerPoint, Collections.emptyList(), obstacles, width, height);
    }

    private void checkContentCorrespondsToHeigth(final int height, final List<String> lines) {
        if (lines.size() != height) {
            throw new IllegalStateException("there are " + lines.size() + " lines in file, but required " + height);
        }
    }

    private static void checkLineCorrespondToWidth(final int width, final String line) {
        if (line.length() != width) {
            throw new IllegalStateException(
                    "there are " + line.length() + " symbols in line, but required " + width
            );
        }
    }

    private String loadContent() {
        try {
            if (resourceDescriptor.startsWith("classpath:")) {
                return new String(
                        FileConfigurationGameLevelInitializer.class
                                .getResourceAsStream("/" + resourceDescriptor.substring("classpath:".length()))
                                .readAllBytes()
                );
            } else {
                return Files.readString(Path.of(resourceDescriptor));
            }
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
