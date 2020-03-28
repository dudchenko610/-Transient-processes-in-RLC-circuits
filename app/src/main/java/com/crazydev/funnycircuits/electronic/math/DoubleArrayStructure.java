package com.crazydev.funnycircuits.electronic.math;

import java.util.ArrayList;

public class DoubleArrayStructure {

    public static int LIMIT = 200;

    private double currentArray [];
    public int counter;

    public double max;
    public double min;

    public ArrayList<double[]> arrays = new ArrayList<double[]>();

    public DoubleArrayStructure() {
        this.currentArray = new double[LIMIT];
        this.arrays.add(this.currentArray);

        this.max = Integer.MIN_VALUE;
        this.min = Integer.MAX_VALUE;

    }

    public void add(double value) {
        if (this.counter == LIMIT) {
            this.counter = 0;

            this.currentArray = new double[LIMIT];
            this.arrays.add(this.currentArray);
        }

        this.currentArray[counter ++] = value;

        if (value > max) {
            max = value;
        }

        if (value < min) {
            min = value;
        }


    }

    public double get(int i) {
        return this.arrays.get(i / LIMIT)[i % LIMIT];
    }

    public int getTotalSize() {

        return (this.arrays.size() - 1) * LIMIT + this.counter;
    }

    public double getAbsoluteMax() {
        double m1 = Math.abs(this.max);
        double m2 = Math.abs(this.min);

        if (m1 > m2) {
            return m1;
        } else {
            return m2;
        }

    }

    public void tryToRelease() {
        this.counter = 0;
        this.arrays = null;
        this.arrays = new ArrayList<double[]>();
        System.gc();

    }



}
