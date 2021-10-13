package ru.mipt.bit.platformer;

import java.util.List;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.driver.GameDriver;
import ru.mipt.bit.platformer.logic.GameLogic;
import ru.mipt.bit.platformer.logic.Player;
import ru.mipt.bit.platformer.logic.Point2D;

public class GameDesktopLauncher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(createGameDriver(), config);
    }

    private static GameDriver createGameDriver() {
        return new GameDriver(
                new GameLogic(
                        new Player(new Point2D(1, 1)),
                        List.of(new Point2D(1, 3))
                ),
                new DefaultGdxDirectionResolver()
        );
    }
}
