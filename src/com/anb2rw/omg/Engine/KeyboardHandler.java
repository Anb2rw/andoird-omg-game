/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Engine;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnKeyListener;

import com.anb2rw.omg.Engine.Pool.PoolObjectFactory;

/**
 *
 * @author АндрюшкА
 */
public class KeyboardHandler implements OnKeyListener {
    
    public static class KeyEvent {
        public static final int KEY_DOWN = 0;
        public static final int KEY_UP = 1;
        public int type;
        public int keyCode;
        public char keyChar;
    }
    
    boolean[] pressedKeys = new boolean[128];
    Pool<KeyEvent> keyEventPool;
    List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>();
    List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
    
    public KeyboardHandler(View view) {
        PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
            
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        };
        keyEventPool = new Pool<KeyEvent>(factory, 100);
        //view.setOnKeyListener(this);
        //view.setFocusableInTouchMode(true);
        //view.requestFocus();
    }            
        
    
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
            return false;
        synchronized (this) {
            KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = true;
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = false;
            }
            keyEventsBuffer.add(keyEvent);
        }
        return true;
    }
    
    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode > 127)
            return false;
        return pressedKeys[keyCode];
    }
    
    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
        int len = keyEvents.size();
        for (int i = 0; i < len; i++)
            keyEventPool.free(keyEvents.get(i));
            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
        }
    }
    
}