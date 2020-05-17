package com.crazydev.funnycircuits.electronic.math.physic;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.elements.Inductor;

public class Inductance extends ComposedElement<Inductor>{

    public Inductance(Branch branch) {
        super(branch);
    }

    @Override
    public void addSchemaElement(Inductor inductor) {
        this.shemaElements.add(inductor);

        this.totalParameter = 0;
        for (int i = 0; i < this.shemaElements.size(); i++) {
            this.totalParameter += this.shemaElements.get(i).inductance;
        }

    }


}
