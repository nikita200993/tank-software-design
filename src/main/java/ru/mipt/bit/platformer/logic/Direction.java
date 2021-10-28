package ru.mipt.bit.platformer.logic;

public enum Direction {
    UP {
        @Override
        float getAngle() {
            return 90;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX(), position.getY() + 1);
        }
    },
    DOWN {
        @Override
        float getAngle() {
            return -90;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX(), position.getY() - 1);
        }
    },
    LEFT {
        @Override
        float getAngle() {
            return -180;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX() - 1, position.getY());
        }
    },
    RIGHT {
        @Override
        float getAngle() {
            return 0;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX() + 1, position.getY());
        }
    };

    abstract float getAngle();

    abstract Point2D computeNextPosition(Point2D position);
}
