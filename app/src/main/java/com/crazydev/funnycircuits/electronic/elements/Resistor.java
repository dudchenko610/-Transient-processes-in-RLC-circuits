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
import com.crazydev.funnycircuits.rendering.sprites.ColoredSprite;

public class Resistor extends Wire implements ILoggable {


    private static float x_base_offset = 0.3f;
    private static float y_base_offset = 0.6f;

    public double resistance = 1.0;

    private ILoggerManager logManager = new LoggerManager() {

        @Override
        public void logVoltage(double current) {
            if (this.voltageLogState) {
                this.voltages.add(current * Resistor.this.resistance);
            }
        }

        @Override
        public void logCharge(double current) {

        }

        @Override
        public void logLinkage(double current) {

        }

    };

    public Resistor(World world, Node nodeA, Node nodeB) {
        super(world, nodeA, nodeB);
        this.type = WireType.RESISTOR;
        this.label = "R";

    }

    @Override
    protected void setupDrawableManager() {


        this.drawableManager = new DrawableManager() {

            @Override
            public void setupSprites() {
                if (Resistor.this.isHorizontal) {

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.1f, y_base_offset));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.1f, y_base_offset));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y + y_base_offset * 0.5f), 3 - 4 * x_base_offset + 0.1f, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y - y_base_offset * 0.5f), 3 - 4 * x_base_offset + 0.1f, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.13f, y_base_offset));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.13f, y_base_offset));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y + y_base_offset * 0.5f), 3 - 4 * x_base_offset + 0.13f, 0.13f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y - y_base_offset * 0.5f), 3 - 4 * x_base_offset + 0.13f, 0.13f));


                } else {
                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.1f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.1f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), y_base_offset, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), y_base_offset, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + y_base_offset * 0.5f, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.1f, 3 - 4 * x_base_offset + 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x - y_base_offset * 0.5f, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.1f, 3 - 4 * x_base_offset + 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.25f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.25f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), y_base_offset, 0.13f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), y_base_offset, 0.13f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + y_base_offset * 0.5f, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.13f, 3 - 4 * x_base_offset + 0.13f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x - y_base_offset * 0.5f, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.13f, 3 - 4 * x_base_offset + 0.13f));


                }
            }

        };

        this.drawableManager.setupSprites();


    }


    @Override
    public void setGraph(Graph graph) {
        this.logManager.setGraph(graph);
    }

    @Override
    public void logCurrent(double current) {
        this.logManager.logCurrent(current);
        this.logManager.logVoltage(current);
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

}
