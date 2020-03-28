package com.crazydev.funnycircuits.rendering.sprites;

import com.crazydev.funnycircuits.math.Rectangle;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.VertexBatcher;

public class ItemColoredSprite extends ColoredSprite {

    public Rectangle rectangle;
    public float xDItem;
    public boolean visible = false;

    public ItemColoredSprite(VertexBatcher vertexBatcher, Vector3D color1, float alpha, Vector2D position, float width, float height) {
        super(vertexBatcher, color1, alpha, position, width, height);

        this.rectangle = new Rectangle();
    }



}
