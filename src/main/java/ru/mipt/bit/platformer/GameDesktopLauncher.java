package ru.mipt.bit.platformer;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.device.DefaultGdxDirectionResolver;
import ru.mipt.bit.platformer.driver.GameDriver;
import ru.mipt.bit.platformer.driver.initalizers.FileConfigurationGameLevelInitializer;
import ru.mipt.bit.platformer.driver.initalizers.RandomGameLevelInitializer;

public class GameDesktopLauncher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(createGameDriver(), config);
    }

    private static GameDriver createGameDriver() {
        return new GameDriver(
                new DefaultGdxDirectionResolver(),
                new RandomGameLevelInitializer(2, 2));
    }
}
