package com.anb2rw.omg;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.anb2rw.omg.Gameplay.Level;

public class Multiplayer {

	// Debugging
    private static final String TAG = "OMG";
    private static final boolean D = true;
	
	public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
 // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothService mService;
    
	private Level level;
    
    public Multiplayer(Level level) {
    	this.level=level;
    	// Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            //return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mService == null) mService = new BluetoothService(mHandler);
        }
    }
    
    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            //Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mService.write(send);
            if(D) Log.i(TAG, "Out:"+message);

        }
    }
    
 // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        private String mConnectedDeviceName;

		@Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:
                    //mTitle.setText(R.string.title_connected_to);
                    //mTitle.append(mConnectedDeviceName);
                    //mConversationArrayAdapter.clear();
                    break;
                case BluetoothService.STATE_CONNECTING:
                    //mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
                    //mTitle.setText(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                break;
            case MESSAGE_READ: 
            	//sendMessage(x/Width+"x"+y/Height+(Erase?"b":"a")+"["+pi+"]"+size+"/"+(int)hsv[0]+"h"+hsv[1]+"s"+hsv[2]+mode+"|");
            	byte[] readBuf = (byte[]) msg.obj;
            	// construct a string from the valid bytes in the buffer
            	String readMessage = new String(readBuf, 0, msg.arg1);
            	
            	if(D) Log.i(TAG, "In:"+readMessage);
            	
            	level.readMessage(readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                //Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                //Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

}
