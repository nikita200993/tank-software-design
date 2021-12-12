package ru.mipt.bit.platformer.driver.initalizers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import ru.mipt.bit.platformer.driver.GameLevelInitializer;
import ru.mipt.bit.platformer.logic.GameObjects;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Obstacle;
import ru.mipt.bit.platformer.logic.Point2D;
import ru.mipt.bit.platformer.logic.Tank;

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
        final List<Obstacle> obstacles = new ArrayList<>();
        Tank player = null;
        final List<String> lines = getLines(height);
        for (int i = 0; i < lines.size(); i++) {
            final var line = lines.get(i);
            checkLineCorrespondToWidth(width, line);
            var result = parseLine(line, height, i);
            player = result.player;
            obstacles.addAll(result.obstacles);
        }
        if (player == null) {
            throw new IllegalStateException("resource at path '" + resourceDescriptor + "' doesn't contain player");
        }
        return new Level(new GameObjects(player, new ArrayList<>(), obstacles, new ArrayList<>()), width, height);
    }

    private List<String> getLines(int height) {
        final String content = loadContent();
        final List<String> lines;
        if (content.contains("\r\n")) {
            lines = Arrays.asList(content.split("\r\n"));
        } else {
            lines = Arrays.asList(content.split("\n"));
        }
        checkContentCorrespondsToHeigth(height, lines);
        return lines;
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

    private static LineParseResult parseLine(String line, int height, int currentLineIndex) {
        Tank player = null;
        var obstacles = new ArrayList<Obstacle>();
        for (int j = 0; j < line.length(); j++) {
            final char aChar = line.charAt(j);
            switch (aChar) {
                case 'T':
                    obstacles.add(new Obstacle(new Point2D(j, height - 1 - currentLineIndex)));
                    break;
                case 'X': {
                    player = new Tank(new Point2D(j, height - 1 - currentLineIndex));
                    break;
                }
            }
        }
        return new LineParseResult(obstacles, player);
    }

    private static class LineParseResult {
        private final List<Obstacle> obstacles;
        @Nullable
        private final Tank player;

        LineParseResult(List<Obstacle> obstacles, @Nullable Tank player) {
            this.obstacles = obstacles;
            this.player = player;
        }
    }
}
