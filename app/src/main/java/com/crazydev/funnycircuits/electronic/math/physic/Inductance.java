package com.crazydev.funnycircuits.electronic.math.physic;

import com.crazydev.funnycircuits.electronic.elements.Inductor;

public class Inductance extends ComposedElement<Inductor>{

    @Override
    public void addSchemaElement(Inductor inductor) {
        this.shemaElements.add(inductor);

        this.totalParameter = 0;
        for (int i = 0; i < this.shemaElements.size(); i++) {
            inductor = this.shemaElements.get(i);

            this.totalParameter += inductor.inductance;
        }
    }


}
