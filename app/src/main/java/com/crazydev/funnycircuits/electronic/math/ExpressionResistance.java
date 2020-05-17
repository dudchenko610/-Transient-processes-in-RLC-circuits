package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;
import com.crazydev.funnycircuits.electronic.math.physic.ElectricElement;
import com.crazydev.funnycircuits.electronic.math.physic.Resistance;

public class ExpressionResistance extends Expression {

    private Resistance resistance;

    public ExpressionResistance(Expression parent, Resistance resistance, boolean uMinus) {
        super(parent, uMinus, ExpressionType.RESISTANCE_VAR);

        this.resistance = resistance;
    }

    @Override
    public void render(String whiteSpace) {

    }

    @Override
    public double getValue(double x) throws ComputationException {
        return this.resistance.getParameter();
    }
}
