package ru.mipt.bit.platformer.driver;

import java.util.List;

import ru.mipt.bit.platformer.logic.Command;

public interface TankController {

    List<Command> getRequestedCommands();
}
