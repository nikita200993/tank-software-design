package ru.mipt.bit.platformer.ai;

import java.util.List;

import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.logic.GameState;

public interface TankGameAI {
    List<Command> computeAiCommands(final GameState gameState);
}
