package ru.mipt.bit.platformer.logic;

/**
 * Domain entity.
 */
class RectangleMap implements Colliding {
    private final int width;
    private final int height;

    RectangleMap(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean collides(final Point2D point2D) {
        return point2D.getX() < 0
                || point2D.getX() >= width
                || point2D.getY() < 0
                || point2D.getY() >= height;
    }

    public boolean collides(final FloatPoint2D floatPoint2D) {
        return floatPoint2D.getX() < 0.0f
                || floatPoint2D.getX() > width - 1.0f
                || floatPoint2D.getY() < 0.0f
                || floatPoint2D.getY() > height - 1.0f;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
