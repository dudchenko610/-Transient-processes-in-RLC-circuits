package com.crazydev.funnycircuits.electronic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Circuit {

    public HashSet<Branch> branches;

    public Circuit() {
        this.branches = new HashSet<Branch>();
    }

    protected void rearrangeBranches() {
        Iterator<Branch> iterator = this.branches.iterator();
        Branch condFirstBranch = iterator.next();
        iterator = this.branches.iterator();

        condFirstBranch.rearrangeFromCircuitFirst(this, condFirstBranch.nodeA, condFirstBranch.nodeA);


    }

    protected void rearrangeWiresInBranches() {

        for (Branch branch : this.branches) {

            if (branch.inCircuit) {
                branch.rearrangeWires();
            }

        }

    /*    for (Branch branch : this.branches) {
            for (Wire wire: branch.wires) {
                wire.isSelected = false;
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }


}
