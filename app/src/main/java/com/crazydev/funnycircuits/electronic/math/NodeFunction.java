package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.math.exceptions.ComputationException;

import java.util.ArrayList;

public class NodeFunction extends Node {

    private String description = "";

    private ArrayList<Node> nodes;
    private ArrayList<Character> operations;

    private ArrayList<Node> nodesCopy;
    private ArrayList<Character> operationsCopy;

    private ArrayList<NodeValue> poolNodeValues;

    private boolean wasChanged = true;


    public NodeFunction(Node parent, boolean uMinus) {
        super(parent, uMinus);
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
                NodeValue nodeValue = new NodeValue(null, subRes, false);
                poolNodeValues.add(nodeValue);
                nodesCopy.add(operation, nodeValue);

            } else {
                NodeValue nodeValue = poolNodeValues.get(i);
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


}
