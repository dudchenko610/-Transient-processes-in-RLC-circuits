package com.crazydev.funnycircuits.electronic.oldmath;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.Circuit;
import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.elements.Capacitor;
import com.crazydev.funnycircuits.electronic.elements.DCSource;
import com.crazydev.funnycircuits.electronic.elements.Inductor;
import com.crazydev.funnycircuits.electronic.elements.Resistor;
import com.crazydev.funnycircuits.electronic.structuresbuilding.Calculator;

import java.util.ArrayList;

public class DifferentialSystemSolver {

    public ArrayList<Equation> equations;
    private Calculator calculator;


    public DifferentialSystemSolver(Calculator calculator) {
        this.calculator = calculator;
        this.equations = new ArrayList<Equation>();

        findActiveElementsInBranches();
        buildSystemWithKirchhoffRules();

        render();

    //    excludeVoltagesAndCurrentsOfResistiveElements();

    }

    private void findActiveElementsInBranches() {
        // Определить наличие активных элементов в ветвях
        for (int i = 0; i < calculator.branches.size(); i ++) {
            calculator.branches.get(i).analyze();
        }
    }

    private void buildSystemWithKirchhoffRules() {

        // Правила Кирхгофа
        /** 1. Fill up part of matrix with currents **/

        Node node;
        for (int i = 0; i < calculator.nodesAmount - 1; i ++) {
            node = calculator.nodes.get(i);

            Branch branch;
            Equation equation = new Equation();

            for (int j = 0; j < calculator.branches.size(); j ++) {
                branch = calculator.branches.get(j);

                if (branch.inCircuit) {
                    if (branch.nodeA.equals(node)) {
                        // save as '-'

                        Addition addition = new Addition();
                        Current variable = new Current();
                        variable.branch = branch;

                        if (branch.containsActiveElements) {
                            equation.isMainEquation  = true;
                            variable.isPureResistive = false;
                        }

                        addition.variable = variable;
                        addition.sign = -1;

                        equation.left.add(addition);


                    } else if (branch.nodeB.equals(node)) {
                        // save as '+'
                        //  calculator.matrix1[i][j] = +1;

                        Addition addition = new Addition();
                        Current variable = new Current();
                        variable.branch = branch;

                        if (branch.containsActiveElements) {
                            equation.isMainEquation  = true;
                            variable.isPureResistive = false;
                        }

                        addition.variable = variable;
                        addition.sign = +1;

                        equation.left.add(addition);

                    } else {
                        //    throw new RuntimeException("Pizdets");
                    }

                }
            }

            Addition zeroAddition = new Addition();
            equation.right.add(zeroAddition);

            this.equations.add(equation);

        }

        /** 2. Fill part of matrix with voltages **/


        Circuit circuit;
        for (int i = 0; i < calculator.graph.circuits.size(); i ++) {

            //    this.equations.add(new CircuitEquationHolder(this, graph.circuits.get(i), i + this.nodesAmount - 1));

            circuit = calculator.graph.circuits.get(i);

            Equation equation = new Equation();

            for (Branch b : circuit.branches) {

                int signCB = b.directionalValues.get(circuit) == '+' ? 1 : -1;

                if (b.inCircuit) {
                    for (Wire wire : b.wires) {

                        if (wire.type == Wire.WireType.RESISTOR) {
                            Resistor resistor = ((Resistor) wire);

                            Addition addition = new Addition();
                            addition.sign = signCB;

                            // variable
                            Current current = new Current();
                            current.branch = b;

                            if (b.containsActiveElements) {
                                equation.isMainEquation  = true;
                                current.isPureResistive = false;
                            }

                            // multiplier
                            Resistance resistance = new Resistance();
                            resistance.resistor = resistor;

                            addition.variable = current;
                            addition.multipliers.add(resistance);

                            equation.left.add(addition);

                        }

                        if (wire.type == Wire.WireType.CAPACITOR) {
                            Capacitor capacitor = ((Capacitor) wire);

                            Addition addition = new Addition();
                            addition.sign = signCB;

                            // variable
                            Voltage voltage = new Voltage();
                            voltage.wire = capacitor;

                            if (b.containsActiveElements) {
                                equation.isMainEquation  = true;
                                voltage.isPureResistive = false;
                            }

                            // multiplier - none

                            addition.variable = voltage;
                            equation.left.add(addition);

                        }

                        if (wire.type == Wire.WireType.INDUCTOR) {
                            Inductor inductor = ((Inductor) wire);

                            Addition addition = new Addition();
                            addition.sign = signCB;

                            // variable
                            Voltage voltage = new Voltage();
                            voltage.wire = inductor;

                            if (b.containsActiveElements) {
                                equation.isMainEquation  = true;
                                voltage.isPureResistive = false;
                            }

                            // multiplier - none

                            addition.variable = voltage;

                            equation.left.add(addition);

                        }

                        if (wire.type == Wire.WireType.DC_SOURCE) {
                            DCSource source = (DCSource) wire;

                            int signBW = wire.directionalValues.get(b) == '+' ? 1 : -1;
                            int signS  = source.signSource;


                            Addition addition = new Addition();
                            addition.sign = signCB * signBW * signS;

                            // variable
                            Voltage voltage = new Voltage();
                            voltage.wire = source;

                            if (b.containsActiveElements) {
                                equation.isMainEquation  = true;
                                voltage.isPureResistive = false;
                            }

                            // multiplier - none

                            addition.variable = voltage;

                            equation.right.add(addition);

                        }

                    }


                }


                //       this.matrix[this.index][this.branches.indexOf(b)] = resistance;

            }

            if (equation.right.isEmpty()) {
                Addition zeroAddition = new Addition();
                equation.right.add(zeroAddition);
            }

            this.equations.add(equation);

            //    this.matrix[this.index][this.matrix[0].length - 1] = eds;

        }
    }

    private void excludeVoltagesAndCurrentsOfResistiveElements() {

        Equation equation;
        Addition addition;
        Variable pureResistiveVariable;

        l:for (int i = 0; i < this.equations.size(); i ++) {
            equation = this.equations.get(i);

            if (equation.isMainEquation) {
                for (int j = 0; j < equation.left.size(); j ++) {
                    addition = equation.left.get(j);

                    if (addition.variable.type == Variable.VariableType.CURRENT && addition.variable.isPureResistive) {
                        // find the same in non-main
                        pureResistiveVariable = addition.variable;

                        Equation eq;
                        for (int k = 0; k < this.equations.size(); k ++) {
                            eq = this.equations.get(k);

                            if (!eq.isMainEquation && eq.contains(pureResistiveVariable)) {
                                eq.expressVariable(pureResistiveVariable);
                                render();
                                continue l;
                            }
                        }

                    }

                }
            }



        }
    }

    public void render() {

        Equation equation;
        for (int i = 0; i < this.equations.size(); i ++) {
            equation = this.equations.get(i);
            Log.i("diffsystem", equation.toString());
        //    Log.i("diffsystem", "ᵃ ᵇ ᶜ ᵈ ᵉ");
        }

        Log.i("diffsystem", "");
        Log.i("diffsystem", "___________________________________________");
        Log.i("diffsystem", "");

    }

}
