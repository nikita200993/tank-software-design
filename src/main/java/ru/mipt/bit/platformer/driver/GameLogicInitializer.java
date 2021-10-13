package ru.mipt.bit.platformer.driver;

import java.util.Map;

import ru.mipt.bit.platformer.logic.GameLogic;

public interface GameLogicInitializer {

    GameLogic init(Map<String, Object> levelProps);
}
