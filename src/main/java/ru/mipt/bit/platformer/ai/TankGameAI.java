package ru.mipt.bit.platformer.ai;

import java.util.List;

import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.GameState;

/**
 * Output port.
 */
public interface TankGameAI {
    List<Command> computeAiCommands(final GameState gameState);
}
