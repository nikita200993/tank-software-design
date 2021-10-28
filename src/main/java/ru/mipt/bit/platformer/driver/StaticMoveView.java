package ru.mipt.bit.platformer.driver;

import ru.mipt.bit.platformer.logic.MoveView;
import ru.mipt.bit.platformer.logic.Point2D;

public class StaticMoveView implements MoveView {
    private final Point2D position;
    private final float angle;

    public StaticMoveView(final Point2D position, final float angle) {
        this.position = position;
        this.angle = angle;
    }

    @Override
    public Point2D currentPosition() {
        return position.copy();
    }

    @Override
    public Point2D destinationPosition() {
        return position.copy();
    }

    @Override
    public float angle() {
        return angle;
    }

    @Override
    public float progress() {
        return 1;
    }
}
