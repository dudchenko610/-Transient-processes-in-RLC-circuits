package com.crazydev.funnycircuits.electronic;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.elements.Capacitor;
import com.crazydev.funnycircuits.electronic.elements.Inductor;
import com.crazydev.funnycircuits.electronic.elements.Resistor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Branch {

    protected static int ID = 0;
    protected int hash_id   = 0;

    public HashSet<Wire>      wires      = new HashSet<Wire>();
    public HashSet<Wire>      sources    = new HashSet<Wire>();

    public HashMap<Circuit, Character> directionalValues = new HashMap<Circuit, Character>();
    public Node nodeA;
    public Node nodeB;

    public boolean inCircuit = false;
    private ArrayList<Character> wiresState = new ArrayList<Character>();

    public double current = 0;

    public Branch() {
        this.hash_id = ID ++;
    }

    @Override
    public boolean equals(Object o) {
        Branch branch = (Branch) o;
        return this.hash_id == branch.hash_id;
    }

    @Override
    public int hashCode() {
        return this.hash_id;
    }


    // works well because HashSet and HashMap contains methods work well
    protected void rearrangeWires() {
        this.sources.clear();

        Wire wire;
        for (Iterator<Wire> i = this.wires.iterator(); i.hasNext();) {
            wire = i.next();

            if (this.nodeA.equals(wire.nodeA) || this.nodeA.equals(wire.nodeB)) {
                wire.rearrangeFromBranchFirst(this, this.nodeA, this.nodeA);
                break;
            }

        }

    }

    protected void rearrangeFromCircuitFirst(Circuit circuit, Node lastNode, Node endpointNode) {
        this.directionalValues.remove(circuit);

        if (this.nodeA.equals(lastNode)) {
            this.directionalValues.put(circuit, '+');

         /*   Log.d("signn", "+");

            for (Wire wire : this.wires) {
                wire.isSelected = true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int a = 2;*/

            for (Branch branch : this.nodeB.branches) {

                if (branch.inCircuit && branch != this && circuit.branches.contains(branch)) {
                    branch.rearrangeFromCircuit(circuit, this.nodeB, endpointNode);
                    return;
                }
            }

        } /* else if (this.nodeB.equals(lastNode)) {
            this.directionalValues.put(circuit, '-');


            Log.d("signn", "-");

            for (Wire wire : this.wires) {
                wire.isSelected = true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int a = 2;

            for (Branch branch: this.nodeA.branches) {
                if (branch.inCircuit && branch != this && circuit.branches.contains(branch)) {
                    branch.rearrangeFromCircuit(circuit, this.nodeA, endpointNode);
                    return;
                }
            }

        } */else {
            throw new RuntimeException("Pizdets!");
        }


    }

    protected void rearrangeFromCircuit(Circuit circuit, Node lastNode, Node endpointNode) {

        this.directionalValues.remove(circuit);

        if (this.nodeA.equals(lastNode)) {
            this.directionalValues.put(circuit, '+');


      /*      Log.d("signn", "+");

            for (Wire wire : this.wires) {
                wire.isSelected = true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int a = 2;*/

            if (this.nodeA.equals(endpointNode) || this.nodeB.equals(endpointNode)) {
                return;
            }

            for (Branch branch: this.nodeB.branches) {
                if (branch.inCircuit && branch != this && circuit.branches.contains(branch)) {
                    branch.rearrangeFromCircuit(circuit, this.nodeB, endpointNode);
                    return;
                }
            }

        } else if (this.nodeB.equals(lastNode)) {
            this.directionalValues.put(circuit, '-');

        /*    Log.d("signn", "-");

            for (Wire wire : this.wires) {
                wire.isSelected = true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int a = 2;*/

            if (this.nodeA.equals(endpointNode) || this.nodeB.equals(endpointNode)) {
                return;
            }

            for (Branch branch : this.nodeA.branches) {
                if (branch.inCircuit && branch != this && circuit.branches.contains(branch)) {
                    branch.rearrangeFromCircuit(circuit, this.nodeA, endpointNode);
                    return;
                }
            }

        } else {
            throw new RuntimeException("Pizdets!");
        }

    }

}
