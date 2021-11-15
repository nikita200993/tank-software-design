package ru.mipt.bit.platformer.newgraphics;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import ru.mipt.bit.platformer.logic.GameLogicListener;
import ru.mipt.bit.platformer.logic.GameObjectView;

public class GameGraphics implements GameLogicListener {

    private final TextureRegion tank;
    private final TextureRegion tree;
    private final Batch batch;
    private final MapRenderer mapRenderer;
    private final RectangleMovement rectangleMovement;
    private final Map<Object, Renderable> gameObjectToRenderable;

    public GameGraphics(
            final TextureRegion tank,
            final TextureRegion tree,
            final Batch batch,
            final MapRenderer mapRenderer,
            final RectangleMovement rectangleMovement
    ) {
        this.tank = tank;
        this.tree = tree;
        this.batch = batch;
        this.mapRenderer = mapRenderer;
        this.rectangleMovement = rectangleMovement;
        this.gameObjectToRenderable = new IdentityHashMap<>();
    }


    public void render() {
        mapRenderer.render();
        batch.begin();
        gameObjectToRenderable.values().forEach(it -> it.render(batch));
        batch.end();
    }

    public void onRegister(final List<GameObjectView> tanks, final List<GameObjectView> trees) {
        tanks.forEach(it -> gameObjectToRenderable.put(it, createTank(it)));
        trees.forEach(it -> gameObjectToRenderable.put(it, createTree(it)));
    }

    private GraphicObject createTank(final GameObjectView gameObjectView) {
        return new GraphicObject(
                tank,
                rectangleMovement,
                gameObjectView
        );
    }

    private GraphicObject createTree(final GameObjectView gameObjectView) {
        return new GraphicObject(
                tree,
                rectangleMovement,
                gameObjectView
        );
    }
}
