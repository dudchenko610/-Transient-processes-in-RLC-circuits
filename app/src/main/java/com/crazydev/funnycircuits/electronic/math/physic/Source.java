package com.crazydev.funnycircuits.electronic.math.physic;

import com.crazydev.funnycircuits.electronic.elements.DCSource;

public class Source extends ElectricElement {

    private DCSource source;

    public Source(DCSource source) {
        this.source = source;
    }

    @Override
    public double getParameter() {
        return this.source.voltage;
    }
}
