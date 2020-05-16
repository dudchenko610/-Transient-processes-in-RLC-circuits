package com.crazydev.funnycircuits.electronic.oldmath;

import java.util.ArrayList;

public class Equation {

    ArrayList<Addition> left;
    ArrayList<Addition> right;

    boolean isMainEquation = false;

    public Equation () {
        this.left  = new ArrayList<Addition>();
        this.right = new ArrayList<Addition>();
    }

    private void expressVariable(Variable var, ArrayList<Addition> from, ArrayList<Addition> to) {
        Addition addition;

        for (int i = 0; i < from.size(); i ++) {
            addition = from.get(i);

            if (!addition.variable.equals(var)) {
                addition.sign *= -1;

                to.add(addition);
                from.remove(addition);
            }

        }

    }

    public void expressVariable(Variable var) {
        Addition addition;

        for (int i = 0; i < this.left.size(); i ++) {
            addition = this.left.get(i);

            if (addition.variable != null && addition.variable.equals(var)) {
                expressVariable(var, left, right);
                return;
            }
        }

        for (int i = 0; i < this.right.size(); i ++) {
            addition = this.right.get(i);

            if (addition.variable != null && addition.variable.equals(var)) {
                expressVariable(var, right, left);

                // swap left and right because of we expressed variable from right part of equation
                ArrayList<Addition> t = this.left;
                this.left = this.right;
                this.right = t;

                return;
            }
        }


    }

    public boolean contains(Variable var) {
        Addition addition;
        for (int i = 0; i < this.left.size(); i ++) {
            addition = this.left.get(i);

            if (addition.variable!= null && addition.variable.equals(var)) {
                return true;
            }
        }

        for (int i = 0; i < this.right.size(); i ++) {
            addition = this.right.get(i);

            if (addition.variable!= null && addition.variable.equals(var)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {

        String text = "";

        for (int i = 0; i < this.left.size(); i ++) {
            text += " " + this.left.get(i).toString() + " ";
        }

        text += "  =  ";

        for (int i = 0; i < this.right.size(); i ++) {
            text += " " + this.right.get(i).toString() + " ";
        }

        if (this.isMainEquation) {
            text += " - IS MAIN";
        }

        return text;
    }
}
