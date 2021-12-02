package ru.mipt.bit.platformer.logic;

/**
 * Domain entity.
 */
public interface Colliding {

    boolean collides(Point2D point2D);
}
