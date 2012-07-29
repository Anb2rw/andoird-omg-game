/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Gameplay;

import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.anb2rw.omg.Assets;
import com.anb2rw.omg.Engine.ParticleEmmiter;

/**
 *
 * @author РђРЅРґСЂСЋС€РєРђ
 */
public class Enemy extends Actor {
    //private int Y;
    //private float X,Y;
	private Actor Parent=null;
	private boolean AI=true;
    private int Color=0x00DF0000;
    private Paint paint;
    private Map M;
    private Level level;
    private int[][] ai;
    private boolean isJump=true,isFire=false;
    private float dX,dY;
    private int Tile=20;
    private boolean move=false;//,Pos=true;
    private Bitmap face;
    private Matrix Mat;
    private boolean Panic=false;
    public int pX,pY;
    private int offset;
    protected Random rnd;
    protected float JumpDelay,AttDelay;
    protected float JumpRnd,AttRnd;
    private List<Team> Teams;
    private int LastHitTeam,LastHit;
	private int MaxSpeed,JumpSpeed=120;
	private float TimeToRegen=0;
	private boolean CanRegen=false;
	private int[] ability={
	    	0, //Air Jump//0
	        0, //Rocket Boots//1
	        0, //Invisibility//2
	        0, //Vampire//3
	        0, //Heal//4
	        0, //Radar//5
	        0, //Dash//6
	        0, //Shadow Clone//7
	        0, //Time//8
	        0, //Miner//9
	        0, //Crit//10
	        0, //Block//11
	        0, //Return//12
	        0, //Fury//10
	        0, //Amaterasu//11
	        0  //Far Away//12
	    };
	

