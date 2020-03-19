package com.crazydev.funnycircuits.rendering;

import com.crazydev.funnycircuits.util.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

public class VertexBinder {

    private ShaderProgram shaderProgram;

    private IntBuffer intBufferColored;
    private IntBuffer intBufferTextured;

    private int[] tempBuffer;

    public static final int STRIDE_TEXTURED = Constants.POSITION_COMPONENT_COUNT_2D + Constants.TEXTURE_COORDINATES_COMPONENT_COUNT;
    public static final int STRIDE_COLORED  = Constants.POSITION_COMPONENT_COUNT_2D + Constants.COLOR_COMPONENT_COUNT;

    private int STRIDE_C = STRIDE_COLORED * Constants.BYTES_PER_FLOAT;
    private int STRIDE_T = STRIDE_TEXTURED * Constants.BYTES_PER_FLOAT;

    private static VertexBinder instance;

    public static VertexBinder getInstance() {
        if (instance == null) {
            instance = new VertexBinder();
        }

        return instance;
    }

    private VertexBinder() {
        int sizeOfVerticesArray = Constants.SIZE_OF_VERTICES_ARRAY;

        this.shaderProgram = ShaderProgram.getInstance();
        this.tempBuffer = new int[sizeOfVerticesArray];

        intBufferColored = ByteBuffer
                .allocateDirect(sizeOfVerticesArray * Integer.SIZE)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();

        intBufferTextured = ByteBuffer
                .allocateDirect(sizeOfVerticesArray * Integer.SIZE)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();

    }

    public void setVerticesOld(float[] vertices, int offset, int length) {
        this.intBufferColored.clear();
        int len = offset + length;
        for (int i = offset, j = 0; i < len; i++, j++) {
            tempBuffer[j] = Float.floatToRawIntBits(vertices[i]);
        }

        this.intBufferColored.put(tempBuffer, offset, length);
    }

    public void setVerticesColored(float[] vertices) {

        for (int i = 0; i < vertices.length; i++) {
            tempBuffer[i] = Float.floatToRawIntBits(vertices[i]);
        }

        this.intBufferColored.put(tempBuffer, 0, vertices.length);
    }

    public void setVerticesTextured(float[] vertices) {

        for (int i = 0; i < vertices.length; i++) {
            tempBuffer[i] = Float.floatToRawIntBits(vertices[i]);
        }

        this.intBufferTextured.put(tempBuffer, 0, vertices.length);
    }

    public void bindDataColor() {

        int offset = 0;
        intBufferColored.position(offset);
        glVertexAttribPointer(shaderProgram.getPositionAttributeLocation(), Constants.POSITION_COMPONENT_COUNT_2D, GL_FLOAT, false, STRIDE_C, intBufferColored);
        glEnableVertexAttribArray(shaderProgram.getPositionAttributeLocation());
        offset += Constants.POSITION_COMPONENT_COUNT_2D;

        intBufferColored.position(offset);
        glVertexAttribPointer(shaderProgram.getColorAttributeLocation(), Constants.COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE_C, intBufferColored);
        glEnableVertexAttribArray(shaderProgram.getColorAttributeLocation());

    }

    public void bindDataTexture() {

        int offset = 0;
        intBufferTextured.position(offset);
        glVertexAttribPointer(shaderProgram.getPositionAttributeLocation(), Constants.POSITION_COMPONENT_COUNT_2D, GL_FLOAT, false, STRIDE_T, intBufferTextured);
        glEnableVertexAttribArray(shaderProgram.getPositionAttributeLocation());
        offset += Constants.POSITION_COMPONENT_COUNT_2D;

        intBufferTextured.position(offset);
        glVertexAttribPointer(shaderProgram.getTextureCoordinatesAttributeLocation(), Constants.TEXTURE_COORDINATES_COMPONENT_COUNT, GL_FLOAT, false, STRIDE_T, intBufferTextured);
        glEnableVertexAttribArray(shaderProgram.getTextureCoordinatesAttributeLocation());

    }

    public void draw(int primiriveType, int numVertices) {
        glDrawArrays(primiriveType, 0, numVertices);

    }

    public void unbindDataColor() {
        glDisableVertexAttribArray(shaderProgram.getPositionAttributeLocation());
        glDisableVertexAttribArray(shaderProgram.getColorAttributeLocation());

    }

    public void unbindDataTexture() {
        glDisableVertexAttribArray(shaderProgram.getPositionAttributeLocation());
        glDisableVertexAttribArray(shaderProgram.getTextureCoordinatesAttributeLocation());
    }

    public void clearVerticesBufferTexture() {
        this.intBufferTextured.clear();
    }

    public void clearVerticesBufferColor() {
        this.intBufferColored.clear();
    }




}

