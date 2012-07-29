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
import com.anb2rw.omg.Settings;
import com.anb2rw.omg.Engine.ParticleEmmiter;

/**
 * 
 * @author РђРЅРґСЂСЋС€РєРђ
 */
public class Player extends Actor {
	private Random rnd;
	private int Color = 0x00DF0000;
	private Paint paint;
	private Map M;
	private Level level;
	private VirtualPad vp;
	private boolean isJump = true, isFire = false;
	private boolean AirJump = false;
	private int Tile = 25;
	private Bitmap face;
	private Matrix Mat;
	private List<Team> Teams;
	private int LastHitTeam;
	private int MaxSpeed, MaxFallSpeed, FallVelocity, JumpSpeed = 120;
	private int Lvl = 100, EXP = 0, MAXEXP = 100, Points = 0;
	private int selAA = -1;
	private int[] Ability={
    	10, //Air Jump//0
        10, //Rocket Boots//1
        10, //Invisibility//2
        10, //Vampire//3
        10, //Heal//4
        10, //Radar//5
        10, //Dash//6
        1, //Shadow Clone//7
        1, //Time//8
        1, //Miner//9
        2, //Crit//10
        2, //Block//11
        1, //Return//12
        0, //Fury//10
        0, //Amaterasu//11
        0  //Far Away//12
    };
	private boolean UpOnce = false;
	private Mine returnMark;

