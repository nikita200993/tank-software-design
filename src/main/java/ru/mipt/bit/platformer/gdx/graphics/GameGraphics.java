package ru.mipt.bit.platformer.gdx.graphics;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import ru.mipt.bit.platformer.logic.GameLogicListener;
import ru.mipt.bit.platformer.logic.GameObjectView;
import ru.mipt.bit.platformer.logic.Tank;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameGraphics implements GameLogicListener {

    private final TextureRegion tankTexture;
    private final TextureRegion treeTexture;
    private final TextureRegion bulletTexture;
    private final UISettings uiSettings;
    private final Batch batch;
    private final ShapeRenderer shapeRenderer;
    private final MapRenderer mapRenderer;
    private final RectangleMovement rectangleMovement;
    private final Map<Object, Renderable> gameObjectToRenderable;
    private final List<GameObjectView> dead;

    public GameGraphics(
            TextureRegion tankTexture,
            TextureRegion treeTexture,
            TextureRegion bulletTexture,
            UISettings uiSettings,
            Batch batch,
            ShapeRenderer shapeRenderer,
            MapRenderer mapRenderer,
            RectangleMovement rectangleMovement
    ) {
        this.tankTexture = tankTexture;
        this.treeTexture = treeTexture;
        this.bulletTexture = bulletTexture;
        this.uiSettings = uiSettings;
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.mapRenderer = mapRenderer;
        this.rectangleMovement = rectangleMovement;
        this.gameObjectToRenderable = new IdentityHashMap<>();
        this.dead = new ArrayList<>();
    }


    public void render() {
        clearScreen();
        mapRenderer.render();
        batch.begin();
        gameObjectToRenderable.values().forEach(Renderable::render);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        gameObjectToRenderable.values().forEach(Renderable::renderShape);
        shapeRenderer.end();
        removeDead();
        dead.clear();
    }

    @Override
    public void onRegister(
            final List<? extends Tank> tanks,
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

    public UISettings getUiSettings() {
        return uiSettings;
    }

    private void removeDead() {
        for (final var view : dead) {
            gameObjectToRenderable.remove(view);
        }
    }

    private Renderable createTank(final Tank tank) {
        var tankGraphics = new GraphicObject(
                batch,
                tankTexture,
                rectangleMovement,
                tank
        );
        return new HealthRenderer(
                uiSettings,
                shapeRenderer,
                tank::getHealthPercent,
                tankGraphics,
                tankGraphics.getRectangle()
        );
    }

    private Renderable createTree(final GameObjectView gameObjectView) {
        return new GraphicObject(
                batch,
                treeTexture,
                rectangleMovement,
                gameObjectView
        );
    }

    private GraphicObject createBullet(final GameObjectView gameObjectView) {
        return new GraphicObject(
                batch,
                bulletTexture,
                rectangleMovement,
                gameObjectView
        );
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }
}
