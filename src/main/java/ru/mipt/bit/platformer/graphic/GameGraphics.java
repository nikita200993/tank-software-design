package ru.mipt.bit.platformer.graphic;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class GameGraphics implements AutoCloseable {
    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer mapRenderer;
    private final List<Renderable> obstaclesGraphics;

    public GameGraphics(
            final Batch batch,
            final TiledMap tiledMap,
            final List<Renderable> obstacleGraphics
    ) {
        this.batch = batch;
        this.tiledMap = tiledMap;
        this.mapRenderer = GdxGameUtils.createSingleLayerMapRenderer(tiledMap, batch);
        this.obstaclesGraphics = obstacleGraphics;
    }

    public void render() {
        mapRenderer.render();
        batch.begin();
        obstaclesGraphics.forEach(obstacleGraphics -> obstacleGraphics.render(batch));
        batch.end();
    }

    @Override
    public void close() {
        batch.dispose();
        tiledMap.dispose();
    }
}
