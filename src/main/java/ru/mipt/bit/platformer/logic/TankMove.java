package ru.mipt.bit.platformer.logic;

import java.util.List;

/**
 * Domain entity.
 */
class TankMove {
    private final Point2D currentPosition;
    private final Point2D destinationPosition;
    private float moveProgress;
    private Direction direction;

    TankMove(Point2D currentPosition) {
        this.currentPosition = currentPosition;
        this.destinationPosition = currentPosition.copy();
        this.moveProgress = 1;
        this.direction = Direction.RIGHT;
    }

    void update(float timeDelta, float speed) {
        moveProgress = Math.min(1, moveProgress + speed * timeDelta);
        if (moveProgress == 1) {
            this.currentPosition.set(destinationPosition);
        }
    }

    void startMove(Direction direction, List<Colliding> collidings) {
        if (isMoving()) {
            return;
        }
        this.direction = direction;
        var nextPosition = direction.computeNextPosition(currentPosition);
        boolean canMove = collidings.stream()
                .noneMatch(colliding -> colliding.collides(nextPosition));
        if (canMove) {
            moveProgress = 0;
            destinationPosition.set(nextPosition);
        }
    }

    Point2D getCurrentPosition() {
        return currentPosition;
    }

    Point2D getDestinationPosition() {
        return destinationPosition;
    }

    Direction getDirection() {
        return direction;
    }

    public float getMoveProgress() {
        return moveProgress;
    }

    private boolean isMoving() {
        return moveProgress != 1;
    }
}
