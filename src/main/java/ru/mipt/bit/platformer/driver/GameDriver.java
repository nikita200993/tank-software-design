package ru.mipt.bit.platformer.driver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.ai.TankGameAI;
import ru.mipt.bit.platformer.gdx.graphics.GameGraphics;
import ru.mipt.bit.platformer.gdx.graphics.GdxGameUtils;
import ru.mipt.bit.platformer.gdx.graphics.RectangleMovement;
import ru.mipt.bit.platformer.gdx.graphics.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.gdx.graphics.UISettings;
import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.Level;

import static ru.mipt.bit.platformer.gdx.graphics.GdxGameUtils.getSingleLayer;

/**
 * Input adapter. (framework callbacks delegated to app layer).
 */
public class GameDriver implements ApplicationListener {

    private final PlayerDevice playerDevice;
    private final GameLevelInitializer gameLevelInitializer;
    private final TankGameAI ai;
    private Level gameState;
    private GameGraphics gameGraphics;
    private final List<Disposable> disposables;

    public GameDriver(
            PlayerDevice playerDevice,
            GameLevelInitializer gameLevelInitializer,
            TankGameAI ai
    ) {
        this.playerDevice = playerDevice;
        this.gameLevelInitializer = gameLevelInitializer;
        this.ai = ai;
        this.disposables = new ArrayList<>();
    }

    @Override
    public void create() {
        final var levelMap = new TmxMapLoader().load("level.tmx");
        disposables.add(levelMap);
        final var propMap = levelMap.getProperties();
        gameState = gameLevelInitializer.init(
                (int) propMap.get("width"),
                (int) propMap.get("height")
        );
        gameGraphics = createGraphics(levelMap);
        gameState.addListener(gameGraphics);
    }


    @Override
    public void render() {
        float timePassedSinceLastRender = getTimePassedSinceLastRender();
        gameState.update(timePassedSinceLastRender);
        final var commands = new ArrayList<>(getPlayerCommands());
        commands.addAll(ai.computeAiCommands(gameState));
        commands.forEach(Command::execute);
        gameGraphics.render();
    }

    @Override
    public void dispose() {
        disposables.forEach(Disposable::dispose);
    }

    @Override
    public void resize(final int width, final int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    private GameGraphics createGraphics(final TiledMap levelMap) {

        final TiledMapTileLayer layer = getSingleLayer(levelMap);
        final var tankTexture = new Texture("images/tank_blue.png");
        final var treeTexture = new Texture("images/greenTree.png");
        final var bulletTexture = new Texture("images/bullet.png");
        final var batch = new SpriteBatch();
        final var shapeRenderer = new ShapeRenderer();
        disposables.add(tankTexture);
        disposables.add(treeTexture);
        disposables.add(bulletTexture);
        disposables.add(batch);
        disposables.add(shapeRenderer);
        return new GameGraphics(
                new TextureRegion(tankTexture),
                new TextureRegion(treeTexture),
                new TextureRegion(bulletTexture),
                new UISettings(),
                batch,
                shapeRenderer,
                GdxGameUtils.createSingleLayerMapRenderer(levelMap, batch),
                new RectangleMovement(layer.getTileWidth(), layer.getTileHeight())
        );
    }

    private float getTimePassedSinceLastRender() {
        return Gdx.graphics.getDeltaTime();
    }

    private List<Command> getPlayerCommands() {
        if (!gameState.isPlayerAlive()) {
            return Collections.emptyList();
        }
        final var commands = new ArrayList<Command>();
        playerDevice.getRequestedMoveDirection()
                .map(
                        direction -> new MoveCommand(
                                gameState.getPlayer(),
                                direction,
                                gameState.getCollidingObjects()
                        )
                ).ifPresent(commands::add);
        if (playerDevice.isShootRequested()) {
            commands.add(new ShootCommand(gameState.getPlayer(), gameState));
        }
        if (playerDevice.isHealthToggleRequested()) {
            commands.add(new ToggleHealthBarCommand(gameGraphics.getUiSettings()));
        }
        return commands;
    }
}
