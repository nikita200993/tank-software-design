package ru.mipt.bit.platformer.graphic;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mipt.bit.platformer.util.GdxGameUtils;

public class GameGraphics implements AutoCloseable {
    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer mapRenderer;
    private final GraphicsSimpleObject playerGraphics;
    private final List<GraphicsSimpleObject> obstaclesGraphics;

    public GameGraphics(
            final Batch batch,
            final TiledMap tiledMap,
            final GraphicsSimpleObject playerGraphics,
            final List<GraphicsSimpleObject> obstacleGraphics
    ) {
        this.batch = batch;
        this.tiledMap = tiledMap;
        this.mapRenderer = GdxGameUtils.createSingleLayerMapRenderer(tiledMap, batch);
        this.playerGraphics = playerGraphics;
        this.obstaclesGraphics = obstacleGraphics;
    }

    public GraphicsSimpleObject getPlayerGraphics() {
        return playerGraphics;
    }

    public void render() {
        mapRenderer.render();
        batch.begin();
        obstaclesGraphics.forEach(obstacleGraphics -> obstacleGraphics.render(batch));
        playerGraphics.render(batch);
        batch.end();
    }

    @Override
    public void close() {
        batch.dispose();
        tiledMap.dispose();
        playerGraphics.close();
        obstaclesGraphics.forEach(GraphicsSimpleObject::close);
    }
}
