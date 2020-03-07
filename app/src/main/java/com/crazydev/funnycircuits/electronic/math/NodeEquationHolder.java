package com.crazydev.funnycircuits.electronic.math;

import com.crazydev.funnycircuits.electronic.Branch;
import com.crazydev.funnycircuits.electronic.Node;

import java.util.ArrayList;

public class NodeEquationHolder extends EquationHolder {

    private Node node;

    public NodeEquationHolder(Calculator calculator, Node node, int indexInArrayList) {
        super(calculator, indexInArrayList);
        this.node = node;

        // fill row
        Branch branch;
        for (int j = 0; j < this.branches.size(); j ++) {
            branch = this.branches.get(j);

            if (branch.inCircuit) {
                if (branch.nodeA.equals(node)) {
                    // save as '-'
                    this.row[j] = -1;

                    this.matrixF[this.countersF[j] ++ ][j] = (short) this.indexInArrayList;
                    this.price ++;

                } else if (branch.nodeB.equals(node)) {
                    // save as '+'
                    this.row[j] = +1;

                    this.matrixF[this.countersF[j] ++ ][j] = (short) this.indexInArrayList;
                    this.price ++;

                } else {
                    //    throw new RuntimeException("Pizdets");
                }

            }
        }


    }

    @Override
    public void recalculate() {

    }

    @Override
    public void setPosition(int index) {
        if (!this.isSelected) {

            this.index      = index;
            this.isSelected = true;

            for (int i = 0; i < this.matrix[0].length; i ++) {
                this.matrix[this.index][i] = this.row[i];
            }
        }


    }
}
