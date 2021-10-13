package ru.mipt.bit.platformer.logic;


import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    @Test
    void testCurrentEqualsDestinationUponCreation() {
        final var player = new Player(new Point2D(1, 1));
        assertThat(player.getCurrentPosition())
                .isEqualTo(player.getDestinationPosition());
    }

    @Test
    void testDestinationChangedOnMove() {
        final var player = new Player(new Point2D(1, 1));
        player.startMove(Direction.UP, Collections.emptyList());
        assertThat(player.getCurrentPosition())
                .isNotEqualTo(player.getDestinationPosition());
    }

    @Test
    void testNoMoveIfCollidesWithObstacle() {
        final var player = new Player(new Point2D(1, 1));
        player.startMove(Direction.UP, List.of(new Point2D(1, 2)));
        assertThat(player.getCurrentPosition())
                .isEqualTo(player.getDestinationPosition());
    }

    @Test
    void testNoMoveIfProgress() {
        final var player = new Player(new Point2D(1, 1));
        player.startMove(Direction.UP, Collections.emptyList());
        final Point2D destination = player.getDestinationPosition();
        player.updateProgress(player.getMoveSpeed() * 0.5f);
        player.startMove(Direction.UP, Collections.emptyList());
        assertThat(player.getDestinationPosition())
                .isEqualTo(destination);
    }

    @Test
    void testDestinationEqualsCurrentIfProgressFinished() {
        final var player = new Player(new Point2D(1, 1));
        player.startMove(Direction.UP, Collections.emptyList());
        player.updateProgress(player.getMoveSpeed() * 2);
        assertThat(player.getDestinationPosition())
                .isEqualTo(player.getCurrentPosition());
    }
}