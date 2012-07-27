/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.anb2rw.omg.Engine.Font;
import com.anb2rw.omg.Engine.MapReader;
import com.anb2rw.omg.Engine.Screen;
import com.anb2rw.omg.Gameplay.Map;

/**
 *
 * @author –ê–Ω–¥—Ä—é—à–∫–ê
 */
public class LoadingScreen extends Screen {
    Paint paint;
    
    public LoadingScreen(Game game) {
        super(game);
        paint = new Paint();
        paint.setTextSize(20);
        paint.setARGB(255, 0, 255, 0);
    }

    @Override
    public void Update(float deltaTime) {
        Resources res=game.getContext().getResources();
        Assets.Control=BitmapFactory.decodeResource(res, R.drawable.control);
        Assets.Menu_background=BitmapFactory.decodeResource(res, R.drawable.menu_back);
        Assets.back=BitmapFactory.decodeResource(res, R.drawable.back).extractAlpha();
        Assets.yes=BitmapFactory.decodeResource(res, R.drawable.yes).extractAlpha();
        Assets.no=BitmapFactory.decodeResource(res, R.drawable.no).extractAlpha();
        Assets.arrow=BitmapFactory.decodeResource(res, R.drawable.arrow).extractAlpha();
        
        Assets.Font=new Font(BitmapFactory.decodeResource(res, R.drawable.font1_30));
        Assets.AbNames=res.getStringArray(R.array.ability_names);
        Assets.BallTypeNames=res.getStringArray(R.array.balltype_names);
        Assets.Menu_custom=BitmapFactory.decodeResource(res, R.drawable.menu_cust);
//        Assets.hat1=BitmapFactory.decodeResource(res, R.drawable.hat1);
//        Assets.hat2=BitmapFactory.decodeResource(res, R.drawable.hat2);
//        Assets.hat3=BitmapFactory.decodeResource(res, R.drawable.hat3);
//        Assets.face1=BitmapFactory.decodeResource(res, R.drawable.face1);
//        Assets.face2=BitmapFactory.decodeResource(res, R.drawable.face2);
//        Assets.face3=BitmapFactory.decodeResource(res, R.drawable.face3);
        
        TypedArray imgs = res.obtainTypedArray(R.array.hats);
        Assets.hats=new Bitmap[imgs.length()];
        for(int i=0;i<imgs.length();i++) {
        	Assets.hats[i]=BitmapFactory.decodeResource(res, imgs.getResourceId(i, -1));
        }
        imgs = res.obtainTypedArray(R.array.faces);
        Assets.faces=new Bitmap[imgs.length()];
        for(int i=0;i<imgs.length();i++) {
        	Assets.faces[i]=BitmapFactory.decodeResource(res, imgs.getResourceId(i, -1));
        }
        imgs = res.obtainTypedArray(R.array.special);
        Assets.special=new Bitmap[imgs.length()];
        for(int i=0;i<imgs.length();i++) {
        	Assets.special[i]=BitmapFactory.decodeResource(res, imgs.getResourceId(i, -1));
        }
        
        //Load maps
        final TypedArray mapsToLoad = res.obtainTypedArray(R.array.loadresource_maps);
        Assets.maps=new Map[mapsToLoad.length()];
        for (int i = 0; i < mapsToLoad.length(); ++i) {
        	final int mapResourceId = mapsToLoad.getResourceId(i, -1);
        	//final String mapName = r.getResourceEntryName(mapResourceId);
        	Assets.maps[i] = new Map();
        	MapReader.read(res, mapResourceId, Assets.maps[i]);
        }
        
        Settings.load(game.getContext());
        game.setScreen(new MainMenu(game));
    }

    @Override
    public void Draw(Canvas canvas, float deltaTime) {
        canvas.drawARGB(255, 0, 0, 0);
        canvas.drawText("«‡„ÛÁÍ‡", 20, 20, paint);
    }

    @Override
    public void pause() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resume() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispose() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
