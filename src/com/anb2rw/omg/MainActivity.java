package com.anb2rw.omg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.anb2rw.omg.Engine.FileIO;
import com.anb2rw.omg.Engine.KeyboardHandler;
import com.anb2rw.omg.Engine.Render;
import com.anb2rw.omg.Engine.Screen;
import com.anb2rw.omg.Engine.TouchHandler;

public class MainActivity extends Activity implements Game {
//    WakeLock wakeLock;
	String bd20="This string was written 20.07.2012 at 20's birthday";
    public Render render;
    Screen screen;
    TouchHandler touch;
    KeyboardHandler key;
    FileIO fio;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
//        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "OMG Lock");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int frameBufferWidth=480;
        int frameBufferHeight=320;
        Bitmap framebuffer=Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Bitmap.Config.RGB_565);
        float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();
    
        screen = getStartScreen();
        
        render=new Render(this, framebuffer);
        setContentView(render);
        
        touch=new TouchHandler(render, scaleX, scaleY);
        render.setOnTouchListener(touch);
        key=new KeyboardHandler(render);
        render.setOnKeyListener(key);
        render.setFocusableInTouchMode(true);
        render.requestFocus();
        
        fio = new FileIO(getAssets());
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
//        wakeLock.acquire();
        render.resume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
//        wakeLock.release();
        render.pause();
        Settings.save(this);
        
        if(isFinishing()) {
        }
    }
    
    public void Exit() {
        finish();
    }

    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");
        
        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.Update(0);
        this.screen = screen;
    }

    public Screen getCurrentScreen() {
        return screen;
    }

    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }

    public TouchHandler getTouch() {
        return touch;
    }
    
    public FileIO getFileIO() {
        return fio;
    }
    
    public KeyboardHandler getKey() {
        return key;
    }
    public Context getContext() {
        return this;
    }
    
    public void SwitchOnBluetooth() {
    	Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, Multiplayer.REQUEST_ENABLE_BT);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case Multiplayer.REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
//                // Get the device MAC address
//                String address = data.getExtras()
//                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                // Get the BLuetoothDevice object
//                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//                // Attempt to connect to the device
//                mChatService.connect(device);
            }
            break;
        case Multiplayer.REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
//            if (resultCode == Activity.RESULT_OK) {
//                // Bluetooth is now enabled, so set up a chat session
//                setupChat();
//            } else {
//                // User did not enable Bluetooth or an error occured
//                Log.d(TAG, "BT not enabled");
//                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
//                finish();
//            }
        }
    }
}
