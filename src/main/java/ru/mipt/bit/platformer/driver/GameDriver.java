package ru.mipt.bit.platformer.driver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.ai.TankGameAI;
import ru.mipt.bit.platformer.device.DirectionResolver;
import ru.mipt.bit.platformer.graphic.GameGraphics;
import ru.mipt.bit.platformer.graphic.GraphicsObject;
import ru.mipt.bit.platformer.graphic.Renderable;
import ru.mipt.bit.platformer.graphic.TileMovement;
import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.GameState;
import ru.mipt.bit.platformer.logic.MoveCommand;
import ru.mipt.bit.platformer.logic.Point2D;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.graphic.GdxGameUtils.getSingleLayer;

public class GameDriver implements ApplicationListener {

    private final DirectionResolver directionResolver;
    private final GameLevelInitializer gameLevelInitializer;
    private final TankGameAI ai;
    private GameState gameState;
    private GameGraphics gameGraphics;
    private final List<Disposable> disposables;

    public GameDriver(
            final DirectionResolver directionResolver,
            final GameLevelInitializer gameLevelInitializer,
            final TankGameAI ai
    ) {
        this.directionResolver = directionResolver;
        this.gameLevelInitializer = gameLevelInitializer;
        this.ai = ai;
        this.disposables = new ArrayList<>();
    }

    @Override
    public void create() {
        final var levelMap = new TmxMapLoader().load("level.tmx");
        final var propMap = toMap(levelMap.getProperties());
        final var level = gameLevelInitializer.init(
                (int) propMap.get("width"),
                (int) propMap.get("height")
        );
        gameState = GameState.create(level);
        final var batch = new SpriteBatch();
        final var tileMovement = new TileMovement(getSingleLayer(levelMap), Interpolation.smooth);
        gameGraphics = new GameGraphics(
                batch,
                levelMap,
                createRenderables(level.getTreesCoordinates(), tileMovement)
        );
    }

    private List<Renderable> createRenderables(
            final List<Point2D> treesCoordinates,
            final TileMovement tileMovement) {
        final var tankTexture = new Texture("images/tank_blue.png");
        disposables.add(tankTexture);
        final List<Renderable> renderables = gameState.getMoveViews()
                .stream()
                .map(moveView -> GraphicsObject.create(moveView, tileMovement, new TextureRegion(tankTexture)))
                .collect(Collectors.toList());
        renderables.addAll(createTreeRenderables(treesCoordinates, tileMovement));
        return renderables;
    }

    @Override
    public void resize(final int width, final int height) {
    }

    @Override
    public void render() {
        float timePassedSinceLastRender = getTimePassedSinceLastRender();
        clearScreen();
        gameState.update(timePassedSinceLastRender);
        final var commands = new ArrayList<>(getPlayerCommands());
        commands.addAll(ai.computeAiCommands(gameState));
        commands.forEach(Command::execute);
        gameGraphics.render();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if (gameGraphics != null) {
            gameGraphics.close();
        }
        disposables.forEach(Disposable::dispose);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private float getTimePassedSinceLastRender() {
        return Gdx.graphics.getDeltaTime();
    }

    private List<Command> getPlayerCommands() {
        return directionResolver.resolveDirection()
                .map(
                        direction -> new MoveCommand(
                                gameState.getPlayer(),
                                direction,
                                gameState.getCollidingObjects()
                        )
                )
                .stream()
                .collect(Collectors.toList());
    }

    private static Map<String, Object> toMap(final MapProperties props) {
        final Iterable<String> keyIterable = props::getKeys;
        final var result = new HashMap<String, Object>();
        for (var key : keyIterable) {
            result.put(key, props.get(key));
        }
        return result;
    }

    private List<Renderable> createTreeRenderables(
            final List<Point2D> pointTrees,
            final TileMovement tileMovement
    ) {
        if (pointTrees.isEmpty()) {
            return Collections.emptyList();
        }
        final var treeTexture = new Texture("images/greenTree.png");
        disposables.add(treeTexture);
        return pointTrees
                .stream()
                .map(point -> GraphicsObject.create(
                        new StaticMoveView(point, 0),
                        tileMovement,
                        new TextureRegion(treeTexture)
                ))
                .collect(Collectors.toList());
    }
}
