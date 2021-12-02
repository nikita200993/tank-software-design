package ru.mipt.bit.platformer.driver;

import ru.mipt.bit.platformer.logic.Level;

/**
 * Input port.
 */
public interface GameLevelInitializer {

    Level init(final int width, final int height);
}
