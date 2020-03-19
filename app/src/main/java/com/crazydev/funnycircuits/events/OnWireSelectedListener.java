package com.crazydev.funnycircuits.events;

import com.crazydev.funnycircuits.electronic.Wire;

public interface OnWireSelectedListener {

    void onWireSelect(Wire wire);
    void onDeselect();

}
