package ru.mipt.bit.platformer.logic;

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
}
