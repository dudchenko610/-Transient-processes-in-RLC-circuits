package com.crazydev.funnycircuits.electronic.oldmath;

import com.crazydev.funnycircuits.electronic.elements.Capacitor;

public class Capacity extends Multiplier {

    Capacitor capacitor;

    @Override
    public String getLabel() {
        return capacitor.label;
    }
}
