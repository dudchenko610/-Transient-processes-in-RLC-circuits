package com.crazydev.funnycircuits.electronic.math.physic;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.elements.Capacitor;

public class Capacity extends ComposedElement<Capacitor> {


    public Capacity(Branch branch) {
        super(branch);
    }

    @Override
    public void addSchemaElement(Capacitor capacitor) {
        this.shemaElements.add(capacitor);

        this.totalParameter = 0;

        for (int i  = 0; i < this.shemaElements.size(); i ++) {
            this.totalParameter += 1.0d / this.shemaElements.get(i).capacity;
        }

        this.totalParameter = 1.0d / totalParameter;
    }
}
