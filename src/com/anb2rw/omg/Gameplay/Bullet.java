/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Gameplay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.anb2rw.omg.Engine.ParticleEmmiter;

/**
 *
 * @author АндрюшкА
 */
public class Bullet {
    public static final int TYPE_NORMAL=0;
    public static final int TYPE_FIRE=1;
    public static final int TYPE_ICE=2;
    public static final int TYPE_POISON=3;
    public static final int TYPE_ELECTRIC=4;
    
    public float X,Y;
    private int state;
    private int Tile=20;
    private int Radius=4;
    private Paint paint;
//    private int[][] map;
    private Map M;
    private Actor player;
    private int Speed=150;
    private Rect rect;
    private boolean isHit;
    private int Power=5;
    private int Type=TYPE_NORMAL;
    private ParticleEmmiter pe;
    
    public Bullet(Actor p, int Size, int Type, int tile, Map m) {
        player=p;
        Tile=tile;
        this.Type=Type;
        X=(int)player.X()+Tile/2; Y=player.Y()+Tile/2;
        state=player.Pos()?1:-1;
        this.Radius=Size;
        Power=6+(Radius-4)*2;
        //Speed=150-(Radius-4)*10;
        Speed=150;
        
        M=m;
//        map = m;
        paint=new Paint();
        paint.setAntiAlias(true);
        rect=new Rect(0,0,0,0);
        
        isHit=false;
        pe=p.ParticleEmmiter();
        
    }
    
    public void Draw(Canvas canvas, int vX, int vY) {
        
        switch(Type) {
            case TYPE_NORMAL:
                paint.setARGB(255, 255, 255, 255);
                break;
            case TYPE_FIRE:
                paint.setARGB(255, 255, 0, 0);
                break;
            case TYPE_ICE:
                paint.setARGB(255, 155, 155, 255);
                break;
            case TYPE_POISON:
                paint.setARGB(255, 55, 255, 0);
                break;
            case TYPE_ELECTRIC:
                paint.setARGB(255, 225, 245, 255);
                break;
        }
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(X-vX, Y-vY, Radius, paint);
        paint.setARGB(255, 0, 0, 0);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(X-vX, Y-vY, Radius, paint);
        
//        pe.Draw(canvas, vX, vY);
    }
    
    public boolean Update(float dt) {
    	if(M.GetTile(((int)X)/Tile, ((int)Y)/Tile)==Map.TILE_WATER) dt*=0.6f;
        
        if(state==1) {
          if(X<player.X()+Tile+5*Tile/4+(1*Tile/2) && M.CanMove(((int)X+Radius), ((int)Y))/*map[((int)Y)/Tile][((int)X+Radius)/Tile]==0*/)
            X+=dt*Speed;
          else state=-2;
        }
        else if(state==-1) {
          if(X>player.X()-3*Tile/2-(1*Tile/2)  && M.CanMove(((int)X-Radius), ((int)Y))/* map[((int)Y)/Tile][((int)X-Radius)/Tile]==0*/)
            X-=dt*Speed;
          else state=2;
        }
        else
            if(state==2) {
          if(X<player.X()+Tile/2)
            X+=dt*Speed;
          else return false;
        }
        else if(state==-2) {
          if(X>player.X()+Tile/2)
            X-=dt*Speed;
          else return false;
        }

        Y=player.Y()+2*Tile/5;
        
        switch(Type) {
            case TYPE_NORMAL:
                break;
            case TYPE_FIRE:
                pe.AddEffect(X, Y, ParticleEmmiter.EFFECT_FIRE, 10, 0, 0xDFFF0000, Radius*2);
                if(M.GetTile(((int)X)/Tile, ((int)Y)/Tile)==Map.TILE_WATER) {
                	Type=TYPE_NORMAL;
                	pe.AddEffect(X, Y, ParticleEmmiter.EFFECT_FIRE, 10, 0, 0xDFAF9090, Radius*2);
                }
                break;
            case TYPE_ICE:
                pe.AddEffect(X, Y, ParticleEmmiter.EFFECT_FIRE, 10, 180, 0xDF8080FF, Radius*2);
                break;
            case TYPE_POISON:
                pe.AddEffect(X, Y, ParticleEmmiter.EFFECT_FIRE, 10, 0, 0xDF50FF00, Radius*2);
                break;
            case TYPE_ELECTRIC:
                pe.AddEffect(X, Y, ParticleEmmiter.EFFECT_EXPLOSION, 3, 0, 0xDFDFEFFF, Radius*2);
                break;
        }
//        pe.Update(dt);
        return true;
    }
    
    public boolean Collide(int x, int y, int w, int h) {
        rect.left=x;
        rect.top=y;
        rect.right=x+w-1;
        rect.bottom=y+h-1;
        if(rect.contains((int)X, (int)Y)) return true; else return false;
//        int r=Radius/2;
//        rect.left=(int)X-Radius;
//        rect.top=(int)Y-Radius;
//        rect.right=(int)X+Radius;
//        rect.bottom=(int)Y+Radius;
//        targetRect.bottom=x;
//        targetRect.top=y;
//        targetRect.right=x+w-1;
//        targetRect.bottom=y+h-1;
    }
    
    public boolean isHit() {
        return isHit;
    }
    
    public void setHit() {
        if(state==1) state=-2;
        else if(state==-1) state=2;
        isHit=true;
    }
    
    public int getPower() {
        return Power;
    }
    
    public int getType() {
        return Type;
    }
    
    public int Radius() {
        return Radius;
    }
}
