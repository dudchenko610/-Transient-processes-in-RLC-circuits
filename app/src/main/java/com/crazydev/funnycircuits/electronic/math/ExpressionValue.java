package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;

public class ExpressionValue extends Expression {

    private double value;

    public ExpressionValue(Expression parent, double value, boolean uMinus) {
        super(parent, uMinus, ExpressionType.VALUE);
        this.value = value;

    }

    public void setValue(double value) {
        this.value  = value;
    }


    @Override
    public void render(String whiteSpace) {

    }

    @Override
    public double getValue(double x) throws ComputationException {
        return this.value * this.sign;
    }


}
