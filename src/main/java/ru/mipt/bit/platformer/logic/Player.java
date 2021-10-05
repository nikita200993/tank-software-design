package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.math.GridPoint2;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Player {

    private final static float ACCURACY = 0.001f;
    private final static float MOVE_SPEED = 0.4f;

    private final GridPoint2 currentPosition;
    private final GridPoint2 destinationPosition;
    private float moveProgress = 1;
    private float rotation;

    public Player(final GridPoint2 currentPosition) {
        this.currentPosition = currentPosition;
        this.destinationPosition = currentPosition.cpy();
    }

    public GridPoint2 getCurrentPosition() {
        return currentPosition;
    }

    public GridPoint2 getDestinationPosition() {
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

    public void startMove(final Direction direction, final GridPoint2 obstacle) {
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
