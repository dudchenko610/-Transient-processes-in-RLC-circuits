package com.crazydev.funnycircuits.electronic.managers;

import com.crazydev.funnycircuits.electronic.Graph;
import com.crazydev.funnycircuits.electronic.interfaces.ILoggerManager;
import com.crazydev.funnycircuits.electronic.structuresbuilding.DoubleArrayStructure;
import com.crazydev.funnycircuits.electronic.structuresbuilding.LogContainer;

public abstract class LoggerManager implements ILoggerManager {

    protected boolean currentLogState;
    protected boolean voltageLogState;
    protected boolean chargeLogState;
    protected boolean linkageLogState;

    public DoubleArrayStructure currents;
    public DoubleArrayStructure voltages;
    public DoubleArrayStructure charges;
    public DoubleArrayStructure linkages;

    private LogContainer logContainer = LogContainer.getInstance();
    private Graph graph;

    public LoggerManager() {
        this.currents = new DoubleArrayStructure();
        this.voltages = new DoubleArrayStructure();
        this.charges  = new DoubleArrayStructure();
        this.linkages = new DoubleArrayStructure();

    }

    @Override
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void logCurrent(double current) {
        if (this.currentLogState) {
            this.currents.add(current);
        }
    }


    @Override
    public void setCurrentLogState(boolean state) {
        this.currentLogState = state;
    }

    @Override
    public void setVoltageLogState(boolean state) {
        this.voltageLogState = state;
    }

    @Override
    public void setChargeLogState(boolean state) {
        this.chargeLogState = state;
    }

    @Override
    public void setLinkageLogState(boolean state) {
        this.linkageLogState = state;
    }


    @Override
    public boolean getCurrentLogState() {
        return this.currentLogState;
    }

    @Override
    public boolean getVoltageLogState() {
        return this.voltageLogState;
    }

    @Override
    public boolean getChargeLogState() {
        return this.chargeLogState;
    }

    @Override
    public boolean getLinkageLogState() {
        return this.linkageLogState;
    }
}
