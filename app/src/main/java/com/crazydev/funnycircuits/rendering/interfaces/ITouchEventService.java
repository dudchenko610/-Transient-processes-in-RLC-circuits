package com.crazydev.funnycircuits.rendering.interfaces;

import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.events.OnWireSelectedListener;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;

public interface ITouchEventService {

    public static final Vector3D CREATING_SUCCESSFUL_WIRE_COLOR = new Vector3D(0,1,0);
    public static final Vector3D CREATING_WRONG_WIRE_COLOR      = new Vector3D(1,0,0);
    public static final Vector3D TRANSLATE_WIRE_COLOR           = new Vector3D(0,0,1);

    void depictCreatingElement();
    void setWireMode(boolean mode);
    void setOnWireSelectedListener(OnWireSelectedListener onWireSelectedListener);
    void setHandlingState(boolean handleTouchEvents);
    void handleTouchEvents();
    void contextMenuCountDown(double deltaTime);
    void onContextMenuClosed();
    boolean isWireSelected();

    interface OnTouchEventsListener {
        void onShowContextMenu();
        void onDeleteButtonStateChanged(boolean state);

    }
}
