package com.crazydev.funnycircuits.electronic.elements;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.electronic.managers.DrawableManager;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.ColoredSprite;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;
import com.crazydev.funnycircuits.rendering.Sprite;

public class DCSource extends Wire {

    public double innerResistance = 1.0;
    public double voltage         = 1.0;

    // because of initialization block runs after call to super() in constructor field can be reset
    public int    signSource;


    public DCSource(World world, Node nodeA, Node nodeB, int orientation) {
        super(world, nodeA, nodeB, orientation);
        this.type        = WireType.DC_SOURCE;
        this.isSource    = true;

        switch (orientation) {
            case 0:
            case 2:
                this.signSource = -1;
                break;
            case 1:
            case 3:
                this.signSource = +1;
                break;
        }

    }


    @Override
    protected void setupDrawableManager() {

        this.drawableManager = new DrawableManager() {


            @Override
            public void setupSprites() {
                float x_base_offset = 0.7f;

                if (DCSource.this.isHorizontal) {

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f));


                    switch (DCSource.this.orientation) {
                        case 0:

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                                    new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.1f, 1f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                                    new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.1f, 0.6f));


                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                                    new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.13f, 1f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                                    new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.13f, 0.6f));

                            break;
                        case 1:

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                                    new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.1f, 0.6f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                                    new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.1f, 1f));


                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                                    new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.13f, 0.6f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                                    new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.13f, 1f));

                            break;
                    }



                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f));


                } else {
                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.1f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.1f, x_base_offset * 2));

                    switch (DCSource.this.orientation) {
                        case 2:
                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                                    new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 1f, 0.1f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                                    new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 0.6f, 0.1f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                                    new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 1f, 0.13f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                                    new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 0.6f, 0.13f));


                            break;
                        case 3:

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                                    new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 0.6f, 0.1f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                                    new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 1f, 0.1f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                                    new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 0.6f, 0.13f));

                            this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                                    new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 1f, 0.13f));

                            break;
                    }


                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.25f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.25f, x_base_offset * 2));

                }

            }
        };

        this.drawableManager.setupSprites();


    }


}
