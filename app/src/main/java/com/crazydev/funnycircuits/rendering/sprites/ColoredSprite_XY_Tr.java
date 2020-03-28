package com.crazydev.funnycircuits.rendering.sprites;

import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.VertexBatcher;

public class ColoredSprite_XY_Tr extends ColoredSprite {

    private float y_Tr_l = 0;
    private float y_Tr_r = 0;

    private float x_Tr_l = 0;
    private float x_Tr_r = 0;

    public ColoredSprite_XY_Tr(VertexBatcher vertexBatcher, Vector3D color1, Vector3D color2, Vector2D position, float width, float height, float y_Tr_l, float y_Tr_r) {
        super(vertexBatcher, color1, color2, position, width, height);

        this.y_Tr_l = y_Tr_l;
        this.y_Tr_r = y_Tr_r;

        this.setPosition(position);

    }

    public ColoredSprite_XY_Tr(VertexBatcher vertexBatcher, Vector3D color1, Vector3D color2, Vector2D position, float width, float height, float x_Tr_l, float x_Tr_r, boolean b) {
        super(vertexBatcher, color1, color2, position, width, height);

        this.x_Tr_l = x_Tr_l;
        this.x_Tr_r = x_Tr_r;

        this.setPosition(position);

    }

    @Override
    public void setPosition(Vector2D position) {
        float x1 = position.x - width / 2;
        float y1 = position.y - height / 2;
        float x2 = position.x + width / 2;
        float y2 = position.y + height / 2;

        int index = 0;

        vertices[index ++] = x1 + this.x_Tr_l;
        vertices[index ++] = y1 + this.y_Tr_l;
        vertices[index ++] = c0.x;
        vertices[index ++] = c0.y;
        vertices[index ++] = c0.z;
        vertices[index ++] = 1f;   // 0

        vertices[index ++] = x2 + this.x_Tr_l;
        vertices[index ++] = y1 + this.y_Tr_r;
        vertices[index ++] = c1.x;
        vertices[index ++] = c1.y;
        vertices[index ++] = c1.z;
        vertices[index ++] = 1f;   // 1

        vertices[index ++] = x2 + this.x_Tr_r;
        vertices[index ++] = y2 + this.y_Tr_r;
        vertices[index ++] = c2.x;
        vertices[index ++] = c2.y;
        vertices[index ++] = c2.z;
        vertices[index ++] = 1f;   // 2

        vertices[index ++] = x2 + this.x_Tr_r;
        vertices[index ++] = y2 + this.y_Tr_r;
        vertices[index ++] = c2.x;
        vertices[index ++] = c2.y;
        vertices[index ++] = c2.z;
        vertices[index ++] = 1f;   // 2

        vertices[index ++] = x1 + this.x_Tr_r;
        vertices[index ++] = y2 + this.y_Tr_l;
        vertices[index ++] = c3.x;
        vertices[index ++] = c3.y;
        vertices[index ++] = c3.z;
        vertices[index ++] = 1f;   // 3

        vertices[index ++] = x1 + this.x_Tr_l;
        vertices[index ++] = y1 + this.y_Tr_l;
        vertices[index ++] = c0.x;
        vertices[index ++] = c0.y;
        vertices[index ++] = c0.z;
        vertices[index ++] = 1f;   // 0
    }
}
