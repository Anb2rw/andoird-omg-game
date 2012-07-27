/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.anb2rw.omg.Assets;
import com.anb2rw.omg.Game;
import com.anb2rw.omg.MainMenu;
import com.anb2rw.omg.Multiplayer;
import com.anb2rw.omg.R;
import com.anb2rw.omg.Settings;
import com.anb2rw.omg.Engine.KeyboardHandler.KeyEvent;
import com.anb2rw.omg.Engine.ParticleEmmiter;
import com.anb2rw.omg.Engine.Screen;

/**
 *
 * @author АндрюшкА
 */
public class Level extends Screen {
    private InGameMenu menu;
    private List<Team> Teams;
    public List<GameObject> gameObjects;
    public Player player;
    public VirtualPad VP;
    private Map Map;
    private int TileSize=25;
    public Random rnd;
    private Paint whitePaint,paint;
    private boolean Pause=false;
    private float viewX=0,viewY=0;
    private Multiplayer Multiplayer;
    private boolean Online=false;
    private float zoomTimer=0f,shakeTimer=0f;
    private float zoom=1f;
    private float Time=1f;
    private ParticleEmmiter pe;
    
    public Level(Game game) {
        super(game);
        
        if(Online) Multiplayer=new Multiplayer(this);
        
        pe = new ParticleEmmiter();
        Teams = new ArrayList<Team>();
        gameObjects = new ArrayList<GameObject>();
//        gameObjects.add(new MovingPlatform(this,50,50));
//        MovingPlatform m = new MovingPlatform(this,50,50);
//        m.isActive=true;
//        gameObjects.add(m);
//        gameObjects.add(new Button(this,3,9,0,m));
//        gameObjects.add(new Button(this,1,8,1,true,m));
        rnd=new Random();
        Map = Assets.maps[Settings.Map];
        Map.init(this);
        
        VP=new VirtualPad(game.getTouch());
        whitePaint=new Paint(); whitePaint.setARGB(255, 255, 255, 255); whitePaint.setTextSize(20); whitePaint.setAntiAlias(true);
        paint=new Paint(); paint.setStyle(Paint.Style.FILL);
        
        Resources res=game.getContext().getResources();
        TypedArray colors = res.obtainTypedArray(R.array.team_colors);
        String[] names = res.getStringArray(R.array.team_names);
        
        List<Integer> l=new ArrayList<Integer>();
        l.add(Settings.PlayerTeam);
        
        for(int i=0;i<Settings.TeamCount;i++) {
            int r;
            if(i==0) r=Settings.PlayerTeam;
            else {
                do {
                    r=rnd.nextInt(names.length);
                } while(l.contains(r));
                l.add(r);
            }
            Team t=new Team(names[r],colors.getColor(r, 0));//0xFFDF0000
            for(int j=0;j<Settings.PlayerCount;j++) {
                if(i==0 && j==0) {
                    player=new Player(this, VP, Map, t.Color);
                    player.TeamNumber=0; player.Number=0;
                    t.Players.add(player);
                }
                else {
                    Enemy e=new Enemy(this,Map,t.Color);
                    e.TeamNumber=i; e.Number=j;
                    if(j==0) {
                    	e.Face=rnd.nextInt(Assets.faces.length);
                    	e.Hat=rnd.nextInt(Assets.hats.length);
                    }
                    t.Players.add(e);
                }
            }
            Teams.add(t);
        }
        
    }
    
