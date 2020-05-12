package com.crazydev.funnycircuits.electronic.structuresbuilding;

import com.crazydev.funnycircuits.electronic.Graph;
import com.crazydev.funnycircuits.electronic.managers.LoggerManager;

import java.util.ArrayList;
import java.util.HashMap;

public class LogContainer {

    private static LogContainer logContainer;
    public static LogContainer getInstance() {
        if (logContainer == null) {
            logContainer = new LogContainer();
        }

        return logContainer;
    }

    protected HashMap<Graph, ArrayList<LoggerManager>> loggables;

    private LogContainer() {
        this.loggables = new HashMap<Graph, ArrayList<LoggerManager>>();

    }

    public void clear() {

    }

}
