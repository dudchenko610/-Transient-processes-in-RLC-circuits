package com.crazydev.funnycircuits.electronic.oldmath;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.Wire;

public class Current extends Variable {

    Branch branch;

    public Current() {
        this.type = VariableType.CURRENT;
    }

    @Override
    public boolean equals(Object o) {
        Current current = (Current) o;

        if (this.branch.equals(current.branch)) {
            return true;
        }
        return false;
    }

    @Override
    public String getLabel() {

        if (branch == null) {
            return "0";
        }

        String label = "I(";

        for (Wire wire : branch.wires) {
            label += wire.label;
        }
        label += ")";

        if (this.isPureResistive) {
            label += "%";
        }

        return label;
    }
}