    public Enemy(Level l, Map m, int Color) {
        //rnd=new Random();
        this.M=m; ai=M.GetAI();
        this.level=l; Tile=level.TileSize();
        rnd=level.rnd;
        this.Color=Color;
        Teams=level.Teams();
        pe = level.ParticleEmmiter();
        MaxSpeed=4*Tile;
        Respawn();
//        X=rnd.nextInt(M.GetMapSize()[0])*Tile;
//        Y=rnd.nextInt(M.GetMapSize()[1])*Tile;
        //X=Y=Tile;
        offset=(int)(Tile*0.75f);
        paint = new Paint();
        Mat=new Matrix();
        JumpRnd=rnd.nextInt(2)+1;
        AttRnd=BallSize/4+rnd.nextFloat();
        face = Bitmap.createBitmap(Tile+1, Tile+1, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(face);

              paint.setStyle(Paint.Style.FILL);
              //paint.setARGB(255, 20, 20, 255);
              paint.setColor(Color+0xFF202020);
              canvas.drawRect(0, 0, Tile, Tile/2, paint);
              //paint.setARGB(255, 0, 0, 235);
              paint.setColor(Color+0xFF000000);
              canvas.drawRect(0, Tile/2, Tile, Tile, paint);
          paint.setStyle(Paint.Style.STROKE);
          paint.setARGB(255, 0, 0, 0);
          canvas.drawRect(0, 0, Tile, Tile, paint);

        if(Pos) {
              canvas.drawLine(Tile/2, 3*Tile/5+1, Tile/2, 7*Tile/10, paint);//P
              canvas.drawLine(9*Tile/10, 3*Tile/5+1, 9*Tile/10, 7*Tile/10, paint);//O
              canvas.drawLine(Tile/2, 7*Tile/10, 9*Tile/10+1, 7*Tile/10, paint);//T
              canvas.drawPoint(Tile/2, 2*Tile/5, paint);
              canvas.drawPoint(9*Tile/10, 2*Tile/5, paint);
          }
          else {
              canvas.drawLine(Tile/10, 3*Tile/5+1, Tile/10, 7*Tile/10, paint);
              canvas.drawLine(Tile/2, 3*Tile/5+1, Tile/2, 7*Tile/10, paint);
              canvas.drawLine(Tile/10, 7*Tile/10, Tile/2+1, 7*Tile/10, paint);
              canvas.drawPoint(Tile/10, 2*Tile/5, paint);
              canvas.drawPoint(Tile/2, 2*Tile/5, paint);
          }
    }
    
    public void setAI(boolean bool) {
    	AI=bool;
    }
    
    public void setParent(Actor actor) {
    	this.Parent=actor;
    }

    public void Move(float x) {
        if(x==0) { if(move) move=false; }
        else {
            if(x<0) {
                if(Pos) Pos=false;
            }
            else {
                if(!Pos) Pos=true;
            }
            dX+=x;
            move=true;
        }
    }

    private void Jump() {
      if (JumpDelay>JumpRnd)
      {
          if(!isJump || M.GetTile(((int)X+Tile/2)/Tile, ((int)Y+Tile/2)/Tile)==Map.TILE_WATER) {
        	  if(!Stun) {
        		  dY = -JumpSpeed;
        		  isJump = true;
        	  }
              JumpDelay=0;
              JumpRnd=rnd.nextInt(2)+1;
          }
      }
    }

    private boolean PlayerNear(int pX, int pY) {
        if(X>pX-40 && X<pX+40 && Y>pY-30 && Y<pY+30) return true;
        return false;
    }
    
    public void Fire(int pX, int pY) {
     if(CanAttack && !isFire && AttDelay>AttRnd && PlayerNear(pX,pY) && !Panic && !Stun) {
        isFire = true;
        CanAttack=false;
        AttDelay=0;
        AttRnd=BallSize/4+rnd.nextFloat();
        bullet=new Bullet(this,BallSize,BallType,Tile,M);
     }
    }

    @Override
    public void Draw(Canvas canvas, int vX, int vY) {
        
        if(AA!=2) {
            
            if(bullet!=null) {
                paint.setARGB(255, 0, 0, 0);
                canvas.drawLine(X+Tile/2-vX, Y+Tile/2-vY, bullet.X-vX, bullet.Y-vY, paint);
            }
            
	        Mat.setTranslate(X-vX, Y-vY);
	        Mat.preSkew(dX*0.0005f,0);
	        if(!Pos) {
	            Mat.preScale(-1, 1);
	            Mat.preTranslate(-Tile, 0);
	        }
	        canvas.drawBitmap(face, Mat, null);
	        
	        //Face
			Mat.preTranslate(Tile/2-20, Tile-20);
			if(Face>0) {
				if(Face-1<Assets.faces.length)
					canvas.drawBitmap(Assets.faces[Face-1], Mat, null);
			}
			//Hat
			Mat.preTranslate(0, -Tile);
			if(Hat>0) {
				if(Hat-1<Assets.hats.length)
					canvas.drawBitmap(Assets.hats[Hat-1], Mat, null);
			}
			
			//HP
        	paint.setARGB(255, 255, 0, 0);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(X-vX, Y-5-vY, X+Tile-vX, Y-3-vY, paint);
            paint.setARGB(255, 0, 255, 0);
            canvas.drawRect(X-vX, Y-5-vY, X+Tile*(int)HP/MAXHP-vX, Y-3-vY, paint);
            
        }

        if(bullet!=null) bullet.Draw(canvas,vX,vY);
        
//        pe.Draw(canvas, vX, vY);
    }

    private void Left() {
    	if(Stun) return;
    	
        if(dX<=0) {
                dX-=5;
                move=true;
          }
        if(Pos) Pos=false;
    }

    private void Right() {
    	if(Stun) return;
    	
        if(dX>=0) {
              dX+=5;
              move=true;
          }
        if(!Pos) Pos=true;
    }
    
    @Override
    public void Update(float dt) {
    	
    	if(M.GetTile(((int)X+Tile/2)/Tile, ((int)Y+Tile/2)/Tile)==Map.TILE_DEATH || M.GetTile(((int)X+Tile/2)/Tile, ((int)Y+Tile/2)/Tile)==-1) {
    		Death();
		}
    	
    	float ep=1f;//Давление среды
    	if(M.GetTile(((int)X+Tile/2)/Tile, ((int)Y+Tile/2)/Tile)==Map.TILE_WATER) {
			ep=0.6f;
			if(Debuff==Bullet.TYPE_FIRE) {
				Debuff=0;
				DebuffTimer=0;
				pe.AddEffect(X + Tile / 2, Y + Tile / 4, ParticleEmmiter.EFFECT_FIRE, 5, 0, 0xFFAF9292, Tile);
			}
		}
		
    	SearchTarget(dt);
    	
    	if(AI) {
        move=false;

        if(isJump) {
           if(dY*dt*ep<dt*ep*8*Tile) dY+=dt*4*Tile*M.GetGravity()*ep;// else dY = 5;
       if(dY<=0) {//Р’РІРµСЂС…
    	   if(M.CanMove((int)(X), (int)(Y+dY*dt*ep)) && M.CanMove((int)(X+Tile-1), (int)(Y+dY*dt*ep)))
//           if(map[(int)(Y+dY*dt)/Tile][(int)(X)/Tile]==0 && map[(int)(Y+dY*dt)/Tile][(int)(X+Tile-1)/Tile]==0)
                { Y+=(dY*dt*ep); }

      else { Y = (int)((Y)/Tile)*Tile; dY = 0; }

       } else //Р’РЅРёР·
    	   if(M.CanMove((int)(X), (int)(Y+dY*dt*ep+1+Tile)) && M.CanMove((int)(X+Tile-1), (int)(Y+dY*dt*ep+1+Tile)))
//          if(map[(int)(Y+dY*dt+1+Tile)/Tile][(int)(X)/Tile]==0 && map[(int)(Y+dY*dt+1+Tile)/Tile][(int)(X+Tile-1)/Tile]==0)
                { Y+=(dY*dt*ep); }
      else //Р—РµРјР»СЏ
          { Y = Round(Y+Tile+dY*dt*ep+1)-Tile;
      dY = 0;
      isJump = false;
          }
       } else {//!Jump
    	   if(M.CanMove((int)(X), ((int)Y+Tile+Tile/4)) && M.CanMove(((int)X+Tile-1), ((int)Y+Tile+Tile/4)))
//       if(map[((int)Y+Tile+Tile/4)/Tile][(int)(X)/Tile]==0 && map[((int)Y+Tile+Tile/4)/Tile][((int)X+Tile-1)/Tile]==0)
       {dY = 5*Tile*dt*ep; isJump=true; }
      else //Y = ((Y+Tile+5)/Tile)*Tile-Tile;
           Y = Round(Y+Tile+5)-Tile;
          }
        
	        if((Y>pY+Tile || Math.abs(X-pX)>2*Tile) && !Panic) {//1-пїЅпїЅпїЅ,2-пїЅпїЅпїЅпїЅпїЅпїЅ,3-пїЅпїЅпїЅпїЅпїЅ,4-пїЅпїЅпїЅпїЅпїЅпїЅ/пїЅпїЅпїЅ,5-пїЅпїЅпїЅпїЅпїЅпїЅ/пїЅпїЅпїЅпїЅпїЅпїЅ,6-пїЅпїЅпїЅпїЅпїЅпїЅ/пїЅпїЅпїЅпїЅпїЅ
	            if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]==2 || ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]==2) Right();
	            else if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]==3 || ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]==3) Left();
	            else if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]>=4 && ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]>=4) Jump();
	            else if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]==1 || ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]==1) {
	                if(pX<X) Left(); else if(pX+Tile>X) Right(); }
	            //else if(BM[(Y+10)/20][(X+1)/20]==4 || BM[(Y+10)/20][(X+19)/20]==4) { Jump(map); Right(map); }
	            //else if(BM[(Y+10)/20][(X+1)/20]==5 || BM[(Y+10)/20][(X+19)/20]==5) { Jump(map); Left(map); }
	        } else if(Y<pY && !Panic) {//пїЅпїЅпїЅпїЅy
	            if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]==2 || ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]==2) Left();
	            else if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]==3 || ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]==3) Right();
	            else if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]==4 && ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]==4) Jump();
	            else if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]==5 && ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]==5) Left();
	            else if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]==6 && ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]==6) Right();
	            else if(ai[((int)Y+Tile/2)/Tile][((int)X+1)/Tile]==1 || ai[((int)Y+Tile/2)/Tile][((int)X+Tile-1)/Tile]==1) {
	                if(pX<X) Left(); else if(pX+Tile>X) Right(); }
	        } else {
		      if (X<pX-offset) { if(!Panic) Right(); else Left(); }
		      else if (X<pX-offset+15) { if(!Pos) Pos=true; if(Panic) Left(); }
		      else
		      if (X>pX+20+offset) { if(!Panic) Left(); else Right(); }
		      else { if (X>pX+offset-15) if(Pos) Pos=false; if(Panic) Right(); }
		        
		        if (Y!=pY || Panic || X<=pX-40 || X>=pX+60)
		          Jump();
	        }
        
        Fire(pX,pY);

      JumpDelay+=dt;
      AttDelay+=dt;
      
      if(M.GetTile(((int)X)/Tile, ((int)Y+Tile+1)/Tile)==3 || M.GetTile(((int)X+Tile-1)/Tile, ((int)Y+Tile+1)/Tile)==3) {//Fire Block
			HP-=2*dt;
			pe.AddEffect(X+2, Y + Tile, ParticleEmmiter.EFFECT_FIRE, 2, 0, 0xFFFF2200, Tile/2);
			pe.AddEffect(X+Tile-2, Y + Tile, ParticleEmmiter.EFFECT_FIRE, 2, 0, 0xFFFF2200, Tile/2);
		}
        
        if(!move) if(Math.abs(dX)<1) dX=0;
        else {
        	if(M.GetTile(((int)X)/Tile, ((int)Y+Tile+1)/Tile)==2 || M.GetTile(((int)X+Tile-1)/Tile, ((int)Y+Tile+1)/Tile)==2) {
				dX *= 0.9;// Ice
				pe.AddEffect(X+2, Y + Tile, ParticleEmmiter.EFFECT_FIRE, 2, 0, 0xDEDDDDFF, Tile/2);
				pe.AddEffect(X+Tile-2, Y + Tile, ParticleEmmiter.EFFECT_FIRE, 2, 0, 0xDEDDDDFF, Tile/2);
			} else
				dX *= 0.5;
		}

        if(dX>0) {
        	if(!M.CanMove(((int)X+Tile+(int)(dX*dt*ep)), ((int)Y)) || !M.CanMove(((int)X+Tile+(int)(dX*dt*ep)), ((int)Y+Tile-1)))
//          if(map[((int)Y)/Tile][((int)X+Tile+(int)(dX*dt))/Tile]!=0 || map[((int)Y+Tile-1)/Tile][((int)X+Tile+(int)(dX*dt))/Tile]!=0 )
            {
              //X = ((X+Tile+(int)dX)/Tile)*Tile-Tile;
              X = Round(X+Tile+dX*dt*ep+1)-Tile;
              dX=0;
            }
      } else if(dX<0)
      {
    	  if(!M.CanMove(((int)X+((int)(dX*dt*ep)-1)), ((int)Y)) || !M.CanMove(((int)X+((int)(dX*dt*ep)-1)), ((int)Y+Tile-1)))
//          if(map[((int)Y)/Tile][((int)X+((int)(dX*dt)-1))/Tile]!=0 || map[((int)Y+Tile-1)/Tile][((int)X+((int)(dX*dt)-1))/Tile]!=0 )
            {
              X = ((X)/Tile)*Tile;
              dX=0;
            }
      }
   
      if(dX<-MaxSpeed) dX=-MaxSpeed;
      else if(dX>MaxSpeed) dX=MaxSpeed;

        X+=(dX*dt*ep);
        
    }//!AI

    	if(Debuff!=0) ProcessDebuff(dt);
		else {
			MaxSpeed=4*Tile;
			JumpSpeed = 120;
		}
    	if(Stun) {
			if((stunTimer-=dt)<=0) {
				Stun=false;
				stunTimer=0;
				pe.AddEffect(X+Tile/2, Y-Tile/4, ParticleEmmiter.EFFECT_EXPLOSION, 10, 0, 0xFFCCCC00, Tile/2);
			} else {
				pe.AddEffect(X+Tile/2, Y-Tile/4, ParticleEmmiter.EFFECT_EXPLOSION, 10, 0, 0xFFCCCC00, Tile/4);
			}
		}
    	
        if(bullet!=null) if(!bullet.Update(dt)) {
            bullet=null;
            isFire=false;
        }
        
        if(HP<=0) {
        	Death();
        } else if(HP<=MAXHP*RegenDivide) {
            if(CanRegen) {
            	HP+=dt;
            	//pe.AddEffect(X+Tile/2, Y+Tile/2, ParticleEmmiter.EFFECT_FIRE, 30, 0, 0xFFEEEE00, Tile);
            }
            else {
            	if((TimeToRegen+=dt)>=2.5f) {
            		TimeToRegen=0;
            		CanRegen=true;
            	}
            }
        }
        
        if(HP<=MAXHP*0.25f) {
        	if(!Panic) Panic=true;
        } else {
        	if(Panic) Panic=false;
        }
        
        if(!CanAttack) {
			if((attackCoolDown+=dt)>=BallSize/4) {
				CanAttack=true;
				attackCoolDown=0;
			}
		}
        
