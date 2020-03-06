package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.Branch;

import java.util.ArrayList;

public abstract class EquationHolder {

    protected ArrayList<Branch> branches;
    protected double[][] matrix;
    protected double[] row;

    protected short[]   countersF;
    protected short[][] matrixF;

    int index;
    protected boolean isSelected = false;
    protected int indexInArrayList = -1;

    public EquationHolder(Calculator calculator,  int indexInArrayList){
        this.branches = calculator.branches;
        this.matrix   = calculator.matrix1;

        this.countersF = calculator.countersF;
        this.matrixF   = calculator.matrixF;

        this.indexInArrayList = indexInArrayList;

        this.row      = new double[matrix[0].length];

    }

    public abstract void recalculate();

    public void setPosition(int index) {
        this.index      = index;
        this.isSelected = true;
    }

    public boolean isAppropriative(int index) {
        return this.row[index] != 0.0;
    }

}
