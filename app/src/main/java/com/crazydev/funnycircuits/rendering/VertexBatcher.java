package com.crazydev.funnycircuits.rendering;

import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.util.Constants;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;

public class VertexBatcher {

    private VertexBinder vertexBinder;
    private ShaderProgram shaderProgram;

    private float [] verticesLines;
    private float [] verticesPoints;

    private int vertLinesOffset  = 0;
    private int vertPointsOffset = 0;

    private int verts = 0;

    private static VertexBatcher instance;

    public static VertexBatcher getInstance() {
        if (instance == null) {
            instance = new VertexBatcher();
        }

        return instance;
    }

    private VertexBatcher() {
        this.shaderProgram = ShaderProgram.getInstance();
        this.vertexBinder  = VertexBinder.getInstance();

        this.verticesLines    = new float[Constants.SIZE_OF_VERTICES_ARRAY * 6 * 2];
        this.verticesPoints   = new float[Constants.SIZE_OF_VERTICES_ARRAY * 6 * 1];

    }

    public void addPoint(Vector2D position, Vector3D color, float alpha) {
        this.addPoint(position.x, position.y, color.x, color.y, color.z, alpha);
    }

    public void addPoint(float x, float y, Vector3D color, float alpha) {
        this.addPoint(x, y, color.x, color.y, color.z, alpha);
    }

    public void addPoint(float x, float y, float r, float g, float b, float a) {
        this.verticesPoints[vertPointsOffset ++] = x;
        this.verticesPoints[vertPointsOffset ++] = y;

        this.verticesPoints[vertPointsOffset ++] = r;
        this.verticesPoints[vertPointsOffset ++] = g;
        this.verticesPoints[vertPointsOffset ++] = b;
        this.verticesPoints[vertPointsOffset ++] = a;
    }

    public void addLine(Vector2D p1, Vector2D p2, Vector3D color1, Vector3D color2, float alpha) {
        this.verticesLines[vertLinesOffset ++] = p1.x;
        this.verticesLines[vertLinesOffset ++] = p1.y;

        this.verticesLines[vertLinesOffset ++] = color1.x;
        this.verticesLines[vertLinesOffset ++] = color1.y;
        this.verticesLines[vertLinesOffset ++] = color1.z;
        this.verticesLines[vertLinesOffset ++] = alpha;

        this.verticesLines[vertLinesOffset ++] = p2.x;
        this.verticesLines[vertLinesOffset ++] = p2.y;

        this.verticesLines[vertLinesOffset ++] = color2.x;
        this.verticesLines[vertLinesOffset ++] = color2.y;
        this.verticesLines[vertLinesOffset ++] = color2.z;
        this.verticesLines[vertLinesOffset ++] = alpha;
    }

    public void addLine(Vector2D p1, Vector2D p2, Vector3D color, float alpha) {
        this.addLine(p1.x, p1.y, p2.x, p2.y, color, alpha);
    }

    public void addLine(float p1_x, float p1_y, float p2_x, float p2_y, Vector3D color, float alpha) {
        this.verticesLines[vertLinesOffset ++] = p1_x;
        this.verticesLines[vertLinesOffset ++] = p1_y;

        this.verticesLines[vertLinesOffset ++] = color.x;
        this.verticesLines[vertLinesOffset ++] = color.y;
        this.verticesLines[vertLinesOffset ++] = color.z;
        this.verticesLines[vertLinesOffset ++] = alpha;

        this.verticesLines[vertLinesOffset ++] = p2_x;
        this.verticesLines[vertLinesOffset ++] = p2_y;

        this.verticesLines[vertLinesOffset ++] = color.x;
        this.verticesLines[vertLinesOffset ++] = color.y;
        this.verticesLines[vertLinesOffset ++] = color.z;
        this.verticesLines[vertLinesOffset ++] = alpha;
    }

    public void addLine(Vector2D p1, Vector2D p2, Vector3D color) {
        addLine(p1, p2, color, 1.0f);
    }

    public void addSpriteVerticesToVertexBatcherTextured(float[] vertices) {
        this.vertexBinder.setVerticesTextured(vertices);
        verts += 6;
    }

    public void depictPointsAndLines() {

        this.shaderProgram.setBoolFlag(true);
        this.shaderProgram.setMVPMatrix();

        this.vertexBinder.bindDataColor();
        this.vertexBinder.setVerticesOld(verticesPoints, 0, vertPointsOffset);
        this.vertexBinder.draw(GL_POINTS, vertPointsOffset / VertexBinder.STRIDE_COLORED);
        this.vertexBinder.unbindDataColor();

        this.vertexBinder.bindDataColor();
        this.vertexBinder.setVerticesOld(verticesLines, 0, vertLinesOffset);
        this.vertexBinder.draw(GL_LINES, vertLinesOffset / VertexBinder.STRIDE_COLORED);
        this.vertexBinder.unbindDataColor();

    }

    public void depictSpritesTextured(Texture texture) {

        this.shaderProgram.setBoolFlag(false);
        this.shaderProgram.setMVPMatrix();
        this.shaderProgram.setTexture(texture.texture);

        this.vertexBinder.bindDataTexture();

        this.vertexBinder.draw(GL_TRIANGLES, verts);
        this.vertexBinder.unbindDataTexture();

        verts = 0;
    }

    public void clearVerticesBufferTexture() {
        this.vertexBinder.clearVerticesBufferTexture();
        this.verts = 0;
    }

    public void clearVerticesBufferColor_Markers() {
        this.vertexBinder.clearVerticesBufferColor();
        this.vertLinesOffset  = 0;
        this.vertPointsOffset = 0;
    }

    public void clearVerticesBufferColor() {
        this.vertexBinder.clearVerticesBufferColor();
        this.verts = 0;
    }

    public void addSpriteVerticesToVertexBatcherColored(float[] vertices) {
        this.vertexBinder.setVerticesColored(vertices);
        this.verts += 6;
    }

    public void depictSpritesColored() {

        this.shaderProgram.setBoolFlag(true);
        this.shaderProgram.setMVPMatrix();

        this.vertexBinder.bindDataColor();

        this.vertexBinder.draw(GL_TRIANGLES, verts);
        this.vertexBinder.unbindDataColor();

        verts = 0;
    }

}

