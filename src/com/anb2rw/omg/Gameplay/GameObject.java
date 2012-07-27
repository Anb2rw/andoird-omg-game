package com.anb2rw.omg.Gameplay;

import android.graphics.Canvas;

public abstract class GameObject {
	public boolean isSolid=false,isActive=false;;
	public float X,Y,dX,dY,HP;
	public int W,H;
	
	public abstract void Draw(Canvas canvas, int vX, int vY);
	
	public abstract boolean Update(float dt);

}
