/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Gameplay;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.anb2rw.omg.Assets;
import com.anb2rw.omg.ByeScreen;
import com.anb2rw.omg.Engine.TouchHandler.TouchEvent;

/**
 *
 * @author АндрюшкА
 */
public class InGameMenu {
    private Level level;
    private int StartX=0,StartY=0,X=0,Y=0;
    private int SX=0,SY=0;
    private final int DRAG_HOR=1;
    private final int DRAG_VER=2;
    private int DragType=0;
    private int Page=0,PageCount=3;
    private Paint paint;
    private boolean isExit=false;
    public boolean isMenu=false;
    private String[] AbNames;
    private String[] BallNames;
    
    //private boolean DragSpeed=false;
    //private float fSX=0;
    
    public InGameMenu(Level l) {
        this.level=l;
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        AbNames=Assets.AbNames;
        BallNames=Assets.BallTypeNames;
    }
    
    public void Draw(Canvas canvas) {
//    	//paint.setColor(0xFFFFFFFF);
//        if(Page==0) paint.setColor(0xFFFFFFFF); else paint.setColor(0xFFAAAAAA);
//        Assets.Font.drawString(canvas, "Меню", X/3+200, 30, 1.0f, paint);
//        int l=Assets.Font.stringWidth("Меню", 1.0f)+290;
//        
//        if(Page==1) paint.setColor(0xFFFFFFFF); else paint.setColor(0xFFAAAAAA);
//        Assets.Font.drawString(canvas, "Способности (+"+level.player.Points()+")", X/3+l, 30, 0.8f, paint);
//        l+=Assets.Font.stringWidth("Способности (+"+level.player.Points()+")", 0.8f)+50;
//        
//        if(Page==2) paint.setColor(0xFFFFFFFF); else paint.setColor(0xFFAAAAAA);
//        Assets.Font.drawString(canvas, "Шарик", X/3+l, 30, 1.0f, paint);
//        l+=Assets.Font.stringWidth("Шарик", 1.0f)+120;
        paint.setColor(0xFFFFFFFF);
        Assets.Font.drawString(canvas, "Меню", X+50, 30, 1.0f, paint);
        Assets.Font.drawString(canvas, "Способности (+"+level.player.Points()+")", X+480+50, 30, 0.8f, paint);
        Assets.Font.drawString(canvas, "Шарик", X+2*480+50, 30, 1.0f, paint);
        
        paint.setColor(0xFFFFFFFF);
        //Assets.Font.drawString(canvas, "Меню", X+50, 30, 1.0f, paint);
            Assets.Font.drawString(canvas, "Игра", X+194, Y+70, 0.8f, paint);
            Assets.Font.drawString(canvas, "Меню", X+194, Y+110, 0.8f, paint);
            Assets.Font.drawString(canvas, "Выход", X+182, Y+150, 0.8f, paint);
            
//            Assets.Font.drawString(canvas, ""+Settings.Speed, X+200, Y+250, 0.8f, paint);
//            Assets.Font.drawString(canvas, "Скорость", X+40, Y+230, 0.7f, paint);
//            paint.setColor(0xAA000000);
//            canvas.drawRect(X+40, Y+250, X+440, Y+270, paint);
//            paint.setColor(0xEEDF0000);
//            canvas.drawCircle(X+40+(Settings.Speed-0.5f)*(400f/2.5f), Y+260, 10, paint);
//        paint.setColor(0xFFFFFFFF);
        //Assets.Font.drawString(canvas, "Способности", X+530, 30, 0.8f, paint);
            for(int i=0;i<AbNames.length;i++) {
                    //SetAlpha(Y+70+i*70);
                Assets.Font.drawString(canvas, AbNames[i], X+520, Y+70+i*70, 0.6f, paint);
                    //SetAlpha(Y+120+i*70);
                Assets.Font.drawString(canvas, "<", X+530, Y+100+i*70, 1.0f, paint);
                Assets.Font.drawString(canvas, ">", X+880, Y+100+i*70, 1.0f, paint);
                Assets.Font.drawString(canvas, ""+level.player.Ability()[i], X+706, Y+100+i*70, 0.6f, paint);
            }
            
            for(int i=0;i<2;i++) {
                if(i==0)
                    Assets.Font.drawString(canvas, "Размер", X+480+520, Y+70+i*70, 0.6f, paint);
                else if(i==1)
                    Assets.Font.drawString(canvas, "Тип", X+480+520, Y+70+70, 0.6f, paint);
                Assets.Font.drawString(canvas, "<", X+480+530, Y+100+i*70, 1.0f, paint);
                Assets.Font.drawString(canvas, ">", X+480+880, Y+100+i*70, 1.0f, paint);
                if(i==0)
                    Assets.Font.drawString(canvas, ""+(level.player.BallSize()-1), X+480+706, Y+100, 0.6f, paint);
                else if(i==1) {
                    String str=BallNames[level.player.BallType()];
                    Assets.Font.drawString(canvas, str, X+480+706-str.length()/2*(int)(28*0.6), Y+100+70, 0.6f, paint);
                }
            }               
            
        int h=1;
        for(int i=0;i<50;i++) {
            paint.setARGB(255-i*255/50, 0, 0, 0);
            canvas.drawRect(0, i*h, 480, i*h+h, paint);
        }
        
        for(int i=49;i>=0;i--) {
            paint.setARGB(255-i*255/50, 0, 0, 0);
            canvas.drawRect(0, 320-i*h-h, 480, 320-i*h, paint);
        }
            //Точки-ориентиры
        int x=240-PageCount*14;
        for(int i=0;i<PageCount;i++) {
            if(i==Page) paint.setColor(0xFFFF0000); else paint.setColor(0xFFFFFFFF);
            Assets.Font.drawString(canvas, ".", x+i*28, canvas.getHeight()-28, 1.0f, paint);
        }
        
        if(isExit || isMenu) {
            canvas.drawARGB(200, 0, 0, 0);
            Assets.Font.drawString(canvas, "Выйти?", 156, 90, 1.0f, paint);
            //canvas.drawRect(180, 120, 230, 170, paint);
            canvas.drawBitmap(Assets.yes, 180, 120, paint);
            //canvas.drawRect(250, 120, 300, 170, paint);
            canvas.drawBitmap(Assets.no, 250, 120, paint);
        }
//        canvas.drawText(""+(Page*480+X), 20, 20, paint);
    }
    
