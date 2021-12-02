package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.Command;

public class ToggleHealthBarCommand implements Command {
    private final UISettings uiSettings;

    public ToggleHealthBarCommand(UISettings uiSettings) {
        this.uiSettings = uiSettings;
    }

    @Override
    public void execute() {
        uiSettings.toggleHealth();
    }
}
