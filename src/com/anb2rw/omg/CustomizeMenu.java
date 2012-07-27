/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.anb2rw.omg.Engine.KeyboardHandler.KeyEvent;
import com.anb2rw.omg.Engine.Screen;
import com.anb2rw.omg.Engine.TouchHandler.TouchEvent;

/**
 *
 * @author АндрюшкА
 */
public class CustomizeMenu extends Screen  {
    private int StartX=0,StartY=0,X=0,Y=0;
    private int SX=0,SY=0;
    private final int DRAG_HOR=1;
    private final int DRAG_VER=2;
    private int DragType=0;
    private int Page=0,PageCount=3;
    private Paint paint;
    private boolean isExit=false,isMenu=false;
    private Game game;
    Bitmap face;
    int Tile=25;
    int Color=0x00AA0000;
    int Hat=0,maxHat,Face=0,maxFace;
    
    //private boolean DragSpeed=false;
    //private float fSX=0;
    
    public CustomizeMenu(Game game) {
    	super(game);
    	this.game=game;
        paint=new Paint();
        //paint.setAntiAlias(true);
        
        Hat = Settings.Hat; maxHat=Assets.hats.length;
        Face = Settings.Face; maxFace=Assets.faces.length;
        face = Bitmap.createBitmap(Tile + 1, Tile + 1, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(face);

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color + 0xFF202020);
		canvas.drawRect(0, 0, Tile, Tile / 2, paint);
		paint.setColor(Color + 0xFF000000);
		canvas.drawRect(0, Tile / 2, Tile, Tile, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setARGB(255, 0, 0, 0);
		canvas.drawRect(0, 0, Tile, Tile, paint);

			canvas.drawLine(Tile / 2, 3 * Tile / 5 + 1, Tile / 2, 7 * Tile / 10, paint);// P
			canvas.drawLine(9 * Tile / 10, 3 * Tile / 5 + 1, 9 * Tile / 10, 7 * Tile / 10, paint);// O
			canvas.drawLine(Tile / 2, 7 * Tile / 10, 9 * Tile / 10 + 1, 7 * Tile / 10, paint);// T
			canvas.drawPoint(Tile / 2, 2 * Tile / 5, paint);
			canvas.drawPoint(9 * Tile / 10, 2 * Tile / 5, paint);
			
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setARGB(255, 0, 255, 0);
    }
    
    @Override
    public void Update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getTouch().getTouchEvents();
        List<KeyEvent> keyEvents = game.getKey().getKeyEvents();
        
        int len = touchEvents.size();

        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            
            if(event.type == TouchEvent.TOUCH_DOWN) {
//               
            } else if(event.type == TouchEvent.TOUCH_DRAGGED) {
//                
            } else if(event.type == TouchEvent.TOUCH_UP) {
            	if(inBorder(event,75,100,50,50))
            		if(--Hat<0) Hat=maxHat;
            	if(inBorder(event,375,100,50,50))
            		if(++Hat>maxHat) Hat=0;
               Settings.Hat=Hat;
               
               if(inBorder(event,75,200,50,50))
           		if(--Face<0) Face=maxFace;
           	if(inBorder(event,375,200,50,50))
           		if(++Face>maxFace) Face=0;
              Settings.Face=Face;
               
               if(inBorder(event, 5, 265, 55, 315)) game.setScreen(new MainMenu(game));
            }
        }
//      
        len = keyEvents.size();

        for(int i = 0; i < len; i++) {
            KeyEvent event = keyEvents.get(i);
            if(event.type == KeyEvent.KEY_DOWN) {
                if(event.keyCode==android.view.KeyEvent.KEYCODE_BACK) {
                	game.setScreen(new MainMenu(game));
                }

            }
        }
    }
    
    private boolean inBorder(TouchEvent event, int x, int y, int w, int h) {
        if(event.x >= x && event.x<=x+w && event.y>=y && event.y<=y+h)
            return true;
        else return false;
    }

	@Override
	public void Draw(Canvas canvas, float deltaTime) {
		canvas.drawBitmap(Assets.Menu_background, 0, 0, null);
		canvas.drawBitmap(Assets.Menu_custom, 0, 10, null);
		canvas.drawBitmap(Assets.Menu_custom, 0, 110, null);
		//canvas.drawARGB(255, 255, 255, 255);
		Assets.Font.drawString(canvas, "Внешний вид", 20, 20, 1.0f, paint);
		
    	int x=230;
    	canvas.drawBitmap(face, x, 120, null);
    	
    	//Face
    	for(int i=-1;i<=1;i++) {
    		int y=200;
    		int hat=Face+i;
    		if(hat<0) hat=maxFace;
    		else if(hat>maxFace) hat=0;
    		if(i==0) y=100;
    		
	    	if(hat>0) {
	    		if(hat-1<Assets.faces.length)
					canvas.drawBitmap(Assets.faces[hat-1], x+i*140-20+Tile/2, y+Tile, null);
			}
    	}
    	//Hat
    	for(int i=-1;i<=1;i++) {
    		int hat=Hat+i;
    		if(hat<0) hat=maxHat;
    		else if(hat>maxHat) hat=0;
    		
	    	if(hat>0) {
	    		if(hat-1<Assets.hats.length)
					canvas.drawBitmap(Assets.hats[hat-1], x+i*140-20+Tile/2, 100, null);
			}
    	}
    	
    	//canvas.drawRect(5, 265, 55, 315, paint);//Back
    	canvas.drawBitmap(Assets.back, 5, 265, paint);
    	
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
    
}
