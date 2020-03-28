package com.crazydev.funnycircuits.electronic.math;

import java.util.ArrayList;

public class DoubleArrayStructure {

    public static int LIMIT = 200;

    private double currentArray [];
    public int counter;

    public ArrayList<double[]> arrays = new ArrayList<double[]>();

    public DoubleArrayStructure() {
        this.currentArray = new double[LIMIT];
        this.arrays.add(this.currentArray);

    }

    public void add(double value) {
        if (this.counter == LIMIT) {
            this.counter = 0;

            this.currentArray = new double[LIMIT];
            this.arrays.add(this.currentArray);
        }

        this.currentArray[counter ++] = value;

    }

    public double get(int i) {
        return this.arrays.get(i / LIMIT)[i % LIMIT];
    }

    public int getTotalSize() {

        return (this.arrays.size() - 1) * LIMIT + this.counter;
    }

    public void tryToRelease() {
        this.counter = 0;
        this.arrays = null;
        this.arrays = new ArrayList<double[]>();
        System.gc();

    }



}
