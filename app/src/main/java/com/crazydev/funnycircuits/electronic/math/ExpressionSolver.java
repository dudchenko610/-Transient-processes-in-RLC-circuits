package com.crazydev.funnycircuits.electronic.math;

public class ExpressionSolver {

    // we assume null as zero
    public static Expression subtract(Expression ex1, Expression ex2) {

        if (ex1 == null) {
            ex2.negate();
            return ex2;
        }



        return null;
    }

}
