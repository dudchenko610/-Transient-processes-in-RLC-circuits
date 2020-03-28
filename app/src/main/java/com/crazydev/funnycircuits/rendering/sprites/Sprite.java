package com.crazydev.funnycircuits.rendering.sprites;


import com.crazydev.funnycircuits.math.Rectangle;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.rendering.VertexBatcher;

public abstract class Sprite {

    protected VertexBatcher vertexBatcher;

    protected Vector2D position = new Vector2D();
    protected float width, height;

    protected float[] vertices;

    public Sprite (VertexBatcher vertexBatcher, Vector2D position, float width, float height) {
        this.vertexBatcher = vertexBatcher;

        this.position.set(position);
        this.width         = width;
        this.height        = height;

    }

    public void setPosition(float x, float y, float width, float height) {
        this.width  = width;
        this.height = height;
        this.position.set(x, y);

        this.setPosition(position);
    }

    public void setPosition(Vector2D position, float width, float height) {
        this.width  = width;
        this.height = height;

        this.setPosition(position);
    }

    public void setPosition(Rectangle rectangle) {

        this.width  = rectangle.corners[1].x - rectangle.corners[0].x;
        this.height = rectangle.corners[3].y - rectangle.corners[0].y;

        this.position.set((rectangle.corners[0].x + rectangle.corners[1].x) * 0.5f,
                (rectangle.corners[0].y + rectangle.corners[3].y) * 0.5f);

        this.setPosition(this.position);

    }

    public abstract void setPosition(Vector2D position);

    public abstract void draw();

}
