package com.crazydev.funnycircuits.electronic;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.elements.Capacitor;
import com.crazydev.funnycircuits.electronic.elements.DCSource;
import com.crazydev.funnycircuits.electronic.elements.Inductor;
import com.crazydev.funnycircuits.electronic.elements.Resistor;
import com.crazydev.funnycircuits.electronic.math.Calculator;
import com.crazydev.funnycircuits.io.Assets;
import com.crazydev.funnycircuits.math.OverlapTester;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;
import com.crazydev.funnycircuits.rendering.VertexBatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class World {

    private VertexBatcher vertexBatcher;

    public HashMap<Integer, Node> nodes;
    public ArrayList<Wire>        wires;
    public ArrayList<Graph>      graphs;

    static HashSet<Branch> branches = new HashSet<Branch>();

    public static final int OFFSET = 2_000;

    private static World world;

    public static World getInstance() {
        if (world == null) {
            world = new World();
        }

        return world;
    }

    private World() {

        this.nodes  = new  HashMap<Integer, Node>();
        this.wires  = new ArrayList<Wire>();
        this.graphs = new ArrayList<Graph>();

        this.vertexBatcher = VertexBatcher.getInstance();

    }

    public void createElement(Vector2D pA, Wire.WireType wireType, int orientation) {

        Vector2D pB;

        switch (orientation) {
            case 0:
            case 1:
                pB = pA.copy().add(3, 0); // right
                break;
            case 2:
            case 3:
                pB = pA.copy().add(0, 3); // up
                break;

            default:
                return;
        }

        while (!checkWire(pA, pB)) {
            pA.add(1, -1);
            pB.add(1, -1);
        }

        this.createWire(wireType, pA, pB, orientation);

    }

    public void createWire(Vector2D pA, Vector2D pB) {
        this.createWire(Wire.WireType.WIRE, pA, pB, -1);
    }

    public void createWire(Vector2D pA, Vector2D pB, int orientation) {
        this.createWire(Wire.WireType.WIRE, pA, pB, orientation);
    }

    private void createWire_withoutBuilding(Wire.WireType wireType, Vector2D pA, Vector2D pB, int orientation) {
        if (!this.checkWire(pA, pB)) {
            return;
        }

        int hashA = ( ((int) pA.x + World.OFFSET) << 16) | ((int) pA.y + World.OFFSET);
        int hashB = ( ((int) pB.x + World.OFFSET) << 16) | ((int) pB.y + World.OFFSET);

        Node nodeA = this.nodes.get(hashA);
        Node nodeB = this.nodes.get(hashB);

        if (nodeA == null) {
            nodeA = new Node(pA.copy(), hashA);
        }

        if (nodeB == null) {
            nodeB = new Node(pB.copy(), hashB);
        }

        Wire wire = null;

        switch (wireType) {
            case WIRE:
                wire = new Wire(this, nodeA, nodeB);
                break;
            case RESISTOR:
                wire = new Resistor(this, nodeA, nodeB);
                break;
            case INDUCTOR:
                wire = new Inductor(this, nodeA, nodeB, orientation);
                break;
            case CAPACITOR:
                wire = new Capacitor(this, nodeA, nodeB);
                break;
            case DC_SOURCE:
                wire = new DCSource(this, nodeA, nodeB, orientation);
                break;
            case AC_SOURCE:
                break;
            default:
                return;
        }

        Wire w;
        for (int i = 0; i < this.wires.size(); i ++) {
            w = this.wires.get(i);

            if (w.isNested(wire)) {
                wire.remove();
                return;
            }
        }

        for (int i = 0; i < this.wires.size(); i ++) {
            w = this.wires.get(i);
            if (w.overlapWires(wire)) {
                i --;
            }
        }

        this.nodes.put(hashA, nodeA);
        this.nodes.put(hashB, nodeB);

        this.wires.add(wire);
    }

    public void createWire(Wire.WireType wireType, Vector2D pA, Vector2D pB, int orientation) {
        this.createWire_withoutBuilding(wireType, pA, pB, orientation);

        for (Wire wire : this.wires) {
            wire.isChecked  = false;
            wire.deselect();
        }

        this.simplifyConnectedWires();
   //     this.buildGraphs();

    }

    public boolean checkWire(Vector2D pA, Vector2D pB) {
        return this.checkWire(null, pA, pB);
    }

    public boolean checkWire(Wire wr, Vector2D pA, Vector2D pB) {
        int hashA = ( ((int) pA.x + World.OFFSET) << 16) | ((int) pA.y + World.OFFSET);
        int hashB = ( ((int) pB.x + World.OFFSET) << 16) | ((int) pB.y + World.OFFSET);

        Node nodeA = new Node(pA.copy(), hashA);
        Node nodeB = new Node(pB.copy(), hashB);

        Wire wire = new Wire(nodeA, nodeB);

        Wire w;
        for (int i = 0; i < this.wires.size(); i ++) {
            w = this.wires.get(i);

            if (w != wr && w.isNested(wire)) {
                return false;
            }

            if (w != wr && w.overlapWires_check(wire)) {
                return false;
            }
        }

        return true;

    }

    public void deselect() {
        Wire wire;
        for (int i = 0; i < this.wires.size(); i ++) {
            wire = this.wires.get(i);
            wire.deselect();
        }
    }

    public Wire tryToSelect(Vector2D cursorPosition) {

        for (Wire wire : this.wires) {
            if (OverlapTester.pointInRectangle(wire.AABB, cursorPosition)) {
                this.deselect();
                wire.select();
                return wire;
            }
        }

        return null;
    }

    public void deleteSelectedWires() {
        Wire wire;
        for (int i = 0; i < this.wires.size(); i ++) {
            wire = this.wires.get(i);

            if (wire.drawableManager.isSelected()) {
                wire.remove();
                this.checkWire(wire.nodeA.location, wire.nodeB.location);
                i --;
            }
            wire.isChecked = false;
        }

        this.simplifyConnectedWires();
     //   this.buildGraphs();
    }

    private void simplifyConnectedWires() {

        Wire wire;
        Wire next;

        for (int i = 0; i < this.wires.size(); i++) {
            wire = this.wires.get(i);
            if (wire.isChecked || wire.type != Wire.WireType.WIRE) {
                continue;
            }

            wire.isChecked = true;

            if (wire.nodeA.wires.size() == 2) {
                next = wire.nodeA.wires.get((wire.nodeA.wires.indexOf(wire) + 1) % 2);

                if (next.type == Wire.WireType.WIRE && wire.isHorizontal == next.isHorizontal) {
                    Vector2D locA = next.nodeA.location.copy();
                    Vector2D locB = wire.nodeB.location.copy();

                    wire.remove();
                    next.remove();

                    this.createWire_withoutBuilding(Wire.WireType.WIRE, locA, locB, -1);

                    i = -1;
                    continue;

                }
            }

            if (wire.nodeB.wires.size() == 2) {
                next = wire.nodeB.wires.get((wire.nodeB.wires.indexOf(wire) + 1) % 2);

                if (next.type == Wire.WireType.WIRE && wire.isHorizontal == next.isHorizontal) {
                    Vector2D locA = wire.nodeA.location.copy();
                    Vector2D locB = next.nodeB.location.copy();

                    wire.remove();
                    next.remove();

                    this.createWire_withoutBuilding(Wire.WireType.WIRE, locA, locB, -1);

                    i = -1;
                    continue;

                }
            }

        }

    }

    private void buildGraphs() {

      /**   1. CLEAR **/

        Branch.ID = 0;
        World.branches.clear();
        this.graphs.clear();

        Graph graph;

        for (Wire wire : this.wires) {
            wire.isChecked  = false;
            wire.deselect();
            wire.nodeA.originalStructure.clear();
            wire.nodeB.originalStructure.clear();
        }

        Set<Map.Entry<Integer, Node>> entrySet = this.nodes.entrySet();

        for (Map.Entry<Integer, Node> entry : entrySet) {
            entry.getValue().isChecked = false;
        }

      /**   2. Build branches **/

        // with nodes
        for (Wire wire : this.wires) {
            if (!wire.isChecked) {

                if (wire.nodeA.wires.size() != 2 || wire.nodeB.wires.size() != 2) {

                    graph = new Graph();
                    wire.buildGraph(graph);
                    this.graphs.add(graph);
                }

            }
        }

        // single-branch
        for (Wire wire : this.wires) {
            if (!wire.isChecked) {
                graph = new Graph();
                wire.buildGraphWithoutNodes(graph);

                if (graph.singleBranch != null) {
                    graphs.add(graph);
                }

            }
        }

      /**   3. Find independent circuits  **/

        for (Graph g : this.graphs) {
            g.findIndependentCircuits();

        }

    /*    Random random = new Random();
        for (Branch branch : World.branches) {

            float r = random.nextFloat();
            float g = random.nextFloat();
            float b = random.nextFloat();

            Log.d("branches", "branch " + branch.hash_id + " = " + branch.wires.size());

            for (Wire w : branch.wires) {
                w.WIRE_COLOR.set(r, g, b);
            }
        }*/

        /**   4. Exclude hanging nodes in graphs  **/
        for (Graph g : this.graphs) {
            g.eliminateNodesWithHangingBranches();
            g.findRealNodesAndBranches();
            g.rearrangeWiresInBranchesInCircuits();
            g.rearrangeBranchesInCircuits();

        }


    //    Log.d("branches", "------branches = " + branches.size());
    //    Log.d("branches", "------graphs = " + graphs.size());

    }

    public void startSimulation() {
        this.buildGraphs();


    }

    public void stopSimulation() {

    }

    public void underlineBranch(int m, int n) {

      //  simplifyConnectedWires();

        Graph graph = this.graphs.get(m);
        Circuit circuit = graph.circuits.get(n);


        Calculator calculator = new Calculator(graph);


        for (Branch branch : circuit.branches) {
           for (Wire wire : branch.wires) {
               wire.select();
           }
        }


    }

    public void draw() {

        vertexBatcher.clearVerticesBufferColor();

        for (Wire wire : this.wires) {
            wire.draw();
        }

        vertexBatcher.depictSpritesColored();

      /*  for (Branch branch : branches) {
            branch.nodeA.draw();
            branch.nodeB.draw();
        }*/

        vertexBatcher.clearVerticesBufferTexture();

        Iterator<Map.Entry<Integer, Node>> iterator = this.nodes.entrySet().iterator();
        Map.Entry pair;
        Node node;

        while (iterator.hasNext()) {
            pair = (Map.Entry) iterator.next();
            node = (Node) pair.getValue();

            node.draw();

        }

        /**
         *
         * E/AndroidRuntime: FATAL EXCEPTION: GLThread 9604
         *     Process: com.crazydev.funnycircuits, PID: 31854
         *     java.util.ConcurrentModificationException
         *         at java.util.HashMap$HashIterator.nextEntry(HashMap.java:787)
         *         at java.util.HashMap$EntryIterator.next(HashMap.java:824)
         *         at java.util.HashMap$EntryIterator.next(HashMap.java:822)
         *         at com.crazydev.funnycircuits.electronic.World.draw(World.java:431)
         *         at com.crazydev.funnycircuits.rendering.services.GLContentService.drawContent(GLContentService.java:38)
         *         at com.crazydev.funnycircuits.rendering.OpenGLRenderer.onDrawFrame(OpenGLRenderer.java:169)
         *         at android.opengl.GLSurfaceView$GLThread.guardedRun(GLSurfaceView.java:1647)
         *         at android.opengl.GLSurfaceView$GLThread.run(GLSurfaceView.java:1352)
         */

        vertexBatcher.depictSpritesTextured(Assets.elements);

    }

}
