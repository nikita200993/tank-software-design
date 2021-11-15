package ru.mipt.bit.platformer.driver;

import ru.mipt.bit.platformer.logic.FloatPoint2D;
import ru.mipt.bit.platformer.logic.GameObjectView;

public class StaticMoveView implements GameObjectView {
    private final FloatPoint2D position;
    private final float angle;

    public StaticMoveView(final FloatPoint2D position, final float angle) {
        this.position = position;
        this.angle = angle;
    }


    @Override
    public FloatPoint2D position() {
        return position;
    }

    @Override
    public float angle() {
        return angle;
    }

}
