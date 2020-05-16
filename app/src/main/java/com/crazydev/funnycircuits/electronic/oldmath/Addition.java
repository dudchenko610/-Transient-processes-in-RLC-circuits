package com.crazydev.funnycircuits.electronic.oldmath;

import java.util.ArrayList;
import java.util.List;

public class Addition {

    int sign = 1;
    List<Multiplier> multipliers;
    Variable variable;

    public Addition() {
        this.multipliers = new ArrayList<Multiplier>();
    }

    @Override
    public String toString() {

        String text = sign == 1 ? "+" : "-";

        Multiplier multiplier;
        for (int i = 0; i < this.multipliers.size(); i ++) {
            multiplier = this.multipliers.get(i);

            if (multiplier.isInverse) {
                text += "(" + multiplier.getLabel() + ")^-1 * ";
            } else {
                text += multiplier.getLabel() + " * ";
            }
        }

        if (variable == null) {
            text += "0";
        } else {
            text += variable.getLabel();
        }


        return text;
    }

}
