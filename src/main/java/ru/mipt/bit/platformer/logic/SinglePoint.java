package ru.mipt.bit.platformer.logic;

public class SinglePoint implements Colliding {
    private final Point2D point2D;

    public SinglePoint(final Point2D point2D) {
        this.point2D = point2D;
    }

    @Override
    public boolean collides(final Point2D point2D) {
        return this.point2D.equals(point2D);
    }
}
