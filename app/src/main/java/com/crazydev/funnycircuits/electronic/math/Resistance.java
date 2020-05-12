package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.elements.Resistor;

public class Resistance extends Multiplier {

    Resistor resistor;

    @Override
    public String getLabel() {
        return this.resistor.label;
    }

}
