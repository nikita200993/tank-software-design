package ru.mipt.bit.platformer.gdx.graphics;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.driver.UISettings;

/**
 * Adapter.
 */
public class HealthRenderer implements Renderable {
    private final UISettings uiSettings;
    private final ShapeRenderer shapeRenderer;
    private final Renderable renderable;
    private final Supplier<Integer> healthPercent;
    private final Rectangle rectangle;

    public HealthRenderer(
            UISettings uiSettings,
            ShapeRenderer shapeRenderer,
            Supplier<Integer> healthPercent,
            Renderable renderable,
            Rectangle rectangle
    ) {
        this.uiSettings = uiSettings;
        this.shapeRenderer = shapeRenderer;
        this.renderable = renderable;
        this.healthPercent = healthPercent;
        this.rectangle = rectangle;
    }

    @Override
    public void render() {
        renderable.render();
    }

    @Override
    public void renderShape() {
        renderable.renderShape();
        if (!uiSettings.isToggleHealth()) {
            return;
        }
        shapeRenderer.setColor(Color.RED);
        float healthBarWidth = rectangle.getWidth() * 0.25f;
        float healthBarLength = rectangle.getHeight() * 0.75f;
        shapeRenderer.rect(
                rectangle.getX() + healthBarWidth,
                rectangle.getY() + healthBarLength,
                50 * healthPercent.get() / 100.0f,
                10
        );
    }
}
