package com.crazydev.funnycircuits.electronic.elements;

import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.ColoredSprite;
import com.crazydev.funnycircuits.rendering.ColoredSprite_XY_Tr;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;
import com.crazydev.funnycircuits.rendering.Sprite;

public class Inductor extends Wire {

    private Sprite spite1;
    private Sprite spite2;
    private Sprite spite3_y;
    private Sprite spite4_y;
    private Sprite spite5_y;
    private Sprite spite6_y;
    private Sprite spite7_y;
    private Sprite spite8_y;
    private Sprite spite9_y;
    private Sprite spite10_y;

    private Sprite wireSelection1;
    private Sprite wireSelection2;
    private Sprite wireSelection3;
    private Sprite wireSelection4;
    private Sprite wireSelection5;
    private Sprite wireSelection6;
    private Sprite wireSelection7;
    private Sprite wireSelection8;
    private Sprite wireSelection9;
    private Sprite wireSelection10;

    private static float x_base_offset = 0.3f;

    public double inductance = 1.0;

    public Inductor(World world, Node nodeA, Node nodeB, int orientation) {
        super(world, nodeA, nodeB, orientation);
        this.type = WireType.INDUCTOR;
    }

    @Override
    protected void setupWireSelectionSprite() {

        float lom_width = 3 - 4 * x_base_offset;
        float el_offset = lom_width / 16;
        float el_width  = lom_width / 8;

        float thickness = 0.10f;
        float thickness2 = 0.25f;

        float y_height_offset = 0.6f;

        switch (this.orientation) {
            case 1:
            case 3:
                y_height_offset *= -1;

        }

        if (this.isHorizontal) {

            this.spite1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f);

            this.spite2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f);


            this.spite3_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + el_offset, nodeB.location.y), el_width, thickness, 0, y_height_offset);

            this.spite4_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 3 * el_offset, nodeB.location.y), el_width, thickness, y_height_offset, 0);

            this.spite5_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 5 * el_offset, nodeB.location.y), el_width, thickness, 0, y_height_offset);

            this.spite6_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 7 * el_offset, nodeB.location.y), el_width, thickness, y_height_offset, 0);

            this.spite7_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 9 * el_offset, nodeB.location.y), el_width, thickness, 0, y_height_offset);

            this.spite8_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 11 * el_offset, nodeB.location.y), el_width, thickness, y_height_offset, 0);

            this.spite9_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 13 * el_offset, nodeB.location.y), el_width, thickness, 0, y_height_offset);

            this.spite10_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 15 * el_offset, nodeB.location.y), el_width, thickness, y_height_offset, 0);



            this.wireSelection1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f);

            this.wireSelection2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f);

            this.wireSelection3 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + el_offset, nodeB.location.y), el_width, thickness2, 0, y_height_offset);

            this.wireSelection4 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER,new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 3 * el_offset, nodeB.location.y), el_width, thickness2, y_height_offset, 0);

            this.wireSelection5 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 5 * el_offset, nodeB.location.y), el_width, thickness2, 0, y_height_offset);

            this.wireSelection6 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 7 * el_offset, nodeB.location.y), el_width, thickness2, y_height_offset, 0);

            this.wireSelection7 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 9 * el_offset, nodeB.location.y), el_width, thickness2, 0, y_height_offset);

            this.wireSelection8 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 11 * el_offset, nodeB.location.y), el_width, thickness2, y_height_offset, 0);

            this.wireSelection9 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 13 * el_offset, nodeB.location.y), el_width, thickness2, 0, y_height_offset);

            this.wireSelection10 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x + 2 * x_base_offset + 15 * el_offset, nodeB.location.y), el_width, thickness2, y_height_offset, 0);


        } else {

            this.spite1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.1f, x_base_offset * 2);

            this.spite2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.1f, x_base_offset * 2);

            this.spite3_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + el_offset), thickness, el_width, 0, y_height_offset, true);

            this.spite4_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 3 * el_offset), thickness, el_width, y_height_offset, 0, true);

            this.spite5_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 5 * el_offset), thickness, el_width, 0, y_height_offset, true);

            this.spite6_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 7 * el_offset), thickness, el_width, y_height_offset, 0, true);

            this.spite7_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 9 * el_offset), thickness, el_width, 0, y_height_offset, true);

            this.spite8_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 11 * el_offset), thickness, el_width, y_height_offset, 0, true);

            this.spite9_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 13 * el_offset), thickness, el_width, 0, y_height_offset, true);

            this.spite10_y = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 15 * el_offset), thickness, el_width, y_height_offset, 0, true);



            this.wireSelection1 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.25f, x_base_offset * 2);

            this.wireSelection2 = new ColoredSprite(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.25f, x_base_offset * 2);

            this.wireSelection3 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + el_offset), thickness2, el_width, 0, y_height_offset, true);

            this.wireSelection4 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 3 * el_offset), thickness2, el_width, y_height_offset, 0, true);

            this.wireSelection5 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 5 * el_offset), thickness2, el_width, 0, y_height_offset, true);

            this.wireSelection6 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 7 * el_offset), thickness2, el_width, y_height_offset, 0, true);

            this.wireSelection7 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 9 * el_offset), thickness2, el_width, 0, y_height_offset, true);

            this.wireSelection8 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 11 * el_offset), thickness2, el_width, y_height_offset, 0, true);

            this.wireSelection9 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 13 * el_offset), thickness2, el_width, 0, y_height_offset, true);

            this.wireSelection10 = new ColoredSprite_XY_Tr(OpenGLRenderer.VERTEX_BATCHER, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                    new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 15 * el_offset), thickness2, el_width, y_height_offset, 0, true);

        }
    }

    @Override
    public void draw() {
        this.spite1.draw();
        this.spite2.draw();
        this.spite3_y.draw();
        this.spite4_y.draw();
        this.spite5_y.draw();
        this.spite6_y.draw();
        this.spite7_y.draw();
        this.spite8_y.draw();
        this.spite9_y.draw();
        this.spite10_y.draw();

        if (this.isSelected) {
            this.wireSelection1.draw();
            this.wireSelection2.draw();
            this.wireSelection3.draw();
            this.wireSelection4.draw();
            this.wireSelection5.draw();
            this.wireSelection6.draw();
            this.wireSelection7.draw();
            this.wireSelection8.draw();
            this.wireSelection9.draw();
            this.wireSelection10.draw();

        }
    }

}
