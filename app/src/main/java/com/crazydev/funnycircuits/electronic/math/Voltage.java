package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.Wire;

public class Voltage extends Variable {

    Wire wire;

    public Voltage() {
        this.type = VariableType.VOLTAGE;
    }

    @Override
    public String getLabel() {

        if (wire.type != Wire.WireType.DC_SOURCE) {
            return "U(" + wire.label +")" + (this.isPureResistive ? "%" : "");
        }

       return wire.label;
    }


}
