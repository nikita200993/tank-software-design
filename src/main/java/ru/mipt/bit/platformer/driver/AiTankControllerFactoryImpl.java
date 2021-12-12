package ru.mipt.bit.platformer.driver;

import org.awesome.ai.AI;
import ru.mipt.bit.platformer.driver.controllers.AIBotController;
import ru.mipt.bit.platformer.driver.controllers.PlayerController;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Tank;

public class AiTankControllerFactoryImpl implements TankControllerFactory {

    private final AI ai;
    private final PlayerDevice playerDevice;

    public AiTankControllerFactoryImpl(AI ai, PlayerDevice playerDevice) {
        this.ai = ai;
        this.playerDevice = playerDevice;
    }

    @Override
    public TankController create(Tank tank, Level level, UISettings uiSettings) {
        if (tank == level.getPlayer()) {
            return new PlayerController(playerDevice, level, uiSettings);
        } else {
            return new AIBotController(tank, level, ai);
        }
    }
}
