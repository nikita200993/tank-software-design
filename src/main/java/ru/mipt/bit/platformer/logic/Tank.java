package ru.mipt.bit.platformer.logic;

import java.util.List;

public class Tank implements Colliding, MoveView {

    private final static float MOVE_SPEED = 2.5f;

    private final Point2D currentPosition;
    private final Point2D destinationPosition;
    private float moveProgress;
    private float rotation;

    Tank(final Point2D currentPosition) {
        this.currentPosition = currentPosition;
        this.destinationPosition = currentPosition.copy();
        this.moveProgress = 1;
        this.rotation = 0;
    }

    @Override
    public boolean collides(final Point2D point2D) {
        if (isMoving()) {
            return point2D.equals(currentPosition) || point2D.equals(destinationPosition);
        } else {
            return point2D.equals(currentPosition);
        }
    }

    @Override
    public Point2D currentPosition() {
        return currentPosition.copy();
    }

    @Override
    public Point2D destinationPosition() {
        return destinationPosition.copy();
    }

    @Override
    public float angle() {
        return rotation;
    }

    @Override
    public float progress() {
        return moveProgress;
    }

    void updateProgress(final float time) {
        moveProgress = Math.min(moveProgress + time * getMoveSpeed(), 1);
        if (finishedMove()) {
            currentPosition.set(destinationPosition);
        }
    }

    void startMove(final Direction direction, List<Colliding> obstacles) {
        if (isMoving()) {
            return;
        }
        rotation = direction.getRotation();
        final Point2D destination = direction.computeNextPosition(currentPosition);
        if (obstacles.stream().noneMatch(obstacle -> obstacle.collides(destination))) {
            moveProgress = 0;
            destinationPosition.set(destination);
        }
    }

    float getMoveSpeed() {
        return MOVE_SPEED;
    }

    private boolean isMoving() {
        return moveProgress != 1;
    }

    private boolean finishedMove() {
        return moveProgress == 1;
    }
}
