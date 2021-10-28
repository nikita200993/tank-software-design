package ru.mipt.bit.platformer.driver;

import ru.mipt.bit.platformer.logic.Level;

public interface GameLevelInitializer {

    Level init(final int width, final int height);
}
