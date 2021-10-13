package ru.mipt.bit.platformer.logic;

import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Player {

    private final static float ACCURACY = 0.001f;
    private final static float MOVE_SPEED = 0.4f;

    private final Point2D currentPosition;
    private final Point2D destinationPosition;
    private float moveProgress = 1;
    private float rotation;

    public Player(final Point2D currentPosition) {
        this.currentPosition = currentPosition;
        this.destinationPosition = currentPosition.copy();
    }

    public Point2D getCurrentPosition() {
        return currentPosition;
    }

    public Point2D getDestinationPosition() {
        return destinationPosition;
    }

    public float getMoveProgress() {
        return moveProgress;
    }

    public float getRotation() {
        return rotation;
    }

    public void updateProgress(final float time) {
        moveProgress = continueProgress(moveProgress, time, MOVE_SPEED);
        if (Math.abs(1 - moveProgress) < ACCURACY) {
            moveProgress = 1;
            currentPosition.set(destinationPosition);
        }
    }

    public void startMove(final Direction direction, List<Point2D> obstacles) {
        if (moveProgress != 1) {
            return;
        }
        this.rotation = direction.getRotation();
        final Point2D destination = direction.computeNextPosition(currentPosition);
        if (obstacles.stream().noneMatch(destination::equals)) {
            this.moveProgress = 0;
            this.destinationPosition.set(destination);
        }
    }
}
