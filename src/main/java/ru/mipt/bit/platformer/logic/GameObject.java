package ru.mipt.bit.platformer.logic;

public interface GameObject extends GameObjectView {
    void update(float time);
    boolean isAlive();
}
