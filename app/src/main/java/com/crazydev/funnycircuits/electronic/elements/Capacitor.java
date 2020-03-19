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
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;

public class Capacitor extends Wire implements ILoggable {


    public double capacity = 1.0;

    private ILoggerManager logManager = new LoggerManager() {

        @Override
        public void logVoltage(double current) {
            if (this.voltageLogState) {
             //   this.voltages.add(current * Resistor.this.resistance);
            }
        }

        @Override
        public void logCharge(double current) {
            if (this.chargeLogState) {
                // this.charges
            }
        }

        @Override
        public void logLinkage(double current) {

        }

    };

    public Capacitor(World world, Node nodeA, Node nodeB) {
        super(world, nodeA, nodeB);
        this.type = WireType.CAPACITOR;


    }

    @Override
    protected void setupDrawableManager() {

        this.drawableManager = new DrawableManager() {

            @Override
            public void setupSprites() {
                float x_base_offset = 0.7f;
                if (Capacitor.this.isHorizontal) {

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.1f, 1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.1f, 1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeB.location.x - x_base_offset, nodeB.location.y), x_base_offset * 2, 0.25f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x + x_base_offset * 2, nodeB.location.y), 0.13f, 1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeB.location.x - x_base_offset * 2, nodeB.location.y), 0.13f, 1f));


                } else {
                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.1f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.1f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 1f, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 1f, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + x_base_offset), 0.25f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - x_base_offset), 0.25f, x_base_offset * 2));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeA.location.y + 2 * x_base_offset), 1f, 0.13f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeA.location.x, nodeB.location.y - 2 * x_base_offset), 1f, 0.13f));

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
        this.logManager.logCharge(current);
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
