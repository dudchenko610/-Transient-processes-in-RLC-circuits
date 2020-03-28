package com.crazydev.funnycircuits.rendering.sprites;

import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.VertexBatcher;
import com.crazydev.funnycircuits.rendering.sprites.Sprite;

public class ColoredSprite extends Sprite {

    protected Vector3D c0;
    protected Vector3D c1;
    protected Vector3D c2;
    protected Vector3D c3;

    protected float alpha = 1.0f;

    public ColoredSprite(VertexBatcher vertexBatcher, Vector3D color1, Vector3D color2, Vector2D position, float width, float height) {
        super(vertexBatcher, position, width, height);

        this.vertices = new float[36];

        if (width > height) {
            this.c0 = color1;
            this.c1 = color2;
            this.c2 = color2;
            this.c3 = color1;
        } else {
            this.c0 = color2;
            this.c1 = color1;
            this.c2 = color1;
            this.c3 = color2;
        }

        setPosition(position);

    }

    public ColoredSprite(VertexBatcher vertexBatcher, Vector3D color, float alpha, Vector2D position, float width, float height) {
        super(vertexBatcher, position, width, height);

        this.vertices = new float[36];

        if (width > height) {
            this.c0 = color;
            this.c1 = color;
            this.c2 = color;
            this.c3 = color;
        } else {
            this.c0 = color;
            this.c1 = color;
            this.c2 = color;
            this.c3 = color;
        }

        setPosition(position);

        if (alpha >= 0 && alpha <= 1) {
            this.alpha = alpha;
        }

    }

    @Override
    public void setPosition(Vector2D position) {
        float x1 = position.x - width / 2;
        float y1 = position.y - height / 2;
        float x2 = position.x + width / 2;
        float y2 = position.y + height / 2;

        int index = 0;

        vertices[index ++] = x1;
        vertices[index ++] = y1;
        vertices[index ++] = c0.x;
        vertices[index ++] = c0.y;
        vertices[index ++] = c0.z;
        vertices[index ++] = this.alpha;   // 0

        vertices[index ++] = x2;
        vertices[index ++] = y1;
        vertices[index ++] = c1.x;
        vertices[index ++] = c1.y;
        vertices[index ++] = c1.z;
        vertices[index ++] = this.alpha;   // 1

        vertices[index ++] = x2;
        vertices[index ++] = y2;
        vertices[index ++] = c2.x;
        vertices[index ++] = c2.y;
        vertices[index ++] = c2.z;
        vertices[index ++] = this.alpha;   // 2

        vertices[index ++] = x2;
        vertices[index ++] = y2;
        vertices[index ++] = c2.x;
        vertices[index ++] = c2.y;
        vertices[index ++] = c2.z;
        vertices[index ++] = this.alpha;   // 2

        vertices[index ++] = x1;
        vertices[index ++] = y2;
        vertices[index ++] = c3.x;
        vertices[index ++] = c3.y;
        vertices[index ++] = c3.z;
        vertices[index ++] = this.alpha;   // 3

        vertices[index ++] = x1;
        vertices[index ++] = y1;
        vertices[index ++] = c0.x;
        vertices[index ++] = c0.y;
        vertices[index ++] = c0.z;
        vertices[index ++] = this.alpha;   // 0
    }

    @Override
    public void draw() {
        this.vertexBatcher.addSpriteVerticesToVertexBatcherColored(vertices);
    }

}