    public boolean Update(float dt) {
        List<TouchEvent> touchEvents = level.game().getTouch().getTouchEvents();
        
        int len = touchEvents.size();

        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            
            if(event.type == TouchEvent.TOUCH_DOWN) {
                if(!isExit && !isMenu) {
                    
                    StartX=event.x; StartY=event.y;
//                    if(Page==0 && inBorder(event,40,250,400,40)) {
//                        DragSpeed=true;
//                        fSX=Settings.Speed;
//                    } else {
                        SX=X; SY=Y;
//                    }
                }
            } else if(event.type == TouchEvent.TOUCH_DRAGGED) {
                if(!isExit && !isMenu) {
//                    if(!DragSpeed) {
                        if(DragType==0) {
                            if(Math.abs(event.x-StartX)>20) { DragType=DRAG_HOR; Y=0; }
                            else if(Math.abs(event.y-StartY)>20) DragType=DRAG_VER;
                        }

                        if(DragType==DRAG_HOR) {
                            int tX=SX+(event.x-StartX);
                            if(X>tX) {
                        		X-=500*dt;
                        		if(X<tX)
                        			X=tX;
                        	}
                            else if(X<tX) {
                        		X+=500*dt;
                        		if(X>tX)
                        			X=tX;
                        	}
                        } else if(DragType==DRAG_VER) {
                            Y=SY+(event.y-StartY);
                        }
//                    } else {
//                        Settings.Speed=0.5f+(event.x-40)*(2.5f/400);//(fSX+(event.x-StartX))/80.0f;
//                        if(Settings.Speed<0.5f) Settings.Speed=0.5f;
//                        else if(Settings.Speed>3f) Settings.Speed=3f;
//                    }
                }
            } else if(event.type == TouchEvent.TOUCH_UP) {
                if(!isExit && !isMenu) {
//                    if(!DragSpeed) {
                            if(inBorder(event,0,0,50,50))
                                return false;
                        //Кнопки
                        if(Page==0) {//Главная
                            if(inBorder(event,194,70,100,30))
                                return false;
                            else if(inBorder(event,194,110,100,30))
                                isMenu=true;
                            else if(inBorder(event,182,150,125,30))
                                isExit=true;

                        } else if(Page==1) {//Способности
                            for(int a=0;a<AbNames.length;a++) {
                                if(inBorder(event,50,Y+100+a*70,30,30)) level.player.ModAbility(a,-1);
                                else if(inBorder(event,400,Y+100+a*70,30,30)) level.player.ModAbility(a,1);
                            }
                        } else if(Page==2) {//Шарик
                            for(int a=0;a<2;a++) {
                                if(inBorder(event,50,Y+100+a*70,30,30)) {
                                    if(a==0)
                                        level.player.ModBallSize(-1);
                                    else if(a==1)
                                        level.player.ChangeBallType(-1);
                                }
                                else if(inBorder(event,400,Y+100+a*70,30,30)) {
                                    if(a==0)
                                        level.player.ModBallSize(1);
                                    else if(a==1)
                                        level.player.ChangeBallType(1);
                                }
                            }
                        }

                        DragType=0;
                        if(X-SX<-75) ++Page;
                        else if(X-SX>75) --Page;

                        if(Page>PageCount-1) Page=PageCount-1;
                        else if(Page<0) Page=0;
//                    } else {
//                        DragSpeed=false;
//                    }
                } else {
                        if(inBorder(event, 180, 120, 50, 50)) {
                            if(isExit) level.game().setScreen(new ByeScreen(level.game()));//game.Exit();
                            else if(isMenu) level.Exit();
                        }
                        else {
                            isExit=false;
                            isMenu=false;
                        }
                }
            }
        }
        if(DragType==0) {
        	if(X>-Page*480) {
        		if(Page*480+X<120)
        			X-=((Page*480+X)*3+100)*dt;
        		else
        			X-=900*dt;
        		
        		if(X<-Page*480)
        			X=-Page*480;
        	}
        	if(X<-Page*480) {
        		
        		if(Page*480+X>-120)
        			X+=(-(Page*480+X)*3+100)*dt;
        		else
        			X+=900*dt;
        		
        		if(X>-Page*480)
        			X=-Page*480;
        	}
            //X=-Page*480;
            //Y=0;
        }
        
        return true;
    }
    
    private boolean inBorder(TouchEvent event, int x, int y, int w, int h) {
        if(event.x >= x && event.x<=x+w && event.y>=y && event.y<=y+h)
            return true;
        else return false;
    }
    
}
