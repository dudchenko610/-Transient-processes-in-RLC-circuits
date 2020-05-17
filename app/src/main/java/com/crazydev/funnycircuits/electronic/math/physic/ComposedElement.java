package com.crazydev.funnycircuits.electronic.math.physic;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.Wire;

import java.util.ArrayList;

public abstract class ComposedElement<T extends Wire> extends ElectricElement {

    protected ArrayList<T> shemaElements = new ArrayList<T>();
    public Branch branch;

    public ComposedElement(Branch branch) {
        this.branch = branch;
    }

    public abstract void addSchemaElement(T t);

    public ArrayList<T> getSchemaElements() {
        return this.shemaElements;
    }
}
