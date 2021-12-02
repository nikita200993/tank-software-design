package ru.mipt.bit.platformer.logic;

/**
 * Domain entity.
 */
public class FloatPoint2D {
    private final float x;
    private final float y;

    public FloatPoint2D(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public FloatPoint2D plus(final float number) {
        return new FloatPoint2D(x + number, y + number);
    }

    public FloatPoint2D plus(final FloatPoint2D other) {
        return new FloatPoint2D(x + other.x, y + other.y);
    }

    public FloatPoint2D minus(final FloatPoint2D other) {
        return new FloatPoint2D(x - other.x, y - other.y);
    }

    public FloatPoint2D multiply(final float other) {
        return new FloatPoint2D(x * other, y * other);
    }

    public static FloatPoint2D from(final Point2D point2D) {
        return new FloatPoint2D(point2D.getX(), point2D.getY());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
