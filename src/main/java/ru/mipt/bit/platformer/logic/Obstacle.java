package ru.mipt.bit.platformer.logic;

public class Obstacle implements GameObjectView, Colliding {
    private final Point2D position;

    public Obstacle(final Point2D position) {
        this.position = position;
    }

    public Point2D getPosition() {
        return position;
    }

    @Override
    public FloatPoint2D position() {
        return new FloatPoint2D(position.getX(), position.getY());
    }

    @Override
    public float angle() {
        return 0;
    }

    @Override
    public boolean collides(final Point2D point2D) {
        return position.equals(point2D);
    }
}
