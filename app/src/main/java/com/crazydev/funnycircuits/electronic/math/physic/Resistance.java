package com.crazydev.funnycircuits.electronic.math.physic;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.elements.Resistor;

public class Resistance extends ComposedElement<Resistor> {

    public

    public Resistance(Branch branch) {
        super(branch);
    }

    @Override
    public void addSchemaElement(Resistor resistor) {

        this.shemaElements.add(resistor);

        this.totalParameter = 0;
        for (int i = 0; i < this.shemaElements.size(); i ++) {
            this.totalParameter += this.shemaElements.get(i).resistance;
        }
    }
}
