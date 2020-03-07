package com.crazydev.funnycircuits.electronic.math;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.Branch;

import java.util.ArrayList;

public abstract class EquationHolder {

    protected ArrayList<Branch> branches;
    protected double[][] matrix;
    protected double[] row;

    protected short[]   countersF;
    protected short[][] matrixF;

    int index = -1;
    protected int price;
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

    public int getPrice() {
        return this.price;
    }

    public abstract void recalculate();

    public abstract void setPosition(int index);

    public void show() {
        String line = "";
        for (int i = 0; i < this.row.length; i ++) {
            line += this.row[i] + " ";
        }

        Log.d("matrixx", line);
    }

}
