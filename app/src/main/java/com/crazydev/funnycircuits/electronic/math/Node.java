package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;

public abstract class Node {
    // I NEED TO USE POOL OF NODE_VALUES FOR COMPUTATIONAL INTENTIONS

    protected Node parent;
    protected boolean uMinus = false;
    protected int sign = 1;

    public Node(Node parent, boolean uMinus) {
        this.parent = parent;
        this.uMinus = uMinus;

        if (uMinus) {
            this.sign = -1;
        }
    }

    public abstract void render(String whiteSpace);
    public abstract double getValue(double x) throws ComputationException;


    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public int getSign() {
        return this.sign;
    }

    public boolean isNegated() {
        return this.uMinus;
    }




}
