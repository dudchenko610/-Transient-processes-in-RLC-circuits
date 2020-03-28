package com.crazydev.funnycircuits.electronic;

import com.crazydev.funnycircuits.electronic.interfaces.IDrawableManager;
import com.crazydev.funnycircuits.electronic.managers.DrawableManager;
import com.crazydev.funnycircuits.math.Rectangle;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.sprites.ColoredSprite;
import com.crazydev.funnycircuits.rendering.VertexBatcher;

import java.util.HashMap;

public class Wire {


    protected VertexBatcher vertexBatcher;
    public enum WireType {
      WIRE,
      RESISTOR,
      INDUCTOR,
      CAPACITOR,
      DC_SOURCE,
      AC_SOURCE

    };

    public WireType type;

    public HashMap<Branch, Character> directionalValues = new HashMap<Branch, Character>();

    private static int ID = 0;
    protected int id      = 0;
    protected boolean isHorizontal = false;

    protected Rectangle AABB;

    public int orientation = -1;

    public Node nodeA;
    public Node nodeB;

    protected boolean isChecked = false;

    public Vector3D WIRE_COLOR = new Vector3D(1, 0, 0);
    private World world;

    protected boolean isSource = false;

  //  public boolean isSelected = false;

    protected IDrawableManager drawableManager;

    public Wire(World world, Node nodeA, Node nodeB) {
        this(world, nodeA, nodeB, - 1);
    }

    public Wire(World world, Node nodeA, Node nodeB, int orientation) {
        this(nodeA, nodeB);

        this.world       = world;
        this.orientation = orientation;

        this.id = Wire.ID ++;
        this.setupDrawableManager();
        this.setupAABB();

        this.nodeA.wires.add(this);
        this.nodeB.wires.add(this);

    }

    public Wire(Node nodeA, Node nodeB) {
        this.type = WireType.WIRE;
        this.vertexBatcher = VertexBatcher.getInstance();

        int w = (int) Math.abs(nodeA.location.x - nodeB.location.x);
        int h = (int) Math.abs(nodeA.location.y - nodeB.location.y);

        if (w > h) {
            this.isHorizontal = true;

            if (nodeA.location.x > nodeB.location.x) {
                this.nodeB = nodeA;
                this.nodeA = nodeB;
            } else {
                this.nodeA = nodeA;
                this.nodeB = nodeB;
            }

        } else {
            this.isHorizontal = false;

            if (nodeA.location.y > nodeB.location.y) {
                this.nodeB = nodeA;
                this.nodeA = nodeB;
            } else {
                this.nodeA = nodeA;
                this.nodeB = nodeB;
            }

        }

    }

