package ru.mipt.bit.platformer.logic;

import java.util.List;

public interface GameLogicListener {
    void onRegister(List<GameObjectView> tanks, List<GameObjectView> trees);
}
