package com.crazydev.funnycircuits.rendering;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;

import com.crazydev.funnycircuits.R;
import com.crazydev.funnycircuits.SchemaActivity;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.events.OnWireSelectedListener;
import com.crazydev.funnycircuits.io.Assets;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.interfaces.ITouchEventService;
import com.crazydev.funnycircuits.rendering.services.TouchEventService;
import com.crazydev.funnycircuits.util.Constants;

import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;



public class OpenGLRenderer extends GLSurfaceView  implements  GLSurfaceView.Renderer, ITouchEventService.OnTouchEventsListener{

    enum GLGameState {
        Running,
        Paused,
        Finished,
        Idle
    }

    private SchemaActivity.UIThreadSynchronizer uiThreadSynchronizer;
    private ITouchEventService touchEventService;

    private OpenGLRendererTouchEventListener openGLRendererTouchEventListener;
    private ShaderProgram shaderProgram;
    public  VertexBatcher vertexBatcher;
    private Context       context;


    private Object stateChanged = new Object();
    private Vector3D color = new Vector3D(1, 1, 1);

    private GLGameState state = GLGameState.Paused;
    private HashMap<String, Sprite> numbersSprites = new HashMap<String, Sprite>();

    private float [] axesVerts = {-20000, 0, 20000, 0, 0, -20000, 0, 20000};
    private Vector3D axesColor = new Vector3D(200 / 255.0f, 200 / 255.0f, 200 / 255.0f); // 128
    private Vector3D gridColor = new Vector3D(230 / 255.0f, 230 / 255.0f, 230 / 255.0f);

    private World electronicWorld;

    private boolean makeScreenShot = false;

    // context menu related vars
    private long time        = 0;
    private double deltaTime = 0;

 //   private Handler handler;

    private boolean toDeleteWire = false;

