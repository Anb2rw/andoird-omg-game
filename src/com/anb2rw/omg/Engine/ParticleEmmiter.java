/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Engine;

/**
 *
 * @author АндрюшкА
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.anb2rw.omg.Assets;

/**
 *
 * @author АндрюшкА
 */
public class ParticleEmmiter {
    public static final int EFFECT_FIRE=1;
    public static final int EFFECT_EXPLOSION=2;

    private interface iParticle {    
        public void Update(float dt);
        public void Draw(Canvas canvas, int viewX, int viewY);
        public float life();
    }
    
    private class Particle implements iParticle {

        private Random rnd;

        private int angle,color;
        private float x,y,life;
        private float velocity,_cos, _sin;
//        protected int x,y,color;
//        protected float velocity,life;
        private Paint paint;
        

        public Particle(int _x, int _y, int _angle, int _color, int _size) {
            rnd=new Random();
            x = _x;
            y = _y;
            angle = _angle;
            color = _color;
            velocity = 20.0f + rnd.nextInt(20);
            life = _size/2;// + rnd.nextInt(_size/2);
            _cos = (float) Math.cos(Math.toRadians(angle+90));
            _sin = (float) Math.sin(Math.toRadians(angle+90));
            paint = new Paint();
            paint.setColor(color);
        }
        
        public void Update(float dt) {
            if (life>0) {
                  x-=(_cos*velocity)*dt;
                  y-=(_sin*velocity)*dt;
                  velocity-=10*dt;
                  if (velocity<0) life=0;
                  else life-=10*dt;
            }
        }

        public void Draw(Canvas canvas, int viewX, int viewY) {
          if (life>0) {
            canvas.drawPoint(x-viewX, y-viewY, paint);
          }
        }

        public float life() {
            return life;
        }
    }
    
    private class PopupText implements iParticle {
        private int x,color;
        private float life,dl;
        private float y,velocity;
        private Paint paint;
        private String text;
        private float size=0.5f;
        
        public PopupText(String text, int _x, int _y, float size, int _color, int _life) {
            x = _x;
            y = _y;
            this.size=size;
            this.text=text;
            color = _color;
            velocity = 50.0f;
            life = _life;// + rnd.nextInt(_size/2);
            dl=life;
            paint = new Paint();
            paint.setColor(color);
            paint.setAntiAlias(true);
        }
        
        public void Update(float dt) {
            if (life>0) {
                  //x-=(int)(velocity*dt);
                  y-=velocity*dt;
                  velocity-=20*(size*2)*dt;
                  if (velocity<0) life=0;
                  else life-=10*dt;
                  paint.setAlpha((int)(life*255/dl));
            }
        }

        public void Draw(Canvas canvas, int viewX, int viewY) {
          if (life>0) {
            Assets.Font.drawString(canvas, text, x-viewX, (int)y-viewY, size, paint);
          }
        }
        
        public float life() {
            return life;
        }
    }

    private List<iParticle> list;

    private  Random rnd;

    public ParticleEmmiter() {
     list=new ArrayList<iParticle>();

     rnd=new Random();
    }

    public void AddEffect(float x, float y, int effect, int ammount, int angle, int color, int size) {
     switch (effect){
         case EFFECT_FIRE://Fire
             int max=ammount/2+rnd.nextInt(ammount/2+1);
             for (int i=0; i<max;i++)
             {
                 Particle t = new Particle((int)x,(int)y,angle-30+rnd.nextInt(60),color,size);//0xFF1000
                 list.add(t);
             }
           break;
         case EFFECT_EXPLOSION://Explosion
             int max2=ammount/2+rnd.nextInt(ammount/2);
             for (int i=0; i<max2;i++)
             {
                 Particle t = new Particle((int)x,(int)y,rnd.nextInt(360),color,size);//0x0050FF
                 list.add(t);
             }
          break;
         default:
         break;
     }
    }
    
    public void AddText(String text, float x, float y, int color, int life) {
        PopupText t = new PopupText(text,(int)x,(int)y,0.5f,color,life);//0xFF1000
        list.add(t);
    }
    
    public void AddText(String text, float x, float y, float size, int color, int life) {
        PopupText t = new PopupText(text,(int)x,(int)y,size,color,life);//0xFF1000
        list.add(t);
    }

    public void Update(float dt) {
        if (list.size()>0)
        for (int i=0; i<list.size();i++) {
           iParticle t = (iParticle)list.get(i);

           t.Update(dt);
           if (t.life()<=0) list.remove(i);//
        }
    }
    
    public void Draw(Canvas canvas, int viewX, int viewY) {

        if (list.size()>0)
        for (int i=0; i<list.size();i++) {
               iParticle t = (iParticle)list.get(i);

            t.Draw(canvas,viewX,viewY);
        }

    }

}

