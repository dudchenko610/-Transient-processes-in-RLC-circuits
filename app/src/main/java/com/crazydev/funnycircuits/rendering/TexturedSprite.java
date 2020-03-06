package com.crazydev.funnycircuits.rendering;

import com.crazydev.funnycircuits.math.Vector2D;

public class TexturedSprite extends Sprite {

    protected TextureRegion textureRegion;

    public TexturedSprite(VertexBatcher vertexBatcher, TextureRegion textureRegion, Vector2D position, float width, float height) {
        super(vertexBatcher, position, width, height);

        this.textureRegion = textureRegion;
        this.vertices = new float[24];

        setPosition(position);

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
        vertices[index ++] = textureRegion.u1;
        vertices[index ++] = textureRegion.v2;  // 0

        vertices[index ++] = x2;
        vertices[index ++] = y1;
        vertices[index ++] = textureRegion.u2;
        vertices[index ++] = textureRegion.v2;  // 1

        vertices[index ++] = x2;
        vertices[index ++] = y2;
        vertices[index ++] = textureRegion.u2;
        vertices[index ++] = textureRegion.v1; // 2

        vertices[index ++] = x2;
        vertices[index ++] = y2;
        vertices[index ++] = textureRegion.u2;
        vertices[index ++] = textureRegion.v1; // 2

        vertices[index ++] = x1;
        vertices[index ++] = y2;
        vertices[index ++] = textureRegion.u1;
        vertices[index ++] = textureRegion.v1; // 3

        vertices[index ++] = x1;
        vertices[index ++] = y1;
        vertices[index ++] = textureRegion.u1;
        vertices[index ++] = textureRegion.v2;  // 0
    }

    @Override
    public void draw() {
        this.vertexBatcher.addSpriteVerticesToVertexBatcherTextured(vertices);
    }
}
