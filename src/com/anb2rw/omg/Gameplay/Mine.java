package com.anb2rw.omg.Gameplay;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.anb2rw.omg.Engine.ParticleEmmiter;

public class Mine extends GameObject {
	
	private Actor Parent=null;
	private Level level;
	private List<Team> Teams;
	private int Type=0;
	private int Width;
	private int Tile=25;
	private Paint paint;
	private float counter;
//	private float HP=10;
	private int Hdistance=0,Vdistance=0;
	
	public Mine(Level l, Actor parent, float x, float y, int type) {
		
		Parent=parent;
		level=l;
		Tile=l.TileSize();
		Teams=l.Teams();
		paint=new Paint();
		Type=type;
		if(Type==0) {
			X=x+Tile/2;
			Y=y+Tile;
			HP=10;
			Width=Tile*3/4;
		} else if(Type==1) {
			X=x;
			Y=y;
			HP=1;
			Width=Tile*3/4;
			paint.setARGB(200, 255, 0, 0);
			paint.setStrokeWidth(10);
		}
		
	}
	
	public void Draw(Canvas canvas, int vX, int vY) {
		canvas.save();
		canvas.translate(-vX, -vY);
		
		if(Type==0) {
			paint.setARGB(255, 200, 200, 200);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawRect(X-Width/2, Y-Tile*2/10, X+Width/2, Y, paint);
			paint.setARGB(255, 0, 0, 0);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(X-Width/2, Y-Tile*2/10, X+Width/2, Y, paint);
            if(counter>=1) paint.setARGB(255, 250, 50, 0);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(X-Width/4, Y-Tile*3/10, X+Width/4, Y-Tile*2/10, paint);
            paint.setARGB(255, 0, 0, 0);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(X-Width/4, Y-Tile*3/10, X+Width/4, Y-Tile*2/10, paint);
        } else if(Type==1) {
        	canvas.drawLine(X+Tile/2-Width/2, Y+Tile/2-Width/2, X+Tile/2+Width/2, Y+Tile/2+Width/2, paint);
        	canvas.drawLine(X+Tile/2+Width/2, Y+Tile/2-Width/2, X+Tile/2-Width/2, Y+Tile/2+Width/2, paint);
        }
		canvas.restore();
	}
	
	public boolean Update(float dt) {
		
		if(Type==0) {
			if((counter+=dt)>=2) counter=0;
			
			int len=Teams.size();
	      for(int t=0;t<len;t++) {
	    	  if(t==Parent.TeamNumber) continue;
	    	  
	          Team team=Teams.get(t);
	          
	          for(int i=0;i<team.Players.size();i++) {
	              Actor p=team.Players.get(i);
	              
	              if(p.X()+Tile>=X-Width/2-Hdistance && p.X()<=X+Width/2+Hdistance)
	            	  if(p.Y()+Tile>=Y-Vdistance && p.Y()<=Y) {
	            		  HP=0;
	            		  p.Hit(50, Parent.TeamNumber, Parent.Number,true);
	            		  Parent.MyHit(50);
	            	  }
	          }
	      }
			
			if((HP-=dt)<=0) {
				level.Shake(0.4f);
				Parent.ParticleEmmiter().AddEffect(X, Y, ParticleEmmiter.EFFECT_EXPLOSION, 50, 0, 0xF0FF2000, Tile);
				return false;
			}
		} else if(Type==1) {
			Parent.ParticleEmmiter().AddEffect(X+Tile/2-Width/2, Y+Tile/2-Width/2, ParticleEmmiter.EFFECT_FIRE, 3, 0, 0xF0FF2000, Tile/2);
			Parent.ParticleEmmiter().AddEffect(X+Tile/2+Width/2, Y+Tile/2-Width/2, ParticleEmmiter.EFFECT_FIRE, 3, 0, 0xF0FF2000, Tile/2);
			Parent.ParticleEmmiter().AddEffect(X+Tile/2, Y+Tile/2, ParticleEmmiter.EFFECT_FIRE, 3, 0, 0xF0FF2000, Tile/2);
			Parent.ParticleEmmiter().AddEffect(X+Tile/2-Width/2, Y+Tile/2+Width/2, ParticleEmmiter.EFFECT_FIRE, 3, 0, 0xF0FF2000, Tile/4);
			Parent.ParticleEmmiter().AddEffect(X+Tile/2+Width/2, Y+Tile/2+Width/2, ParticleEmmiter.EFFECT_FIRE, 3, 0, 0xF0FF2000, Tile/4);
			
			if(HP<=0) {
				Parent.ParticleEmmiter().AddEffect(X, Y, ParticleEmmiter.EFFECT_EXPLOSION, 50, 0, 0xF0FF2000, Tile);
				return false;
			}
		}
		
		return true;
		
	}

}
