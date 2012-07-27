/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Gameplay;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.anb2rw.omg.Assets;
import com.anb2rw.omg.Engine.TouchHandler;
import com.anb2rw.omg.Engine.TouchHandler.TouchEvent;

/**
 *
 * @author АндрюшкА
 */
public class VirtualPad {

    private TouchHandler touch;
    Paint paint,Fpaint,Spaint;
    Rect rect,Fire,Special;
    int control=-1;
    public boolean UP,DOWN,LEFT,RIGHT;
    public boolean FIRE,SPECIAL,SPECIAL_U=false,SPECIAL_D=false;
    private int padFinger=-1,specFinger=-1;
    private Matrix Matrix;
    public Bitmap specialIcon=null;

    public VirtualPad(TouchHandler t) {
        this.touch=t;
        paint = new Paint();
        paint.setARGB(255,0,0,0);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Fpaint = new Paint();
        Fpaint.setARGB(100, 255, 100, 100);
        Fpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Spaint = new Paint();
        Spaint.setARGB(100, 100, 100, 255);
        Spaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Matrix=new Matrix();

        int h=(int)(200*touch.GetScale()[1]);
        int d=(int)(0.1f*h);
        rect = new Rect(0, h-d, 100+2*d, h+100+d);
        Fire = new Rect((int)(1.8f*h), h, (int)(1.8f*h)+100, h+100);
        Special = new Rect(Fire.centerX(),Fire.top-Fire.height()/2-20,Fire.right, Fire.top-20);
    }

    public void Control() {
        List<TouchEvent> touchEvents = touch.getTouchEvents();
        
        Dpad(touchEvents);
        Fire();
        if(specialIcon!=null) Special(touchEvents);
    }
    
    private void Dpad(List<TouchEvent> touchEvents) {
    	int len = touchEvents.size();
        
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_DOWN) {
                if(rect.contains(event.x,event.y)) {
                	padFinger=event.pointer;
                	paint.setAlpha(200);
                    return;
                } 
            } else if(event.type == TouchEvent.TOUCH_UP) {
            	paint.setAlpha(100);
            	UP=false; DOWN=false; LEFT=false; RIGHT=false;
            	padFinger=-1;
            }
         }
        if(padFinger!=-1 && !touch.isTouchDown(padFinger)) {
        	paint.setAlpha(100);
        	padFinger=-1;
        }
        
        int x,y;
          if(touch.isTouchDown(padFinger)) {
              x=touch.getTouchX(padFinger);
              y=touch.getTouchY(padFinger);
                      paint.setAlpha(200);
                      if(x<rect.centerX()-rect.width()/6) { LEFT=true; RIGHT=false; }
                      else if(x>rect.centerX()+rect.width()/6) { RIGHT=true; LEFT=false; }
                      else { LEFT=false; RIGHT=false; }

                      if(y<rect.centerY()-rect.width()/6) { UP=true; DOWN=false; }
                      else if(y>rect.centerY()+rect.width()/6) { DOWN=true; UP=false; }
                      else { UP=false; DOWN=false; }
          }
        
    }
    
    private void Fire() {
        int len = touch.getFingers();
        int x,y;
        for(int i = 0; i < len; i++)
            if(touch.isTouchDown(i)) {
                x=touch.getTouchX(i);
                y=touch.getTouchY(i);
                    if(Fire.contains(x, y)) {
                    	Fpaint.setAlpha(200);
                    	FIRE=true;
                    	return;
                    }
            }
        Fpaint.setAlpha(100);
        FIRE=false;
    }
    
    
    private void Special(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_DOWN) {
                if(Special.contains(event.x,event.y)) {
                	specFinger=event.pointer;
                	Spaint.setAlpha(200);
                    return;
                } 
            } else if(event.type == TouchEvent.TOUCH_UP) {
            	if(specFinger==event.pointer)
            	if(Special.contains(event.x,event.y)) {
                    SPECIAL=!SPECIAL;
                    return;
                } else if(event.x>=Special.left-20 && event.y<Special.top-5) {
                    SPECIAL_U=true;
                    SPECIAL_D=false;
                    return;
                } else if(event.x>=Special.left-20 && event.y>Special.bottom+5) {
                    SPECIAL_D=true;
                    SPECIAL_U=false;
                    return;
                }
            }
         }
        if(specFinger!=-1 && !touch.isTouchDown(specFinger)) {
        	Spaint.setAlpha(100);
        	specFinger=-1;
        }
    }

    public void Draw(Canvas canvas) {

        canvas.drawBitmap(Assets.Control, rect.left+20, rect.top+20, paint);
        
        if(LEFT||RIGHT||UP||DOWN) {
        	canvas.save();
            canvas.clipRect(rect.left+20, rect.top+20, rect.right-20, rect.bottom-20);
            paint.setMaskFilter(new BlurMaskFilter(30,BlurMaskFilter.Blur.NORMAL));
	        if(LEFT) {
	        	canvas.drawLine(rect.left+20, rect.top+20, rect.left+20, rect.bottom-20, paint);
	        } else if(RIGHT) {
	        	canvas.drawLine(rect.right-20, rect.top+20, rect.right-20, rect.bottom-20, paint);
	        }
	        if(UP) {
	        	canvas.drawLine(rect.left+20, rect.top+20, rect.right-20, rect.top+20, paint);
	        } else if(DOWN) {
	        	canvas.drawLine(rect.left+20, rect.bottom-20, rect.right-20, rect.bottom-20, paint);
	        }
	        paint.setMaskFilter(null);
	        canvas.restore();
        }
        
        //canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width()/2, paint);
        //if(control!=-1)
        //    canvas.drawCircle(touch.x[control], touch.y[control], rect.width()/4, paint);
        canvas.drawRect(Fire, Fpaint);
        canvas.drawRect(Fire.left, Fire.top+5, Fire.left+5, Fire.bottom-5, Fpaint);//l
        canvas.drawRect(Fire.left, Fire.top, Fire.right, Fire.top+5, Fpaint);//t
        canvas.drawRect(Fire.right-5, Fire.top+5, Fire.right, Fire.bottom-5, Fpaint);//r
        canvas.drawRect(Fire.left, Fire.bottom-5, Fire.right, Fire.bottom, Fpaint);//b
        //canvas.drawCircle(Fire.centerX(), Fire.centerY(), Fire.width()/2, Fpaint);
        //canvas.drawRect(Fire.left+5, 5, Fire.right-5, Fire.top-5, Fpaint);
        if(specialIcon!=null) {
	        if(specFinger!=-1) {
	        	Matrix.setTranslate(Special.centerX()-15, Special.top-20);
	        	canvas.drawBitmap(Assets.arrow, Matrix, Spaint);
	        	Matrix.setTranslate(Special.centerX()-15, Special.bottom+20);
	        	Matrix.preScale(1, -1);
	        	canvas.drawBitmap(Assets.arrow, Matrix, Spaint);
	        }
	        canvas.drawRect(Special, Spaint);
	        canvas.drawBitmap(specialIcon, Special.left, Special.top, null);
        }
    }

    boolean AnyTouch() {
        return touch.getTouchEvents().isEmpty()?false:true;
    }

}
