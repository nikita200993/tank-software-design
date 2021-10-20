package ru.mipt.bit.platformer.driver.initalizers;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ru.mipt.bit.platformer.logic.Point2D;

class RandomGameLogicInitializerTest {

    private final Random random = Mockito.mock(Random.class);

    @Test
    void testInit() {
        final int height = 3;
        final int width = 3;
        final var tilesCount = width * height;
        final float maxObstaclesShare = 0.25f;
        final var initializer = new RandomGameLogicInitializer(random, maxObstaclesShare);
        final int maxObstacles = (int) Math.floor(tilesCount * maxObstaclesShare);
        stubRandom(maxObstacles + 1, 2, 2, 2, 0, 0, 2);
        final var gameLogic = initializer.init(Map.of("width", width, "height", height));
        Assertions.assertThat(gameLogic)
                .returns(new Point2D(2, 2), logic -> logic.getPlayer().getCurrentPosition())
                .returns(
                        Set.of(new Point2D(2, 0), new Point2D(0, 2)),
                        logic -> new HashSet<>(logic.getObstacles())
                );
    }

    private void stubRandom(final int numPoints, final Integer... coordinates) {
        Mockito.when(random.nextInt(ArgumentMatchers.anyInt()))
                .thenReturn(numPoints, coordinates);
    }


}