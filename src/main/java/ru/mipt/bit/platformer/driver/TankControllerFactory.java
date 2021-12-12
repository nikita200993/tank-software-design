package ru.mipt.bit.platformer.driver;

import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Tank;

public interface TankControllerFactory {
    TankController create(Tank tank, Level level, UISettings uiSettings);
}
