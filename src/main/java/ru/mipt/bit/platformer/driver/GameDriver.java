package ru.mipt.bit.platformer.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ru.mipt.bit.platformer.gdx.graphics.GameGraphics;
import ru.mipt.bit.platformer.gdx.graphics.GdxGameUtils;
import ru.mipt.bit.platformer.gdx.graphics.RectangleMovement;
import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.CreationEvent;
import ru.mipt.bit.platformer.logic.DeathEvent;
import ru.mipt.bit.platformer.logic.GameEvent;
import ru.mipt.bit.platformer.logic.GameLogicListener;
import ru.mipt.bit.platformer.logic.GameObjectView;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Tank;

import static ru.mipt.bit.platformer.gdx.graphics.GdxGameUtils.getSingleLayer;

/**
 * Input adapter. (framework callbacks delegated to app layer).
 */
public class GameDriver implements ApplicationListener, GameLogicListener {

    private final GameLevelInitializer gameLevelInitializer;
    private Level gameState;
    private final UISettings uiSettings;
    private final List<Disposable> disposables;
    private final Map<Tank, TankController> tankControllers;
    private final TankControllerFactory tankControllerFactory;
    private GameGraphics gameGraphics;

    public GameDriver(
            GameLevelInitializer gameLevelInitializer,
            TankControllerFactory tankControllerFactory,
            UISettings uiSettings
    ) {
        this.gameLevelInitializer = gameLevelInitializer;
        this.tankControllerFactory = tankControllerFactory;
        this.uiSettings = uiSettings;
        this.tankControllers = new HashMap<>();
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
        gameState.addListener(this);
    }


    @Override
    public void render() {
        float timePassedSinceLastRender = getTimePassedSinceLastRender();
        gameState.update(timePassedSinceLastRender);
        collectCommands().forEach(Command::execute);
        gameGraphics.render();
    }

    @Override
    public void onEvent(GameEvent<? extends GameObjectView> gameEvent) {
        var source = gameEvent.source();
        if (!(source instanceof Tank)) {
            return;
        }
        var tank = (Tank) gameEvent.source();
        if (gameEvent instanceof CreationEvent) {
            tankControllers.put(tank, tankControllerFactory.create(tank, gameState, uiSettings));
        } else if (gameEvent instanceof DeathEvent) {
            tankControllers.remove(tank);
        }
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
                uiSettings,
                batch,
                shapeRenderer,
                GdxGameUtils.createSingleLayerMapRenderer(levelMap, batch),
                new RectangleMovement(layer.getTileWidth(), layer.getTileHeight())
        );
    }

    private float getTimePassedSinceLastRender() {
        return Gdx.graphics.getDeltaTime();
    }

    private List<Command> collectCommands() {
        var result = new ArrayList<Command>();
        for (var controller : tankControllers.values()) {
            result.addAll(controller.getRequestedCommands());
        }
        return result;
    }
}
