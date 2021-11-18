package ru.mipt.bit.platformer.driver;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.ai.TankGameAI;
import ru.mipt.bit.platformer.device.PlayerDevice;
import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.GameState;
import ru.mipt.bit.platformer.logic.MoveCommand;
import ru.mipt.bit.platformer.logic.ShootCommand;
import ru.mipt.bit.platformer.newgraphics.GameGraphics;
import ru.mipt.bit.platformer.newgraphics.GdxGameUtils;
import ru.mipt.bit.platformer.newgraphics.RectangleMovement;

import static ru.mipt.bit.platformer.newgraphics.GdxGameUtils.getSingleLayer;

public class GameDriver implements ApplicationListener {

    private final PlayerDevice playerDevice;
    private final GameLevelInitializer gameLevelInitializer;
    private final TankGameAI ai;
    private GameState gameState;
    private GameGraphics gameGraphics;
    private final List<Disposable> disposables;

    public GameDriver(
            final PlayerDevice playerDevice,
            final GameLevelInitializer gameLevelInitializer,
            final TankGameAI ai
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
        final var level = gameLevelInitializer.init(
                (int) propMap.get("width"),
                (int) propMap.get("height")
        );
        gameState = GameState.create(level);
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
        final var batch = new SpriteBatch();
        disposables.add(batch);
        final TiledMapTileLayer layer = getSingleLayer(levelMap);
        final var tankTexture = new Texture("images/tank_blue.png");
        final var treeTexture = new Texture("images/greenTree.png");
        disposables.add(tankTexture);
        disposables.add(treeTexture);
        return new GameGraphics(
                new TextureRegion(tankTexture),
                new TextureRegion(treeTexture),
                new SpriteBatch(),
                GdxGameUtils.createSingleLayerMapRenderer(levelMap, batch),
                new RectangleMovement(layer.getTileWidth(), layer.getTileHeight())
        );
    }

    private float getTimePassedSinceLastRender() {
        return Gdx.graphics.getDeltaTime();
    }

    private List<Command> getPlayerCommands() {
        final var commands = new ArrayList<Command>();
        playerDevice.getMoveDirection()
                .map(
                        direction -> new MoveCommand(
                                gameState.getPlayer(),
                                direction,
                                gameState.getCollidingObjects()
                        )
                ).ifPresent(commands::add);
        if (playerDevice.isShootRequested()) {
            System.out.println("shoot requested");
            commands.add(new ShootCommand(gameState.getPlayer(), gameState));
        }
        return commands;
    }
}
