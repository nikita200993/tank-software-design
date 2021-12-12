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
import ru.mipt.bit.platformer.driver.UISettings;
import ru.mipt.bit.platformer.logic.CreationEvent;
import ru.mipt.bit.platformer.logic.DeathEvent;
import ru.mipt.bit.platformer.logic.GameEvent;
import ru.mipt.bit.platformer.logic.GameLogicListener;
import ru.mipt.bit.platformer.logic.GameObjectView;
import ru.mipt.bit.platformer.logic.Obstacle;
import ru.mipt.bit.platformer.logic.Tank;
import ru.mipt.bit.platformer.logic.shoot.Bullet;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Output adapter.
 */
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
    public void onEvent(GameEvent<? extends GameObjectView> gameEvent) {
        if (gameEvent instanceof CreationEvent) {
            onCreation((CreationEvent) gameEvent);
        } else if (gameEvent instanceof DeathEvent) {
            onDeath((DeathEvent) gameEvent);
        }
    }

    private void onCreation(CreationEvent creationEvent) {
        var source = creationEvent.source();
        if (source instanceof Tank) {
            gameObjectToRenderable.put(source, createTank((Tank) source));
        } else if (source instanceof Obstacle) {
            gameObjectToRenderable.put(source, createTree(source));
        } else if (source instanceof Bullet) {
            gameObjectToRenderable.put(source, createBullet(source));
        }
    }

    private void onDeath(DeathEvent deathEvent) {
        dead.add(deathEvent.source());
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
