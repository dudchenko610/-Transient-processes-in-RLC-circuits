package com.crazydev.funnycircuits.rendering.services;

import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.events.OnWireSelectedListener;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.OpenGLRendererTouchEventListener;
import com.crazydev.funnycircuits.rendering.ShaderProgram;
import com.crazydev.funnycircuits.rendering.VertexBatcher;
import com.crazydev.funnycircuits.rendering.interfaces.ITouchEventService;
import com.crazydev.funnycircuits.views.eventhandling.Input;

import java.util.List;

public class TouchEventService implements ITouchEventService {

    private VertexBatcher vertexBatcher = VertexBatcher.getInstance();

    private OnWireSelectedListener onWireSelectedListener;
    private ITouchEventService.OnTouchEventsListener onTouchEventsListener;

    private OpenGLRendererTouchEventListener openGLRendererTouchEventListener;
    protected ShaderProgram shaderProgram;
    private World electronicWorld;

    private boolean handleTouchEvents;
    private boolean isWireCreating;
    private boolean isWireSelected;
    private Vector3D creatingWireColor;

    private Vector2D downSavedPos = new Vector2D();
    private Vector2D upSavedPos   = new Vector2D();


    private Vector2D savedWirePosition = new Vector2D();
    private Vector2D newPosition       = new Vector2D();
    private boolean wireTranslateMode;

    private Vector2D touchedDown = new Vector2D();
    private Vector2D diff        = new Vector2D();
    private Vector2D delta       = new Vector2D();
    private boolean moving;
    private boolean zooming;
    private float pLength = 0;
    private boolean isWireMode;

    private Vector2D selectedTranslatePoint = new Vector2D();
    private Vector2D vec                    = new Vector2D();

    private Wire selectedWire;

    private boolean isCountingToContextMenu;
    private double timeSum = 0;

    public TouchEventService(OpenGLRendererTouchEventListener openGLRendererTouchEventListener,
                             ITouchEventService.OnTouchEventsListener onTouchEventsListener) {

        this.openGLRendererTouchEventListener = openGLRendererTouchEventListener;
        this.shaderProgram = ShaderProgram.getInstance();
        this.onTouchEventsListener = onTouchEventsListener;
        this.electronicWorld = World.getInstance();

        this.initialize();

    }

    private void initialize() {
        this.handleTouchEvents       = true;
        this.isWireCreating          = false;
        this.isWireSelected          = false;
        this.wireTranslateMode       = false;
        this.moving                  = false;
        this.zooming                 = false;
        this.isWireMode              = false;
        this.isCountingToContextMenu = false;

        this.downSavedPos.set(0, 0);
        this.upSavedPos.set(0, 0);
        this.savedWirePosition.set(0, 0);
        this.newPosition.set(0, 0);
        this.touchedDown.set(0, 0);
        this.diff.set(0, 0);
        this.delta.set(0, 0);
        this.selectedTranslatePoint.set(0, 0);
        this.vec.set(0, 0);

        this.timeSum = 0;
        this.pLength = 0;

        this.creatingWireColor = ITouchEventService.CREATING_SUCCESSFUL_WIRE_COLOR;
    }

    @Override
    public void setHandlingState(boolean handleTouchEvents) {
        this.handleTouchEvents = handleTouchEvents;
    }

    @Override
    public void handleTouchEvents() {
        List<Input.TouchEvent> touchEvents = openGLRendererTouchEventListener.getTouchEvents();

        if (!this.handleTouchEvents) {
            return;
        }

        int max = 0;

        for (int i = 0; i < touchEvents.size(); i ++) {
            if (touchEvents.get(i).pointer > max) {
                max = touchEvents.get(i).pointer;
            }
        }

        // more than two fingers
        // EXCEPTIONAL
        if (max > 1) {
            this.isCountingToContextMenu = false;
            moving  = false;
            zooming = false;
            return;
        }

        // two fingers
        // ZOOMING
        if (max == 1) {
            this.handleTwoFingerTouch(touchEvents);
            return;
        }

        zooming = false;

        // one finger
        // TRANSLATING, PICKING UP
        if (max == 0) {
            this.handleOneFingerTouch(touchEvents);
        }
    }


