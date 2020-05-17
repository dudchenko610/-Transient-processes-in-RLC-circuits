package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;
import com.crazydev.funnycircuits.electronic.math.exceptions.EmptyFunctionException;

public abstract class Expression {
    // I NEED TO USE POOL OF NODE_VALUES FOR COMPUTATIONAL INTENTIONS

    public static final char PLUS = '+';
    public static final char MINUS = '-';
    public static final char MULTIPLY = '*';
    public static final char DEVIDE = '/';
    public static final char POWER = '^';

    public enum ExpressionType {
        FUNCTION,
        VALUE,
        RESISTANCE_VAR,
        CAPACITY_VAR,
        INDUCTANCE_VAR,
        BRANCH_CURRENT,
        INDUCTANCE_VOLTAGE
    }

    public final ExpressionType type;

    protected Expression parent;
    protected boolean uMinus = false;
    protected int sign = 1;

    public Expression(Expression parent, boolean uMinus, ExpressionType type) {
        this.parent = parent;
        this.uMinus = uMinus;
        this.type = type;

        if (uMinus) {
            this.sign = -1;
        }
    }

    public abstract void render(String whiteSpace);
    public abstract double getValue(double x) throws ComputationException;

    public void setParent(Expression parent) {
        this.parent = parent;
    }

    public Expression getParent() {
        return parent;
    }

    public int getSign() {
        return this.sign;
    }

    public boolean isNegated() {
        return this.uMinus;
    }

    public void negate() {
        this.uMinus = !this.uMinus;
        this.sign *= -1;
    }





}