    public void Draw(Canvas canvas, float dt) {
        Map.DrawBack(canvas,(int)viewX,(int)viewY);
        
        canvas.save();
        if(zoom!=1f) {
        	canvas.scale(zoom, zoom);
        	canvas.translate((player.X()-viewX)*(1-zoom), (player.Y()-viewY)*(1-zoom));
        }
        if(shakeTimer>0) canvas.translate(0, ((int)(100*shakeTimer))%2==0?5:-5*shakeTimer);
        Map.Draw(canvas,(int)viewX,(int)viewY);
        
        int len=Teams.size();
        int lenT;
        for(int t=0;t<len;t++) {
            Team team=Teams.get(t);
            lenT=team.Players.size();
            for(int i=0;i<lenT;i++) {
                Actor p=team.Players.get(i);
                p.Draw(canvas,(int)viewX,(int)viewY);
            }
        }
        
        for(int i=0;i<gameObjects.size();i++) {
        	GameObject m=gameObjects.get(i);
        	m.Draw(canvas, (int)viewX, (int)viewY);
        }
        
        pe.Draw(canvas, (int)viewX, (int)viewY);
        
        Map.DrawOver(canvas,(int)viewX,(int)viewY);
        canvas.restore();
        
        if(zoom!=1f) {
        	whitePaint.setMaskFilter(new BlurMaskFilter(40,BlurMaskFilter.Blur.NORMAL));
        	whitePaint.setStrokeWidth(30);
        	whitePaint.setStyle(Paint.Style.STROKE);
        	canvas.drawRect(0, 0, 480, 320, whitePaint);
        	whitePaint.setMaskFilter(null);
        }
        if(player.HP()<player.MAXHP()*0.25f) {
        	whitePaint.setMaskFilter(new BlurMaskFilter(40,BlurMaskFilter.Blur.NORMAL));
        	whitePaint.setStrokeWidth((player.MAXHP()*0.25f-player.HP())/(player.MAXHP()*0.25f)*30+1);
        	whitePaint.setStyle(Paint.Style.STROKE);
        	whitePaint.setColor(0xFFFF0000);
        	canvas.drawRect(0, 0, 480, 320, whitePaint);
        	whitePaint.setColor(0xFFFFFFFF);
        	whitePaint.setMaskFilter(null);
        }
        //Player HP
        paint.setARGB(255, 255, 0, 0);
        canvas.drawRect(20, 10, 170, 20, paint);
        paint.setARGB(255, 0, 255, 0);
        canvas.drawRect(20, 10, 20+150*(int)player.HP()/player.MAXHP(), 20, paint);
        //Player MP
        paint.setARGB(255, 255, 0, 0);
        canvas.drawRect(20, 20, 170, 30, paint);
        paint.setARGB(255, 0, 0, 255);
        canvas.drawRect(20, 20, 20+150*(int)player.MP()/player.MAXMP(), 30, paint);
        //Player EXP
        paint.setARGB(255, 100, 100, 0);
        canvas.drawRect(20, 30, 170, 35, paint);
        paint.setARGB(255, 255, 255, 0);
        canvas.drawRect(20, 30, 20+150*player.EXP()/player.MAXEXP(), 35, paint);
        
        paint.setARGB(255, 255, 255, 255);
        Assets.Font.drawString(canvas, ""+player.Level(), 5, 5, 1.0f, paint);
        
        VP.Draw(canvas);
        float left=475;
        ///�����
        if(player.Ability()[5]>0) {
	        Paint paint=new Paint();
	        paint.setARGB(200, 0, 0, 0);
	        float size=0.2f;
	        left=475-Map.GetMapSize()[0]*TileSize*size;
	        //canvas.drawRect(left, 5, 475, 5+Map.GetMapSize()[1]*size, paint);
//	        int m[][]=Map.GetMap();
	        for(int i=0;i<Map.GetMapSize()[0];i++)
	        	for(int j=0;j<Map.GetMapSize()[1];j++) {
	        		if(!Map.CanMove(i*TileSize, j*TileSize)) canvas.drawRect(left+i*TileSize*size, 5+j*TileSize*size, left+i*TileSize*size+TileSize*size, 5+j*TileSize*size+TileSize*size, paint);
	        	}
	        paint.setARGB(200, 255, 255, 255);
	        canvas.drawRect(left+player.X()*size, 5+player.Y()*size, left+player.X()*size+TileSize*size, 5+player.Y()*size+TileSize*size, paint);
	        
	        boolean friends=false;
	        if(player.Ability()[5]>3) {
		        for(int t=0;t<len;t++) {
		            Team team=Teams.get(t);
		            lenT=team.Players.size();
		            if(team.Players.contains(player)) {
		            	paint.setARGB(200, 100, 100, 255);
		            	friends=true;
		            } else {
		            	paint.setARGB(200, 255, 100, 100);
		            	friends=false;
		            }
		            if(friends || player.Ability()[5]>6)
		            for(int i=0;i<lenT;i++) {
		                Actor p=team.Players.get(i);
		                if(!p.equals(player) && (player.Ability()[5]==10 || p.AA()!=2))
		                	canvas.drawRect(left+p.X()*size, 5+p.Y()*size, left+p.X()*size+TileSize*size, 5+p.Y()*size+TileSize*size, paint);
		            }
		        }
	        }
        }
        
        for(int t=0;t<len;t++) {
            Team team=Teams.get(t);
            Assets.Font.drawString(canvas, team.Name+": "+team.Score, (int)left-5-Assets.Font.stringWidth(team.Name+": "+team.Score, 0.5f), 25+t*15, 0.5f, whitePaint);
            //canvas.drawText(team.Name+": "+team.Score, 300, 25+t*15, whitePaint);
        }
        
        if(Pause) {
            canvas.drawARGB(150, 0, 0, 0);
            //canvas.drawText("Пауза. Коснитесь экрана, чтобы возобновить", 0, canvas.getHeight()-15, whitePaint);
            if(menu!=null) menu.Draw(canvas);
        }
        
    }
    
