package ru.mipt.bit.platformer.driver;

import javax.annotation.Nullable;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.DirectionResolver;
import ru.mipt.bit.platformer.UserInput;
import ru.mipt.bit.platformer.graphic.GameGraphics;
import ru.mipt.bit.platformer.graphic.GraphicsSimpleObject;
import ru.mipt.bit.platformer.logic.GameLogic;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDriver implements ApplicationListener {
    private final GameLogic gameLogic;
    private final DirectionResolver directionResolver;
    @Nullable
    private GameGraphics gameGraphics;

    public GameDriver(
            final GameLogic gameLogic,
            final DirectionResolver directionResolver
    ) {
        this.gameLogic = gameLogic;
        this.directionResolver = directionResolver;
    }

    @Override
    public void create() {
        final var batch = new SpriteBatch();
        final var level = new TmxMapLoader().load("level.tmx");
        final var tileMovement = new TileMovement(getSingleLayer(level), Interpolation.smooth);
        final var playerTexture = new Texture("images/tank_blue.png");
        final var treeTexture = new Texture("images/greenTree.png");
        final var player = gameLogic.getPlayer();
        gameGraphics = new GameGraphics(
                batch,
                level,
                new GraphicsSimpleObject(
                        tileMovement,
                        player.getCurrentPosition(),
                        player.getDestinationPosition(),
                        playerTexture,
                        new TextureRegion(playerTexture),
                        player.getRotation(),
                        player.getMoveProgress()
                ),
                new GraphicsSimpleObject(
                        tileMovement,
                        gameLogic.getObstacle(),
                        gameLogic.getObstacle(),
                        treeTexture,
                        new TextureRegion(treeTexture),
                        0,
                        1
                )
        );
    }

    @Override
    public void resize(final int width, final int height) {
    }

    @Override
    public void render() {
        float timePassedSinceLastRender = getTimePassedSinceLastRender();
        clearScreen();
        gameLogic.update(
                new UserInput(directionResolver.resolveDirection().orElse(null)),
                timePassedSinceLastRender
        );
        final var player = gameLogic.getPlayer();
        gameGraphics.getPlayerGraphics()
                .withCurrent(player.getCurrentPosition())
                .withDestination(player.getDestinationPosition())
                .withRotation(player.getRotation())
                .withMoveProgress(player.getMoveProgress());
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
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private float getTimePassedSinceLastRender() {
        return Gdx.graphics.getDeltaTime();
    }
}
