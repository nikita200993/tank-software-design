package ru.mipt.bit.platformer.graphics;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import ru.mipt.bit.platformer.logic.GameLogicListener;
import ru.mipt.bit.platformer.logic.GameObjectView;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameGraphics implements GameLogicListener {

    private final TextureRegion tank;
    private final TextureRegion tree;
    private final TextureRegion bullet;
    private final Batch batch;
    private final MapRenderer mapRenderer;
    private final RectangleMovement rectangleMovement;
    private final Map<Object, Renderable> gameObjectToRenderable;
    private final List<GameObjectView> dead;

    public GameGraphics(
            final TextureRegion tank,
            final TextureRegion tree,
            final TextureRegion bullet,
            final Batch batch,
            final MapRenderer mapRenderer,
            final RectangleMovement rectangleMovement
    ) {
        this.tank = tank;
        this.tree = tree;
        this.bullet = bullet;
        this.batch = batch;
        this.mapRenderer = mapRenderer;
        this.rectangleMovement = rectangleMovement;
        this.gameObjectToRenderable = new IdentityHashMap<>();
        this.dead = new ArrayList<>();
    }


    public void render() {
        clearScreen();
        mapRenderer.render();
        batch.begin();
        gameObjectToRenderable.values().forEach(it -> it.render(batch));
        batch.end();
        removeDead();
        dead.clear();
    }

    @Override
    public void onRegister(
            final List<? extends GameObjectView> tanks,
            final List<? extends GameObjectView> trees,
            final List<? extends GameObjectView> bullets
    ) {
        tanks.forEach(it -> gameObjectToRenderable.put(it, createTank(it)));
        trees.forEach(it -> gameObjectToRenderable.put(it, createTree(it)));
        bullets.forEach(it -> gameObjectToRenderable.put(it, createBullet(it)));
    }

    @Override
    public void onTanksDeath(final List<? extends GameObjectView> tank) {
        dead.addAll(tank);
    }

    @Override
    public void onBulletsDeath(final List<? extends GameObjectView> bullets) {
        dead.addAll(bullets);
    }

    @Override
    public void onBulletCreated(final GameObjectView bullet) {
        gameObjectToRenderable.put(bullet, createBullet(bullet));
    }

    private void removeDead() {
        for (final var view : dead) {
            gameObjectToRenderable.remove(view);
        }
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

    private GraphicObject createBullet(final GameObjectView gameObjectView) {
        return new GraphicObject(
                bullet,
                rectangleMovement,
                gameObjectView
        );
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }
}
