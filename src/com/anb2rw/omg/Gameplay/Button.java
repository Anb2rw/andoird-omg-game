package com.anb2rw.omg.Gameplay;

import com.anb2rw.omg.Engine.ParticleEmmiter;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Button extends GameObject {
	public static int TYPE_FLOOR=0;
	public static int TYPE_WALL=1;
	
	private Level level;
//	private float X,Y;
//	private float dX=0,dY=0;
	private Paint paint;
	private int Tile=25;
	public boolean isPressed=false;
	private GameObject[] parent;
	private ParticleEmmiter pe;
	private int Type=TYPE_FLOOR;
	private boolean wallToLeft=false;
	private float Timer=0f;
	
	public Button(Level level, int i, int j, GameObject... parent) {
		this(level, i, j, 0, false, parent);
	}
	
	public Button(Level level, int i, int j, int type, GameObject... parent) {
		this(level, i, j, type, false, parent);
	}
	
	public Button(Level level, int i, int j, int type, boolean walltoleft, GameObject... parent) {
		this.level=level;
		Tile=level.TileSize();
		this.Type=type;
		this.wallToLeft=walltoleft;
		if(Type==TYPE_FLOOR) {
			W=Tile*3/4;
			H=Tile*1/5;
			X=i*Tile+(Tile-W)/2; Y=j*Tile-H;
			isSolid=false;
		} else if(Type==TYPE_WALL) {
			W=Tile*1/5;
			H=Tile*3/5;
			if(wallToLeft)
				X=i*Tile;
			else
				X=i*Tile+(Tile-W);
			Y=j*Tile+(Tile-H)/2;
			isSolid=false;
		}
		paint = new Paint();
		this.parent = parent;
		pe=level.ParticleEmmiter();
	}
	
	public void Draw(Canvas canvas, int vX, int vY) {
		canvas.save();
		canvas.translate(-vX, -vY);
		
//		if(Type==TYPE_FLOOR) {
			paint.setARGB(255, 75, 75, 75); paint.setStyle(Paint.Style.FILL);
	        canvas.drawRect(X, Y, X+W, Y+H, paint);
	        paint.setARGB(255, 0, 0, 0); paint.setStyle(Paint.Style.STROKE);
	        canvas.drawRect(X, Y, X+W, Y+H, paint);
//		} else if(Type==TYPE_WALL) {
//			paint.setARGB(255, 75, 75, 75); paint.setStyle(Paint.Style.FILL);
//	        canvas.drawRect(X, Y, X+W, Y+H, paint);
//	        paint.setARGB(255, 0, 0, 0); paint.setStyle(Paint.Style.STROKE);
//	        canvas.drawRect(X, Y, X+W, Y+H, paint);
//		}
        
		canvas.restore();
	}

	public boolean Update(float dt) {
		
		Actor p=level.player;
		if(Type==TYPE_FLOOR) {
			if(p.X>X-Tile && p.X<X+Tile)
				if(p.Y>Y-Tile && p.Y<Y+Tile) {
					if(!isPressed) {
						Timer=3f;
						isPressed=true;
						Y+=H*0.9f;
						for(int i=0;i<parent.length;i++)
							parent[i].isActive=!parent[i].isActive;
						pe.AddText("Click", X-10, Y+5, 0.3f, 0xFFFFFFFF, 7);
					}
						
				}
		} else if(Type==TYPE_WALL) {
			Bullet b=p.Bullet();
			if(b!=null) {
				if(b.Collide((int)X, (int)Y, W, H)) {
//				if(b.X>X-b.Radius() && b.X<X+b.Radius())
//					if(b.Y>Y-b.Radius() && b.Y<Y+b.Radius()) {
						if(!isPressed) {
							Timer=3f;
							isPressed=true;
							if(wallToLeft)
								X-=W*0.9f;
							else
								X+=W*0.9f;
							for(int i=0;i<parent.length;i++)
								parent[i].isActive=!parent[i].isActive;
							pe.AddText("Click", X-10, Y+5, 0.3f, 0xFFFFFFFF, 7);
						}
							
					}
			}
		}
		if(Timer>0) {
			if((Timer-=dt)<=0) {
				Timer=0;
				if(isPressed) {
					isPressed=false;
					if(Type==TYPE_FLOOR)
						Y-=H*0.9f;
					else if(Type==TYPE_WALL)
						if(wallToLeft)
							X+=W*0.9f;
						else
							X-=W*0.9f;
				}
			}
		}
//		X+=dX*dt;
//		Y+=dY*dt;
		return true;
	}
}