    protected void handleOneFingerTouch(List<Input.TouchEvent> touchEvents) {

        if (!this.isWireMode) {
            for (int i = 0; i < touchEvents.size(); i ++) {
                Input.TouchEvent event = touchEvents.get(i);
                shaderProgram.touchToWorld(event);

                if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                    this.isCountingToContextMenu = true;

                    touchedDown.set(event.touchPosition);

                    float x = this.shaderProgram.position.x  - (this.shaderProgram.right - this.shaderProgram.left) / 2.0f + event.touchPosition.x ;
                    float y = this.shaderProgram.position.y  - (this.shaderProgram.top - this.shaderProgram.bottom) / 2.0f + event.touchPosition.y;

                    int _x = Math.round(x);
                    int _y = Math.round(y);

                    this.downSavedPos.set(event.touchPosition);

                    Wire wire;
                    if ((wire = this.electronicWorld.tryToSelect(new Vector2D(x, y))) != null) {

                        this.onTouchEventsListener.onDeleteButtonStateChanged(true);

                        if (this.onWireSelectedListener != null) {
                            this.onWireSelectedListener.onDeselect();
                            this.onWireSelectedListener.onWireSelect(wire);
                            this.isWireSelected = true;
                        }

                        this.wireTranslateMode = true;
                        this.isCountingToContextMenu = true;
                        this.creatingWireColor = ITouchEventService.TRANSLATE_WIRE_COLOR;
                        this.selectedTranslatePoint.set(_x, _y);

                        this.selectedWire = wire;

                        this.savedWirePosition.set(this.selectedWire.nodeA.location);

                        this.newPosition.set(this.selectedWire.nodeB.location);

                        continue;
                    }

                    moving = true;
                }

                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    this.isCountingToContextMenu = false;

                    touchedDown.set(event.touchPosition);
                    moving = false;

                    this.upSavedPos.set(event.touchPosition);

                    if (this.wireTranslateMode) {
                        this.wireTranslateMode = false;
                        this.creatingWireColor =  ITouchEventService.CREATING_SUCCESSFUL_WIRE_COLOR;

                        if (!this.savedWirePosition.equals(this.selectedWire.nodeA.location) && !this.newPosition.equals(this.selectedWire.nodeB.location)) {
                            if (this.electronicWorld.checkWire(this.selectedWire, savedWirePosition, newPosition)) {

                                if (this.onWireSelectedListener != null) {
                                    this.onWireSelectedListener.onDeselect();
                                    this.isWireSelected = false;
                                }

                                this.selectedWire.remove();
                                this.electronicWorld.createWire(this.selectedWire.type, this.savedWirePosition, this.newPosition, this.selectedWire.orientation);
                            }
                        }

                    } else {
                        if (this.upSavedPos.subtract(this.downSavedPos).lengthSquared() < 0.01f) {
                            this.electronicWorld.deselect();

                            if (this.onWireSelectedListener != null) {
                                this.onWireSelectedListener.onDeselect();
                                this.isWireSelected = false;
                            }

                            this.onTouchEventsListener.onDeleteButtonStateChanged(false);
                        }
                    }
                }

                if (event.type == Input.TouchEvent.TOUCH_DRAGGED) {
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
                                this.creatingWireColor =  ITouchEventService.TRANSLATE_WIRE_COLOR;
                            } else {
                                this.creatingWireColor =  ITouchEventService.CREATING_WRONG_WIRE_COLOR;
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
                Input.TouchEvent event = touchEvents.get(i);
                shaderProgram.touchToWorld(event);

                float x = this.shaderProgram.position.x  - (this.shaderProgram.right - this.shaderProgram.left) / 2.0f + event.touchPosition.x ;
                float y = this.shaderProgram.position.y  - (this.shaderProgram.top - this.shaderProgram.bottom) / 2.0f + event.touchPosition.y;

                int _x = Math.round(x);
                int _y = Math.round(y);

                if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                    this.isCountingToContextMenu = false;

                    this.savedWirePosition.set(_x, _y);
                    this.newPosition.set(this.savedWirePosition);
                    this.isWireCreating = true;
                    this.creatingWireColor =  ITouchEventService.CREATING_SUCCESSFUL_WIRE_COLOR;
                    continue;
                }

                if (event.type == Input.TouchEvent.TOUCH_UP) {
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
                    this.creatingWireColor =  ITouchEventService.CREATING_SUCCESSFUL_WIRE_COLOR;
                    continue;
                }

                if (this.electronicWorld.checkWire(newPosition, savedWirePosition)) {
                    this.creatingWireColor =  ITouchEventService.CREATING_SUCCESSFUL_WIRE_COLOR;

                } else {
                    this.creatingWireColor =  ITouchEventService.CREATING_WRONG_WIRE_COLOR;
                }


            }
        }
    }

    protected void handleTwoFingerTouch(List<Input.TouchEvent> touchEvents) {
        this.isCountingToContextMenu = false;
        this.moving = false;

        if (touchEvents.size() > 1) {
            for (int i = 0; i < touchEvents.size(); i += 2) {

                if (i + 1 == touchEvents.size()) {
                    break;
                }

                Input.TouchEvent event_0 = touchEvents.get(i + 0);
                Input.TouchEvent event_1 = touchEvents.get(i + 1);

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
    }



    @Override
    public void contextMenuCountDown(double deltaTime) {
        if (this.isCountingToContextMenu) {
            this.timeSum += deltaTime;

            if (this.timeSum > .1) {
                this.timeSum = 0;

                this.handleTouchEvents = false;
                this.wireTranslateMode = false;
                this.isCountingToContextMenu = false;

                this.onTouchEventsListener.onShowContextMenu();
            }
        } else {
            this.timeSum = 0;
        }
    }

    @Override
    public void onContextMenuClosed() {
        this.handleTouchEvents = true;
    }

    @Override
    public boolean isWireSelected() {
        return this.isWireSelected;
    }

    @Override
    public void setOnWireSelectedListener(OnWireSelectedListener onWireSelectedListener) {
        this.onWireSelectedListener = onWireSelectedListener;
    }

    @Override
    public void setWireMode(boolean wireMode) {
        this.isWireMode = wireMode;
        this.wireTranslateMode = false;

        if (this.electronicWorld != null) {
            this.electronicWorld.deselect();
        }

    }

    @Override
    public void depictCreatingElement() {

        if ((this.isWireMode && !this.moving && this.isWireCreating && creatingWireColor != null) || (this.wireTranslateMode && !this.savedWirePosition.equals(this.selectedWire.nodeA.location)
                && !this.newPosition.equals(this.selectedWire.nodeB.location))) {

            vertexBatcher.addPoint(savedWirePosition.x, savedWirePosition.y, creatingWireColor, 1);
            vertexBatcher.addPoint(newPosition.x, newPosition.y, creatingWireColor, 1);
            vertexBatcher.addLine(this.savedWirePosition, this.newPosition, this.creatingWireColor);

        }
    }

}
