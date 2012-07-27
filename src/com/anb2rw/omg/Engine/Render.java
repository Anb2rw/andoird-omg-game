/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Engine;

import com.anb2rw.omg.Gameplay.Level;
import com.anb2rw.omg.MainActivity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 *
 * @author АндрюшкА
 */
public class Render extends SurfaceView implements Runnable {
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    Bitmap framebuffer;
    Level level;
    MainActivity game;
    Paint paint;
    
    public Render(MainActivity game, Bitmap buffer) {
        super(game);
        this.game=game;
        holder = getHolder();
        framebuffer=buffer;
        paint = new Paint();
        //framebuffer=Bitmap.createBitmap(480, 320, Bitmap.Config.RGB_565);
    }
    
    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }
    
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        Canvas buffer=new Canvas(framebuffer);
        paint.setAlpha(255);
        while(running) {
            if(!holder.getSurface().isValid())
                continue;
            
            float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            startTime = System.nanoTime();
            
            //game.getCurrentScreen().update(deltaTime);
            //game.getCurrentScreen().present(deltaTime);
            //level.Update(deltaTime);
            
            game.getCurrentScreen().Update(1.5f*deltaTime);
            game.getCurrentScreen().Draw(buffer, deltaTime);

            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, paint);
//            Paint paint = new Paint();
//            paint.setARGB(255, 255, 255, 255);
            
            //canvas.drawText("dT: "+deltaTime, 40, 40, paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        while(true) {
            try {
                renderThread.join();
            } catch (InterruptedException e) {
                // retry
            }
            finally {
                break;
            }
        }
    }
    
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        
//    }
    
}
