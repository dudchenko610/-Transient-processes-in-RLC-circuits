package com.crazydev.funnycircuits.electronic.interfaces;

import com.crazydev.funnycircuits.electronic.Graph;

public interface ILoggable {

    void setGraph(Graph graph);

    void logCurrent(double current);

    void setCurrentLogState(boolean state);
    void setVoltageLogState(boolean state);
    void setChargeLogState(boolean state);
    void setLinkageLogState(boolean state);

    boolean getCurrentLogState();
    boolean getVoltageLogState();
    boolean getChargeLogState();
    boolean getLinkageLogState();

}
