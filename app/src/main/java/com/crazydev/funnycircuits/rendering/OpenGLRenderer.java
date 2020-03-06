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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.crazydev.funnycircuits.R;
import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.events.OnWireSelected;
import com.crazydev.funnycircuits.io.Assets;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.util.Constants;
import com.crazydev.funnycircuits.views.eventhandling.Input.TouchEvent;

import java.util.HashMap;
import java.util.List;

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

public class OpenGLRenderer extends GLSurfaceView  implements  GLSurfaceView.Renderer {

    private ShaderProgram shaderProgram;
    public static VertexBatcher VERTEX_BATCHER;
    private Context       context;
    private OpenGLRendererTouchEventListener openGLRendererTouchEventListener;

    private Object stateChanged = new Object();
    private Vector3D color = new Vector3D(1, 1, 1);
    enum    GLGameState {
        Running,
        Paused,
        Finished,
        Idle
    };

    private GLGameState state = GLGameState.Paused;
    private HashMap<String, Sprite> numbersSprites = new HashMap<String, Sprite>();

    private float [] axesVerts = {-20000, 0, 20000, 0, 0, -20000, 0, 20000};
    private Vector3D axesColor = new Vector3D(200 / 255.0f, 200 / 255.0f, 200 / 255.0f); // 128
    private Vector3D gridColor = new Vector3D(230 / 255.0f, 230 / 255.0f, 230 / 255.0f);
    private Vector2D touchedDown = new Vector2D();
    private Vector2D diff = new Vector2D();
    private Vector2D delta = new Vector2D();
    private boolean moving  = false;
    private boolean zooming = false;
    private float pLength = 0;

    private boolean isWireMode     = false;
    private boolean isWireSelected = false;
    private World electronicWorld;

    private boolean makeScreenShot = false;

    private Vector2D downSavedPos               = new Vector2D();
    private Vector2D upSavedPos                 = new Vector2D();
    private Vector3D creatingSuccesfulWireColor = new Vector3D(0,1,0);
    private Vector3D creatingWrongWireColor     = new Vector3D(1,0,0);
    private Vector3D translateWireColor         = new Vector3D(0,0,1);
    private boolean  isWireCreating             = false;
    private Vector2D savedWirePosition          = new Vector2D();
    private Vector2D newPosition                = new Vector2D();

    private boolean wireTranslateMode = false;

    private Handler handler;

    private Wire     selectedWire;
    private Vector2D selectedTranslatePoint = new Vector2D();
    private Vector3D creatingWireColor      = this.creatingSuccesfulWireColor;
    private boolean  toDeleteWire           = false;
    private Vector2D vec                    = new Vector2D();

    private OnWireSelected onWireSelected;

    private long time        = 0;
    private double deltaTime = 0;
    private double timeSum   = 0;
    private boolean handleTouchEvents = true;

