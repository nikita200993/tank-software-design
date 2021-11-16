package ru.mipt.bit.platformer.logic;

import java.util.List;

public interface GameLogicListener {
    void onRegister(List<? extends GameObjectView> tanks, List<? extends GameObjectView> trees);
}
