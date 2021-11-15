package ru.mipt.bit.platformer.logic;

public class StaticGameObjectView implements GameObjectView {
    private final FloatPoint2D point2D;

    public StaticGameObjectView(final FloatPoint2D point2D) {
        this.point2D = point2D;
    }

    @Override
    public FloatPoint2D position() {
        return point2D;
    }

    @Override
    public float angle() {
        return 0;
    }
}
