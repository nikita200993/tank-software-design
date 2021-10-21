package ru.mipt.bit.platformer.logic;

public interface MoveView {
    Point2D currentPosition();
    Point2D destinationPosition();
    float angle();
    float progress();
}