    private Handler onLongClickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            showContextMenu();


        }
    };


    public void onContextMenuClosed() {
        this.handleTouchEvents = true;
    }

    public OpenGLRenderer (Context context, AttributeSet attribs) {
        super(context, attribs);

        this.context = context;
        this.setEGLContextClientVersion(2);
        this.setRenderer(this);

        this.openGLRendererTouchEventListener = new OpenGLRendererTouchEventListener();

        this.setOnTouchListener(openGLRendererTouchEventListener);

        int color = context.getResources().getColor(R.color.default_color_surface);

        int A = (color >> 24) & 0xff; // or color >>> 24
        int R = (color >> 16) & 0xff;
        int G = (color >>  8) & 0xff;
        int B = (color      ) & 0xff;

        this.color.x = R;
        this.color.y = G;
        this.color.z = B;

        Log.d("taggg", "OpenGLRenderer CONSTRUCTOR");

        this.shaderProgram = new ShaderProgram();
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

            VERTEX_BATCHER.clearVerticesBufferColor_Markers();
            VERTEX_BATCHER.clearVerticesBufferTexture();

            GLES20.glLineWidth(1.0f);

            if (this.handleTouchEvents) {
                this.handleTouchEvents();
            }


            this.depictDecartGrid();
            VERTEX_BATCHER.depictPointsAndLines();

            VERTEX_BATCHER.depictSpritesTextured(Assets.digits);


            this.electronicWorld.draw();


            VERTEX_BATCHER.clearVerticesBufferColor_Markers();

            GLES20.glLineWidth(2.0f);
            this.depictCreatingElement();
            VERTEX_BATCHER.depictPointsAndLines();


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


        if (this.isCountingToContextMenu) {
            this.timeSum += deltaTime;

            if (this.timeSum > .1) {
                this.timeSum = 0;

                this.handleTouchEvents = false;
                this.wireTranslateMode = false;
                this.isCountingToContextMenu = false;
                this.onLongClickHandler.sendEmptyMessage(0);
            }
        } else {
            this.timeSum = 0;
        }

    //    Log.d("translating", "translating = " + deltaTime + " timeSum = " + timeSum);

        this.deltaTime = (System.nanoTime() - this.time) / 1_000_000_000.0d;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        this.shaderProgram.compileAndLinkGLProgram(context, R.raw.vertex_shader, R.raw.fragment_shader, getWidth(), getHeight());
        this.shaderProgram.setSides(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        VERTEX_BATCHER = new VertexBatcher(shaderProgram, 200000);

        this.state = GLGameState.Running;

        Assets.load(context);

        Sprite sprite;

        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_m_b, new Vector2D(1, 1), 2, 4);
        numbersSprites.put("-", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_0_b, new Vector2D(1, 1), 2, 4);
        numbersSprites.put("0", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_1_b, new Vector2D(3, 1), 2, 4);
        numbersSprites.put("1", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_2_b, new Vector2D(5, 1), 2, 4);
        numbersSprites.put("2", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_3_b, new Vector2D(7, 1), 2, 4);
        numbersSprites.put("3", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_4_b, new Vector2D(9, 1), 2, 4);
        numbersSprites.put("4", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_5_b, new Vector2D(11, 1), 2, 4);
        numbersSprites.put("5", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_6_b, new Vector2D(13, 1), 2, 4);
        numbersSprites.put("6", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_7_b, new Vector2D(15, 1), 2, 4);
        numbersSprites.put("7", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_8_b, new Vector2D(17, 1), 2, 4);
        numbersSprites.put("8", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_9_b, new Vector2D(19, 1), 2, 4);
        numbersSprites.put("9", sprite);
        sprite = new TexturedSprite(VERTEX_BATCHER, Assets.digitsRegion_p_b, new Vector2D(21, 1), 2, 4);
        numbersSprites.put("p", sprite);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.electronicWorld = new World();

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


    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);

        MenuInflater menuInflater = new MenuInflater(context);

        if (this.isWireSelected) {
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

            VERTEX_BATCHER.addLine(i, y_bottom, i, y_top, gridColor, 1.0f);
        }

        for (int i = y_b; i <= y_t; i ++) {
         /*   gridVerts[gridLen ++] = x_left;
            gridVerts[gridLen ++] = i;
            gridVerts[gridLen ++] = x_right;
            gridVerts[gridLen ++] = i;*/

            VERTEX_BATCHER.addLine(x_left, i, x_right, i, gridColor, 1.0f);

        }

    //   vertexBatcher.depictCurve(gridColor, 1.0f, gridVerts, gridLen, GL_LINES);
        VERTEX_BATCHER.addLine(axesVerts[0], axesVerts[1], axesVerts[2], axesVerts[3], axesColor, 1.0f);
        VERTEX_BATCHER.addLine(axesVerts[4], axesVerts[5], axesVerts[6], axesVerts[7], axesColor, 1.0f);

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

    private boolean isCountingToContextMenu = false;

    private void handleTouchEvents() {
        List<TouchEvent> touchEvents = openGLRendererTouchEventListener.getTouchEvents();

        int max = 0;

        for (int i = 0; i < touchEvents.size(); i ++) {
            if (touchEvents.get(i).pointer > max) {
                max = touchEvents.get(i).pointer;
            }
        }

        if (max > 1) {
            this.isCountingToContextMenu = false;
            moving  = false;
            zooming = false;
            return;
        }

        if (max == 1) {
            this.isCountingToContextMenu = false;
            moving = false;

            if (touchEvents.size() > 1) {
                for (int i = 0; i < touchEvents.size(); i += 2) {

                    if (i + 1 == touchEvents.size()) {
                        break;
                    }

                    TouchEvent event_0 = touchEvents.get(i + 0);
                    TouchEvent event_1 = touchEvents.get(i + 1);

                    shaderProgram.touchToWorld_no_zoom(event_0);
                    shaderProgram.touchToWorld_no_zoom(event_1);

                    diff.set(event_0.touchPosition).subtract(event_1.touchPosition);

                    if (!zooming) {
                        pLength = diff.length();
                        zooming = true;
                        continue;
                    }

                    float t = diff.length();

         //           Log.d("update", "" + t + " k " + event_0.pointer + " " + event_1.pointer);
                    shaderProgram.zoom(pLength - t);

                    pLength = t;

                }
            }

            return;
        }

        zooming = false;

        if (max == 0 && !this.isWireMode) {

            for (int i = 0; i < touchEvents.size(); i ++) {
                TouchEvent event = touchEvents.get(i);
                shaderProgram.touchToWorld(event);

                if (event.type == TouchEvent.TOUCH_DOWN) {
                    this.isCountingToContextMenu = true;

                    touchedDown.set(event.touchPosition);

                    float x = this.shaderProgram.position.x  - (this.shaderProgram.right - this.shaderProgram.left) / 2.0f + event.touchPosition.x ;
                    float y = this.shaderProgram.position.y  - (this.shaderProgram.top - this.shaderProgram.bottom) / 2.0f + event.touchPosition.y;

                    int _x = Math.round(x);
                    int _y = Math.round(y);

                    this.downSavedPos.set(event.touchPosition);

                    Wire wire;
                    if ((wire = this.electronicWorld.tryToSelect(new Vector2D(x, y))) != null) {
                        this.deleteButtonActivate();

                        if (this.onWireSelected != null) {
                            this.onWireSelected.onDeselect();
                            this.onWireSelected.onSelect(wire);
                            this.isWireSelected = true;
                        }

                        this.wireTranslateMode = true;
                        this.isCountingToContextMenu = true;
                        this.creatingWireColor = this.translateWireColor;
                        this.selectedTranslatePoint.set(_x, _y);
                        this.selectedWire = wire;

                        this.savedWirePosition.set(this.selectedWire.nodeA.location);

                        this.newPosition.set(this.selectedWire.nodeB.location);

                        continue;
                    }

                    moving = true;
                }

                if (event.type == TouchEvent.TOUCH_UP) {
                    this.isCountingToContextMenu = false;

                    touchedDown.set(event.touchPosition);
                    moving = false;

                    this.upSavedPos.set(event.touchPosition);

                    if (this.wireTranslateMode) {
                        this.wireTranslateMode = false;
                        this.creatingWireColor = this.creatingSuccesfulWireColor;

                        if (!this.savedWirePosition.equals(this.selectedWire.nodeA.location) && !this.newPosition.equals(this.selectedWire.nodeB.location)) {
                            if (this.electronicWorld.checkWire(this.selectedWire, savedWirePosition, newPosition)) {

                                if (this.onWireSelected != null) {
                                    this.onWireSelected.onDeselect();
                                    this.isWireSelected = false;
                                }

                                this.selectedWire.remove();
                                this.electronicWorld.createWire(this.selectedWire.type, this.savedWirePosition, this.newPosition, this.selectedWire.orientation);
                            }
                        }

                    } else {
                        if (this.upSavedPos.subtract(this.downSavedPos).lengthSquared() < 0.01f) {
                            this.electronicWorld.deselect();

                            if (this.onWireSelected != null) {
                                this.onWireSelected.onDeselect();
                                this.isWireSelected = false;
                            }

                            this.deleteButtonDeactivate();
                        }
                    }
                }

                if (event.type == TouchEvent.TOUCH_DRAGGED) {
                    if (this.moving) {
                        delta.set(touchedDown).subtract(event.touchPosition);
                        touchedDown.set(event.touchPosition);
                        shaderProgram.translateViewport(delta);

                        if (delta.lengthSquared() > 0.0008) {
                            this.isCountingToContextMenu = false;
                        }

                    } else if (this.wireTranslateMode) {
                        // wire translate logic

                        float x = this.shaderProgram.position.x  - (this.shaderProgram.right - this.shaderProgram.left) / 2.0f + event.touchPosition.x;
                        float y = this.shaderProgram.position.y  - (this.shaderProgram.top - this.shaderProgram.bottom) / 2.0f + event.touchPosition.y;

                        int _x = Math.round(x);
                        int _y = Math.round(y);

                        this.vec.set(this.savedWirePosition);

                        this.savedWirePosition.set(this.selectedWire.nodeA.location).add(_x - this.selectedTranslatePoint.x,
                                                                                         _y - this.selectedTranslatePoint.y);

                        this.newPosition.set(this.selectedWire.nodeB.location.copy()).add(_x - this.selectedTranslatePoint.x,
                                                                                          _y - this.selectedTranslatePoint.y);

                        if (!this.vec.equals(this.savedWirePosition)) {

                            this.isCountingToContextMenu = false;

                            if (this.electronicWorld.checkWire(this.selectedWire, savedWirePosition, newPosition)) {
                                this.creatingWireColor = this.translateWireColor;
                            } else {
                                this.creatingWireColor = this.creatingWrongWireColor;
                            }
                        }

                     //   Log.d("translating", "translating = " + deltaTime + " timeSum = " + timeSum);
                    }


                }

             //   shaderProgram.touchToWorld_no_zoom(event);
             //   Log.d("logggg", "x = " + event.touchPosition.x + " y = " + event.touchPosition.y);

            }
        } else {
            this.moving = false;

            for (int i = 0; i < touchEvents.size(); i ++) {
                TouchEvent event = touchEvents.get(i);
                shaderProgram.touchToWorld(event);

                float x = this.shaderProgram.position.x  - (this.shaderProgram.right - this.shaderProgram.left) / 2.0f + event.touchPosition.x ;
                float y = this.shaderProgram.position.y  - (this.shaderProgram.top - this.shaderProgram.bottom) / 2.0f + event.touchPosition.y;

                int _x = Math.round(x);
                int _y = Math.round(y);

                if (event.type == TouchEvent.TOUCH_DOWN) {
                    this.isCountingToContextMenu = false;

                    this.savedWirePosition.set(_x, _y);
                    this.newPosition.set(this.savedWirePosition);
                    this.isWireCreating = true;
                    this.creatingWireColor = this.creatingSuccesfulWireColor;
                    continue;
                }

                if (event.type == TouchEvent.TOUCH_UP) {
                    this.isCountingToContextMenu = false;

                    this.isWireCreating = false;
                    Vector2D nP = new Vector2D(_x, _y);
                    Vector2D diff = savedWirePosition.copy().subtract(nP);

                    diff.x = Math.abs(diff.x);
                    diff.y = Math.abs(diff.y);

                    if (!diff.isNullLength()) {
                        if (diff.x >= diff.y) {
                            nP.y = savedWirePosition.y;
                        } else {
                            nP.x = savedWirePosition.x;
                        }
                    } else {
                        continue;
                    }

                    this.electronicWorld.createWire(savedWirePosition, nP);
                }

                this.newPosition.set(_x, _y);
                Vector2D diff = savedWirePosition.copy().subtract(newPosition);

                diff.x = Math.abs(diff.x);
                diff.y = Math.abs(diff.y);

                if (!diff.isNullLength()) {
                    if (diff.x >= diff.y) {
                        newPosition.y = savedWirePosition.y;
                    } else {
                        newPosition.x = savedWirePosition.x;
                    }
                } else {
                    this.creatingWireColor = this.creatingSuccesfulWireColor;
                    continue;
                }

                if (this.electronicWorld.checkWire(newPosition, savedWirePosition)) {
                    this.creatingWireColor = this.creatingSuccesfulWireColor;

                } else {
                    this.creatingWireColor = this.creatingWrongWireColor;
                }


            }

        }

    }

    public void deleteSelectedWires() {
        if (this.toDeleteWire) {
            this.electronicWorld.deleteSelectedWires();
            this.deleteButtonDeactivate();
        }
    }

    private void deleteButtonActivate() {
        this.toDeleteWire = true;
        Message message = new Message();
        message.what = 1;
        this.handler.sendMessage(message);
    }

    private void deleteButtonDeactivate() {
        this.toDeleteWire = false;

        Message message = new Message();
        message.what = 0;
        this.handler.sendMessage(message);
    }

    public void setDeleteWireButton(Handler handler) {
        this.handler = handler;
    }

    private void depictCreatingElement() {

        if ((this.isWireMode && !this.moving && this.isWireCreating && creatingWireColor != null) || (this.wireTranslateMode && !this.savedWirePosition.equals(this.selectedWire.nodeA.location)
                                                                                                                             && !this.newPosition.equals(this.selectedWire.nodeB.location))) {

            VERTEX_BATCHER.addPoint(savedWirePosition.x, savedWirePosition.y, creatingWireColor, 1);
            VERTEX_BATCHER.addPoint(newPosition.x, newPosition.y, creatingWireColor, 1);
            VERTEX_BATCHER.addLine(this.savedWirePosition, this.newPosition, this.creatingWireColor);

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

    public void onResume() {
        synchronized(stateChanged) {
            state = GLGameState.Running;
        }
    }

    public void makeScreenShot() {
        this.makeScreenShot = true;
    }

    public void setWireMode(boolean wireMode) {
        this.isWireMode = wireMode;
        this.wireTranslateMode = false;

        if (this.electronicWorld != null) {
            this.electronicWorld.deselect();
        }

    }

    public void setOnWireSelected(OnWireSelected onWireSelected) {
        this.onWireSelected = onWireSelected;
    }

    public World getElectronicWorld() {
        return this.electronicWorld;
    }

    public ShaderProgram getShaderProgram() {
        return this.shaderProgram;
    }

    public VertexBatcher getVertexBatcher() {
        return this.VERTEX_BATCHER;
    }

}