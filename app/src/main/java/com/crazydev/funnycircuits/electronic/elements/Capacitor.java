package com.crazydev.funnycircuits.electronic.elements;

import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.ColoredSprite;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;
import com.crazydev.funnycircuits.rendering.Sprite;

public class Capacitor extends Wire {

    private Sprite spite1;
    private Sprite spite2;
    private Sprite spite3;
    private Sprite spite4;

    private Sprite wireSelection1;
    private Sprite wireSelection2;
    private Sprite wireSelection3;
    private Sprite wireSelection4;

    public double capacity = 1.0;

    public Capacitor(World world, Node nodeA, Node nodeB) {
        super(world, nodeA, nodeB);
        this.type = WireType.CAPACITOR;
    }

    private static float x_base_offset = 0.7f;

    @Override
    protected void setupWireSelectionSprite() {
        if (this.isHorizontal) {

            this.spite1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f);

            this.spite2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f);

            this.spite3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.1f, 1f);

            this.spite4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.1f, 1f);


            this.wireSelection1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f);

            this.wireSelection2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f);

            this.wireSelection3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.13f, 1f);

            this.wireSelection4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.13f, 1f);

        } else {

            this.spite1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.1f, x_base_offset * 2);

            this.spite2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.1f, x_base_offset * 2);

            this.spite3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 1f, 0.1f);

            this.spite4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 1f, 0.1f);

            this.wireSelection1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.25f, x_base_offset * 2);

            this.wireSelection2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.25f, x_base_offset * 2);

            this.wireSelection3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 1f, 0.13f);

            this.wireSelection4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 1f, 0.13f);
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
