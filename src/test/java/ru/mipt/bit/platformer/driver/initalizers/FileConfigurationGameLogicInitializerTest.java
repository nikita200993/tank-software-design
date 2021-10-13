package ru.mipt.bit.platformer.driver.initalizers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.mipt.bit.platformer.logic.Point2D;

import static org.assertj.core.api.Assertions.assertThat;

class FileConfigurationGameLogicInitializerTest {

    @Test
    void testInit(@TempDir final Path tempDir) throws IOException {
        final var configPath = tempDir.resolve("level.config");
        Files.writeString(
                configPath,
                "---" + System.lineSeparator()
                        + "-TX" + System.lineSeparator()
                        + "-TT" + System.lineSeparator()
        );
        final var gameLogic = new FileConfigurationGameLogicInitializer(configPath.toString())
                .init(Map.of("height", 3, "width", 3));
        assertThat(gameLogic)
                .returns(new Point2D(2, 1), logic -> logic.getPlayer().getCurrentPosition())
                .returns(
                        Set.of(new Point2D(1, 1), new Point2D(2, 0), new Point2D(1, 0)),
                        logic -> new HashSet<>(logic.getObstacles())
                );
    }
}