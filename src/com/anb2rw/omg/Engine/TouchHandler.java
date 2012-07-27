/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Engine;

import com.anb2rw.omg.Engine.Pool.PoolObjectFactory;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author АндрюшкА
 */
 public class TouchHandler implements OnTouchListener {
    
    public static class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;
        public int type;
        public int x, y;
        public int pointer;
    }
    
    int Fingers=10;
    boolean[] isTouched = new boolean[Fingers];
    int[] touchX = new int[Fingers];
    int[] touchY = new int[Fingers];
    Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    float scaleX;
    float scaleY;


    public TouchHandler(View view, float scaleX, float scaleY) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {

           
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };

        touchEventPool = new Pool<TouchEvent>(factory, 100);
        //view.setOnTouchListener(this);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    
    public boolean onTouch(View v, MotionEvent event) {
        
    	
    	synchronized (this) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)>> MotionEvent.ACTION_POINTER_ID_SHIFT;
            int pointerId = event.getPointerId(pointerIndex);
            TouchEvent touchEvent;

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = TouchEvent.TOUCH_DOWN;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = true;
                    touchEventsBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = TouchEvent.TOUCH_UP;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = false;
                    touchEventsBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int pointerCount = event.getPointerCount();
                    for (int i = 0; i < pointerCount; i++) {
                        pointerIndex = i;
                        pointerId = event.getPointerId(pointerIndex);
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                        touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                        touchEventsBuffer.add(touchEvent);
                    }
                    break;
                }
            return true;
        }
    }
    
    public int getFingers() {
        return Fingers;
    }
    
   // @Override
    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= Fingers)
                return false;
            else
                return isTouched[pointer];
        }
    }

    //@Override
    public int getTouchX(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= Fingers)
                return 0;
            else
                return touchX[pointer];
        }
    }
         //   @Override
    public int getTouchY(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= Fingers)
                return 0;
            else
                return touchY[pointer];
        }
    }
    
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            
            for (int i = 0; i < len; i++)
                touchEventPool.free(touchEvents.get(i));
            
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }
    
    public float[] GetScale() {
        float ret[]={scaleX,scaleY};
        return ret;
    }
}
//                
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction() & MotionEvent.ACTION_MASK;
//        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
//        int pointerId = event.getPointerId(pointerIndex);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_POINTER_DOWN:
//                touched[pointerId] = true;
//                x[pointerId] = (int)(event.getX(pointerIndex)*scaleX);
//                y[pointerId] = (int)(event.getY(pointerIndex)*scaleY);
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//            case MotionEvent.ACTION_CANCEL:
//                touched[pointerId] = false;
//                x[pointerId] = (int)(event.getX(pointerIndex)*scaleX);
//                y[pointerId] = (int)(event.getY(pointerIndex)*scaleY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int pointerCount = event.getPointerCount();
//                for (int i = 0; i < pointerCount; i++) {
//                    pointerIndex = i;
//                    pointerId = event.getPointerId(pointerIndex);
//                    x[pointerId] = (int)(event.getX(pointerIndex)*scaleX);
//                    y[pointerId] = (int)(event.getY(pointerIndex)*scaleY);
//                }
//                break;
//        }
//
//        return true;
//    }
//    
//    public boolean AnyTouch() {
//        for(int i=0;i<fingers;i++)
//            if(touched[i])
//                return true;
//            
//        return false;
//                        
//    }
//    
//}