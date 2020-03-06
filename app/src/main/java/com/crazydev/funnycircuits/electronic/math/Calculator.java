package com.crazydev.funnycircuits.electronic.math;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.Circuit;
import com.crazydev.funnycircuits.electronic.Graph;
import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.elements.DCSource;
import com.crazydev.funnycircuits.electronic.elements.Resistor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Calculator {

    private Branch[]   currents;


    protected double [][] matrix1;
    private double [][] matrix2;

    private ArrayList<Node>   nodes;
    protected ArrayList<Branch> branches;

    protected int nodesAmount;
    protected int independentCircuitsAmount;
    protected int currentsAmount;

    private ArrayList<EquationHolder> equations;

    protected short[] countersF;
    protected short[][] matrixF;

    protected short countersS[];
    protected short matrixS[][];

    public Calculator(Graph graph) {
        this.nodes     = new ArrayList<Node>();
        this.branches  = new ArrayList<Branch>();
        this.equations = new ArrayList<EquationHolder>();

        this.nodes.addAll(graph.realNodes);
        this.branches.addAll(graph.realBranches);

        this.nodesAmount               = this.nodes.size();
        this.currentsAmount            = this.branches.size();
        this.independentCircuitsAmount = graph.circuits.size();


        this.matrix1 = new double[this.currentsAmount][this.currentsAmount + 1];
        this.matrix2 = new double[this.currentsAmount][this.currentsAmount + 1];

        this.countersF = new short[this.currentsAmount];
        this.matrixF   = new short[this.currentsAmount][this.currentsAmount];

        this.countersS = new short[this.currentsAmount];
        this.matrixS   = new short[this.currentsAmount][this.currentsAmount];

        // Find currents with correct direction

        /** 1. Fill up part of matrix with currents**/

        for (int i = 0; i < this.nodesAmount - 1; i ++) {

            this.equations.add(new NodeEquationHolder(this, this.nodes.get(i), i));

       /**     for (int j = 0; j < this.branches.size(); j ++) {
                branch = this.branches.get(j);

                if (branch.inCircuit) {
                    if (branch.nodeA.equals(node)) {
                        // save as '-'
                        this.upperMatrixReferencePart[i][j] = -1;

                    } else if (branch.nodeB.equals(node)) {
                        // save as '+'
                        this.upperMatrixReferencePart[i][j] = +1;

                    } else {
                    //    throw new RuntimeException("Pizdets");
                    }

                }
            }**/

        }


        /** 2. Fill part of matrix with voltages **/

        for (int i = 0; i < graph.circuits.size(); i ++) {

            this.equations.add(new CircuitEquationHolder(this, graph.circuits.get(i), i));

        /**    double eds = 0;

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

                        Log.d("signS" ,"signS = " + signS + " voltage = " + source.voltage);

                        eds += source.voltage * signCB * signBW * signS;

                    }


                }

                this.lowerMatrixReferencePart[i][this.branches.indexOf(b)] = resistance;

            }

            this.lowerMatrixReferencePart[i][this.currentsAmount]      = eds;**/
        }

        /** 3. Sort equations **/
            /** A. Fill up matrixF from equations ArrayList // it is ready!!! **/


        /** OUTPUT **/

        Log.d("matrixx", "_____________________________________");
        Log.d("matrixx", "_____________________________________");
        Log.d("matrixx", "_____________________________________");

        for (int u = 0; u < matrix1.length; u ++) {

            String line = "";

            for (int j = 0; j < this.currentsAmount + 1; j ++) {
                line += matrix1[u][j] + "  ";
                //       System.out.print(resultMatrix[u][j] + "\t");
            }

            Log.d("matrixx", line);

        }

        Log.d("matrixx", "_____________________________________");
    //    this.calculate();


    }

    // too long execution
    public void calculate() {

        // Fill matrices

        // Jordan-Gauss method matrix resolution

        double[][] inputMatrix  = matrix1;
        double[][] resultMatrix = matrix2;

        double[][] third;

        int size = inputMatrix.length;
        int len  = inputMatrix[0].length;


        for (int i = 0; i < size; i ++) {

            // 1. Fill next matrix with i-copied-row and 0-1 columns

            for (int j = 0; j < size; j ++) {
                for (int k = 0; k <= i; k ++) {
                    if (j == k) {
                        resultMatrix[j][k] = 1;
                    } else {
                        resultMatrix[j][k] = 0;
                    }

                }
            }


            double ii = inputMatrix[i][i];
            for (int j = i; j < len; j ++) {
                inputMatrix[i][j] = inputMatrix[i][j] / ii;
            }


            for (int j = i + 1; j < len; j ++) {
                resultMatrix[i][j] = inputMatrix[i][j];
            }


            for (int j = 0; j < size; j ++) {
                if (j != i) {
                    for (int k = i + 1; k < len; k ++) {
                        resultMatrix[j][k] = inputMatrix[i][i] * inputMatrix[j][k] - inputMatrix[j][i] * inputMatrix[i][k];

                    }
                }
            }


        //    System.out.println();

            for (int u = 0; u < size; u ++) {

                for (int j = 0; j < len; j ++) {
             //       System.out.print(resultMatrix[u][j] + "\t");
                }

            }

            // 2. Swap matrices

            third        = inputMatrix;
            inputMatrix  = resultMatrix;
            resultMatrix = third;

        }


        for (int u = 0; u < inputMatrix.length; u ++) {

            String line = "";

            for (int j = 0; j < this.currentsAmount + 1; j ++) {
                line += inputMatrix[u][j] + "  ";
                //       System.out.print(resultMatrix[u][j] + "\t");
            }

            Log.d("matrixx", line);

        }

        Log.d("matrixx", "_____________________________________");


    }


}
