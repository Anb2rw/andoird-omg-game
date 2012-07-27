package com.anb2rw.omg.Gameplay;

import android.graphics.Canvas;
import android.graphics.Paint;

public class MovingPlatform extends GameObject {
	
	private Level level;
//	private float X,Y;
//	private float dX=0,dY=0;
	private Paint paint;
	private int Tile=25;
	private boolean Pos=true;
	
	public MovingPlatform(Level level, int x, int y) {
		this.level=level;
		Tile=level.TileSize();
		X=x+Tile; Y=y;
		paint = new Paint();
		isSolid=true;
		W=Tile;
		H=Tile;
	}
	
	public void Draw(Canvas canvas, int vX, int vY) {
		canvas.save();
		canvas.translate(-vX, -vY);
		
		paint.setARGB(255, 75, 75, 75); paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(X, Y, X+Tile, Y+Tile/2, paint);
        paint.setARGB(255, 55, 55, 55); paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(X, Y+Tile/2, X+Tile, Y+Tile, paint);
        paint.setARGB(255, 0, 0, 0); paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(X, Y, X+Tile, Y+Tile, paint);
        
		canvas.restore();
	}

	public boolean Update(float dt) {
		
		if(isActive) {
			if(Pos) {
				if(Y<50) Pos=false;
				dY=-40;
			} else {
				if(Y>200) Pos=true;
				dY=40;
			}
			
	//		Actor p=level.player;
	//		if(p.X>X-Tile && p.X<X+Tile)
	//			if(p.Y>Y-Tile && p.Y<Y+Tile) {
	//				p.Y=Y-Tile;
	//				p.dY=0;//dY
	//			}
	//		
			X+=dX*dt;
			Y+=dY*dt;
		}
		return true;
	}
}
