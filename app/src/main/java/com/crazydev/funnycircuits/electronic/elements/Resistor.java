package com.crazydev.funnycircuits.electronic.elements;

import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.ColoredSprite;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;
import com.crazydev.funnycircuits.rendering.Sprite;

public class Resistor extends Wire {

    private Sprite spite1;
    private Sprite spite2;
    private Sprite spite3;
    private Sprite spite4;
    private Sprite spite5;
    private Sprite spite6;

    private Sprite wireSelection1;
    private Sprite wireSelection2;
    private Sprite wireSelection3;
    private Sprite wireSelection4;
    private Sprite wireSelection5;
    private Sprite wireSelection6;

    private static float x_base_offset = 0.3f;
    private static float y_base_offset = 0.6f;

    public double resistance = 1.0;

    public Resistor(World world, Node nodeA, Node nodeB) {
        super(world, nodeA, nodeB);
        this.type = WireType.RESISTOR;
    }

    @Override
    protected void setupWireSelectionSprite() {
        if (this.isHorizontal) {

            this.spite1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f);

            this.spite2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f);

            this.spite3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.1f, y_base_offset);

            this.spite4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.1f, y_base_offset);

            this.spite5 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y + y_base_offset * 0.5f), 3 - 4 * x_base_offset + 0.1f, 0.1f);

            this.spite6 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y - y_base_offset * 0.5f), 3 - 4 * x_base_offset + 0.1f, 0.1f);

            this.wireSelection1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f);

            this.wireSelection2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f);

            this.wireSelection3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.13f, y_base_offset);

            this.wireSelection4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.13f, y_base_offset);

            this.wireSelection5 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y + y_base_offset * 0.5f), 3 - 4 * x_base_offset + 0.13f, 0.13f);

            this.wireSelection6 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y - y_base_offset * 0.5f), 3 - 4 * x_base_offset + 0.13f, 0.13f);

        } else {

            this.spite1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.1f, x_base_offset * 2);

            this.spite2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.1f, x_base_offset * 2);

            this.spite3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), y_base_offset, 0.1f);

            this.spite4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), y_base_offset, 0.1f);

            this.spite5 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + y_base_offset * 0.5f, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.1f, 3 - 4 * x_base_offset + 0.1f);

            this.spite6 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x - y_base_offset * 0.5f, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.1f, 3 - 4 * x_base_offset + 0.1f);

            this.wireSelection1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.25f, x_base_offset * 2);

            this.wireSelection2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.25f, x_base_offset * 2);

            this.wireSelection3 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), y_base_offset, 0.13f);

            this.wireSelection4 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), y_base_offset, 0.13f);

            this.wireSelection5 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + y_base_offset * 0.5f, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.13f, 3 - 4 * x_base_offset + 0.13f);

            this.wireSelection6 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x - y_base_offset * 0.5f, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.13f, 3 - 4 * x_base_offset + 0.13f);

        }
    }

    @Override
    public void draw() {
        this.spite1.draw();
        this.spite2.draw();
        this.spite3.draw();
        this.spite4.draw();
        this.spite5.draw();
        this.spite6.draw();

        if (this.isSelected) {
            this.wireSelection1.draw();
            this.wireSelection2.draw();
            this.wireSelection3.draw();
            this.wireSelection4.draw();
            this.wireSelection5.draw();
            this.wireSelection6.draw();
        }
    }

}
