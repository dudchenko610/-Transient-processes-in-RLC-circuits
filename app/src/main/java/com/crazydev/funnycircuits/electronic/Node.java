package com.crazydev.funnycircuits.electronic;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crazydev.funnycircuits.io.Assets;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;
import com.crazydev.funnycircuits.rendering.Sprite;
import com.crazydev.funnycircuits.rendering.TexturedSprite;
import com.crazydev.funnycircuits.rendering.VertexBatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Node {

    protected VertexBatcher vertexBatcher;

    public    Vector2D location;
    protected ArrayList<Wire> wires;
    protected HashMap<Branch, Node> originalStructure;
    protected HashMap<Branch, Node> copyStructure;

    public HashSet<Branch> branches;

    protected Sprite nodeSprite;
    protected Sprite nodeSelectionSprite;

    protected boolean isChecked  = false;
    protected boolean isSelected = false;
    protected int hash_code = 0;

    public static Vector3D NODE_COLOR = new Vector3D(1, 0, 0);

    public Node(Vector2D location) {
        this.location          = location;
        this.wires             = new ArrayList<>();
        this.originalStructure = new HashMap<Branch, Node>();
        this.copyStructure     = new HashMap<Branch, Node>();
        this.branches          = new HashSet<Branch>();

        int x = (int) location.x + World.OFFSET;
        int y = (int) location.y + World.OFFSET;

        if (x < Short.MIN_VALUE || x > Short.MAX_VALUE || y < Short.MIN_VALUE || y > Short.MAX_VALUE) {
            throw new RuntimeException();
        }

        this.hash_code = (x << 16) | y;

        this.initializeSprites();
    }

    public Node(Vector2D location, int hash_code) {
        this.vertexBatcher     = VertexBatcher.getInstance();
        this.location          = location;
        this.wires             = new ArrayList<>();
        this.originalStructure = new HashMap<Branch, Node>();
        this.copyStructure     = new HashMap<Branch, Node>();
        this.branches          = new HashSet<Branch>();

        this.hash_code = hash_code;

        this.initializeSprites();
    }

    private void initializeSprites() {
        this.nodeSprite = new TexturedSprite(vertexBatcher, Assets.nodeRegion, this.location, 0.30f,0.30f);
        this.nodeSelectionSprite = new TexturedSprite(vertexBatcher, Assets.nodeSelectionRegion, this.location, 0.35f,0.35f);

    }

    public void draw() {
     //   OpenGLRenderer.VERTEX_BATCHER.addPoint(location, NODE_COLOR, 1);

        if (this.isSelected) {
            this.nodeSelectionSprite.draw();
        } else {
            this.nodeSprite.draw();
        }

  //      Log.d("jointt", "wires = " + this.wires.size());

    }

    // Maybe wrong because of changed
    @Override
    public boolean equals(Object o) {
        Node node = (Node) o;

        return this.hash_code == node.hash_code;
  //    return this.location.equals(node.location);

    }

    public void remove(World world, Wire wire) {
        this.wires.remove(wire);
        this.isSelected = false;
        if (this.wires.size() == 0) {
            world.nodes.remove(this.hash_code);
        }

    }

    @Override
    public int hashCode() {
        return this.hash_code;
    }

    private Set<Branch> keySet = new HashSet<>();

    public void buildTree(Graph graph, Node parent) {

        Node   child;
        Node  _parent = parent;

        this.keySet.clear();
        this.keySet.addAll(originalStructure.keySet());

        for (Branch branchKey : this.keySet) {
            child     = this.originalStructure.get(branchKey);

            if (child != parent) {
                if (!child.isChecked) {
                    child.isChecked = true;
                } else {
                    // remove
                    this.originalStructure.remove(branchKey);
                    child.originalStructure.remove(branchKey);
                    graph.puncturedBranches.add(branchKey);
                    graph.originalBranches.remove(branchKey);
                }
            } else {
                parent = null;
            }

        }

        this.keySet.clear();
        this.keySet.addAll(originalStructure.keySet());

        for (Branch branchKey : this.keySet) {
            child = this.originalStructure.get(branchKey);

            if (child != _parent) {
                child.buildTree(graph, this);
            }
        }

    }

}
