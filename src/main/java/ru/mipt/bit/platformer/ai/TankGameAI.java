package ru.mipt.bit.platformer.ai;

import java.util.List;

import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.Level;

/**
 * Output port.
 */
public interface TankGameAI {
    List<Command> computeAiCommands(final Level level);
}
