package com.crazydev.funnycircuits.electronic.elements;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.ColoredSprite;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;
import com.crazydev.funnycircuits.rendering.Sprite;

public class DCSource extends Wire {

    private Sprite spite1;
    private Sprite spite2;
    private Sprite spite3;
    private Sprite spite4;

    private Sprite wireSelection1;
    private Sprite wireSelection2;
    private Sprite wireSelection3;
    private Sprite wireSelection4;

    public double innerResistance = 1.0;
    public double voltage         = 1.0;


    // because of initialization block runs after call to super() in constructor field can be reset
    public int    signSource;

    public DCSource(World world, Node nodeA, Node nodeB, int orientation) {
        super(world, nodeA, nodeB, orientation);
        this.type        = WireType.DC_SOURCE;
        this.isSource    = true;

    }

    private static float x_base_offset = 0.7f;

    @Override
    protected void setupWireSelectionSprite() {
        if (this.isHorizontal) {

            this.spite1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f);

            this.spite2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f);

            Log.d("orient", "orientation = " + this.orientation);

            switch (this.orientation) {
                case 0:
                    this.signSource = -1;

                    this.spite3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.1f, 1f);

                    this.spite4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.1f, 0.6f);


                    this.wireSelection3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.13f, 1f);

                    this.wireSelection4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.13f, 0.6f);
                    break;
                case 1:

                    this.signSource = +1;

                    this.spite3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.1f, 0.6f);

                    this.spite4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.1f, 1f);


                    this.wireSelection3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.13f, 0.6f);

                    this.wireSelection4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.13f, 1f);

                    break;
            }




            this.wireSelection1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f);

            this.wireSelection2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f);


        } else {

            this.spite1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.1f, x_base_offset * 2);

            this.spite2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.1f, x_base_offset * 2);

            switch (this.orientation) {
                case 2:
                    this.signSource = -1;

                    this.spite3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 1f, 0.1f);

                    this.spite4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 0.6f, 0.1f);

                    this.wireSelection3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 1f, 0.13f);

                    this.wireSelection4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 0.6f, 0.13f);

                    break;
                case 3:

                    this.signSource = +1;

                    this.spite3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 0.6f, 0.1f);

                    this.spite4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 1f, 0.1f);

                    this.wireSelection3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 0.6f, 0.13f);

                    this.wireSelection4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 1f, 0.13f);

                    break;
            }



            this.wireSelection1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.25f, x_base_offset * 2);

            this.wireSelection2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.25f, x_base_offset * 2);


        }
    }

    @Override
    public void draw() {
        this.spite1.draw();
        this.spite2.draw();
        this.spite3.draw();
        this.spite4.draw();

        if (this.isSelected) {
            this.wireSelection1.draw();
            this.wireSelection2.draw();
            this.wireSelection3.draw();
            this.wireSelection4.draw();
        }
    }

}
