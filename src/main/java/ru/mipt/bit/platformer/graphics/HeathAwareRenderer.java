package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.logic.HealthAware;

public class HeathAwareRenderer implements Renderable {
    private final Renderable renderable;
    private final HealthAware healthAware;

    public HeathAwareRenderer(HealthAware healthAware, Renderable renderable) {
        this.renderable = renderable;
        this.healthAware = healthAware;
    }

    @Override
    public void render(Batch batch) {
        // TODO: add health bar rendering
        renderable.render(batch);
    }

    Renderable unwrap() {
        return renderable;
    }
}
