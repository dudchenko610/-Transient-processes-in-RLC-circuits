package com.crazydev.funnycircuits.rendering.services.simulating;

import com.crazydev.funnycircuits.math.OverlapTester;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.rendering.OpenGLRendererTouchEventListener;
import com.crazydev.funnycircuits.rendering.services.TouchEventService;
import com.crazydev.funnycircuits.views.eventhandling.Input;

import java.util.ArrayList;
import java.util.List;

public class TouchEventServiceSimulating extends TouchEventService {

    private GLContentServiceSimulating glContentServiceSimulating;
    protected ArrayList<Item> items;

    private Vector2D lastPos = new Vector2D();
    private Vector2D currPos = new Vector2D();
    private Vector2D difference = new Vector2D();
    private Vector2D pos = new Vector2D();
    private boolean handlePressed = false;
    private boolean workingAreaPressed = false;
    private Item selectedSprite;

    public TouchEventServiceSimulating(OpenGLRendererTouchEventListener openGLRendererTouchEventListener,
                                       OnTouchEventsListener onTouchEventsListener, GLContentServiceSimulating glContentServiceSimulating) {
        super(openGLRendererTouchEventListener, onTouchEventsListener);

        this.glContentServiceSimulating = glContentServiceSimulating;
        this.items = glContentServiceSimulating.items;

    }

    @Override
    protected void handleOneFingerTouch(List<Input.TouchEvent> touchEvents) {

        this.isCountingToContextMenu = false;

        boolean b = false;
        boolean z = false;
        Item item;

        for (int i = 0; i < touchEvents.size(); i++) {
            Input.TouchEvent event = touchEvents.get(i);
            shaderProgram.touchToWorld(event);

            float x = this.shaderProgram.position.x  - (this.shaderProgram.right - this.shaderProgram.left) / 2.0f + event.touchPosition.x ;
            float y = this.shaderProgram.position.y  - (this.shaderProgram.top - this.shaderProgram.bottom) / 2.0f + event.touchPosition.y;

            this.pos.set(x, y);

            if (OverlapTester.pointInRectangle(glContentServiceSimulating.handleRectangle, pos)) {
                b = true;
            }

            if (OverlapTester.pointInRectangle(glContentServiceSimulating.workingAreaRectangle, pos)) {
                z = true;
            }


            if (event.type == Input.TouchEvent.TOUCH_DOWN) {

                if (b) {
                    this.handlePressed = true;
                    this.lastPos.set(x, y);
                }

                if (z) {
                    workingAreaPressed = true;
                    this.lastPos.set(x, y);

                    for (int j = 0; j < this.items.size(); j ++) {
                        item = this.items.get(j);

                        if (item.visible && OverlapTester.pointInRectangle(item.rectangle, pos)) {
                            this.selectedSprite = item;
                            break;
                        }
                    }

                }

            } else if (event.type == Input.TouchEvent.TOUCH_DRAGGED) {

                if (this.handlePressed ) {
                    b = true;

                    this.currPos.set(x, y);
                    this.difference.set(this.currPos).subtract(this.lastPos);

                    this.lastPos.set(this.currPos);

                    this.glContentServiceSimulating.moveHandle(this.difference.y);

                }

                if (this.workingAreaPressed) {
                    z = true;

                    this.currPos.set(x, y);
                    this.difference.set(this.currPos).subtract(this.lastPos);

                    this.lastPos.set(this.currPos);

                    if (Math.abs(this.difference.y) > Math.abs(this.difference.x)) {
                        this.glContentServiceSimulating.moveWorkingArea(this.difference.y);
                    } else if (this.selectedSprite != null){
                        this.glContentServiceSimulating.moveItem(this.selectedSprite, this.difference.x);
                    }

                }


            } else if (event.type == Input.TouchEvent.TOUCH_UP) {
                this.handlePressed      = false;
                this.workingAreaPressed = false;
                this.selectedSprite     = null;
                this.lastPos.set(0, 0);
                this.currPos.set(0, 0);
            }


        }

        if (!b && !z) {
            super.handleOneFingerTouch(touchEvents, false);
        }


    }

    @Override
    protected void handleTwoFingerTouch(List<Input.TouchEvent> touchEvents) {

        this.isCountingToContextMenu = false;

        if (touchEvents.size() > 1) {

            Vector2D pos;
            for (int i = 0; i < touchEvents.size(); i += 2) {

                if (i + 1 == touchEvents.size()) {
                    return;
                }

                Input.TouchEvent event_0 = touchEvents.get(i + 0);
                Input.TouchEvent event_1 = touchEvents.get(i + 1);

                pos = shaderProgram.touchToWorld_no_changes(event_0);

                pos.x = this.shaderProgram.position.x  - (this.shaderProgram.right - this.shaderProgram.left) / 2.0f + pos.x ;
                pos.y = this.shaderProgram.position.y  - (this.shaderProgram.top - this.shaderProgram.bottom) / 2.0f + pos.y;

                if (OverlapTester.pointInRectangle(glContentServiceSimulating.handleRectangle, pos)) {
                    return;
                }

                if (OverlapTester.pointInRectangle(glContentServiceSimulating.workingAreaRectangle, pos)) {
                    return;
                }

                pos = shaderProgram.touchToWorld_no_changes(event_1);

                pos.x = this.shaderProgram.position.x  - (this.shaderProgram.right - this.shaderProgram.left) / 2.0f + pos.x ;
                pos.y = this.shaderProgram.position.y  - (this.shaderProgram.top - this.shaderProgram.bottom) / 2.0f + pos.y;

                if (OverlapTester.pointInRectangle(glContentServiceSimulating.handleRectangle, pos)) {
                    return;
                }

                if (OverlapTester.pointInRectangle(glContentServiceSimulating.workingAreaRectangle, pos)) {
                    return;
                }
            }

            super.handleTwoFingerTouch(touchEvents);
            this.glContentServiceSimulating.recalculateSizes();
        }

    }

}