//        pe.Update(dt);


    }
    
    public void Death() {
    	pe.AddEffect(X+Tile/2, Y+Tile/2, ParticleEmmiter.EFFECT_EXPLOSION, 30, 0, 0xFFFFFFFF, Tile);
    	if(Parent==null) {
	        if(LastHitTeam>-1) {
	            Team team=Teams.get(LastHitTeam);
	            if(!team.Players.contains(this)) {
	                team.Score++;
	                if(team.Players.contains(level.player) && LastHit==0)
	                    level.player.addEXP(50);
	            }
	        }
	        this.Respawn();
    	} else {//пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
    		
    		int len=Teams.size();
    		for(int i=0;i<len;i++) {
    			Team team=Teams.get(i);
    			if(team.Players.contains(this)) {
    				int me=team.Players.lastIndexOf(this);
    				if(me!=-1) {
    					team.Players.remove(me);
    					Parent.Clone=null;
    				}
    			}
    		}
    	}
    }
    
    private void Respawn() {
        bullet=null; isFire=false;
        LastHitTeam=-1;
        LastHit=-1;
        Panic=false;
        HP=MAXHP;
        if(Parent==null) {
	        int t1,t2;
	        do {
	         t1=rnd.nextInt(M.GetMapSize()[0]-2)+1;
	         t2=rnd.nextInt(M.GetMapSize()[1]-2)+1;
	        } while(!M.CanMove(t1*Tile, t2*Tile));
	        X=t1*Tile;
	        Y=t2*Tile;
	        pe.AddEffect(X+Tile/2, Y+Tile/2, ParticleEmmiter.EFFECT_EXPLOSION, 30, 0, 0xFFFFFFFF, Tile);
        }
        
        BallSize=2+rnd.nextInt(9);
        BallType=rnd.nextInt(Bullet.TYPE_ELECTRIC);
        attackCoolDown=0;
		CanAttack=true;
		Debuff=0;
		DebuffTimer=0;
		
		ability[Ability.Stat_crit]=rnd.nextInt(10);
		ability[Ability.Stat_block]=rnd.nextInt(10);
    }

    private int Round(float x) {
        return ((int)x/Tile)*Tile;
    }
    
    public void SetDebuff(int type, float durMult) {
		if(type!=Bullet.TYPE_NORMAL) Debuff=type;
		switch(Debuff) {
			case Bullet.TYPE_FIRE:
				DebuffTimer=3*durMult;
				break;
			case Bullet.TYPE_ICE:
				DebuffTimer=4*durMult;
				break;
			case Bullet.TYPE_POISON:
				DebuffTimer=3*durMult;
				break;
			default:
				DebuffTimer=0;
				break;
		}
	}
	
	private void ProcessDebuff(float dt) {
		if(DebuffTimer>0) {
			switch(Debuff) {
				case Bullet.TYPE_FIRE:
					HP-=20*dt;
					pe.AddEffect(X + Tile / 2, Y + Tile / 4, ParticleEmmiter.EFFECT_FIRE, 5, 0, 0xFFFF2200, Tile);
					break;
				case Bullet.TYPE_ICE:
					MaxSpeed=2*Tile;
					JumpSpeed=100;
					pe.AddEffect(X + Tile / 2, Y + 3*Tile / 4, ParticleEmmiter.EFFECT_FIRE, 5, 180, 0xDF8080FF, Tile);
					break;
				case Bullet.TYPE_POISON:
					HP-=10*dt;
					MaxSpeed=3*Tile;
					JumpSpeed=110;
					pe.AddEffect(X + Tile / 2, Y + Tile / 4, ParticleEmmiter.EFFECT_FIRE, 5, 0, 0xDF50FF00, Tile);
					break;
			}
			if((DebuffTimer-=dt)<=0) {
				Debuff=0;
				DebuffTimer=0;
			}
		}
	}
    
    public float Hit(float damage, int team, int who, boolean show) {
    	//TODO Ability
    	if(show && rnd.nextInt(100)<ability[Ability.Stat_block]*3) {//Block
			pe.AddText("Block", X+Tile/2-Assets.Font.stringWidth("show", 0.5f)/2, Y, Color + 0xFF000000, Tile / 2);
			pe.AddEffect(X+Tile/2, Y+Tile/2, ParticleEmmiter.EFFECT_EXPLOSION, 30, 0, 0xFFFFFF00, Tile);
			return 0;
		} else {//Hit
			if(show && rnd.nextInt(100)<ability[Ability.Stat_crit]*3) {//Crit
				damage*=2;
//				HP-=damage;
				if(show) {
					pe.AddText("" + (int)damage, X, Y-3, 0.9f, Color==0x00000000?0xFFFFFFFF:0xFF010101, Tile / 2);
					pe.AddText("" + (int)damage, X, Y, 0.75f, Color + 0xFF000000, Tile / 2);
				}
			} else {//Normal
//				HP-=damage;
				if(show) pe.AddText("" + (int)damage, X, Y, Color + 0xFF000000, Tile / 2);
			}
	    	HP-=damage;
	    	//if(show) pe.AddText(""+(int)damage, X+Tile/3, Y, Color+0xFF000000, Tile/2);
			if(team>=0) LastHitTeam=team;
	    	if(who>=0) LastHit=who;
	        CanRegen=false;
	        TimeToRegen=0;
		}
    	return damage;
    }

    private void SearchTarget(float dt) {
        int len=Teams.size();
        int lenT;
        int dist,minDist=1000;
        for(int t=0;t<len;t++) {
            Team team=Teams.get(t);
            if(!team.Players.contains(this)) {
                lenT=team.Players.size();
                for(int i=0;i<lenT;i++) {
                    Actor p=team.Players.get(i);
                    if(AI && p.AA()!=2) {//пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ
                        dist=(int)Math.sqrt(((X+10)-(p.X()+10))*((X+10)-(p.X()+10))+((Y+10)-(p.Y()+10))*((Y+10)-(p.Y()+10)));
                        if(dist<minDist) {
                            pX=(int)p.X();
                            pY=(int)p.Y();
                            minDist=dist;
                        }
                    }
                    if(p.AA()==1 && p.X()>=X-Tile/2 && p.X()<=X+3*Tile/2 && p.Y()>=Y-3*Tile/2 && p.Y<=Y+Tile/2) {//пїЅпїЅпїЅпїЅпїЅ пїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅ
                    	Hit(10*dt,t,p.Number,false);
                    }
                    	
                    if(p.Bullet()!=null) {
                        if(!p.Bullet().isHit() && p.Bullet().Collide((int)X, (int)Y, Tile, Tile)) {
//                        	Hit(p.Bullet().getPower(),t,p.Number,true);
                        	float dmg = Hit(p.Bullet().getPower(),t,p.Number,true);
							if(dmg>0) {
								p.MyHit(dmg);
								SetDebuff(p.Bullet().getType(),dmg/20f);
							}
                            
                            p.Bullet().setHit();
                            //CanRegen=false;
                        }
                    }
                    
                }
            } else {//пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅ
                 lenT=team.Players.size();
                for(int i=0;i<lenT;i++) {
                    Actor p=team.Players.get(i);
                   
                    if(p.AA()==4) {
                        dist=(int)Math.sqrt(((X+10)-(p.X()+10))*((X+10)-(p.X()+10))+((Y+10)-(p.Y()+10))*((Y+10)-(p.Y()+10)));
                        if(dist<2*Tile) {
                            if(HP+5*dt<MAXHP) HP+=5*dt;
                            else HP=MAXHP;
                            pe.AddEffect(X+1, Y+4*Tile/5, ParticleEmmiter.EFFECT_FIRE, 5, 0, 0xDF00E0E0, Tile/2);
                            pe.AddEffect(X+Tile/2, Y+4*Tile/5, ParticleEmmiter.EFFECT_FIRE, 5, 0, 0xDF00E0E0, Tile/2);
                            pe.AddEffect(X+Tile-1, Y+4*Tile/5, ParticleEmmiter.EFFECT_FIRE, 5, 0, 0xDF00E0E0, Tile/2);
                        }
                    }
                        
                    
                }
            }
        }
    }

}
