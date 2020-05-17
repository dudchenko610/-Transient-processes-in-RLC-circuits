package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;

public class ExpressionVoltage extends Expression {

    public Branch branch;

    public ExpressionVoltage(Expression parent, Branch branch, boolean uMinus) {
        super(parent, uMinus, ExpressionType.INDUCTANCE_VOLTAGE);
        this.branch = branch;
    }

    @Override
    public void render(String whiteSpace) {

    }

    @Override
    public double getValue(double x) throws ComputationException {
        return 0;
    }
}
