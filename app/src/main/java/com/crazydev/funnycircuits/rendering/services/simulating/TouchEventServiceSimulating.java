package com.crazydev.funnycircuits.rendering.services.simulating;

import com.crazydev.funnycircuits.rendering.OpenGLRendererTouchEventListener;
import com.crazydev.funnycircuits.rendering.services.TouchEventService;

public class TouchEventServiceSimulating extends TouchEventService {

    public TouchEventServiceSimulating(OpenGLRendererTouchEventListener openGLRendererTouchEventListener, OnTouchEventsListener onTouchEventsListener) {
        super(openGLRendererTouchEventListener, onTouchEventsListener);
    }


}
