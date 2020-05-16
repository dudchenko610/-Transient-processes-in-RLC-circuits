package com.crazydev.funnycircuits.electronic.math.physic;

import com.crazydev.funnycircuits.electronic.Wire;

import java.util.ArrayList;

public abstract class ComposedElement<T extends Wire> extends Element {

    protected ArrayList<T> shemaElements = new ArrayList<T>();

    public abstract void addSchemaElement(T t);

    public ArrayList<T> getSchemaElements() {
        return this.shemaElements;
    }
}
