package com.crazydev.funnycircuits.electronic.structuresbuilding;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.Graph;
import com.crazydev.funnycircuits.electronic.Node;
import com.crazydev.funnycircuits.electronic.math.DifferentialSystemSolver;

import java.util.ArrayList;

public class Calculator {

    public Graph graph;

    protected double [][] matrix1;

    public ArrayList<Node>   nodes;
    public ArrayList<Branch> branches;

    public int nodesAmount;
    public int independentCircuitsAmount;
    public int currentsAmount;

    public ArrayList<CircuitEquationHolder> equations;

    public Calculator(Graph graph) {
        this.graph = graph;

        this.nodes     = new ArrayList<Node>();
        this.branches  = new ArrayList<Branch>();
        this.equations = new ArrayList<CircuitEquationHolder>();

        this.nodes.addAll(graph.realNodes);
        this.branches.addAll(graph.realBranches);

        this.nodesAmount               = this.nodes.size();
        this.currentsAmount            = this.branches.size();
        this.independentCircuitsAmount = graph.circuits.size();

        this.matrix1 = new double[this.currentsAmount][this.currentsAmount + 1];

        DifferentialSystemSolver differentialSystemSolver = new DifferentialSystemSolver(this);
     //   differentialSystemSolver.render();

        /** 1. Fill up part of matrix with currents**/

      /*  Node node;
        for (int i = 0; i < this.nodesAmount - 1; i ++) {
            node = this.nodes.get(i);

            Branch branch;
            for (int j = 0; j < this.branches.size(); j ++) {
                branch = this.branches.get(j);

                if (branch.inCircuit) {
                    if (branch.nodeA.equals(node)) {
                        // save as '-'
                        this.matrix1[i][j] = -1;

                    } else if (branch.nodeB.equals(node)) {
                        // save as '+'
                        this.matrix1[i][j] = +1;

                    } else {
                        //    throw new RuntimeException("Pizdets");
                    }

                }
            }

        }
*/
        /** 2. Fill part of matrix with voltages **/

       /* for (int i = 0; i < graph.circuits.size(); i ++) {
            this.equations.add(new CircuitEquationHolder(this, graph.circuits.get(i), i + this.nodesAmount - 1));
        */

    }

    public void calculate() {

        /** 1. Make upper-triangular matrix **/

        double[] toSwap;
        double[] answers = new double[matrix1.length];

        for (int i = 0; i < matrix1.length; i ++) {
            double frac;

            if (matrix1[i][i] == 0.0) {
                // I need to swap rows

                for (int j = i + 1; j < matrix1.length; j ++) {
                    if (matrix1[j][i] != 0.0) {
                        // swap
                        toSwap = matrix1[i];
                        matrix1[i] = matrix1[j];
                        matrix1[j] = toSwap;
                        break;
                    }
                }

            }

            for (int j = i + 1; j < matrix1.length; j ++) {
                if (matrix1[j][i] != 0) {
                    frac = matrix1[j][i] / matrix1[i][i];

                    for (int k = i; k < matrix1[i].length; k ++) {
                        matrix1[j][k] -= frac * matrix1[i][k];

                    }
                }


            }
        }

        Log.d("matrixx","__________upper-triangular-matrix_____________");

        for (int u = 0; u < matrix1.length; u ++) {
            String line = "";

            for (int j = 0; j < this.currentsAmount + 1; j ++) {
                line += matrix1[u][j] + "  ";
            }

            Log.d("matrixx", line);
        }

        Log.d("matrixx", "_____________________________________");


        /** 2. Find solutions by back substitution **/

        double lastM;

        for (int i = matrix1.length - 1; i > -1 ; i --) {
            lastM = matrix1[i][matrix1.length];

            for (int j = i + 1; j < matrix1.length ; j ++) {
                lastM -= matrix1[i][j] * answers[j];
            }

            answers[i] = lastM / matrix1[i][i];

        }

        String line = "";
        for (int u = 0; u < answers.length; u ++) {
            line += answers[u] + ", ";

        }

        Log.d("matrixx", line);


    }


}
