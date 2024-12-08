package aoc.utils;

import java.util.Objects;

public class Vector2DInt {

    public int x;
    public int y;

    public Vector2DInt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2DInt add(Vector2DInt v){
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2DInt scalarMultiply(int i){
        this.x *= i;
        this.y *= i;
        return this;
    }

    public Vector2DInt subtract(Vector2DInt v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Vector2DInt vector2D = (Vector2DInt) object;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public Vector2DInt clone() {
        return new Vector2DInt(this.x, this.y);
    }

    @Override
    public String toString() {
        return this.x + "  " + this.y;
    }
}
