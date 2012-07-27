/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg;

import com.anb2rw.omg.Engine.FileIO;
import com.anb2rw.omg.Engine.Screen;
import com.anb2rw.omg.Engine.TouchHandler;
import com.anb2rw.omg.Engine.KeyboardHandler;
import android.content.Context;

/**
 *
 * @author АндрюшкА
 */
public interface Game {
    public TouchHandler getTouch();
    public KeyboardHandler getKey();
    public FileIO getFileIO();
    
    public void setScreen(Screen screen);
    public Screen getCurrentScreen();
    public Screen getStartScreen();

    public void Exit();
    public Context getContext();
    public void SwitchOnBluetooth();
}
