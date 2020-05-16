package com.crazydev.funnycircuits.electronic.oldmath;

import com.crazydev.funnycircuits.electronic.elements.Resistor;

public class Resistance extends Multiplier {

    Resistor resistor;

    @Override
    public String getLabel() {
        return this.resistor.label;
    }

}
