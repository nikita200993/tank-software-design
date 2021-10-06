package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

public class TileMovement {

    private final TiledMapTileLayer tileLayer;
    private final Interpolation interpolation;

    public TileMovement(TiledMapTileLayer tileLayer, Interpolation interpolation) {
        this.tileLayer = tileLayer;
        this.interpolation = interpolation;
    }

    public Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle, GridPoint2 fromTileCoordinates, GridPoint2 toTileCoordinates, float progress) {
        moveRectangleAtTileCenter(rectangle, fromTileCoordinates);
        float fromTileBottomLeftX = rectangle.x;
        float fromTileBottomLeftY = rectangle.y;

        GdxGameUtils.moveRectangleAtTileCenter(tileLayer, rectangle, toTileCoordinates);
        float toTileBottomLeftX = rectangle.x;
        float toTileBottomLeftY = rectangle.y;

        float intermediateBottomLeftX = interpolation.apply(fromTileBottomLeftX, toTileBottomLeftX, progress);
        float intermediateBottomLeftY = interpolation.apply(fromTileBottomLeftY, toTileBottomLeftY, progress);

        return rectangle
                .setX(intermediateBottomLeftX)
                .setY(intermediateBottomLeftY);
    }

    public Rectangle moveRectangleAtTileCenter(Rectangle rectangle, GridPoint2 tileCoordinates) {
        return GdxGameUtils.moveRectangleAtTileCenter(tileLayer, rectangle, tileCoordinates);
    }
}
