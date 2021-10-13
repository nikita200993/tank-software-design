package ru.mipt.bit.platformer.logic;

import java.util.Objects;

public final class Point2D {
    private int x;
    private int y;

    public Point2D(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void set(final Point2D point2D) {
        this.x = point2D.x;
        this.y = point2D.y;
    }

    public int getY() {
        return y;
    }

    public Point2D copy() {
        return new Point2D(x, y);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Point2D point2D = (Point2D) o;
        return x == point2D.x && y == point2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
