package com.crazydev.funnycircuits.rendering;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;


import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.util.FileUtils;

import com.crazydev.funnycircuits.views.eventhandling.Input.TouchEvent;

import static android.opengl.GLES20.GL_TEXTURE5;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {

    public static final String U_MVPMATRIX    = "u_MVPMatrix";
    public static final String U_COLOR        = "u_Color";
    public static final String U_TEXTURE_UNIT = "u_TextureUnit";
    public static final String U_HASCOLOR     = "u_HasColor";

    public static final String A_POSITION     = "a_Position";
    public static final String A_COLOR        = "a_Color";

    public static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    private int program;

    private int uMVPmatrixLocation;
    private int uHasColorLocation;
    private int aTextureCoordinatesLocation;
    private int aPositionLocation;
    private int aColorLocation;
    private int uTextureUnitLocation;

    protected Vector2D position;
    protected float xComponent_ratio = 1;
    protected float yComponent_ratio = 1;
    protected float zoom = 1f;


    private float eyeX = 0;
    private float eyeY = 0;
    private float eyeZ = 1.0f;

    private float centerX = 0;
    private float centerY = 0;
    private float centerZ = 0;

    private float upX = 0f;
    private float upY = 1f;
    private float upZ = 0f;

    private final float[] V_matrix  = new float[16];
    private final float[] P_matrix  = new float[16];
    private final float[] VP_matrix = new float[16];

    protected float width;
    protected float height;

    public float ACTUAL_WIDTH;
    public float ACTUAL_HEIGHT;

    public float ACTUAL_START_WIDTH;
    public float ACTUAL_START_HEIGHT;

    private float parentViewWidth;
    private float parentViewHeight;

    public ShaderProgram() {
        this.position = new Vector2D(0, 0);
    }

    public void compileAndLinkGLProgram(Context context,  int vertexShaderResourceId, int fragmentShaderResourceId, int parentViewWidth, int parentViewHeight) {

        this.parentViewWidth  = parentViewWidth;
        this.parentViewHeight = parentViewHeight;

        // Compile the program and link the program
        program = ShaderHelper.buildProgram(FileUtils.readTextFileFromResource(context, vertexShaderResourceId),
                (FileUtils.readTextFileFromResource(context, fragmentShaderResourceId)));

        glUseProgram(program);

        uMVPmatrixLocation   = glGetUniformLocation(program, U_MVPMATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        uHasColorLocation    = glGetUniformLocation(program, U_HASCOLOR);

        aColorLocation       = glGetAttribLocation(program, A_COLOR);
        aPositionLocation    = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public int getPositionAttributeLocation () {

        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }

    public void setBoolFlag(boolean flag) {

        glUniform1i(uHasColorLocation, flag ? 1 : 0);
    }

    public void setMVPMatrix() {
        glUniformMatrix4fv(uMVPmatrixLocation, 1, false, VP_matrix, 0);
    }

    public void setTexture(int texture) {
        glActiveTexture(GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, texture);
        glUniform1i(uTextureUnitLocation, 5);
    }

    public float left, right, bottom, top;

    private void setViewportAndMatrices() {

        left    =   position.x / xComponent_ratio - (width * zoom) / 2.0f;
        left         *=   xComponent_ratio;
        right   =   position.x / xComponent_ratio + (width * zoom) / 2.0f;
        right        *=   xComponent_ratio;
        bottom  =   position.y / yComponent_ratio  - (height * zoom) / 2.0f;
        bottom       *=   yComponent_ratio;
        top     =   position.y / yComponent_ratio + (height * zoom) / 2.0f;
        top          *=   yComponent_ratio;

        ACTUAL_WIDTH  = right - left;
        ACTUAL_HEIGHT = top - bottom;

        // right - left
        // top - bottom

        float near = 1.0f;
        float far = 5.0f;

        Matrix.frustumM(P_matrix, 0, left, right, bottom, top, near, far);

        Matrix.setLookAtM(V_matrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        Matrix.multiplyMM(VP_matrix, 0, P_matrix, 0, V_matrix, 0);
    }

    public void setSides(float width, float height) {
        this.width  = width;
        this.height = height;

        this.setViewportAndMatrices();
    }

    public void setRatio(float horizontal_comp, float vertical_comp, float w, float h) {
        this.xComponent_ratio = horizontal_comp;
        this.yComponent_ratio = vertical_comp;

        this.ACTUAL_START_WIDTH  = horizontal_comp * width;
        this.ACTUAL_START_HEIGHT = vertical_comp * height;

        this.setViewportAndMatrices();
    }

    public void translateViewport(Vector2D delta) {
        this.position.add(delta);

        if (this.position.x >= World.OFFSET - 1000) {
            this.position.x = World.OFFSET - 1000;
        }

        if (this.position.x <= -World.OFFSET + 1000) {
            this.position.x = -World.OFFSET +1000;
        }

        if (this.position.y >= World.OFFSET - 1000) {
            this.position.y = World.OFFSET - 1000;
        }

        if (this.position.y <= -World.OFFSET + 1000) {
            this.position.y = -World.OFFSET + 1000;
        }


        this.setViewportAndMatrices();
    }

    public void setViewPort(Vector2D point) {
        this.position.set(point);
    }

    public void zoom(float delta) {

        float d = zoom + delta / 5;
        
        if (d > 6 || d < 0.2f) {
            return;
        }

        Log.d("log", "zoom = " + zoom);

        this.zoom = d;

     //   Log.d("update", "" + zoom);

        this.setViewportAndMatrices();
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public Vector2D getViewportPosition() {
        return this.position;
    }

    public void touchToWorld(TouchEvent event) {
        event.touchPosition.set((event.x / (float) parentViewWidth) * ACTUAL_START_WIDTH * zoom,
                                (1 - event.y / (float) parentViewHeight ) * ACTUAL_START_HEIGHT * zoom);
    }

    public void touchToWorld_no_zoom(TouchEvent event) {
        event.touchPosition.set((event.x / (float) parentViewWidth) * ACTUAL_START_WIDTH,
                (1 - event.y / (float) parentViewHeight ) * ACTUAL_START_HEIGHT);
    }

}
