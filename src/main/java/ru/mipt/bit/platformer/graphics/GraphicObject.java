package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.logic.GameObjectView;

import static ru.mipt.bit.platformer.graphics.GdxGameUtils.drawTextureRegionUnscaled;

public class GraphicObject implements Renderable {

    private final TextureRegion textureRegion;
    private final RectangleMovement rectangleMovement;
    private final GameObjectView view;
    private final Rectangle rectangle;

    public GraphicObject(
            final TextureRegion textureRegion,
            final RectangleMovement rectangleMovement,
            final GameObjectView view
    ) {
        this.textureRegion = textureRegion;
        this.rectangleMovement = rectangleMovement;
        this.view = view;
        final var rectangle = new Rectangle();
        rectangle.setWidth(textureRegion.getRegionWidth());
        rectangle.setHeight(textureRegion.getRegionHeight());
        this.rectangle = rectangleMovement.moveRectangle(rectangle, view.position());
    }


    @Override
    public void render(final Batch batch) {
        rectangleMovement.moveRectangle(rectangle, view.position());
        drawTextureRegionUnscaled(batch, textureRegion, rectangle, view.angle());
    }
}
