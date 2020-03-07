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

    protected short takenPositions[];

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

        this.takenPositions = new short[this.currentsAmount];

        for (int i = 0; i < this.matrixF.length; i ++) {

            for (int j = 0; j < this.matrixF[i].length; j ++) {
                this.matrixF[i][j] = -1;
                this.matrixS[i][j] = -1;
            }
        }

        // Find currents with correct direction

        /** 1. Fill up part of matrix with currents**/

        for (int i = 0; i < this.nodesAmount - 1; i ++) {

            this.equations.add(new NodeEquationHolder(this, this.nodes.get(i), i));


        }


        /** 2. Fill part of matrix with voltages **/

        for (int i = 0; i < graph.circuits.size(); i ++) {

            this.equations.add(new CircuitEquationHolder(this, graph.circuits.get(i), i + this.nodesAmount - 1));

        }

        /** 3. Form matrixS from matrixF **/


        for (int i = 0; i < this.countersF.length; i ++) {
            this.matrixS[this.countersS[this.countersF[i] - 1] ++ ][this.countersF[i] - 1] = (short) i;
        }

        int indexF;
        int min;
        EquationHolder equationH;
        EquationHolder equationHC;

        for (int i = 0; i < this.matrixS.length; i ++) {
            for (int j = 0; j < this.countersS[i]; j ++) {
                indexF = this.matrixS[j][i];

                min = Integer.MAX_VALUE;
                equationHC = null;

                for (int k = 0; k < this.countersF[indexF]; k ++) {
                    // int equationNumber = this.matrixF[k][indexF];
                    equationH = this.equations.get(this.matrixF[k][indexF]);

                    if (equationH.price < min && !equationH.isSelected) {
                        min = equationH.price;
                        equationHC = equationH;
                    }
                }

                if (equationHC != null) {
                    equationHC.setPosition(indexF); // indexF
                } else {
                    Log.d("matrixx", "NULL");
                }

            }
        }


        for (EquationHolder equationHolder : this.equations) {
            equationHolder.show();
        }

        /** OUTPUT **/

        Log.d("matrixx", "_____________________________________");
        Log.d("matrixx", "______________matrixF________________");
        Log.d("matrixx", "_____________________________________");

        for (int u = 0; u < matrixF.length; u ++) {

            String line = "";

            for (int j = 0; j < this.currentsAmount; j ++) {
                line += matrixF[u][j] + "  ";
            }

            Log.d("matrixx", line);

        }

        Log.d("matrixx", "_____________________________________");
        Log.d("matrixx", "______________matrixS________________");
        Log.d("matrixx", "_____________________________________");

        for (int u = 0; u < matrixS.length; u ++) {

            String line = "";

            for (int j = 0; j < this.currentsAmount; j ++) {
                line += matrixS[u][j] + "  ";
            }

            Log.d("matrixx", line);

        }

        Log.d("matrixx", "_____________________________________");
        Log.d("matrixx", "______________matrix1________________");
        Log.d("matrixx", "_____________________________________");

        for (int u = 0; u < matrix1.length; u ++) {

            String line = "";

            for (int j = 0; j < this.currentsAmount; j ++) {
                line += matrix1[u][j] + "  ";
            }

            Log.d("matrixx", line);

        }

        Log.d("matrixx", "_____________________________________");

        String line = "";
        for (int u = 0; u < this.equations.size(); u ++) {
            line += this.equations.get(u).index + " ";
        }

        Log.d("matrixx", line);

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
