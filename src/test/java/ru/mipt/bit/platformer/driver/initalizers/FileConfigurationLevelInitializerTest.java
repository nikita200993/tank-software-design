package ru.mipt.bit.platformer.driver.initalizers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.mipt.bit.platformer.logic.Obstacle;
import ru.mipt.bit.platformer.logic.Point2D;

import static org.assertj.core.api.Assertions.assertThat;

class FileConfigurationLevelInitializerTest {

    @Test
    void testInit(@TempDir final Path tempDir) throws IOException {
        final var configPath = tempDir.resolve("level.config");
        Files.writeString(
                configPath,
                "---" + System.lineSeparator()
                        + "-TX" + System.lineSeparator()
                        + "-TT" + System.lineSeparator()
        );
        final var gameObjectsPositions = new FileConfigurationGameLevelInitializer(configPath.toString())
                .init(3, 3);
        assertThat(gameObjectsPositions)
                .returns(new Point2D(2, 1), it -> it.getPlayer().currentPosition())
                .returns(
                        Set.of(new Point2D(1, 1), new Point2D(2, 0), new Point2D(1, 0)),
                        it -> it.getObstacles().stream().map(Obstacle::getPosition).collect(Collectors.toSet())
                );
    }
}