    private Handler onLongClickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            showContextMenu();


        }
    };


    public OpenGLRenderer (Context context, AttributeSet attribs) {
        super(context, attribs);

        this.context = context;
        this.setEGLContextClientVersion(2);
        this.setRenderer(this);

        this.openGLRendererTouchEventListener = new OpenGLRendererTouchEventListener();
        this.setOnTouchListener(openGLRendererTouchEventListener);

        this.touchEventService = new TouchEventService(this.openGLRendererTouchEventListener, this);

        int color = context.getResources().getColor(R.color.default_color_surface);

        int A = (color >> 24) & 0xff; // or color >>> 24
        int R = (color >> 16) & 0xff;
        int G = (color >>  8) & 0xff;
        int B = (color      ) & 0xff;

        this.color.x = R;
        this.color.y = G;
        this.color.z = B;

        this.shaderProgram = ShaderProgram.getInstance();
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(color.x, color.y, color.z, 1f);

        this.time = System.nanoTime();

        GLGameState state = null;
        synchronized(stateChanged) {
            state = this.state;
        }

        if (state == GLGameState.Running) {

            vertexBatcher.clearVerticesBufferColor_Markers();
            vertexBatcher.clearVerticesBufferTexture();

            GLES20.glLineWidth(1.0f);


            // handle
            this.touchEventService.handleTouchEvents();


            this.depictDecartGrid();
            vertexBatcher.depictPointsAndLines();
            vertexBatcher.depictSpritesTextured(Assets.digits);
            this.electronicWorld.draw();

            vertexBatcher.clearVerticesBufferColor_Markers();

            GLES20.glLineWidth(2.0f);
            this.touchEventService.depictCreatingElement();
            vertexBatcher.depictPointsAndLines();



            if (makeScreenShot) {
                this.makeScreenShot = false;
                try {
              //      ViewShot.makeGLSurfaceViewShot(this, gl, this.context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (state == GLGameState.Paused) {

            synchronized(stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }

        if (state == GLGameState.Finished) {

            synchronized(stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }

        this.touchEventService.contextMenuCountDown(this.deltaTime);
        // contextMenuCountdown()

        this.deltaTime = (System.nanoTime() - this.time) / 1_000_000_000.0d;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        this.shaderProgram.compileAndLinkGLProgram(context, R.raw.vertex_shader, R.raw.fragment_shader, getWidth(), getHeight());
        this.shaderProgram.setSides(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        this.vertexBatcher = VertexBatcher.getInstance();

        this.state = GLGameState.Running;

        Assets.load(context);

        Sprite sprite;

        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_m_b, new Vector2D(1, 1), 2, 4);
        numbersSprites.put("-", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_0_b, new Vector2D(1, 1), 2, 4);
        numbersSprites.put("0", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_1_b, new Vector2D(3, 1), 2, 4);
        numbersSprites.put("1", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_2_b, new Vector2D(5, 1), 2, 4);
        numbersSprites.put("2", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_3_b, new Vector2D(7, 1), 2, 4);
        numbersSprites.put("3", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_4_b, new Vector2D(9, 1), 2, 4);
        numbersSprites.put("4", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_5_b, new Vector2D(11, 1), 2, 4);
        numbersSprites.put("5", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_6_b, new Vector2D(13, 1), 2, 4);
        numbersSprites.put("6", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_7_b, new Vector2D(15, 1), 2, 4);
        numbersSprites.put("7", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_8_b, new Vector2D(17, 1), 2, 4);
        numbersSprites.put("8", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_9_b, new Vector2D(19, 1), 2, 4);
        numbersSprites.put("9", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_p_b, new Vector2D(21, 1), 2, 4);
        numbersSprites.put("p", sprite);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.electronicWorld = World.getInstance();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        float ratio = 1f;

        float verticalRatio, horizontalRatio;

        if (width > height) {
            ratio = (float) width / height;

            horizontalRatio = ratio;
            verticalRatio = 1;

        } else {
            ratio = (float) height / width;

            horizontalRatio = 1;
            verticalRatio = ratio;

        }

        shaderProgram.setRatio(horizontalRatio, verticalRatio, width, height);

    }


    public void onResume() {
        super.onResume();
        synchronized(stateChanged) {
            state = GLGameState.Running;
        }
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);

        MenuInflater menuInflater = new MenuInflater(context);

        if (this.touchEventService.isWireSelected()) {
            menuInflater.inflate(R.menu.menu_options_selected, menu);
        } else {
            menuInflater.inflate(R.menu.menu_options_unselected, menu);
        }

    }

    private void depictDecartGrid() {

        float w = shaderProgram.ACTUAL_WIDTH;
        float h = shaderProgram.ACTUAL_HEIGHT;

        float x_c = shaderProgram.left   + (shaderProgram.right - shaderProgram.left  ) / 2;
        float y_c = shaderProgram.bottom + (shaderProgram.top   - shaderProgram.bottom) / 2;

        float x_left  = x_c - w / 2.0f;
        float x_right = x_c + w / 2.0f;

        float y_bottom = y_c - h / 2.0f;
        float y_top    = y_c + h / 2.0f;

        int x_l = ((int) x_left)  + (x_left < 0  ? 0 : 1);
        int x_r = ((int) x_right) + (x_right < 0 ? 1 : 0);

        int y_b = ((int) y_bottom) + (y_bottom < 0  ? 0 : 1);
        int y_t = ((int) y_top)    + (y_top < 0 ? 1 : 0);

        int gridLen = 0;
        for (int i = x_l; i <= x_r; i ++) {
      /*      gridVerts[gridLen ++] = i;
            gridVerts[gridLen ++] = y_bottom;
            gridVerts[gridLen ++] = i;
            gridVerts[gridLen ++] = y_top;*/

            vertexBatcher.addLine(i, y_bottom, i, y_top, gridColor, 1.0f);
        }

        for (int i = y_b; i <= y_t; i ++) {
         /*   gridVerts[gridLen ++] = x_left;
            gridVerts[gridLen ++] = i;
            gridVerts[gridLen ++] = x_right;
            gridVerts[gridLen ++] = i;*/

            vertexBatcher.addLine(x_left, i, x_right, i, gridColor, 1.0f);

        }

    //   vertexBatcher.depictCurve(gridColor, 1.0f, gridVerts, gridLen, GL_LINES);
        vertexBatcher.addLine(axesVerts[0], axesVerts[1], axesVerts[2], axesVerts[3], axesColor, 1.0f);
        vertexBatcher.addLine(axesVerts[4], axesVerts[5], axesVerts[6], axesVerts[7], axesColor, 1.0f);

   //    vertexBatcher.depictCurve(axesColor, 1.0f, axesVerts, axesVerts.length, GL_LINES);

       float r_x = shaderProgram.ACTUAL_WIDTH  / 70;
       float r_y = shaderProgram.ACTUAL_HEIGHT / 70;

       if (r_x > r_y) {
           r_y *= 1.8f;
           r_x = 0.7f * r_y;
       } else {
           r_y = r_x / 0.7f;
       }


       int prec = -100000;

       for (int i = y_b - 1; i <= y_t + 1; i ++) {

           if (i == 0) {
               continue;
           }

           int j = Math.abs(i);
           double piParrams = (j / Math.PI) - ((int) (j / Math.PI)) ;
           int p_num = (int) (i / Math.PI);
           String p_label = p_num + "p";

           String num = String.valueOf(i);
           float x_offset = 0;

           if (x_right - num.length() * r_x - r_x < 0) {
               x_offset = x_right - num.length() * r_x ;
           }

           if (x_left > 0) {
               x_offset = x_left + r_x;
           }

           if (x_left < 0 && x_right - num.length() * r_x - r_x > 0) {
               x_offset = r_x;
           }

           float n_p_offset = 0;
           float p_offset   = -1.0f * p_label.length() * r_x - r_x;

           if (x_left + p_label.length() * r_x + r_x > 0) {
               n_p_offset = x_left + 1.1f *r_x + r_x + num.length() * r_x;

               p_offset = n_p_offset;

           } else {
               p_offset += x_offset;

           }


           if (shaderProgram.getZoom() > 3) {
               if (i % 10 == 0) {
                   depictNumber(num, x_offset, i + r_y / 1.2f, r_x, r_y);
               }

               continue;
           }

           if (shaderProgram.getZoom() > 2) {
               if (i % 5 == 0) {
                   depictNumber(num, x_offset, i + r_y / 1.2f, r_x, r_y);

               }

               continue;
           }

           if (shaderProgram.getZoom() > 1) {
               if (i % 2 == 0) {
                   depictNumber(num, x_offset, i + r_y / 1.2f, r_x, r_y);

               }

               continue;
           } else {

               depictNumber(num, x_offset, i + r_y / 1.2f, r_x, r_y);
           }


       }

       prec = -10000;

       for (int i = x_l - 1; i <= x_r + 1; i ++) {

           if (i == 0) {
               continue;
           }

           String num = String.valueOf(i);

           int j = Math.abs(i);
           double piParrams = (j / Math.PI) -  ((int) (j / Math.PI)) ;
           int p_num = (int) (i / Math.PI);
           String p_label = p_num + "p";

           float y_offset = 0;
           float r_y_2 = r_y / 1.2f;

           if (y_top - num.length() * r_y_2 - r_y_2 < 0) {
               y_offset = y_top -  r_y_2 ;
           }

           if (y_bottom > 0) {
               y_offset = y_bottom + r_y_2;
           }

           if (y_bottom < 0 && y_top - r_y_2 - r_y_2 > 0) {
               y_offset = r_y_2;
           }


           float n_p_offset = 0;
           float p_offset   = y_offset;

           if (y_bottom + r_y > 0) {
               n_p_offset = y_bottom +  4*r_y;

               p_offset = n_p_offset;

           } else {
               p_offset = y_offset;
           }

           if (shaderProgram.getZoom() > 3) {
               if (i % 10 == 0) {
                   depictNumber(num, i + r_x / 1f, y_offset, r_x, r_y);
               }


               continue;
           }

           if (shaderProgram.getZoom() > 2) {
               if (i % 5 == 0) {
                   depictNumber(num, i + r_x / 1f, y_offset, r_x, r_y);
               }


               continue;
           }

           if (shaderProgram.getZoom() > 1) {
               if (i % 2 == 0) {
                   depictNumber(num, i + r_x / 1f, y_offset, r_x, r_y);
               }


               continue;
           }

           depictNumber(num, i + r_x / 1f, y_offset, r_x, r_y);

       }

       depictNumber("0", r_x, r_y, r_x, r_y);

    }


    public void deleteSelectedWires() {
        if (this.toDeleteWire) {
            this.electronicWorld.deleteSelectedWires();
            this.onDeleteButtonStateChanged(false);
        }
    }


    private void depictNumber(String num, float x, float y, float w, float h) {

        float offset = 0;
        Sprite sprite;
        for (int i = 0; i < num.length(); i++) {

            char c = num.charAt(i);

       /*     if (c == 'p') {
                sprite = numbersSprites.get("p");
                sprite.setPosition(x + w / 2+ offset - w / 2.2f, y, w, h);
                sprite.draw();
                continue;
            }*/

            sprite = numbersSprites.get(String.valueOf(c));

            if (num.charAt(i) == '-') {
                sprite.setPosition(x + offset - w / 2.2f, y, w / 1.8f, h / 12);
                offset += w / 2.5f;
            } else {
                sprite.setPosition(x + offset, y, w, h);
                offset += w;
            }

            sprite.draw();
        }
    }

    @Override
    public void onShowContextMenu() {
        this.onLongClickHandler.sendEmptyMessage(0); // show
    }

    @Override
    public void onDeleteButtonStateChanged(boolean state) {
        this.toDeleteWire = state;
        this.uiThreadSynchronizer.setDeleteButtonState(state);
    }


    public void setUIThreadSynchronizer(SchemaActivity.UIThreadSynchronizer uiThreadSynchronizer) {
        this.uiThreadSynchronizer = uiThreadSynchronizer;
    }

    public ITouchEventService getTouchEventService() {
        return this.touchEventService;
    }

    public void makeScreenShot() {
        this.makeScreenShot = true;
    }


}