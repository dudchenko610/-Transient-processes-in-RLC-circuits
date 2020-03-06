package com.crazydev.funnycircuits.events;

import com.crazydev.funnycircuits.electronic.Wire;

public interface OnWireSelected {

    void onSelect(Wire wire);
    void onDeselect();

}
