package com.crazydev.funnycircuits.electronic.oldmath;

import com.crazydev.funnycircuits.electronic.elements.Inductor;

public class Inductance extends Multiplier {

    Inductor inductor;

    @Override
    public String getLabel() {
        return inductor.label;
    }
}
