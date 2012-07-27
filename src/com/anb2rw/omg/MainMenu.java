
package com.anb2rw.omg;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.anb2rw.omg.Engine.KeyboardHandler.KeyEvent;
import com.anb2rw.omg.Engine.Screen;
import com.anb2rw.omg.Engine.TouchHandler.TouchEvent;
import com.anb2rw.omg.Gameplay.Level;

/**
 *
 * @author АндрюшкА
 */
public class MainMenu extends Screen {
    private final int PAGE_MAIN=1;
    private final int PAGE_SINGLEPLAYER=2;
    private final int PAGE_OPTIONS=3;
    //private final int PAGE_CUSTOMIZE=4;

    private Paint paint;
    private int Page=1;
    private boolean isExit=false,isReset=false;
    private String[] TeamNames;


    public MainMenu(Game game) {
        super(game);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setARGB(230, 0, 255, 0);
        paint.setTextSize(20);
        paint.setAntiAlias(true);

        TeamNames=game.getContext().getResources().getStringArray(R.array.team_names);
    }

    @Override
    public void Update(float deltaTime) {
 

        List<TouchEvent> touchEvents = game.getTouch().getTouchEvents();
        List<KeyEvent> keyEvents = game.getKey().getKeyEvents();
        int len = touchEvents.size();

       	//if(Page==PAGE_CUSTOMIZE && custMenu!=null) custMenu.Update(deltaTime);
       	//else {
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(!isExit) {
                    switch(Page) {
                        case PAGE_MAIN:
                            if(inBorder(event, 50, 30, 430, 80)) Page=PAGE_SINGLEPLAYER;
                            else if(inBorder(event, 50, 100, 430, 150)) Page=PAGE_OPTIONS;
                            else if(inBorder(event, 50, 170, 430, 220)) isExit=true;
                            break;
                        case PAGE_SINGLEPLAYER:
                            if(inBorder(event, 50, 50, 430, 100)) game.setScreen(new Level(game));
                            else if(inBorder(event, 50, 140, 100, 170)) Settings.TeamCount--;
                            else if(inBorder(event, 400, 140, 450, 170)) Settings.TeamCount++;
                            else if(inBorder(event, 50, 210, 100, 240)) Settings.PlayerCount--;
                            else if(inBorder(event, 400, 210, 450, 240)) Settings.PlayerCount++;
                            else if(inBorder(event, 100, 280, 450, 320)) Settings.Map++;
                            else if(inBorder(event, 5, 265, 55, 315)) Page=PAGE_MAIN;

                            if(Settings.TeamCount<2) Settings.TeamCount=2;
                            else if(Settings.TeamCount>8) Settings.TeamCount=8;
                            if(Settings.PlayerCount<1) Settings.PlayerCount=1;
                            if(Settings.Map>=Assets.maps.length) Settings.Map=0;
                            break;
                        case PAGE_OPTIONS:
                            if(isReset) {
                                if(inBorder(event, 180, 120, 230, 170)) { Settings.Reset(); isReset=false; }
                                else if(inBorder(event, 250, 120, 300, 170)) isReset=false;
                            } else {
                                if(inBorder(event, 50, 140, 100, 170)) Settings.PlayerTeam--;
                                else if(inBorder(event, 400, 140, 450, 170)) Settings.PlayerTeam++;
                                else if(inBorder(event, 200, 210, 300, 230)) game.setScreen(new CustomizeMenu(game));
                                else if(inBorder(event, 200, 270, 300, 290)) isReset=true;
                                else if(inBorder(event, 5, 265, 55, 315)) Page=PAGE_MAIN;

                                if(Settings.PlayerTeam<0) Settings.PlayerTeam=0;
                                else if(Settings.PlayerTeam>TeamNames.length-1) Settings.PlayerTeam=TeamNames.length-1;
                            }
                            break;
                    }
                } else {//Exit
                    if(inBorder(event, 180, 120, 230, 170)) game.Exit();//game.setScreen(new ByeScreen(game));//game.Exit();//Y
                    else if(inBorder(event, 250, 120, 300, 170)) isExit=false;//N
                }
            }
        }
       	//}


        len = keyEvents.size();

        for(int i = 0; i < len; i++) {
            KeyEvent event = keyEvents.get(i);
            if(event.type == KeyEvent.KEY_DOWN) {
                if(event.keyCode==android.view.KeyEvent.KEYCODE_BACK) {
                    switch(Page) {
                        case PAGE_MAIN:
                            if(isExit) game.Exit();//game.setScreen(new ByeScreen(game));//game.Exit();
                            else isExit=true;
                            //isExit=!isExit;//game.Exit();
                            break;
                        case PAGE_SINGLEPLAYER:
                            Page=PAGE_MAIN;
                            break;
                        case PAGE_OPTIONS:
                            if(isReset) isReset=false;
                            else Page=PAGE_MAIN;
                            break;

                    }
                } else if(event.keyCode==android.view.KeyEvent.KEYCODE_MENU) {
                	//game.setScreen(new CustomizeMenu(game));
                }

            }
        }

    }

    @Override
    public void Draw(Canvas canvas, float deltaTime) {
        canvas.drawBitmap(Assets.Menu_background, 0, 0, null);
        switch(Page) {
            case PAGE_MAIN:
                //canvas.drawARGB(255, 0, 0, 0);
                //canvas.drawRect(50, 30, 430, 80, paint);
                Assets.Font.drawString(canvas, "Игра", 50, 30, 1.0f, paint);
                //canvas.drawRect(50, 100, 430, 150, paint);
                Assets.Font.drawString(canvas, "Опции", 50, 100, 1.0f, paint);
                //canvas.drawRect(50, 170, 430, 220, paint);
                Assets.Font.drawString(canvas, "Выход", 50, 170, 1.0f, paint);
                break;
            case PAGE_SINGLEPLAYER:
                //canvas.drawARGB(255, 0, 0, 0);
                //canvas.drawRect(50, 50, 430, 100, paint);
                Assets.Font.drawString(canvas, "СТАРТ", 170, 50, 1.0f, paint);
                //canvas.drawText("Команд:", 40, 120, paint);
                Assets.Font.drawString(canvas, "Команд:", 40, 120, 0.6f, paint);
                //canvas.drawRect(50, 140, 100, 170, paint);
                Assets.Font.drawString(canvas, "<", 50, 140, 1.0f, paint);
                //canvas.drawRect(260, 140, 310, 170, paint);
                Assets.Font.drawString(canvas, ">", 400, 140, 1.0f, paint);
                //canvas.drawText(""+Settings.TeamCount, 150, 150, paint);
                Assets.Font.drawString(canvas, ""+Settings.TeamCount, 226, 150, 0.6f, paint);
                //canvas.drawText("Игроков в команде:", 40, 190, paint);
                Assets.Font.drawString(canvas, "Игроков в команде:", 40, 190, 0.6f, paint);
                //canvas.drawRect(50, 210, 100, 240, paint);
                Assets.Font.drawString(canvas, "<", 50, 210, 1.0f, paint);
                //canvas.drawRect(260, 210, 310, 240, paint);
                Assets.Font.drawString(canvas, ">", 400, 210, 1.0f, paint);
                //canvas.drawText(""+Settings.PlayerCount, 150, 210, paint);
                Assets.Font.drawString(canvas, ""+Settings.PlayerCount, 240-Assets.Font.stringWidth(""+Settings.PlayerCount, 0.6f), 210, 0.6f, paint);
                //canvas.drawRect(5, 265, 55, 315, paint); 200, 280, 450, 310
                Assets.Font.drawString(canvas, Assets.maps[Settings.Map].name, 200, 280, 0.6f, paint);
                canvas.drawBitmap(Assets.back, 5, 265, paint);
                break;
            case PAGE_OPTIONS:
                //canvas.drawARGB(255, 0, 0, 0);
                //canvas.drawText("Опции", 20, 20, paint);
                Assets.Font.drawString(canvas, "Опции", 20, 20, 1.0f, paint);
                //canvas.drawText("Команда игрока:", 40, 100, paint);
                Assets.Font.drawString(canvas, "Команда игрока:", 40, 100, 0.6f, paint);
                //canvas.drawRect(50, 140, 100, 170, paint);
                Assets.Font.drawString(canvas, "<", 50, 140, 1.0f, paint);
                //.drawRect(300, 140, 350, 170, paint);
                Assets.Font.drawString(canvas, ">", 400, 140, 1.0f, paint);
                //canvas.drawText(TeamNames[Settings.PlayerTeam], 150, 150, paint);
                Assets.Font.drawString(canvas, TeamNames[Settings.PlayerTeam], 240-Assets.Font.stringWidth(TeamNames[Settings.PlayerTeam], 0.6f)/2, 150, 0.6f, paint);//лолшто?
                Assets.Font.drawString(canvas, "Внешний вид", 145, 210, 0.6f, paint);
                Assets.Font.drawString(canvas, "Сброс", 200, 270, 0.6f, paint);
                //canvas.drawRect(5, 265, 55, 315, paint);
                canvas.drawBitmap(Assets.back, 5, 265, paint);
                if(isReset) {
                    canvas.drawARGB(200, 0, 0, 0);
                    Assets.Font.drawString(canvas, "Сбросить?", 114, 90, 1.0f, paint);
                    //canvas.drawRect(180, 120, 230, 170, paint);
                    canvas.drawBitmap(Assets.yes, 180, 120, paint);
                    //canvas.drawRect(250, 120, 300, 170, paint);
                    canvas.drawBitmap(Assets.no, 250, 120, paint);
                }
                break;
        }
        if(isExit) {
            canvas.drawARGB(200, 0, 0, 0);
            Assets.Font.drawString(canvas, "Выйти?", 156, 90, 1.0f, paint);
          //canvas.drawRect(180, 120, 230, 170, paint);
            canvas.drawBitmap(Assets.yes, 180, 120, paint);
            //canvas.drawRect(250, 120, 300, 170, paint);
            canvas.drawBitmap(Assets.no, 250, 120, paint);
        }
    }

    private boolean inBorder(TouchEvent event, int x1, int y1, int x2, int y2) {
        if(event.x >= x1 && event.x<=x2 && event.y>=y1 && event.y<=y2)
            return true;
        else return false;
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