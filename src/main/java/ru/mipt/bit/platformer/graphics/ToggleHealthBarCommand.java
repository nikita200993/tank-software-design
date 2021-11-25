package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.logic.Command;

public class ToggleHealthBarCommand implements Command {
    private final GameGraphics gameGraphics;

    public ToggleHealthBarCommand(GameGraphics gameGraphics) {
        this.gameGraphics = gameGraphics;
    }

    @Override
    public void execute() {
        gameGraphics.toggleHealth();
    }
}