	public Player(Level l, VirtualPad vp, Map m, int C) {
		
		this.level = l;
		this.vp = vp;
		this.Color = C;
		
		rnd = level.rnd;
		Lvl = Settings.Level;
		EXP = Settings.PlayerEXP;
		MAXEXP = 100 + 50 * (Lvl - 1);
		
		Ability = Settings.Ability;
		selAA=Settings.selAA;
		if(selAA==-1) SwitchAA(false);
		if(selAA>=0 && selAA<Assets.special.length) vp.specialIcon=Assets.special[selAA]; else vp.specialIcon=null;
		Hat=Settings.Hat;
		Face=Settings.Face;
		
		this.M = m;
		Tile = level.TileSize();
		
		pe = level.ParticleEmmiter();
		BallSize=Settings.BulletSize;
		BallType=Settings.BulletType;
		Respawn();
		// X=Y=Tile;
		MaxSpeed = 4 * Tile;
		MaxFallSpeed = 2 * MaxSpeed;
		FallVelocity = MaxSpeed;
		JumpSpeed = 5 * Tile;
		Teams = level.Teams();
		paint = new Paint();
		Mat = new Matrix();
		
		face = Bitmap.createBitmap(Tile + 1, Tile + 1, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(face);

		paint.setStyle(Paint.Style.FILL);
		// paint.setARGB(255, 255, 20, 20);
		paint.setColor(Color + 0xFF202020);
		canvas.drawRect(0, 0, Tile, Tile / 2, paint);
		// paint.setARGB(255, 235, 0, 0);
		paint.setColor(Color + 0xFF000000);
		canvas.drawRect(0, Tile / 2, Tile, Tile, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setARGB(255, 0, 0, 0);
		canvas.drawRect(0, 0, Tile, Tile, paint);

		if (Pos) {
			canvas.drawLine(Tile / 2, 3 * Tile / 5 + 1, Tile / 2, 7 * Tile / 10, paint);// P
			canvas.drawLine(9 * Tile / 10, 3 * Tile / 5 + 1, 9 * Tile / 10, 7 * Tile / 10, paint);// O
			canvas.drawLine(Tile / 2, 7 * Tile / 10, 9 * Tile / 10 + 1, 7 * Tile / 10, paint);// T
			canvas.drawPoint(Tile / 2, 2 * Tile / 5, paint);
			canvas.drawPoint(9 * Tile / 10, 2 * Tile / 5, paint);
		} else {
			canvas.drawLine(Tile / 10, 3 * Tile / 5 + 1, Tile / 10, 7 * Tile / 10, paint);
			canvas.drawLine(Tile / 2, 3 * Tile / 5 + 1, Tile / 2, 7 * Tile / 10, paint);
			canvas.drawLine(Tile / 10, 7 * Tile / 10, Tile / 2 + 1, 7 * Tile / 10, paint);
			canvas.drawPoint(Tile / 10, 2 * Tile / 5, paint);
			canvas.drawPoint(Tile / 2, 2 * Tile / 5, paint);
		}
		
		int num=0;
		for(int i=0;i<Ability.length;i++) {
			num+=Ability[i];
			Points=Lvl-num;
		}
	}

//	public void Fire() {
//		if (!isFire && CanAttack) {
//			isFire = true;
//			CanAttack=false;
//			bullet = new Bullet(this, BallSize, BallType, Tile, map);
//			if(AA==2) { AA=-1; vp.SPECIAL = false; }
//		}
//	}

	@Override
	public void Draw(Canvas canvas, int vX, int vY) {

		// paint.setARGB(255, 255, 0, 0);
		// paint.setStyle(Paint.Style.FILL);
		// canvas.drawRect(X, Y-5, X+Tile, Y-3, paint);
		// paint.setARGB(255, 0, 255, 0);
		// canvas.drawRect(X, Y-5, X+Tile*HP/MAXHP, Y-3, paint);

		if (bullet != null) {
			paint.setARGB(255, 0, 0, 0);
			canvas.drawLine(X + Tile / 2 - vX, Y + Tile / 2 - vY, bullet.X - vX, bullet.Y - vY, paint);
		}
		if (AA != 2) {
			Mat.setTranslate(X - vX, Y - vY);
//			Mat.preScale(-1, -1);
			Mat.preSkew(dX*0.0005f,0,-Tile,Tile);
//			Mat.preScale(-1, -1);
			if (!Pos) {
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
			
		} else {
			paint.setColor(0xFF000000);
			canvas.drawRect(X - vX, Y - vY, X - vX + Tile, Y - vY + Tile, paint);
		}
		if(AA==6) {
			paint.setColor(0xFFFFFFFF);
			for(int i=0;i<Tile;i+=2) {
				canvas.drawLine(X-vX, Y-vY+i, X+Tile-vX, Y-vY+i, paint);
			}
		}
		paint.setColor(0xFF000000);

		if (bullet != null)
			bullet.Draw(canvas, vX, vY);

		
//		pe.Draw(canvas, vX, vY);
		
//		Assets.Font.drawString(canvas, ""+selAA, vp.Special.left+10, vp.Special.top+10, 0.9f, paint);
	}

	@Override
	public void Update(float dt) {
		
		if(M.GetTile(((int)X+Tile/2)/Tile, ((int)Y+Tile/2)/Tile)==Map.TILE_DEATH || M.GetTile(((int)X+Tile/2)/Tile, ((int)Y+Tile/2)/Tile)==-1) {
			Kill();
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
			
		if (AA == 1)
			AA = -1;
		
		// Jump
		boolean UP = vp.UP;
		if (UP) {
			if (UpOnce)
				UpOnce = false;
			else
				UP = false;
		} else
			UpOnce = true;

		if (UP && !Stun) {
			if (!isJump || M.GetTile(((int)X+Tile/2)/Tile, ((int)Y+Tile/2)/Tile)==Map.TILE_WATER) {
				isJump = true;
				dY = -JumpSpeed;
			} else if (Ability[0] > 0 && !AirJump) {
				if (MP > (50 - Ability[0] * 5)) {
					MP -= (50 - Ability[0] * 5);
					dY = -JumpSpeed * 3 / 4;
					AirJump = true;
					pe.AddEffect(X + Tile / 2, Y + Tile, 2, 40, 0, 0xFF0050DF, 20);
				}
			} else if (Ability[1] > 0 && vp.UP) {// Fly
				AA = 1;
				UpOnce = true;
			}
		}


		if (AA == -1) {//пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
			if (MP < MAXMP)
				MP += 10 * dt;//пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ
		} else if (AA == 1) {
			if (MP > (11 - Ability[1]) * dt)
				MP -= 10*(11 - Ability[1]) * dt;
			else {
				MP = 0;
				AA = -1;
			}
		} else if (AA == 2) {
			if (MP > 10*(11 - Ability[2])*dt)
				MP -= 10*(11 - Ability[2]) * dt;
			else {
				AA = -1;
				vp.SPECIAL = false;
			}
		} else if (AA == 4) {
			if (MP > 10*(11 - Ability[4])*dt)
				MP -= 10*(11 - Ability[4]) * dt;
			else {
				AA = -1;
				vp.SPECIAL = false;
			}
		} else if (AA == 6) {
				AA = -1;
		}
		
		if (AA == 1) {
			if (dY > -MaxFallSpeed / 3)
				dY -= dt * FallVelocity * 2 * ep;
			pe.AddEffect(X + Tile / 4, Y + Tile, 1, 20, 180, 0xDFFF1000, 6);
			pe.AddEffect(X + Tile * 3 / 4, Y + Tile, 1, 20, 180, 0xDFFF1000, 6);
		} else if (AA == 4) {
			if (HP + 10 * dt < MAXHP)
				HP += 10 * dt;
			else
				HP = MAXHP;
			pe.AddEffect(X + 1, Y + 4 * Tile / 5, ParticleEmmiter.EFFECT_FIRE, 5, 0, 0xDF00E0E0, Tile / 2);
			pe.AddEffect(X + Tile / 2, Y + 4 * Tile / 5, ParticleEmmiter.EFFECT_FIRE, 5, 0, 0xDF00E0E0, Tile / 2);
			pe.AddEffect(X + Tile - 1, Y + 4 * Tile / 5, ParticleEmmiter.EFFECT_FIRE, 5, 0, 0xDF00E0E0, Tile / 2);
		}

		if (isJump) {
			if (dY * dt*ep < dt*ep * MaxFallSpeed)
				dY += dt * FallVelocity*M.GetGravity()*ep;// else dY = 5;
			if (dY <= 0) {
				if(M.CanMove((int) (X), (int) (Y + dY * dt*ep)) && M.CanMove((int) (X + Tile - 1), (int) (Y + dY * dt*ep))) {
					Y += (dY * dt*ep);
				} else {
//					Y=M.GetPositionY(X, Y + dY * dt*ep)+Tile;
					Y = (int) ((Y) / Tile) * Tile;
					dY = 0;
				}

			} else
			if(M.CanMove((int) (X), (int) (Y + dY * dt*ep + 1 + Tile)) && M.CanMove((int) (X + Tile - 1), (int) (Y + dY * dt*ep + 1 + Tile))) {
				Y += (dY * dt*ep);
			} else 
			{
//				Y=M.GetPositionY(X, Y + dY * dt*ep + 1 + Tile)-Tile;
				Y = Round(Y + Tile + dY * dt*ep + 1) - Tile;
				dY = 0;
				isJump = false;
				AirJump = false;
			}
		} else {// !Jump
			if(M.CanMove((int) (X), ((int) Y + Tile + Tile / 4)) && M.CanMove(((int) X + Tile - 1), ((int) Y + Tile + Tile / 4))) {
				dY = 5 * Tile * dt*ep;
				isJump = true;
			} else
//				Y=M.GetPositionY(X, Y + Tile + Tile / 4)-Tile;
				Y = Round(Y + Tile + 5) - Tile;
		}
		
		if(vp.FIRE) {
			if (!isFire && CanAttack && !Stun) {
				isFire = true;
				CanAttack=false;
				bullet = new Bullet(this, BallSize, BallType, Tile, M);
				if(AA==2) { AA=-1; vp.SPECIAL = false; }
			}
		}
		
		if(AA!=1) {//пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
			if (vp.SPECIAL && selAA!=-1 && !Stun) {
				if (Settings.ActivableAbility[selAA] == 2) {
					if (AA != 2)
						pe.AddEffect(X + Tile / 2, Y + Tile / 2, ParticleEmmiter.EFFECT_EXPLOSION, 20, 0, 0xEFFFFFFF, Tile);
					AA = 2;
				} else if (Settings.ActivableAbility[selAA] == 4) {
					AA = 4;
				} else if (Settings.ActivableAbility[selAA] == 6) {
					vp.SPECIAL = false;
					if(MP>55-Ability[6]*5)
				          if(!Pos) {
				        	  if(X>Tile*2 && M.CanMove(((int)X-Tile*2), (int)(Y)) && M.CanMove(((int)X-Tile*2), ((int)Y+Tile-1)) && M.CanMove(((int)X-Tile-1), ((int)Y)) && M.CanMove(((int)X-Tile-1), ((int)Y+Tile-1)))
				                { X-=Tile*2; AA=6; MP-=55-Ability[6]*5; 
									level.SetZoom(1.2f, 0.4f);}
				        	  else if(X>Tile && M.CanMove(((int)X-Tile), ((int)Y)) && M.CanMove(((int)X-Tile), ((int)Y+Tile-1)) && M.CanMove(((int)X-1), ((int)Y)) && M.CanMove(((int)X-1), ((int)Y+Tile-1)))
				                { X-=Tile; AA=6; MP-=52-Ability[6]*5; 
									level.SetZoom(1.1f, 0.3f);}
				              else {
								  X = Round(X);
								  
							  }
				          }
				          else {
				        	 if(X<Tile*(M.GetMapSize()[0]-3) && M.CanMove(((int)X+Tile*2+1), ((int)Y)) && M.CanMove(((int)X+Tile*2+1), ((int)Y+Tile-1)) && M.CanMove(((int)X+Tile*3-1), ((int)Y)) && M.CanMove(((int)X+Tile*3-1), ((int)Y+Tile-1)))
				                { X+=Tile*2; AA=6; MP-=55-Ability[6]*5; 
									level.SetZoom(1.2f, 0.4f);}
				        	 else if(X<Tile*(M.GetMapSize()[0]-2) && M.CanMove(((int)X+Tile+1), ((int)Y)) && M.CanMove(((int)X+Tile+1), ((int)Y+Tile-1)) && M.CanMove(((int)X+Tile*2-1), ((int)Y)) && M.CanMove(((int)X+Tile*2-1), ((int)Y+Tile-1)))
				                { X+=Tile; AA=6; MP-=52-Ability[6]*5;
									level.SetZoom(1.1f, 0.3f);}
				             else {
								 X = Round(X+Tile+Tile/2) - Tile;
								 
							 }
				          }
				} else if(Settings.ActivableAbility[selAA]==7) {
					vp.SPECIAL = false;
					level.SetZoom(1.4f, 2);
				} else if(Settings.ActivableAbility[selAA]==8) {
					vp.SPECIAL = false;
					if(Clone==null) {
						if(MP>=100) {
							MP-=100;
							MakeClone();
						}
					} else Clone.Death();
				} else if(Settings.ActivableAbility[selAA]==9) {
					vp.SPECIAL = false;
					if(!isJump) {
						Mine mine=new Mine(level,this,X,Y,0);
						level.gameObjects.add(mine);
					}
				} else if(Settings.ActivableAbility[selAA]==12) {
					vp.SPECIAL = false;
					if(returnMark==null) {
						returnMark=new Mine(level,this,X,Y,1);
						level.gameObjects.add(returnMark);
					} else {
						X=returnMark.X;
						Y=returnMark.Y;
						returnMark.HP=0;
						level.Shake(0.4f);
						returnMark=null;
					}
				}
				
			} else {
				if (AA != 1)
				AA = -1;
			}
		}//AA!=1
		
		if(vp.SPECIAL_U) {
			vp.SPECIAL_U=false;//!
			
			SwitchAA(false);
			
		} else if(vp.SPECIAL_D) {
			vp.SPECIAL_D=false;//!
			
			SwitchAA(true);
			
		}

		boolean move = false;
		// РЈРїСЂР°РІР»РµРЅРёРµ
		if (vp.RIGHT && !Stun) {
			if (dX >= 0) {
				dX += 5;
				move = true;
			}
			if (!Pos)
				Pos = true;
		}

		if (vp.LEFT && !Stun) {
			if (dX <= 0) {
				dX -= 5;// 10*vp.HStickOffset;
				move = true;
			}
			if (Pos)
				Pos = false;
		}
		
		if(M.GetTile(((int)X)/Tile, ((int)Y+Tile+1)/Tile)==3 || M.GetTile(((int)X+Tile-1)/Tile, ((int)Y+Tile+1)/Tile)==3) {//Fire Block
			HP-=2*dt;
			pe.AddEffect(X+2, Y + Tile, ParticleEmmiter.EFFECT_FIRE, 2, 0, 0xFFFF2200, Tile/2);
			pe.AddEffect(X+Tile-2, Y + Tile, ParticleEmmiter.EFFECT_FIRE, 2, 0, 0xFFFF2200, Tile/2);
		}

		if (!move)
			if (Math.abs(dX) < 1)
				dX = 0;
			else {
				if(M.GetTile(((int)X)/Tile, ((int)Y+Tile+1)/Tile)==2 || M.GetTile(((int)X+Tile-1)/Tile, ((int)Y+Tile+1)/Tile)==2) {
					dX *= 0.9;// Ice
					pe.AddEffect(X+2, Y + Tile, ParticleEmmiter.EFFECT_FIRE, 2, 0, 0xDEDDDDFF, Tile/2);
					pe.AddEffect(X+Tile-2, Y + Tile, ParticleEmmiter.EFFECT_FIRE, 2, 0, 0xDEDDDDFF, Tile/2);
				} else
					dX *= 0.5;
			}

		if (dX > 0) {
			if(!M.CanMove(((int) X + Tile + (int) (dX * dt*ep)), ((int) Y)) || !M.CanMove(((int) X + Tile + (int) (dX * dt*ep)), ((int) Y + Tile - 1))) {
				X = Round(X + Tile + dX * dt*ep + 1) - Tile;
				dX = 0;
			}
		} else if (dX < 0) {
			if(!M.CanMove(((int) X + ((int) (dX * dt*ep) - 1)), ((int) Y)) || !M.CanMove(((int) X + ((int) (dX * dt*ep) - 1)), ((int) Y + Tile - 1))) {
				X = ((X) / Tile) * Tile;
				dX = 0;
			}
		}
		
		if (dX < -MaxSpeed)
			dX = -MaxSpeed;
		else if (dX > MaxSpeed)
			dX = MaxSpeed;

		X += (dX * dt*ep);

		if (bullet != null)
			if (!bullet.Update(dt)) {
				bullet = null;
				isFire = false;
			}

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
		DetectDamage();

		if (HP <= 0) {
			Kill();
		}
		
		if(!CanAttack) {
			if((attackCoolDown+=dt)>=BallSize/4) {
				CanAttack=true;
				attackCoolDown=0;
			}
		}

//		pe.Update(dt);

	}

	private int Round(float x) {
		return ((int) x / Tile) * Tile;
	}
	
	public void Kill() {
		if (LastHitTeam > -1) {
			Team team = Teams.get(LastHitTeam);
			if (!team.Players.contains(this))
				team.Score++;
		}
		level.Shake(0.5f);
		Death();
	}
	
	public void Death() {
    	pe.AddEffect(X+Tile/2, Y+Tile/2, ParticleEmmiter.EFFECT_EXPLOSION, 30, 0, 0xFFFFFFFF, Tile);
    	if(Clone!=null) Clone.Death();
        this.Respawn();
    }

	private void Respawn() {
		bullet = null;
		isFire = false;
		LastHitTeam = -1;
		HP = MAXHP;
		MP = 0;
		X = Tile;
		Y = Tile;
		level.SetCamera(0, 0);
		pe.AddEffect(X + Tile / 2, Y + Tile / 2, ParticleEmmiter.EFFECT_EXPLOSION, 30, 0, 0xFFFFFFFF, Tile);
		attackCoolDown=0;
		CanAttack=true;
		Debuff=0;
		DebuffTimer=0;
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

	private void DetectDamage() {
		int len = Teams.size();
		int lenT;
		for (int t = 0; t < len; t++) {
			Team team = Teams.get(t);
			if (!team.Players.contains(this)) {
				lenT = team.Players.size();
				for (int i = 0; i < lenT; i++) {
					Actor p = team.Players.get(i);
					if (p.Bullet() != null) {
						if (!p.Bullet().isHit() && p.Bullet().Collide((int) X, (int) Y, Tile, Tile)) {
							float dmg = Hit(p.Bullet().getPower(),t,p.Number,true);
							if(dmg>0) {
								SetDebuff(p.Bullet().getType(),dmg/20f);
							}
							p.Bullet().setHit();
							
						}
					}
				}
			}
		}
	}
	
	public float Hit(float damage, int team, int who, boolean show) {
		if(show && rnd.nextInt(100)<Ability[11]*3) {//Block
			pe.AddText("Block", X+Tile/2-Assets.Font.stringWidth("Block", 0.5f)/2, Y, Color + 0xFF000000, Tile / 2);
			pe.AddEffect(X+Tile/2, Y+Tile/2, ParticleEmmiter.EFFECT_EXPLOSION, 30, 0, 0xFFFFFF00, Tile);
			return 0;
		} else {//Hit
			if(show && rnd.nextInt(100)<Ability[10]*3) {//Crit
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
			if(team>=0) LastHitTeam=team;
		}
		return damage;
    }

	public float GetCameraX(float vX, int w, float dt) {
		float viewX = vX;
		float speed = 0;
		if (dX == 0)
			speed = MaxSpeed * dt;
		else
			speed = Math.abs(dX) * dt;

		if (Pos) {
			if (X - viewX > w / 3) {
				if (viewX < M.GetMapSize()[0] * Tile - w - speed)
					viewX += speed;
				else
					viewX = M.GetMapSize()[0] * Tile - w;
			} else if (X - viewX < w / 10)
				if (viewX > speed)
					viewX -= speed;
				else
					viewX = 0;
		} else {
			if (X - viewX < 2 * w / 3) {
				if (viewX > speed)
					viewX -= speed;
				else
					viewX = 0;
			} else if (X + Tile - viewX > 9 * w / 10)
				if (viewX < M.GetMapSize()[0] * Tile - w - speed)
					viewX += speed;
				else
					viewX = M.GetMapSize()[0] * Tile - w;
		}

		return viewX;
	}

	public float GetCameraY(float vY, int h, float dt) {
		float viewY = vY;
		float speed = 0;
		if (dY == 0)
			speed = MaxFallSpeed * dt;
		else
			speed = Math.abs(dY) * dt;

		if (Y - viewY < h / 3) {
			if (viewY > speed) {
				viewY -= speed;
			} else
				viewY = 0;
		} else {
			if (Y - viewY > 2 * h / 3)
				if (viewY < M.GetMapSize()[1] * Tile - h - speed) {
					viewY += speed;
				} else
					viewY = M.GetMapSize()[1] * Tile - h;
		}

		return viewY;
	}

	public int Level() {
		return Lvl;
	}

	public int EXP() {
		return EXP;
	}

	public int MAXEXP() {
		return MAXEXP;
	}

	public void addEXP(int e) {
		EXP += e;
		if (EXP >= MAXEXP) {
			pe.AddText("Level Up", X - 50, Y, 0xFFFFFFFF, Tile);
			int dL=EXP / MAXEXP;
			Lvl += dL;
			Points += dL;
			EXP %= MAXEXP;
			MAXEXP += 50;
			Settings.Level = Lvl;
			level.Save();
		}
		Settings.PlayerEXP = EXP;
	}

	public int[] Ability() {
		return Ability;
	}

	public void ModAbility(int a, int mod) {
		if (a >= 0 && a < Ability.length)
			if (Points - mod >= 0) {
				if (Ability[a] + mod >= 0 && Ability[a] + mod <= 10) {
					Ability[a] += mod;
					Points -= mod;
					Settings.Ability[a] = Ability[a];
					if(Ability[a]==0) if(selAA!=-1 && Settings.ActivableAbility[selAA]==a) SwitchAA(false);
					if(Ability[a]>0 && selAA==-1) SwitchAA(true);
					level.Save();
				}
			}
	}

	public int BallSize() {
		return BallSize;
	}

	public void ModBallSize(int mod) {
		if (BallSize + mod > 1 && BallSize + mod < 12) {
			BallSize += mod;
			Settings.BulletSize=BallSize;
		}
	}

	public int Points() {
		return Points;
	}

	public int BallType() {
		return BallType;
	}

	public void ChangeBallType(int mod) {
		BallType += mod;
		if (BallType < 0)
			BallType = Bullet.TYPE_ELECTRIC;
		else if (BallType > Bullet.TYPE_ELECTRIC)
			BallType = 0;
		Settings.BulletType=BallType;
	}

	@Override
	public void MyHit(float Damage) {
		addEXP((int)(Damage/4) + 1);
		if (Ability[3] > 0)
			if (rnd.nextInt(15) <= Ability[3]) {
				if (HP + Damage/2.0f<= MAXHP)
					HP += Damage/2.0f;
				else
					HP = MAXHP;
				pe.AddEffect(X + Tile / 5, Y + 2, ParticleEmmiter.EFFECT_FIRE, (int)Damage, 0, 0xEF00A0A0, Tile / 2);
				pe.AddEffect(X + 4 * Tile / 5, Y + 2, ParticleEmmiter.EFFECT_FIRE, (int)Damage, 0, 0xEF00A0A0, Tile / 2);
			}
	}
	
	private void SwitchAA(boolean down) {
//		int prevAA=selAA;
		int t=selAA==-1?0:selAA;
		do {
			if(down) {
				if(--t<0) t=Settings.ActivableAbility.length-1;
			} else {
				if(++t>=Settings.ActivableAbility.length) t=0;
			}
			if(t==selAA || (selAA==-1 && t==0)) {//Сделали круг
				if(Ability[Settings.ActivableAbility[t]]==0) {
					vp.specialIcon=null;
					selAA=-1;
				} else {
					break;
				}
				return;
			}
			
		} while(Ability[Settings.ActivableAbility[t]]==0);
		selAA=t;
		Settings.selAA=selAA;
		if(t<Assets.special.length) vp.specialIcon=Assets.special[t]; else vp.specialIcon=null;
	}
	
	private void MakeClone() {
		
		int len=Teams.size();
		
		for(int i=0;i<len;i++) {
			Team team=Teams.get(i);
			if(team.Players.contains(this)) {
				Enemy e=new Enemy(level,M,Color+0x00010101);
				e.Number=Number;
				e.X=X; e.Y=Y; e.MAXHP=MAXHP; e.HP=(Ability[8]/10f)*MAXHP;
				e.BallSize=BallSize; e.BallType=BallType;
				e.Face=Face; e.Hat=Hat;
				e.RegenDivide=0;
				e.setParent(this);
				team.Players.add(e);
				this.Clone=e;
				pe.AddEffect(X+Tile/2, Y+Tile/2, ParticleEmmiter.EFFECT_EXPLOSION, 30, 0, 0xFFFFFFFF, Tile);
			}
		}
		
	}

}
