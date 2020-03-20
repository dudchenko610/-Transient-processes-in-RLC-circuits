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
import com.crazydev.funnycircuits.rendering.interfaces.IGLContentService;
import com.crazydev.funnycircuits.rendering.interfaces.ITouchEventService;
import com.crazydev.funnycircuits.rendering.services.GLContentService;
import com.crazydev.funnycircuits.rendering.services.TouchEventService;
import com.crazydev.funnycircuits.rendering.services.simulating.GLContentServiceSimulating;
import com.crazydev.funnycircuits.rendering.services.simulating.TouchEventServiceSimulating;
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
    private IGLContentService glContentService;

    private ITouchEventService touchEventServiceEditing;
    private ITouchEventService touchEventServiceSimulating;

    private IGLContentService glContentServiceEditing;
    private IGLContentService glContentServiceSimulating;

    private OpenGLRendererTouchEventListener openGLRendererTouchEventListener;
    private ShaderProgram shaderProgram;
    public  VertexBatcher vertexBatcher;
    private Context       context;


    private Object stateChanged = new Object();
    private Vector3D color = new Vector3D(1, 1, 1);

    private GLGameState state = GLGameState.Paused;


    private World electronicWorld;

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

        this.touchEventServiceEditing = new TouchEventService(this.openGLRendererTouchEventListener, this);
        this.touchEventServiceSimulating = new TouchEventServiceSimulating(this.openGLRendererTouchEventListener, this);

        this.glContentServiceEditing = new GLContentService();
        this.glContentServiceSimulating = new GLContentServiceSimulating();

        this.setEditingMode();

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

    public void setSimulatingMode() {
        this.touchEventService = this.touchEventServiceSimulating;
        this.glContentService = this.glContentServiceSimulating;
    }

    public void setEditingMode() {
        this.touchEventService = this.touchEventServiceEditing;
        this.glContentService = this.glContentServiceEditing;
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


            // draw content
            this.glContentService.drawContent();

            this.electronicWorld.draw();

            vertexBatcher.clearVerticesBufferColor_Markers();

            GLES20.glLineWidth(2.0f);
            this.touchEventService.depictCreatingElement();
            vertexBatcher.depictPointsAndLines();


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

        this.deltaTime = (System.nanoTime() - this.time) / 1_000_000_000.0d;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        this.shaderProgram.compileAndLinkGLProgram(context, R.raw.vertex_shader, R.raw.fragment_shader, getWidth(), getHeight());
        this.shaderProgram.setSides(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        this.vertexBatcher = VertexBatcher.getInstance();

        this.state = GLGameState.Running;

        Assets.load(context);

        this.glContentServiceEditing.initialize();
        this.glContentServiceSimulating.initialize();

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

    public void deleteSelectedWires() {
        if (this.toDeleteWire) {
            this.electronicWorld.deleteSelectedWires();
            this.onDeleteButtonStateChanged(false);
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


}