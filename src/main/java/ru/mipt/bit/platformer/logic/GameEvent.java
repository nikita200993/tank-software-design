package ru.mipt.bit.platformer.logic;

public interface GameEvent<T extends GameObjectView> {

    T source();
}
