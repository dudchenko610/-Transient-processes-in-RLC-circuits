package com.crazydev.funnycircuits.electronic;

import android.util.Log;
import android.util.TimeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Graph {

    protected HashSet<Node> nodes;                        // nodes including hanging branches
    protected HashSet<Branch> originalBranches;
    protected HashSet<Branch> copyBranches;
    protected Branch singleBranch;

    public ArrayList<Circuit> circuits;
    protected ArrayList<Branch> puncturedBranches;
    public HashSet<Node> realNodes;                  // nodes excluding hanging branches (nodes that we use in computations)
    public HashSet<Branch> realBranches;

    private ArrayList<Branch> branchess;

    public Graph() {
        this.nodes             = new HashSet<Node>();
        this.circuits          = new ArrayList<Circuit>();
        this.puncturedBranches = new ArrayList<Branch>();
        this.originalBranches  = new HashSet<Branch>();
        this.copyBranches      = new HashSet<Branch>();

        this.realNodes         = new HashSet<Node>();
        this.branchess         = new ArrayList<>();
        this.realBranches      = new HashSet<Branch>();

    }

    public void findIndependentCircuits() {

        this.circuits.clear();
        this.puncturedBranches.clear();

        if (this.singleBranch != null) {
            Circuit circuit = new Circuit();
            this.singleBranch.inCircuit = true;
            circuit.branches.add(this.singleBranch);

            this.circuits.add(circuit);
        } else {

            Iterator<Branch> iterator = this.originalBranches.iterator();

            Branch branch;
            while (iterator.hasNext()) {
                branch = iterator.next();

                if (branch.nodeA.equals(branch.nodeB)) {
                    Circuit circuit = new Circuit();
                    branch.inCircuit = true;
                    circuit.branches.add(branch);

                    this.circuits.add(circuit);

                    branch.nodeA.originalStructure.remove(branch);
                    iterator.remove();
                }
            }

            this.findHealthyIndependentCircuits();
        }

    }

    private void findHealthyIndependentCircuits() {

        Node node;

        /** 1. Build tree with branches **/

        Iterator<Node> iterator = this.nodes.iterator();
        if (iterator.hasNext()) {
            node = iterator.next();

            node.isChecked = true;
            node.buildTree(this, null);

        } else {
            return;
        }

        Log.d("treeee", "Punctured branches " + this.puncturedBranches.size());


        /** 2. Form independent circuits **/

        for (Branch branch : this.puncturedBranches) {

            // A. Copy
            this.copyBranches.clear();
            this.copyBranches.addAll(this.originalBranches);
            for (Node _node: this.nodes) {
                _node.copyStructure.clear();
                _node.copyStructure.putAll(_node.originalStructure);
            }

            // B. Add current punctured branch to graph
            branch.nodeA.copyStructure.put(branch, branch.nodeB);
            branch.nodeB.copyStructure.put(branch, branch.nodeA);
            this.copyBranches.add(branch);

            // C. Eliminate hanging branches
            Branch  _branch;
            Set<Branch> brs;
            boolean b;

            Iterator<Branch> _iterator = this.copyBranches.iterator();
            while (_iterator.hasNext()) {
                _branch = _iterator.next();

                b = false;

                // for nodeA
                brs = _branch.nodeA.copyStructure.keySet();

                for (Branch br : brs) {
                    if (br != _branch && this.copyBranches.contains(br)) {
                        b = true;
                        break;
                    }

                }

                if (!b) {
                    _iterator.remove();
                    _iterator = this.copyBranches.iterator();
                    continue;
                }

                b = false;

                // for nodeB
                brs = _branch.nodeB.copyStructure.keySet();

                for (Branch br : brs) {
                    if (br != _branch && this.copyBranches.contains(br)) {
                        b = true;
                        break;
                    }

                }

                if (!b) {
                    _iterator.remove();
                    _iterator = this.copyBranches.iterator();
                    continue;
                }

            }

            Circuit circuit = new Circuit();

            for (Branch branch1 : this.copyBranches) {
                branch1.inCircuit = true;
            }

            circuit.branches.addAll(this.copyBranches);
//////////////////////////////////////////////////////////////////
            this.circuits.add(circuit);

        }
    }

    public void eliminateNodesWithHangingBranches() {

        Iterator<Branch> branchIterator;
        Circuit                 circuit;
        Circuit                       c;
        Branch                   branch;

        for (int i = 0; i < this.circuits.size(); i ++) {
            circuit        = this.circuits.get(i);
            branchIterator = circuit.branches.iterator();

            while (branchIterator.hasNext()) {
                branch = branchIterator.next();
                branch.nodeA.branches.clear();
                branch.nodeB.branches.clear();

            }

        }

        for (int i = 0; i < this.circuits.size(); i ++) {
            circuit        = this.circuits.get(i);
            branchIterator = circuit.branches.iterator();

            while (branchIterator.hasNext()) {
                branch = branchIterator.next();
                branch.nodeA.branches.add(branch);
                branch.nodeB.branches.add(branch);

            }

        }

        for (int i = 0; i < this.circuits.size(); i ++) {
            circuit        = this.circuits.get(i);

            branchess.clear();
            branchess.addAll(circuit.branches);

            for (int n = 0; n < branchess.size(); n ++) {
                branch = branchess.get(n);

                int j = 0;
                Branch anotherBranch = null;


                for (Branch br : branch.nodeA.branches) {
                    if (br.inCircuit) {

                        if (br != branch) {
                            anotherBranch = br;
                        }


                        j ++;
                    }
                }

                if (j == 2) {
                //    branch.nodeA.isSelected = true;

                    // create new branch
                    // replace in all circuits
                    // reset branchIterator

                    if (anotherBranch != null) {


               /*         for (Wire wire : branch.wires) {
                            wire.isSelected = true;
                        }

                        for (Wire wire : anotherBranch.wires) {
                            wire.isSelected = true;
                        }

                        break;*/

                        Branch newBranch = new Branch();
                        newBranch.inCircuit = true;
                        newBranch.nodeA = branch.nodeB;


                        newBranch.wires.addAll(branch.wires);
                        newBranch.wires.addAll(anotherBranch.wires);

                        if (branch.nodeA.equals(anotherBranch.nodeB)) {
                            newBranch.nodeB = anotherBranch.nodeA;

                        } else {
                            newBranch.nodeB = anotherBranch.nodeB;
                        }

 /////////////////////////////////////////////////////////////////////////////////////////////////////////////

                   /*     branch.nodeA.isSelected = true;
                        branch.nodeB.isSelected = true;
                        for (Wire wire : branch.wires) {
                            wire.isSelected = true;
                        }

                        anotherBranch.nodeA.isSelected = true;
                        anotherBranch.nodeB.isSelected = true;
                        for (Wire wire : anotherBranch.wires) {
                            wire.isSelected = true;
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        int a = 2;

                        branch.nodeA.isSelected = false;
                        branch.nodeB.isSelected = false;
                        for (Wire wire : branch.wires) {
                            wire.isSelected = false;
                        }

                        anotherBranch.nodeA.isSelected = false;
                        anotherBranch.nodeB.isSelected = false;
                        for (Wire wire : anotherBranch.wires) {
                            wire.isSelected = false;
                        }

                        newBranch.nodeA.isSelected = true;
                        newBranch.nodeB.isSelected = true;
                        for (Wire wire : newBranch.wires) {
                            wire.isSelected = true;
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        a = 3;


                        newBranch.nodeA.isSelected = false;
                        newBranch.nodeB.isSelected = false;
                        for (Wire wire : newBranch.wires) {
                            wire.isSelected = false;
                        }*/

 /////////////////////////////////////////////////////////////////////////////////////////////////////////////


                      /*  newBranch.nodeA.isSelected = true;
                        newBranch.nodeB.isSelected = true;

                        for (Wire wire : newBranch.wires) {
                            wire.isSelected = true;
                        }*/

                        newBranch.nodeA.branches.add(newBranch);
                        newBranch.nodeB.branches.add(newBranch);

                        newBranch.nodeB.branches.remove(branch);
                        newBranch.nodeB.branches.remove(anotherBranch);
                        newBranch.nodeA.branches.remove(branch);
                        newBranch.nodeA.branches.remove(anotherBranch);

                        branch.nodeA.branches.remove(branch);
                        branch.nodeA.branches.remove(anotherBranch);

                        branch.inCircuit        = false;
                        anotherBranch.inCircuit = false;


                        for (int u = 0; u < this.circuits.size(); u ++) {
                            Circuit crct = this.circuits.get(u);

                            if (crct.branches.contains(branch) && crct.branches.contains(anotherBranch)) {
                                crct.branches.remove(branch);
                                crct.branches.remove(anotherBranch);
                                crct.branches.add(newBranch);
                            }

                        }



                        branchess.clear();
                        branchess.addAll(circuit.branches);

                 /*       for (int k = 0; k < this.circuits.size(); k ++) {

                            c = this.circuits.get(k);
                            c.branches.remove(branch);
                            c.branches.remove(anotherBranch);
                            c.branches.add(newBranch);

                        }*/

                        n = -1;
                        continue;

                    } else {
                        // exception
                    }


                }



                j = 0;
                anotherBranch = null;

                for (Branch br : branch.nodeB.branches) {
                    if (br.inCircuit) {

                        if (br != branch) {
                            anotherBranch = br;
                        }

                        j ++;
                    }
                }

                if (j == 2) {
             //     branch.nodeB.isSelected = true;

                    // create new branch
                    // replace in all circuits
                    // reset branchIterator

                    if (anotherBranch != null) {

               /*         for (Wire wire : branch.wires) {
                            wire.isSelected = true;
                        }

                        for (Wire wire : anotherBranch.wires) {
                            wire.isSelected = true;
                        }

                        break;*/

                        Branch newBranch = new Branch();
                        newBranch.inCircuit = true;
                        newBranch.nodeB = branch.nodeA;


                        newBranch.wires.addAll(branch.wires);
                        newBranch.wires.addAll(anotherBranch.wires);

                        if (branch.nodeB.equals(anotherBranch.nodeA)) {
                            newBranch.nodeA = anotherBranch.nodeB;
                        } else {
                            newBranch.nodeA = anotherBranch.nodeA;
                        }


  /////////////////////////////////////////////////////////////////////////////////////////////////////////////

                /*        branch.nodeA.isSelected = true;
                        branch.nodeB.isSelected = true;
                        for (Wire wire : branch.wires) {
                            wire.isSelected = true;
                        }

                        anotherBranch.nodeA.isSelected = true;
                        anotherBranch.nodeB.isSelected = true;
                        for (Wire wire : anotherBranch.wires) {
                            wire.isSelected = true;
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        int a = 2;

                        branch.nodeA.isSelected = false;
                        branch.nodeB.isSelected = false;
                        for (Wire wire : branch.wires) {
                            wire.isSelected = false;
                        }

                        anotherBranch.nodeA.isSelected = false;
                        anotherBranch.nodeB.isSelected = false;
                        for (Wire wire : anotherBranch.wires) {
                            wire.isSelected = false;
                        }

                        newBranch.nodeA.isSelected = true;
                        newBranch.nodeB.isSelected = true;
                        for (Wire wire : newBranch.wires) {
                            wire.isSelected = true;
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        a = 3;


                        newBranch.nodeA.isSelected = false;
                        newBranch.nodeB.isSelected = false;
                        for (Wire wire : newBranch.wires) {
                            wire.isSelected = false;
                        }*/

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////


                   /*     newBranch.nodeA.isSelected = true;
                        newBranch.nodeB.isSelected = true;


                        for (Wire wire : newBranch.wires) {
                            wire.isSelected = true;
                        }*/

                        newBranch.nodeA.branches.add(newBranch);
                        newBranch.nodeB.branches.add(newBranch);

                        newBranch.nodeB.branches.remove(branch);
                        newBranch.nodeB.branches.remove(anotherBranch);
                        newBranch.nodeA.branches.remove(branch);
                        newBranch.nodeA.branches.remove(anotherBranch);

                        branch.nodeB.branches.remove(branch);
                        branch.nodeB.branches.remove(anotherBranch);

                        branch.inCircuit = false;
                        anotherBranch.inCircuit = false;


                        for (int u = 0; u < this.circuits.size(); u ++) {
                            Circuit crct = this.circuits.get(u);

                            if (crct.branches.contains(branch) && crct.branches.contains(anotherBranch)) {
                                crct.branches.remove(branch);
                                crct.branches.remove(anotherBranch);
                                crct.branches.add(newBranch);
                            }

                        }

                        branchess.clear();
                        branchess.addAll(circuit.branches);



                        n = -1;
                        continue;

                    } else {
                        // exception
                    }


                }




            }

      //      Log.d("razzm", "size = " + branchess.size());

        }

         /*   if (branch.inCircuit) {
                    for (Wire wire : branch.wires) {
                        wire.isSelected = true;
                    }
                }
        */

    }

    public void findRealNodesAndBranches() {
        this.realNodes.clear();
        this.realBranches.clear();

        for (Circuit circuit : this.circuits) {
            for (Branch branch : circuit.branches) {

                if (branch.inCircuit) {
                    this.realBranches.add(branch);
                    this.realNodes.add(branch.nodeA);
                    this.realNodes.add(branch.nodeB);

                }

            }
        }


    }

    public void rearrangeWiresInBranchesInCircuits() {

        for (Circuit circuit : this.circuits) {
            circuit.rearrangeWiresInBranches();
        }

    }

    public void rearrangeBranchesInCircuits() {
        for (Circuit circuit : circuits) {

            circuit.rearrangeBranches();


       /*     int a = 3;


            for (Branch branch : circuit.branches) {
                branch.nodeA.isSelected = true;
                branch.nodeB.isSelected = true;

                a = 2;

                for (Wire wire : branch.wires) {
                    wire.isSelected = true;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            a = 4;

            for (Branch branch : circuit.branches) {
                branch.nodeA.isSelected = false;
                branch.nodeB.isSelected = false;



                for (Wire wire : branch.wires) {
                    wire.isSelected = false;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

*/
        }
    }
}
