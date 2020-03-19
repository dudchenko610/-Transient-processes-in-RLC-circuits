package com.crazydev.funnycircuits.electronic.elements;

import com.crazydev.funnycircuits.electronic.Graph;
import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.electronic.interfaces.ILoggerManager;
import com.crazydev.funnycircuits.electronic.interfaces.ILoggable;
import com.crazydev.funnycircuits.electronic.managers.DrawableManager;
import com.crazydev.funnycircuits.electronic.managers.LoggerManager;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.ColoredSprite;
import com.crazydev.funnycircuits.rendering.ColoredSprite_XY_Tr;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;

public class Inductor extends Wire implements ILoggable {

    public double inductance = 1.0;

    private ILoggerManager logManager = new LoggerManager() {

        @Override
        public void logVoltage(double current) {
            if (this.voltageLogState) {
                //   this.voltages.add(current * Resistor.this.resistance);
            }
        }

        @Override
        public void logCharge(double current) {

        }

        @Override
        public void logLinkage(double current) {
            if (this.linkageLogState) {

            }
        }

    };

    public Inductor(World world, Node nodeA, Node nodeB, int orientation) {
        super(world, nodeA, nodeB, orientation);
        this.type = WireType.INDUCTOR;

    }

    @Override
    protected void setupDrawableManager() {

        this.drawableManager = new DrawableManager() {

            @Override
            public void setupSprites() {
                float x_base_offset = 0.3f;

                float lom_width = 3 - 4 * x_base_offset;
                float el_offset = lom_width / 16;
                float el_width  = lom_width / 8;

                float thickness = 0.10f;
                float thickness2 = 0.25f;

                float y_height_offset = 0.6f;

                switch (Inductor.this.orientation) {
                    case 1:
                    case 3:
                        y_height_offset *= -1;

                }

                if (Inductor.this.isHorizontal) {

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f));


                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + el_offset, nodeB.location.y), el_width, thickness, 0, y_height_offset));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 3 * el_offset, nodeB.location.y), el_width, thickness, y_height_offset, 0));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 5 * el_offset, nodeB.location.y), el_width, thickness, 0, y_height_offset));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 7 * el_offset, nodeB.location.y), el_width, thickness, y_height_offset, 0));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 9 * el_offset, nodeB.location.y), el_width, thickness, 0, y_height_offset));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 11 * el_offset, nodeB.location.y), el_width, thickness, y_height_offset, 0));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 13 * el_offset, nodeB.location.y), el_width, thickness, 0, y_height_offset));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 15 * el_offset, nodeB.location.y), el_width, thickness, y_height_offset, 0));



                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + el_offset, nodeB.location.y), el_width, thickness2, 0, y_height_offset));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher,new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 3 * el_offset, nodeB.location.y), el_width, thickness2, y_height_offset, 0));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 5 * el_offset, nodeB.location.y), el_width, thickness2, 0, y_height_offset));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 7 * el_offset, nodeB.location.y), el_width, thickness2, y_height_offset, 0));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 9 * el_offset, nodeB.location.y), el_width, thickness2, 0, y_height_offset));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 11 * el_offset, nodeB.location.y), el_width, thickness2, y_height_offset, 0));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 13 * el_offset, nodeB.location.y), el_width, thickness2, 0, y_height_offset));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + 2 * x_base_offset + 15 * el_offset, nodeB.location.y), el_width, thickness2, y_height_offset, 0));


                } else {
                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.1f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.1f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + el_offset), thickness, el_width, 0, y_height_offset, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 3 * el_offset), thickness, el_width, y_height_offset, 0, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 5 * el_offset), thickness, el_width, 0, y_height_offset, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 7 * el_offset), thickness, el_width, y_height_offset, 0, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 9 * el_offset), thickness, el_width, 0, y_height_offset, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 11 * el_offset), thickness, el_width, y_height_offset, 0, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 13 * el_offset), thickness, el_width, 0, y_height_offset, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 15 * el_offset), thickness, el_width, y_height_offset, 0, true));


                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.25f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.25f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + el_offset), thickness2, el_width, 0, y_height_offset, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 3 * el_offset), thickness2, el_width, y_height_offset, 0, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 5 * el_offset), thickness2, el_width, 0, y_height_offset, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 7 * el_offset), thickness2, el_width, y_height_offset, 0, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 9 * el_offset), thickness2, el_width, 0, y_height_offset, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 11 * el_offset), thickness2, el_width, y_height_offset, 0, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 13 * el_offset), thickness2, el_width, 0, y_height_offset, true));

                    this.sprites.add(new ColoredSprite_XY_Tr(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y  + 2 * x_base_offset + 15 * el_offset), thickness2, el_width, y_height_offset, 0, true));

                }

            }

        };

        this.drawableManager.setupSprites();


    }

    @Override
    public void logCurrent(double current) {
        this.logManager.logCurrent(current);
        this.logManager.logVoltage(current);
        this.logManager.logLinkage(current);
    }

    @Override
    public void setCurrentLogState(boolean state) {
        this.logManager.setCurrentLogState(state);
    }

    @Override
    public void setVoltageLogState(boolean state) {
        this.logManager.setVoltageLogState(state);
    }

    @Override
    public void setChargeLogState(boolean state) {
        this.logManager.setChargeLogState(state);
    }

    @Override
    public void setLinkageLogState(boolean state) {
        this.logManager.setLinkageLogState(state);
    }

    @Override
    public boolean getCurrentLogState() {
        return this.logManager.getCurrentLogState();
    }

    @Override
    public boolean getVoltageLogState() {
        return this.logManager.getVoltageLogState();
    }

    @Override
    public boolean getChargeLogState() {
        return this.logManager.getChargeLogState();
    }

    @Override
    public boolean getLinkageLogState() {
        return this.logManager.getLinkageLogState();
    }

    @Override
    public void setGraph(Graph graph) {
        this.logManager.setGraph(graph);
    }


}
