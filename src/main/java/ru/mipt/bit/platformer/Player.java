package ru.mipt.bit.platformer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.decrementedX;
import static ru.mipt.bit.platformer.util.GdxGameUtils.decrementedY;
import static ru.mipt.bit.platformer.util.GdxGameUtils.incrementedX;
import static ru.mipt.bit.platformer.util.GdxGameUtils.incrementedY;

import com.badlogic.gdx.math.GridPoint2;

class Player {
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
        if (moveProgress == 1) {
            currentPosition.set(destinationPosition);
        }
    }

    void startMove(final Direction direction, final GridPoint2 obstacle) {
        if (moveProgress != 1) {
            return;
        }
        final GridPoint2 destination;
        final float rotation;
        switch (direction) {
            case UP:
                destination = new GridPoint2(incrementedY(currentPosition));
                rotation = 90;
                break;
            case LEFT:
                destination = new GridPoint2(decrementedX(currentPosition));
                rotation = -180;
                break;
            case DOWN:
                destination = new GridPoint2(decrementedY(currentPosition));
                rotation = -90;
                break;
            case RIGHT:
                destination = new GridPoint2(incrementedX(currentPosition));
                rotation = 0;
                break;
            case NONE:
                destination = null;
                rotation = this.rotation;
                break;
            default:
                throw new IllegalStateException("Unreachable switch branch.");
        }
        this.rotation = rotation;
        if (destination != null && !obstacle.equals(destination)) {
            this.moveProgress = 0;
            this.destinationPosition.set(destination);
        }
    }
}
