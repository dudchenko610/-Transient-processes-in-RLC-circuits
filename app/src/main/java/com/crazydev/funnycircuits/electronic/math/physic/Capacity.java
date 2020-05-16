package com.crazydev.funnycircuits.electronic.math.physic;

import com.crazydev.funnycircuits.electronic.elements.Capacitor;

public class Capacity extends ComposedElement<Capacitor> {


    @Override
    public void addSchemaElement(Capacitor capacitor) {
        this.shemaElements.add(capacitor);

        this.totalParameter = 0;

        for (int i  = 0; i < this.shemaElements.size(); i ++) {
            capacitor = this.shemaElements.get(i);

            this.totalParameter += 1.0d / capacitor.capacity;
        }

        this.totalParameter = 1.0d / totalParameter;
    }
}
