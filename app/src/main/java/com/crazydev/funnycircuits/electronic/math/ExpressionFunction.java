package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;
import com.crazydev.funnycircuits.electronic.math.physic.ElectricElement;

import java.util.ArrayList;

public class ExpressionFunction extends Expression {

    private String description = "";

    private ArrayList<Expression> nodes;
    private ArrayList<Character> operations;


    private ArrayList<ExpressionValue> poolNodeValues;
    private ArrayList<Expression> nodesCopy;
    private ArrayList<Character> operationsCopy;

    private boolean wasChanged = true;
    private boolean containsMultipliersOnly = true;


    public ExpressionFunction(Expression parent, boolean uMinus) {
        super(parent, uMinus, ExpressionType.FUNCTION);
    }

    @Override
    public void render(String whiteSpace) {

    }

    @Override
    public double getValue(double x) throws ComputationException {
        nodesCopy.clear();
        nodesCopy.addAll(nodes);
        operationsCopy.clear();
        operationsCopy.addAll(operations);

        int i = 0;

        while (!operationsCopy.isEmpty()) {
            // find the most important sign
            int operation = getHighestOperation();

            double f1 = nodesCopy.get(operation + 0).getValue(x);
            double f2 = nodesCopy.get(operation + 1).getValue(x);

            double subRes;

            switch (operationsCopy.get(operation)) {
                case '+':
                    subRes = f1 + f2;
                    break;
                case '-':
                    subRes = f1 - f2;
                    break;
                case '*':
                    subRes = f1 * f2;
                    break;
                case '/':
                    subRes = f1 / f2;
                    break;
                case '^':
                    subRes = Math.pow(f1, f2);
                    break;
                default:
                    throw new ComputationException("Unknown operation !!!");
            }

            nodesCopy.remove(operation + 0);
            nodesCopy.remove(operation + 0);
            operationsCopy.remove(operation);

            if (wasChanged) {
                ExpressionValue nodeValue = new ExpressionValue(null, subRes, false);
                poolNodeValues.add(nodeValue);
                nodesCopy.add(operation, nodeValue);

            } else {
                ExpressionValue nodeValue = poolNodeValues.get(i);
                nodeValue.setValue(subRes);
                nodesCopy.add(operation, nodeValue);
            }

            i ++;
        }

        this.wasChanged = false;

        if (nodesCopy.size() != 1) {
            throw new ComputationException("EEEERRRRROOORRRR !!!");
        }

        // function-related computations
        double m        = this.isNegated() ? -1.0f : 1.0f;
        double argument = nodesCopy.get(0).getValue(x);

        switch(description) {
            case "":
                return m * argument;
            case "a":
                return m * Math.sin(argument);
            case "b":
                return m * Math.cos(argument);
            case "c":
            case "d":
                return m * Math.log(argument);
            case "e":
                return m * Math.log10(argument);
            case "f":
                return m * Math.sqrt(argument);
            case "g":
                return m * Math.abs(argument);
            case "h":
                return m * Math.tan(argument);
            case "i":
                return m * Math.acos(argument);
            case "j":
                return m * Math.asin(argument);
            case "k":
                return m * Math.atan(argument);
            case "l":
                return m * Math.ceil(argument);
            case "m":
                return m * Math.floor(argument);
            case "n":
                return m * Math.exp(argument);
            case "o":
                return 1.0 / Math.tan(argument) * m;
            case "p":
                return 1.0 / Math.atan(argument) * m;
            case "q":
                return 1.0 / Math.cos(argument) * m;
            case "r":
                return 1.0 / Math.sin(argument) * m;
            case "s":
                return m * Math.sinh(argument);
            case "u":
                return m * Math.cosh(argument);
            default:
                return Double.NaN;
        }
    }

    public Expression subtract(Expression expression) {

        this.operations.add(MINUS);
        this.nodes.add(expression);
        this.containsMultipliersOnly = false;

        return this;
    }

    public Expression add(Expression expression) {

        this.operations.add(PLUS);
        this.nodes.add(expression);
        this.containsMultipliersOnly = false;

        return this;
    }

    public Expression multiply(Expression e) {

        if (this.containsMultipliersOnly) {
            this.operations.add(MULTIPLY);
            this.nodes.add(e);

        } else {
            ExpressionFunction expressionFunction = this.replaceFunctionIntoNew();
            this.nodes.add(expressionFunction);
            this.operations.add(MULTIPLY);
            this.nodes.add(e);

        }

        return this;
    }

    public Expression devide(Expression e) {
        if (this.containsMultipliersOnly) {
            this.operations.add(DEVIDE);
            this.nodes.add(e);

        } else {
            ExpressionFunction expressionFunction = this.replaceFunctionIntoNew();
            this.nodes.add(expressionFunction);
            this.operations.add(DEVIDE);
            this.nodes.add(e);

        }

        return this;
    }

    private int getHighestOperation() throws ComputationException {

        int prioritet = -1;
        int index = -1;

        for (int i = 0; i < operationsCopy.size(); i ++) {
            char c = operationsCopy.get(i);
            int p = -1;

            switch(c) {
                case '+':
                case '-':
                    p = 1;
                    break;

                case '*':
                case '/':
                    p = 2;
                    break;

                case '^':
                    p = 3;
                    break;
                default:
                    throw new ComputationException("Unknown operation !!!");
            }

            if (p > prioritet) {
                prioritet = p;
                index = i;
            }
        }

        return index;

    }

    private ExpressionFunction replaceFunctionIntoNew() {
        ExpressionFunction expressionFunction = new ExpressionFunction(this, false);

        expressionFunction.nodes.addAll(this.nodes);
        expressionFunction.operations.addAll(this.operations);

        expressionFunction.description             = this.description;
        expressionFunction.containsMultipliersOnly = this.containsMultipliersOnly;
        expressionFunction.uMinus                  = this.uMinus;
        expressionFunction.sign                    = this.sign;

        // reset old function
        this.nodes.clear();
        this.operations.clear();
        this.description = "";
        this.containsMultipliersOnly = true;
        this.uMinus                  = false;
        this.sign                    = 1;

        return expressionFunction;
    }

    public static ExpressionFunction createExpressionWithValue(double value) {

        ExpressionFunction expressionFunction = new ExpressionFunction(null, false);
        expressionFunction.nodes.add(new ExpressionValue(expressionFunction, value, false));
        return expressionFunction;

    }




}
