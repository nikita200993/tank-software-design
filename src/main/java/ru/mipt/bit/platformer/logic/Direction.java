package ru.mipt.bit.platformer.logic;

public enum Direction {
    UP {
        @Override
        float getRotation() {
            return 90;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX(), position.getY() + 1);
        }
    },
    DOWN {
        @Override
        float getRotation() {
            return -90;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX(), position.getY() - 1);
        }
    },
    LEFT {
        @Override
        float getRotation() {
            return -180;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX() - 1, position.getY());
        }
    },
    RIGHT {
        @Override
        float getRotation() {
            return 0;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX() + 1, position.getY());
        }
    };

    abstract float getRotation();

    abstract Point2D computeNextPosition(Point2D position);
}
