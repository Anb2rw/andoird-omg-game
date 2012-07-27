/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg;

import com.anb2rw.omg.Engine.Screen;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 *
 * @author РђРЅРґСЂСЋС€РєРђ
 */
public class ByeScreen extends Screen {
    private Paint paint;
    private float Timer=0f;
    
    public ByeScreen(Game game) {
        super(game);
        paint = new Paint();
        paint.setColor(0xFFFFFFFF);
        paint.setTextSize(20);
    }

    @Override
    public void Update(float deltaTime) {
    	game.Exit();
        Timer+=deltaTime;
        if(Timer>5) if(Timer>15 || !game.getTouch().getTouchEvents().isEmpty() || !game.getKey().getKeyEvents().isEmpty()) game.Exit();
    }

    @Override
    public void Draw(Canvas canvas, float deltaTime) {
//        canvas.drawBitmap(Assets.Menu_background, 0, 0, null);
//        canvas.drawText("Спасибо, что купили игру = )", 20+Timer*5.0f, 20+Timer*1.0f, paint);
//        canvas.drawText("Скоро:", Timer*2.5f, 150, paint);
//        canvas.drawText("- Много уникальных способностей!", 60-Timer*2.5f, 170, paint);
//        canvas.drawText("- Много новых уровней!", Timer*1.5f, 190, paint);
//        canvas.drawText("- Много режимов игры!", 50-Timer*2.8f, 210, paint);
//        canvas.drawText("- Мультиплеер по Bluetooth!", Timer*3.0f, 230, paint);
//        if(Timer>5) canvas.drawText(">_<", 230, 300, paint);
//        else canvas.drawText(">"+(5-(int)Timer)+"<", 230, 300, paint);
    }

    @Override
    public void pause() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resume() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispose() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