    protected void setupDrawableManager() {
        this.drawableManager = new DrawableManager() {

            @Override
            public void setupSprites() {
                if (Wire.this.isHorizontal) {

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f), new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y), nodeB.location.x - nodeA.location.x, 0.1f));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y), nodeB.location.x - nodeA.location.x, 0.25f));

                } else {

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.30196f, 0.30196f, 0.30196f) , new Vector3D(0.30196f, 0.30196f, 0.30196f),
                            new Vector2D(nodeB.location.x, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.1f, nodeB.location.y - nodeA.location.y));

                    this.sprites.add(new ColoredSprite(vertexBatcher, new Vector3D(0.02352f, 0f, 0.65098f), new Vector3D(0.02352f, 0f, 0.65098f),
                            new Vector2D(nodeB.location.x, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.25f, nodeB.location.y - nodeA.location.y));

                }
            }

        };

        this.drawableManager.setupSprites();
    }

    protected void setupAABB() {
        if (this.isHorizontal) {
            this.AABB = new Rectangle(new Vector2D((nodeB.location.x + nodeA.location.x) / 2.0f, nodeB.location.y), nodeB.location.x - nodeA.location.x, 0.56f);
        } else {
            this.AABB = new Rectangle(new Vector2D(nodeB.location.x, (nodeB.location.y + nodeA.location.y) / 2.0f), 0.56f, nodeB.location.y - nodeA.location.y);
        }
    }

    public boolean isNested(Wire wire) {

        int xA_l = (int) this.nodeA.location.x;
        int xA_r = (int) this.nodeB.location.x;

        int xB_l = (int) wire.nodeA.location.x;
        int xB_r = (int) wire.nodeB.location.x;


        int yA_l = (int) this.nodeA.location.y;
        int yA_r = (int) this.nodeB.location.y;

        int yB_l = (int) wire.nodeA.location.y;
        int yB_r = (int) wire.nodeB.location.y;

        if (wire.isHorizontal) {
            if (yB_l == yA_l && xA_l > xB_l && xA_l < xB_r) {
                return true;
            }

            if (yB_l == yA_r && xA_r > xB_l && xA_r < xB_r) {
                return true;
            }
        } else {
            if (xB_l == xA_l && yA_l > yB_l && yA_l < yB_r) {
                return true;
            }

            if (xB_l == xA_r && yA_r > yB_l && yA_r < yB_r) {
                return true;
            }
        }

        if (this.isHorizontal && wire.isHorizontal) {

            if (this.nodeA.location.y !=  wire.nodeA.location.y) {
                return false;
            }

            if (xB_l >= xA_l && xB_r <= xA_r) {
                return true;
            }

            if (xA_l >= xB_l && xA_r <= xB_r) {
                return true;
            }

        } else if (!this.isHorizontal && !wire.isHorizontal) {
            if (this.nodeA.location.x !=  wire.nodeA.location.x) {
                return false;
            }

            if (yB_l >= yA_l && yB_r <= yA_r) {
                return true;
            }

            if (yA_l >= yB_l && yA_r <= yB_r) {
                return true;
            }

        }

        return false;
    }

    public boolean overlapWires(Wire wire) {
        int xA_l = (int) this.nodeA.location.x;
        int xA_r = (int) this.nodeB.location.x;

        int xB_l = (int) wire.nodeA.location.x;
        int xB_r = (int) wire.nodeB.location.x;


        int yA_l = (int) this.nodeA.location.y;
        int yA_r = (int) this.nodeB.location.y;

        int yB_l = (int) wire.nodeA.location.y;
        int yB_r = (int) wire.nodeB.location.y;

        if (this.isHorizontal && !wire.isHorizontal) {

            if (yA_l == yB_l && xB_l > xA_l && xB_l < xA_r) {
                // node A

                this.remove_1();

                Wire w1 = new Wire(this.world, this.nodeA, wire.nodeA);
                Wire w2 = new Wire(this.world, wire.nodeA, this.nodeB);

                world.wires.add(w1);
                world.wires.add(w2);

                //      Log.d("here", "here");
                return true;
            }

            if (yA_l == yB_r && xB_r > xA_l && xB_r < xA_r) {
                // node B

                this.remove_1();

                Wire w1 = new Wire(this.world, this.nodeA, wire.nodeB);
                Wire w2 = new Wire(this.world, wire.nodeB, this.nodeB);

                world.wires.add(w1);
                world.wires.add(w2);

                return true;
                //    Log.d("here", "here");
            }
        }

        if (!this.isHorizontal && wire.isHorizontal) {
            if (xA_l == xB_l && yB_l > yA_l && yB_l < yA_r) {
                // node A

                this.remove_1();

                Wire w1 = new Wire(this.world, this.nodeA, wire.nodeA);
                Wire w2 = new Wire(this.world, wire.nodeA, this.nodeB);

                world.wires.add(w1);
                world.wires.add(w2);

                return true;
                //      Log.d("here", "here");
            }

            if (xA_l == xB_r && yB_r > yA_l && yB_r < yA_r) {
                // node B

                this.remove_1();

                Wire w1 = new Wire(this.world, this.nodeA, wire.nodeB);
                Wire w2 = new Wire(this.world, wire.nodeB, this.nodeB);

                world.wires.add(w1);
                world.wires.add(w2);

                return true;
                //        Log.d("here", "here");
            }
        }

        return false;

    }

    public boolean overlapWires_check(Wire wire) {
        int xA_l = (int) this.nodeA.location.x;
        int xA_r = (int) this.nodeB.location.x;

        int xB_l = (int) wire.nodeA.location.x;
        int xB_r = (int) wire.nodeB.location.x;


        int yA_l = (int) this.nodeA.location.y;
        int yA_r = (int) this.nodeB.location.y;

        int yB_l = (int) wire.nodeA.location.y;
        int yB_r = (int) wire.nodeB.location.y;

        if (this.type == WireType.WIRE && wire.type == WireType.WIRE) {
            return false;
        }

        if (this.isHorizontal && !wire.isHorizontal) {

            if (yA_l == yB_l && xB_l > xA_l && xB_l < xA_r) {
                // node A

                //      Log.d("here", "here");
                return true;
            }

            if (yA_l == yB_r && xB_r > xA_l && xB_r < xA_r) {
                // node B

                return true;
                //    Log.d("here", "here");
            }
        }

        if (!this.isHorizontal && wire.isHorizontal) {
            if (xA_l == xB_l && yB_l > yA_l && yB_l < yA_r) {
                // node A

                return true;
                //      Log.d("here", "here");
            }

            if (xA_l == xB_r && yB_r > yA_l && yB_r < yA_r) {


                return true;
                //        Log.d("here", "here");
            }
        }

        return false;

    }

    public void draw() {
        this.drawableManager.drawSprites();
    }

    @Override
    public boolean equals(Object o) {
        Wire wire = (Wire) o;
        return this.id == wire.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public void select() {
        this.drawableManager.setSelected(true);
        this.nodeA.isSelected = true;
        this.nodeB.isSelected = true;
    }

    public void deselect() {
        this.drawableManager.setSelected(false);
        this.nodeA.isSelected = false;
        this.nodeB.isSelected = false;
    }

    public void remove() {
        this.world.wires.remove(this);
        this.nodeA.remove(this.world, this);
        this.nodeB.remove(this.world, this);
        this.isChecked = false;
    }

    public void remove_1() {
        this.world.wires.remove(this);
        this.nodeA.wires.remove(this);
        this.nodeB.wires.remove(this);
        this.nodeA.isSelected = false;
        this.nodeB.isSelected = false;
        this.isChecked = false;
    }

    private static boolean builtSingle = false;

    protected void buildGraphWithoutNodes(Graph graph) {

        Wire.builtSingle = false;

        if (this.nodeA.wires.size() == 2 && this.nodeB.wires.size() == 2) {
            Branch branch = new Branch();
            branch.nodeA = this.nodeA;
            this.isChecked = true;
            branch.wires.add(this);

            //  Log.d("pxdz", "start");

            if (nodeB.wires.get(0) != this) {
                nodeB.wires.get(0).buildBranch(branch, graph);

            } else if (nodeB.wires.get(1) != this) {
                nodeB.wires.get(1).buildBranch(branch, graph);

            } else {
                throw new RuntimeException("Pizdets!!!");
            }

        }

    }

    private void buildBranch(Branch branch, Graph graph) {

        if (!this.isChecked && !Wire.builtSingle) {
            this.isChecked = true;

            if (this.nodeA.wires.size() == 2 && this.nodeB.wires.size() == 2) {

                branch.wires.add(this);
                this.whatToDo(graph, branch, this.nodeB);

                if (Wire.builtSingle) {
                    return;
                }

                this.whatToDo(graph, branch, this.nodeA);


            }

        }

    }

    private void whatToDo(Graph graph, Branch branch, Node node) {

        branch.nodeB = node;

        if (branch.nodeA == branch.nodeB && branch.nodeA != null && branch.nodeB != null) {
            World.branches.add(branch);
            graph.singleBranch = branch;
            Wire.builtSingle = true;
            return;
        }

        if (node.wires.get(0) != this) {
            if (!node.wires.get(0).isChecked) {
                node.wires.get(0).buildBranch(branch, graph);
            }

        } else if (node.wires.get(1) != this) {
            if (!node.wires.get(1).isChecked) {
                node.wires.get(1).buildBranch(branch, graph);
            }

        } else {
            throw new RuntimeException("Pizdets!!!");
        }

    }

    protected void buildGraph(Graph graph) {

        Branch branch = new Branch();
        this.buildGraph(graph, branch);
    }

    protected void buildGraph(Graph graph, Branch branch) {

        if (!this.isChecked) {
            this.isChecked = true;

            World.branches.add(branch);
            graph.originalBranches.add(branch);

            if (branch.nodeA != null && branch.nodeB != null) {
                return;
            }

            if (this.nodeA.wires.size() < this.nodeB.wires.size()) {
                this.whatToDoNext(graph, branch, this.nodeA);
                if (branch.nodeA != null && branch.nodeB != null) {
                    return;
                }
                this.whatToDoNext(graph, branch, this.nodeB);
            } else {
                this.whatToDoNext(graph, branch, this.nodeB);
                if (branch.nodeA != null && branch.nodeB != null) {
                    return;
                }

                this.whatToDoNext(graph, branch, this.nodeA);
            }

        }

    }

    private void whatToDoNext(Graph graph, Branch branch, Node node) {
        switch (node.wires.size()) {
            case 1:
                branch.wires.add(this);
                graph.nodes.add(node);

                if (branch.nodeA != null) {
                    branch.nodeB = node;

                    branch.nodeB.originalStructure.put(branch, branch.nodeA);
                    branch.nodeA.originalStructure.put(branch, branch.nodeB);
                } else {
                    branch.nodeA = node;
                }

                break;
            case 2:
                branch.wires.add(this);
                if (node.wires.get(0) != this) {
                    if (!node.wires.get(0).isChecked) {
                        node.wires.get(0).buildGraph(graph, branch);
                    } else {
                        //    throw new RuntimeException("Pizdets!!!");
                    }

                } else if (node.wires.get(1) != this) {
                    if (!node.wires.get(1).isChecked) {
                        node.wires.get(1).buildGraph(graph, branch);
                    } else {
                        //    throw new RuntimeException("Pizdets!!!");
                    }

                } else {
                    throw new RuntimeException("Pizdets!!!");
                }

                break;
            case 3:
            case 4:

                branch.wires.add(this);
                graph.nodes.add(node);

                if (branch.nodeA != null) {
                    branch.nodeB = node;

                    branch.nodeB.originalStructure.put(branch, branch.nodeA);
                    branch.nodeA.originalStructure.put(branch, branch.nodeB);
                } else {
                    branch.nodeA = node;
                }

                for (Wire wire : node.wires) {
                    if (wire != this && !wire.isChecked) {
                        wire.buildGraph(graph, new Branch());
                    }
                }

                break;
            default:
                throw new RuntimeException("Pizdets!!!");
        }
    }

    // starter for arranging. it is without nodeA verifying
    protected void rearrangeFromBranchFirst(Branch branch, Node lastNode, Node node) {
        this.directionalValues.remove(branch);

        if (this.isSource) {
            branch.sources.add(this);
        }

        if (this.nodeA.equals(lastNode)) {
            this.directionalValues.put(branch, '+');

        /*    Log.d("signn", "+");
            this.isSelected = true;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int a = 2;*/

            // try to find next wire in branch-wire-chain
            for (Wire wire : this.nodeB.wires) {
                if (branch.wires.contains(wire) && !wire.equals(this)) {

                    wire.rearrangeFromBranch(branch, this.nodeB, node);
                    return;
                }
            }

        } else if (this.nodeB.equals(lastNode)) {
            this.directionalValues.put(branch, '-');

        /*    Log.d("signn", "-");
            this.isSelected = true;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int a = 2;*/

            // try to find next wire in branch-wire-chain
            for (Wire wire : this.nodeA.wires) {
                if (branch.wires.contains(wire) && !wire.equals(this)) {

                    wire.rearrangeFromBranch(branch, this.nodeA, node);
                    return;
                }
            }

        } else {
            throw new RuntimeException("Pizdets!");
        }


    }

    private void rearrangeFromBranch(Branch branch, Node lastNode, Node node) {
        this.directionalValues.remove(branch);

        if (this.isSource) {
            branch.sources.add(this);
        } /*else if (this.type == WireType.RESISTOR) {
            branch.resistors.add((Resistor) this);
        } else if (this.type == WireType.CAPACITOR) {
            branch.capacitors.add((Capacitor) this);
        } else if (this.type == WireType.INDUCTOR) {
            branch.inductors.add((Inductor) this);
        }*/

        if (this.nodeA.equals(lastNode)) {
            this.directionalValues.put(branch, '+');

      /*      Log.d("signn", "+");
            this.isSelected = true;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int a = 2;*/

            if (this.nodeA.equals(node) || this.nodeB.equals(node)) {
                return;
            }



            // try to find next wire in branch-wire-chain
            for (Wire wire : this.nodeB.wires) {
                if (branch.wires.contains(wire) && !wire.equals(this)) {

                    wire.rearrangeFromBranch(branch, this.nodeB, node);
                    return;
                }
            }

        } else if (this.nodeB.equals(lastNode)) {
            this.directionalValues.put(branch, '-');

       /*     Log.d("signn", "-");
            this.isSelected = true;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int a = 2;*/

            if (this.nodeA.equals(node) || this.nodeB.equals(node)) {
                return;
            }

            // try to find next wire in branch-wire-chain
            for (Wire wire : this.nodeA.wires) {
                if (branch.wires.contains(wire) && !wire.equals(this)) {

                    wire.rearrangeFromBranch(branch, this.nodeA, node);
                    return;
                }
            }

        } else {
            throw new RuntimeException("Pizdets!");
        }
    }


}
