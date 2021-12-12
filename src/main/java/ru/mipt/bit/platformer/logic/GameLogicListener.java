package ru.mipt.bit.platformer.logic;

/**
 * Output port.
 */
public interface GameLogicListener {
    void onEvent(GameEvent<? extends GameObjectView> gameEvent);
}
