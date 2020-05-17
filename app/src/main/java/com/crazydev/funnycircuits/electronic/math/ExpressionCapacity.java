package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.elements.Capacitor;
import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;
import com.crazydev.funnycircuits.electronic.math.physic.Capacity;

public class ExpressionCapacity extends Expression {

    public Capacity capacity;

    public ExpressionCapacity(Expression parent, Capacity capacity, boolean uMinus) {
        super(parent, uMinus, ExpressionType.CAPACITY_VAR);

        this.capacity = capacity;
    }

    @Override
    public void render(String whiteSpace) {

    }

    @Override
    public double getValue(double x) throws ComputationException {
        return this.capacity.getParameter();
    }
}
