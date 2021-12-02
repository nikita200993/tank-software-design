package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.logic.GameObjectView;

import static ru.mipt.bit.platformer.graphics.GdxGameUtils.drawTextureRegionUnscaled;

public class GraphicObject implements Renderable {

    private final Batch batch;
    private final TextureRegion textureRegion;
    private final RectangleMovement rectangleMovement;
    private final GameObjectView view;
    private final Rectangle rectangle;

    public GraphicObject(
            Batch batch,
            TextureRegion textureRegion,
            RectangleMovement rectangleMovement,
            GameObjectView view
    ) {
        this.batch = batch;
        this.textureRegion = textureRegion;
        this.rectangleMovement = rectangleMovement;
        this.view = view;
        final var rectangle = new Rectangle();
        rectangle.setWidth(textureRegion.getRegionWidth());
        rectangle.setHeight(textureRegion.getRegionHeight());
        this.rectangle = rectangleMovement.moveRectangle(rectangle, view.position());
    }


    @Override
    public void render() {
        rectangleMovement.moveRectangle(rectangle, view.position());
        drawTextureRegionUnscaled(batch, textureRegion, rectangle, view.angle());
    }

    public GameObjectView getView() {
        return view;
    }

    Rectangle getRectangle() {
        return rectangle;
    }
}
