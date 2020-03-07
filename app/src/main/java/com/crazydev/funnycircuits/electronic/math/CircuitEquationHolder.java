package com.crazydev.funnycircuits.electronic.math;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.Circuit;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.elements.DCSource;
import com.crazydev.funnycircuits.electronic.elements.Resistor;

import java.util.ArrayList;

public class CircuitEquationHolder extends EquationHolder {

    private Circuit circuit;

    public CircuitEquationHolder(Calculator calculator, Circuit circuit, int indexInArrayList) {
        super(calculator, indexInArrayList);

        this.circuit = circuit;
        double eds = 0;
        DCSource source;
        int j;

        for (Branch b : circuit.branches) {

            int signCB = b.directionalValues.get(circuit) == '+' ? 1 : -1;

            double resistance = 0;
            if (b.inCircuit) {
                for (Resistor resistor : b.resistors) {
                    resistance += resistor.resistance * signCB;
                }

                for (Wire wire : b.sources) {
                    source = (DCSource) wire;

                    int signBW = wire.directionalValues.get(b) == '+' ? 1 : -1;
                    int signS  = source.signSource;

                    eds += source.voltage * signCB * signBW * signS;

                }


            }

            if (resistance != 0.0) {
                j = this.branches.indexOf(b);
                this.row[j] = resistance;

                this.matrixF[this.countersF[j] ++ ][j] = (short) this.indexInArrayList;
                this.price ++;
            }


        }

        this.row[this.row.length - 1] = eds;

    }


    @Override
    public void recalculate() {

        double eds = 0;
        DCSource source;

        for (Branch b : this.circuit.branches) {

            int signCB = b.directionalValues.get(circuit) == '+' ? 1 : -1;
            double resistance = 0;

            if (b.inCircuit) {
                for (Resistor resistor : b.resistors) {
                    resistance += resistor.resistance * signCB;
                }

                for (Wire wire : b.sources) {
                    source = (DCSource) wire;

                    int signBW = wire.directionalValues.get(b) == '+' ? 1 : -1;
                    int signS  = source.signSource;

                    eds += source.voltage * signCB * signBW * signS;

                }

            }

            this.matrix[this.index][this.branches.indexOf(b)] = resistance;

        }

        this.matrix[this.index][this.row.length - 1] = eds;

    }

    @Override
    public void setPosition(int index) {
        if (!this.isSelected) {

            this.index      = index;
            this.isSelected = true;

            this.recalculate();

        }

    }


}
