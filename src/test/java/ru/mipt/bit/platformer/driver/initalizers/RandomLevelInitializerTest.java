package ru.mipt.bit.platformer.driver.initalizers;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Point2D;

class RandomLevelInitializerTest {

    private final Random random = Mockito.mock(Random.class);

    @Test
    void testInit() {
        final int height = 3;
        final int width = 3;
        final var initializer = new RandomGameLevelInitializer(random, 2, 1);
        stubRandom(2, 2, 1, 1, 2, 0, 0, 2);
        final var gameObjectsPositions = initializer.init(width, height);
        Assertions.assertThat(gameObjectsPositions)
                .returns(new Point2D(2, 2), Level::getPlayerCoordinate)
                .returns(
                        Set.of(new Point2D(2, 0), new Point2D(0, 2)),
                        it -> new HashSet<>(it.getTreesCoordinates())
                )
                .returns(Set.of(new Point2D(1, 1)), it -> new HashSet<>(it.getAiPlayers()));
    }

    private void stubRandom(final Integer coordinate, final Integer... coordinates) {
        Mockito.when(random.nextInt(ArgumentMatchers.anyInt()))
                .thenReturn(coordinate, coordinates);
    }


}