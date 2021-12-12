package ru.mipt.bit.platformer;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.awesome.ai.strategy.NotRecommendingAI;
import ru.mipt.bit.platformer.driver.AiTankControllerFactoryImpl;
import ru.mipt.bit.platformer.driver.GameDriver;
import ru.mipt.bit.platformer.driver.UISettings;
import ru.mipt.bit.platformer.driver.initalizers.RandomGameLevelInitializer;
import ru.mipt.bit.platformer.gdx.device.PlayerDeviceImpl;

public class GameDesktopLauncher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(createGameDriver(), config);
    }

    private static GameDriver createGameDriver() {
        return new GameDriver(
                new RandomGameLevelInitializer(2, 2),
                new AiTankControllerFactoryImpl(new NotRecommendingAI(), new PlayerDeviceImpl()),
                new UISettings()
        );
    }
}
