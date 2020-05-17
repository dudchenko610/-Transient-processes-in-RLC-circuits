package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;

import static com.crazydev.funnycircuits.electronic.math.Expression.ExpressionType.BRANCH_CURRENT;

public class ExpressionCurrent extends Expression {

    public Branch branch;

    public ExpressionCurrent(Expression parent, Branch branch, boolean uMinus) {
        super(parent, uMinus, BRANCH_CURRENT);

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
