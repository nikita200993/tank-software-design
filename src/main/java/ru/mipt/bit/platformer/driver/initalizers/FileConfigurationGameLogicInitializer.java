package ru.mipt.bit.platformer.driver.initalizers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mipt.bit.platformer.driver.GameLogicInitializer;
import ru.mipt.bit.platformer.logic.GameLogic;
import ru.mipt.bit.platformer.logic.Player;
import ru.mipt.bit.platformer.logic.Point2D;

public class FileConfigurationGameLogicInitializer implements GameLogicInitializer {

    private final Path pathToFile;

    public FileConfigurationGameLogicInitializer(final Path pathToFile) {
        this.pathToFile = pathToFile;
    }

    @Override
    public GameLogic init(final Map<String, Object> levelProps) {
        try {
            final int width = (int) levelProps.get("width");
            final int height = (int) levelProps.get("height");
            final List<Point2D> obstacles = new ArrayList<>();
            Point2D playerPoint = null;
            final var lines = Files.readAllLines(pathToFile);
            if (lines.size() != height) {
                throw new IllegalStateException("there are " + lines.size() + " lines in file, but required " + height);
            }
            for (int i = 0; i < lines.size(); i++) {
                final var line = lines.get(i);
                if (line.length() != width) {
                    throw new IllegalStateException(
                            "there are " + lines.size() + " symbols in line, but required " + width
                    );
                }
                for (int j = 0; j < line.length(); j++) {
                    final char aChar = line.charAt(j);
                    switch (aChar) {
                        case 'T':
                            obstacles.add(new Point2D(i, j));
                            break;
                        case 'P': {
                            if (playerPoint != null) {
                                throw new IllegalStateException("There must be one player in the game.");
                            }
                            playerPoint = new Point2D(i, j);
                            break;
                        }
                    }
                }
            }
            if (playerPoint == null) {
                throw new IllegalStateException("file at path '" + pathToFile.toAbsolutePath() + "' doesn't contain player");
            }
            return new GameLogic(new Player(playerPoint), obstacles);
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
