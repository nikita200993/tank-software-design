package ru.mipt.bit.platformer.logic;


import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TankTest {

    @Test
    void testCurrentEqualsDestinationUponCreation() {
        final var player = new Tank(new Point2D(1, 1));
        assertThat(player.currentPosition())
                .isEqualTo(player.destinationPosition());
    }

    @Test
    void testDestinationChangedOnMove() {
        final var player = new Tank(new Point2D(1, 1));
        player.startMove(Direction.UP, Collections.emptyList());
        assertThat(player.currentPosition())
                .isNotEqualTo(player.destinationPosition());
    }

    @Test
    void testNoMoveIfCollidesWithObstacle() {
        final var player = new Tank(new Point2D(1, 1));
        player.startMove(Direction.UP, List.of(new SinglePoint(new Point2D(1, 2))));
        assertThat(player.currentPosition())
                .isEqualTo(player.destinationPosition());
    }

    @Test
    void testNoMoveIfProgress() {
        final var player = new Tank(new Point2D(1, 1));
        player.startMove(Direction.UP, Collections.emptyList());
        final Point2D destination = player.destinationPosition();
        player.updateProgress(player.getMoveSpeed() * 0.1f);
        player.startMove(Direction.UP, Collections.emptyList());
        assertThat(player.destinationPosition())
                .isEqualTo(destination);
    }

    @Test
    void testDestinationEqualsCurrentIfProgressFinished() {
        final var player = new Tank(new Point2D(1, 1));
        player.startMove(Direction.UP, Collections.emptyList());
        player.updateProgress(player.getMoveSpeed() * 2);
        assertThat(player.destinationPosition())
                .isEqualTo(player.currentPosition());
    }
}