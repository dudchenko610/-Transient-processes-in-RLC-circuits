package com.crazydev.funnycircuits.electronic.structuresbuilding;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.Circuit;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.elements.DCSource;
import com.crazydev.funnycircuits.electronic.elements.Resistor;

import java.util.ArrayList;

public class CircuitEquationHolder {

    private Circuit circuit;
    private ArrayList<Branch> branches;
    private double[][] matrix;
    private int index;


    public CircuitEquationHolder(Calculator calculator, Circuit circuit, int index) {

        this.branches = calculator.branches;
        this.matrix   = calculator.matrix1;
        this.index    = index;
        this.circuit  = circuit;

        this.recalculate();

    }

    public void recalculate() {

        double eds = 0;
        DCSource source;

        for (Branch b : circuit.branches) {

            int signCB = b.directionalValues.get(circuit) == '+' ? 1 : -1;

            double resistance = 0;
            if (b.inCircuit) {
                for (Wire wire : b.wires) {
                    if (wire.type == Wire.WireType.RESISTOR) {
                        resistance += ((Resistor) wire).resistance * signCB;
                    }

                }

                for (Wire wire : b.wires) {

                    if (wire.type == Wire.WireType.DC_SOURCE) {
                        source = (DCSource) wire;

                        int signBW = wire.directionalValues.get(b) == '+' ? 1 : -1;
                        int signS  = source.signSource;

                        eds += source.voltage * signCB * signBW * signS;
                    }

                }
            }


            this.matrix[this.index][this.branches.indexOf(b)] = resistance;


        }

        this.matrix[this.index][this.matrix[0].length - 1] = eds;

    }



}