    @Override
    public void Update(float dt) {
    	dt*=Time;
        List<KeyEvent> keyEvents = game.getKey().getKeyEvents();
        
        int l = keyEvents.size();
        
        for(int i = 0; i < l; i++) {
            KeyEvent event = keyEvents.get(i);
            if(event.type == KeyEvent.KEY_DOWN) {
                if(event.keyCode==android.view.KeyEvent.KEYCODE_BACK) {
                    Pause();
                } else if(event.keyCode==android.view.KeyEvent.KEYCODE_MENU) {
                	if(!Pause) {
                        Pause=true;
                        menu=new InGameMenu(this);
                    } else {
                        Pause=false;
                        menu=null;
                    }
                }
            }
        }
        
        if(!Pause) {
        	boolean isplaball=player.Bullet()!=null?true:false;
        	
        	if(zoomTimer>0) {
        		zoomTimer-=dt;
        		if(zoomTimer<=0) {
        			zoom=1f;
        			Time=1f;
        		}
        	}
        	
        	if(shakeTimer>0) {
        		shakeTimer-=dt;
        		if(shakeTimer<=0) {
        			zoom=1f;
        			Time=1f;
        		}
        	}
        	
            VP.Control();
            int len=Teams.size();
//            int lenT;
            for(int t=0;t<len;t++) {
                Team team=Teams.get(t);
//                lenT=team.Players.size();
                for(int i=0;i<team.Players.size();i++) {
                    Actor p=team.Players.get(i);
                    if(p!=null)
                    	p.Update(dt);
                }
            }
            
            for(int i=0;i<gameObjects.size();i++) {
            	GameObject m=gameObjects.get(i);
            	if(!m.Update(dt)) {
            		gameObjects.remove(m);
//            		--i;
            	}
            }
            
            pe.Update(dt);
            
            //if(player.X()>360*3/4) viewX+=player.*dt;
            viewX=player.GetCameraX(viewX,480,dt);
            viewY=player.GetCameraY(viewY,220,dt);
            
            if(Online) {
            	boolean is2=player.Bullet()!=null?true:false;
            	String s;
            	if(!isplaball && is2) s="A"; else s="";
            	String str="X"+player.X()+"|"+"Y"+player.Y()+"|"+s+"#";
            	Multiplayer.sendMessage(str);
            }
        } else {
            if(menu!=null) 
            if(!menu.Update(dt)) {
                Pause=false;
                menu=null;
            }
        }
        //player.Update(dt);
    }
    
    public int TileSize() {
        return TileSize;
    }
    
    public ParticleEmmiter ParticleEmmiter() {
    	return pe;
    }
    
    public List<Team> Teams() {
        return Teams;
    }
    
    public void SetCamera(int x, int y) {
        viewX=x;
        viewY=y;
    }
    
    private void Pause() {
        if(!Pause) {
            Pause=true;
            menu=new InGameMenu(this);
        } else {
        	if(menu!=null)
        		if(menu.isMenu)
        			game.setScreen(new MainMenu(game));
        		else menu.isMenu=true;
        }
    }

    @Override
    public void pause() {
        //
    }

    @Override
    public void resume() {
        //
    }

    @Override
    public void dispose() {
        Teams.clear();
    }

    public void Save() {
        Settings.save(game.getContext());
    }
    
    public Game game() {
        return game;
    }
    
    public void Exit() {
        game.setScreen(new MainMenu(game));
    }
    
    public void SetZoom(float zoom, float duration) {
    	this.zoom=zoom;
    	this.zoomTimer = duration;
    	Time=2-zoom;
    }
    
    public void Shake(float duration) {
    	shakeTimer = duration;
    }

    public void readMessage(String readMessage) {
		if(!Online) return;
		
		Enemy e=(Enemy)Teams.get(1).Players.get(0);
		
		int xpos=readMessage.indexOf("X");
		int ypos=readMessage.indexOf("Y");
		int apos=readMessage.indexOf("A");
		int end;
		if(xpos!=-1 && ypos!=-1) {
			end=readMessage.indexOf("|", xpos);
			float x=Float.parseFloat(readMessage.substring(xpos+1, end));
			end=readMessage.indexOf("|", ypos);
			float y=Float.parseFloat(readMessage.substring(ypos+1, end));
			
			e.X=x; e.Y=y;
		}
		
		if(apos!=-1) e.Fire(e.pX, e.pY);
		
		
	}

    
}
