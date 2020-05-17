package com.crazydev.funnycircuits.electronic.math;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.Circuit;
import com.crazydev.funnycircuits.electronic.Graph;
import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.elements.Capacitor;
import com.crazydev.funnycircuits.electronic.elements.Inductor;
import com.crazydev.funnycircuits.electronic.elements.Resistor;
import com.crazydev.funnycircuits.electronic.math.physic.Capacity;
import com.crazydev.funnycircuits.electronic.math.physic.Inductance;
import com.crazydev.funnycircuits.electronic.math.physic.Resistance;
import com.crazydev.funnycircuits.electronic.oldmath.Equation;

import java.util.ArrayList;

public class Matrix {

    private static final String T = "matrixB";

    private Graph graph;
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ArrayList<Branch> branches = new ArrayList<Branch>();
    private ArrayList<Circuit> circuits = new ArrayList<Circuit>();

    private ArrayList<Variable> variables = new ArrayList<Variable>();
    private ExpressionFunction [][] matrix;

    int equationNumber = 0;

    public Matrix (Graph graph) {
        this.graph = graph;

        this.circuits = graph.circuits;

        // скопировать в список вершины графа
        this.nodes.addAll(graph.realNodes);

        // скопировать в список ветви
        for (Branch branch : graph.realBranches) {
            if (branch.inCircuit) {
                this.branches.add(branch);
            }
        }

        summarizeElements();
        findMainVariables();
    //    fillSystemMatrixWithKirchhoffRules();
        render();

    }

    // знайти індекс вітки
    private int getBranchIndexInMatrix(Branch branch) {
        return this.variables.indexOf(branch);
    }

    // сплюсовать все сопротивления, емкости и индуктивности в ветвях
    public void summarizeElements() {
        Branch branch;
        for (int i = 0; i < branches.size(); i ++) {
            branch = branches.get(i);
            branch.expressionCurrent = new ExpressionCurrent(null, branch, false);

            for (Wire wire : branch.wires) {
                if (wire.type == Wire.WireType.RESISTOR) {
                    if (branch.resistance == null) {
                        branch.resistance = new Resistance(branch);
                    }

                    branch.resistance.addSchemaElement((Resistor) wire);

                } else if (wire.type == Wire.WireType.CAPACITOR) {
                    if (branch.capacity == null) {
                        branch.capacity = new Capacity(branch);
                    }

                    branch.capacity.addSchemaElement((Capacitor) wire);

                } else if (wire.type == Wire.WireType.INDUCTOR) {
                    if (branch.inductance == null) {
                        branch.inductance = new Inductance(branch);
                        branch.expressionVoltage = new ExpressionVoltage(null, branch, false);

                    }

                    branch.inductance.addSchemaElement((Inductor) wire);
                }

            }


        }
    }

    // знайти головні змінні, відносно яких потрібно буде вирішувати систему
    public void findMainVariables() {

        Branch branch;
        Variable variable;
        for (int i = 0; i < this.branches.size(); i ++) {
            branch = this.branches.get(i);
            variable = new Variable();
            variable.branch = branch;

            if (branch.inductance == null) {
                this.variables.add(variable);
            } else {
                if (branch.capacity != null) {
                    this.variables.add(variable);
                }

                variable = new Variable();
                variable.branch = branch;
                variable.inductance = branch.inductance;

                this.variables.add(variable);

            }
        }

    }

    // заполнить матрицу системы по правилам Кирхгофа
    public void fillSystemMatrixWithKirchhoffRules() {

        this.matrix = new ExpressionFunction[this.variables.size()][this.variables.size() + 1];

        Node node;
        Branch branch;



        /** 1. Fill up part of matrix with currents **/

        for (int i = 0; i < this.nodes.size() - 1; i ++) {
            node = this.nodes.get(i);

            // last zero element, in b column
            this.matrix[equationNumber][this.matrix.length] =  ExpressionFunction.createExpressionWithValue(0);

            for (int j = 0; j < this.branches.size(); j ++) {
                branch = this.branches.get(j);

                int index = getBranchIndexInMatrix(branch);

                if (branch.nodeA.equals(node)) {
                    // save as '-'

                    // це струм у вітці, що має тільки катушку (можливо з спротивом), НЕ ВХОДИТЬ ДО ЗМІННИХ СИСТЕМИ
                    if (index == -1) {
                        this.matrix[equationNumber][this.matrix.length].add(branch.expressionCurrent);
                    } else {
                        this.matrix[equationNumber][index] = ExpressionFunction.createExpressionWithValue(-1);
                    }

                } else if (branch.nodeB.equals(node)) {
                    // save as '+'

                    if (index == -1) {
                        this.matrix[equationNumber][this.matrix.length].subtract(branch.expressionCurrent);
                    } else {
                        this.matrix[equationNumber][index] = ExpressionFunction.createExpressionWithValue(+1);
                    }

                } else {
                    //    throw new RuntimeException("Pizdets");
                }

            }

            equationNumber ++;

        }

        /** 2. Fill part of matrix with voltages **/


        Circuit circuit;
        for (int i = 0; i < this.circuits.size(); i ++) {
            circuit = this.circuits.get(i);

            for (Branch b : circuit.branches) {

                int signCB = b.directionalValues.get(circuit) == '+' ? 1 : -1;

                if (b.resistance != null) {
              //      this.matrix[equationNumber][getBranchIndex()]
                }

                if (b.capacity != null) {

                }

                if (b.inductance != null) {

                }


         /*       for (Wire wire : b.wires) {

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


            }*/

          /*  if (equation.right.isEmpty()) {
                Addition zeroAddition = new Addition();
                equation.right.add(zeroAddition);
            }

            this.equations.add(equation);*/

        //        equationNumber++;

                //    this.matrix[this.index][this.matrix[0].length - 1] = eds;

            }
        }

    }

    // отобразить матрицу
    public void render() {
        String s = "";

        Log.i(T, "...");
        Log.i(T, "VARIABLES RENDERING");
        Log.i(T, "...");

        Variable variable;
        for (int i = 0; i < this.variables.size(); i ++) {
            variable = this.variables.get(i);

            if (variable.inductance == null) {
                s += "I(" + this.branches.indexOf(variable.branch) + ")    ";
            } else {
                s += "U(" + this.branches.indexOf(variable.branch) + ")    ";
            }

        }

        Log.i(T, s);

        Log.i(T, "...");
        Log.i(T, "MATRIX RENDERING");
        Log.i(T, "...");

        for (int i = 0; i < equationNumber; i ++) {
            for (int j = 0; j < this.variables.size() + 1; j ++) {

            }
        }
    }




    public void underlineBranch(int n) {

        Branch branch = this.branches.get(n);

        for (Wire wire : branch.wires) {
            wire.select();
        }

    }
}
