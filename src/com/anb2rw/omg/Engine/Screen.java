/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Engine;

import com.anb2rw.omg.Game;
import android.graphics.Canvas;

/**
 *
 * @author АндрюшкА
 */
public abstract class Screen {
    protected final Game game;
    
    public Screen(Game game) {
        this.game = game;
    }
    
    public abstract void Update(float deltaTime);
    public abstract void Draw(Canvas canvas, float deltaTime);
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();
}
