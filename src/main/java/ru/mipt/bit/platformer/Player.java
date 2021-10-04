package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

class Player {

    private final static float ACCURACY = 0.001f;

    private final GridPoint2 currentPosition;
    private final GridPoint2 destinationPosition;
    private float moveProgress = 1;
    private float rotation;

    Player(final GridPoint2 currentPosition) {
        this.currentPosition = currentPosition;
        this.destinationPosition = currentPosition.cpy();
    }

    GridPoint2 getCurrentPosition() {
        return currentPosition;
    }

    GridPoint2 getDestinationPosition() {
        return destinationPosition;
    }

    float getMoveProgress() {
        return moveProgress;
    }

    float getRotation() {
        return rotation;
    }

    void updateProgress(final float time, final float moveSpeed) {
        moveProgress = continueProgress(moveProgress, time, moveSpeed);
        if (Math.abs(1 - moveProgress) < ACCURACY) {
            moveProgress = 1;
            currentPosition.set(destinationPosition);
        }
    }

    void startMove(final Direction direction, final GridPoint2 obstacle) {
        if (moveProgress != 1) {
            return;
        }
        this.rotation = direction.getRotation();
        final GridPoint2 destination = direction.computeNextPosition(currentPosition);
        if (!obstacle.equals(destination)) {
            this.moveProgress = 0;
            this.destinationPosition.set(destination);
        }
    }
}
