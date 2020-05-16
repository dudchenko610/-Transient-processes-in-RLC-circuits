package com.crazydev.funnycircuits.electronic.oldmath;

public abstract class Variable {

    public enum VariableType {
        CURRENT,
        VOLTAGE
    }

    boolean isDerivative = false;
    boolean isPureResistive = true;
    VariableType type;

    public abstract String getLabel();

}
