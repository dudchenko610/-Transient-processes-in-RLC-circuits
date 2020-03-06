package com.crazydev.funnycircuits.rendering;


import com.crazydev.funnycircuits.math.Vector2D;

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

    public abstract void setPosition(Vector2D position);

    public abstract void draw();

}
