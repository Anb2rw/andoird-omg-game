/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Gameplay;

import com.anb2rw.omg.Engine.ParticleEmmiter;

import android.graphics.Canvas;

/**
 *
 * @author АндрюшкА
 */
public abstract class Actor {
	protected int TeamNumber=0,Number=0;
    protected float HP,MP;
    protected int MAXHP=100,MAXMP=100;
    protected float X,Y;
    protected float dX,dY;
    protected boolean Pos=true;
    protected Bullet bullet;
    protected int BallSize=4,BallType=Bullet.TYPE_NORMAL;
    protected int AA=-1;
    protected ParticleEmmiter pe;
    protected float attackCoolDown=0,DebuffTimer=0,stunTimer=0;
    protected boolean Stun = false;
    protected boolean CanAttack=true;
    protected int Debuff=0;
    protected Enemy Clone;
    protected int Hat=0,Face;
    protected float RegenDivide=0.25f;
    
    public float X() {
        return X;
    }

    public float Y() {
        return Y;
    }
    
    public boolean Pos() {
        return Pos;
    }
    
    public float HP() {
        return HP;
    }
    public int MAXHP() {
        return MAXHP;
    }
    
    public float MP() {
        return MP;
    }
    public int MAXMP() {
        return MAXMP;
    }
    
    public int AA() {
        return AA;
    }
    
    public ParticleEmmiter ParticleEmmiter() {
    	return pe;
    }
    
    public void Draw(Canvas canvas, int viewX, int ViewY) {
        
    }
    
    public void Update(float deltatime) {
        
    }
    
    public void MyHit(float Damage) {
        
    }
    
    public Bullet Bullet() {
        return bullet;
    }
    
    public float Hit(float damage, int team, int who, boolean show) {
		return 0;
    }
    
    public void Death() {
    	
    }
    
}
