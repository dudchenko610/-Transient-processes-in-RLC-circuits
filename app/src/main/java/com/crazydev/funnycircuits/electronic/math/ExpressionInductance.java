package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;
import com.crazydev.funnycircuits.electronic.math.physic.Inductance;

public class ExpressionInductance extends Expression{

    public Inductance inductance;

    public ExpressionInductance(Expression parent, Inductance inductance, boolean uMinus) {
        super(parent, uMinus, ExpressionType.INDUCTANCE_VAR);

        this.inductance = inductance;
    }

    @Override
    public void render(String whiteSpace) {

    }

    @Override
    public double getValue(double x) throws ComputationException {
        return this.inductance.getParameter();
    }
}
