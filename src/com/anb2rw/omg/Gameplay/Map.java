/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Gameplay;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 *
 * @author РђРЅРґСЂСЋС€РєРђ
 */
public class Map {
	public static int TILE_EMPTY=0;
	public static int TILE_SOLID=1;
	public static int TILE_ICE=2;
	public static int TILE_FIRE=3;
	public static int TILE_WATER=4;
	public static int TILE_DEATH=5;
	
	public String name="default_map";
	protected Level level;
	protected Paint paint;
    protected int Tile=25;
    public int RoomW=20, RoomH=20;
    public float Gravity=1f;
    public int BackColor=0xFF9696EB;
  //0-Empty, 1-Solid, 2-Ice, 3-Fire, 4-Water, 5-Death
    public int[][] map = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
        {1,0,0,0,0,4,1,4,4,4,0,0,0,0,0,0,0,1,1,1},
        {1,1,0,0,1,4,4,4,4,4,0,0,0,1,1,1,0,0,0,1},
        {1,0,0,0,1,4,4,1,4,4,0,1,0,0,0,2,0,0,0,1},
        {1,0,0,1,1,4,4,4,4,1,1,2,0,0,0,2,0,0,0,1},
        {1,0,0,0,0,4,4,4,4,0,0,0,0,0,0,0,0,2,0,1},
        {1,0,0,0,0,4,4,4,1,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,0,0,0,2,2,2,2,2,1,1,1},
        {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,0,1},
        {1,0,0,0,0,0,1,4,1,1,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,1,4,1,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,3,1,0,0,0,1,1,0,1,0,1,0,0,0,0,1},
        {1,1,1,3,3,1,1,1,1,1,1,3,1,1,1,1,5,1,1,1}
    };
  //1-сам,2-вправо,3-влево,4-прыжок/сам,5-прыжок/вправо,6-прыжок/влево
    public int[][] ai = {
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,1,1,3,3,1,1,1,1,1,1,3,3,1,2,1,1,0},
            {0,1,1,1,1,0,6,3,1,1,1,2,2,2,0,3,1,3,3,0},
            {0,4,2,2,4,1,0,3,3,3,2,2,2,5,1,6,1,0,0,0},
            {0,0,1,2,0,1,3,6,2,3,1,5,1,0,0,0,1,1,1,0},
            {0,1,1,5,0,1,1,0,2,5,5,0,1,1,1,0,1,1,1,0},
            {0,1,1,0,0,1,1,1,2,0,0,0,1,1,1,0,1,3,1,0},
            {0,1,1,1,1,1,1,2,6,3,1,1,1,1,1,1,1,0,1,0},
            {0,2,2,2,2,2,2,5,0,3,1,1,1,1,1,1,6,3,6,0},
            {0,0,0,0,0,0,0,0,0,6,3,1,0,0,0,0,0,0,0,0},
            {0,1,1,1,1,1,1,1,1,0,2,4,3,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,2,2,2,0,3,3,3,1,1,1,1,0},
            {0,1,1,1,1,1,2,2,1,2,5,0,6,3,3,1,1,1,1,0},
            {0,1,1,1,1,1,2,1,5,1,0,0,0,0,6,1,1,1,1,0},
            {0,1,1,1,2,2,5,3,0,4,3,1,1,0,0,1,1,1,1,0},
            {0,1,1,1,2,3,0,3,0,0,3,1,1,1,1,1,1,1,1,0},
            {0,1,1,2,5,3,0,5,0,2,6,3,3,3,3,3,1,1,1,0},
            {0,1,1,2,0,6,3,0,2,5,0,3,3,3,3,3,1,1,1,0},
            {0,2,2,5,0,0,6,1,5,0,0,6,0,6,0,6,3,3,3,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
        };
    
    public Map() {
    	
    }
    
    public Map(Level l) {
        init(l);
    }
    
    public void init(Level l) {
    	this.level = l;
        Tile=level.TileSize();
        paint = new Paint();
    }
    
    public int[][] GetMap() {
        return map;
    }
    
    public int[][] GetAI() {
        return ai;
    }
    
    public float GetGravity() {
    	return Gravity;
    }
    
    public int[] GetMapSize() {
        int m[]={RoomW,RoomH};
        return m;
    }
    
    public void DrawBack(Canvas canvas, int vX, int vY) {
    	canvas.drawColor(BackColor);
    }
    
    public void Draw(Canvas canvas, int vX, int vY) {
        for(int i=0;i<RoomW;i++)
            for(int j=0;j<RoomH;j++) {
                if(i*Tile>=vX-Tile && i*Tile<=vX+canvas.getWidth() && j*Tile>=vY-Tile && j*Tile<=vY+canvas.getHeight())
                switch(map[j][i]) {
                    case 1:
                        paint.setARGB(255, 75, 75, 75); paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(i*Tile-vX, j*Tile-vY, i*Tile+Tile-vX, j*Tile+Tile/2-vY, paint);
                        paint.setARGB(255, 55, 55, 55); paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(i*Tile-vX, j*Tile+Tile/2-vY, i*Tile+Tile-vX, j*Tile+Tile-vY, paint);
                        paint.setARGB(255, 0, 0, 0); paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(i*Tile-vX, j*Tile-vY, i*Tile+Tile-vX, j*Tile+Tile-vY, paint);
                        break;
                    case 2:
                        paint.setARGB(225, 220, 220, 255); paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(i*Tile-vX, j*Tile-vY, i*Tile+Tile-vX, j*Tile+Tile/2-vY, paint);
                        paint.setARGB(225, 200, 200, 245); paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(i*Tile-vX, j*Tile+Tile/2-vY, i*Tile+Tile-vX, j*Tile+Tile-vY, paint);
                        paint.setARGB(255, 0, 0, 0); paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(i*Tile-vX, j*Tile-vY, i*Tile+Tile-vX, j*Tile+Tile-vY, paint);
                        break;
                    case 3:
                        paint.setARGB(255, 255, 150, 150); paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(i*Tile-vX, j*Tile-vY, i*Tile+Tile-vX, j*Tile+Tile/2-vY, paint);
                        paint.setARGB(255, 245, 130, 130); paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(i*Tile-vX, j*Tile+Tile/2-vY, i*Tile+Tile-vX, j*Tile+Tile-vY, paint);
                        paint.setARGB(255, 0, 0, 0); paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(i*Tile-vX, j*Tile-vY, i*Tile+Tile-vX, j*Tile+Tile-vY, paint);
                        break;
                }
                
            }
    }
    
    public void DrawOver(Canvas canvas, int vX, int vY) {
        for(int i=0;i<RoomW;i++)
            for(int j=0;j<RoomH;j++) {
                if(i*Tile>=vX-Tile && i*Tile<=vX+canvas.getWidth() && j*Tile>=vY-Tile && j*Tile<=vY+canvas.getHeight())
                switch(map[j][i]) {
                    case 4:
                        paint.setARGB(155, 50, 50, 255); paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(i*Tile-vX, j*Tile-vY, i*Tile+Tile-vX, j*Tile+Tile-vY, paint);
                        break;
                }
                
            }
    }
    
    public int GetTile(int i, int j) {
    	if(i<0 || i>=RoomW || j<0 || j>=RoomH) return -1;
        return map[j][i];
    }
    
    public boolean CanMove(int x, int y) {
    	int i=x/Tile;
    	int j=y/Tile;
    	if(i<0 || i>=RoomW || j<0 || j>=RoomH) return false;
    	
    	int ret=map[j][i];
    	if(ret!=0 && ret!=4 && ret!=5) return false;
    	else {//Если можно идти
    		GameObject go;
    		for(int g=0;g<level.gameObjects.size();g++) {
    			go=level.gameObjects.get(g);
    			if(go.isSolid) {
    				if(x>go.X && x<go.X+go.W)
    					if(y>go.Y && y<go.Y+go.H)
    						return false;
    			}
    		}
    		return true;
    	}
    }
    
    public float GetPositionY(float x, float y) {
    	int i=(int) (x/Tile);
    	int j=(int) (y/Tile);
    	if(i<0 || i>=RoomW || j<0 || j>=RoomH) return y;
    	
    	float pos=y;
    	//Left side
    	//If tile
    	int ret=map[j][i];
    	if(ret!=0 && ret!=4 && ret!=5) {//If Block - not empty
    		if(pos>(int) ((y) / Tile) * Tile)
    			pos = (int) ((y) / Tile) * Tile;
    	} //if empty - check platforms
    	Rect goRect,actRect;
    	actRect=new Rect((int)x,(int)y,(int)x+Tile,(int)y+Tile);
    		GameObject go;
    		for(int g=0;g<level.gameObjects.size();g++) {
    			go=level.gameObjects.get(g);
    			if(go.isSolid) {
    				goRect=new Rect((int)go.X,(int)go.Y,(int)go.X+go.W,(int)go.Y+go.H);
    				if(goRect.intersect(actRect)) {
    					if(pos>go.Y-1)
							pos = go.Y-1;//Collide with platform
    				}
//    				if(x>go.X && x<go.X+go.W)
//    					if(y>go.Y && y<go.Y+go.H) {
//    						if(pos>go.Y-Tile)
//    							pos = go.Y-Tile;//Collide with platform
//    					}
    			}
    		}
//    		if(pos>y)
//    			pos = y;//if true empty
    	
    	
    	//RightSide
    	x+=Tile;
    	i=(int) (x/Tile);
    	ret=map[j][i];
    	if(ret!=0 && ret!=4 && ret!=5) {//If Block - not empty 
    		if(pos>(int) ((y) / Tile) * Tile)
    			pos = (int) ((y) / Tile) * Tile;
    	} //if empty - check platforms
//    		GameObject go;
    		for(int g=0;g<level.gameObjects.size();g++) {
    			go=level.gameObjects.get(g);
    			if(go.isSolid) {
    				goRect=new Rect((int)go.X,(int)go.Y,(int)go.X+go.W,(int)go.Y+go.H);
    				if(goRect.intersect(actRect)) {
    					if(pos>go.Y-1)
							pos = go.Y-1;//Collide with platform
    				}
//    				if(x>go.X && x<go.X+go.W)
//    					if(y>go.Y && y<go.Y+go.H) {
//    						if(pos>go.Y-Tile)
//    							pos = go.Y-Tile;//Collide with platform
//    					}
    			}
    		}
//    		if(pos>y)
//    			pos = y;//if true empty
    	
    	return pos;
    }
    
}
