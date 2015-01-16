package org.solarex.testwm;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class FloatWindowService extends Service {
    private static final String TAG = "FloatWindowService";
    WindowManager wm = null;
    
    WindowManager.LayoutParams wmParams = null;  
  
    View view;  
  
    private float mTouchStartX;  
    private float mTouchStartY;  
  
    private float x;  
    private float y;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        view = LayoutInflater.from(this).inflate(R.layout.float_window, null);
        Log.d(TAG, "onCreate view = " + view);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int flag = super.onStartCommand(intent, flags, startId);
        createView();
        Log.d(TAG, "onStartCommand flag = " + flag);
        return flag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if(view != null && wm != null){
            wm.removeView(view);
            view = null;
        }
    }

    private void createView() {  
        wm = (WindowManager) getApplicationContext().getSystemService("window");  
  
        wmParams = new WindowManager.LayoutParams();  
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;  
        wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;  
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;  
  
        wmParams.x = 0;  
        wmParams.y = 0;  
  
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  
        wmParams.format = PixelFormat.RGBA_8888;  
        Log.d(TAG, "createView wm = " + wm + ", params = " + wmParams);
        wm.addView(view, wmParams);  
  
        view.setOnTouchListener(new OnTouchListener() {  
  
            public boolean onTouch(View v, MotionEvent event) {

                x = event.getRawX();
                y = event.getRawY();
                Log.d(TAG, "createView x = " + x + ",y = " + y);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY() + view.getHeight() / 2;
                        Log.d(TAG, "createView ACTION_DOWN startX = " + mTouchStartX + ",startY = " + mTouchStartY);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "createView ACTION_MOVE");
                        updateViewPosition();
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "createView ACTION_UP");
                        updateViewPosition();
                        mTouchStartX = mTouchStartY = 0;
                        break;
                }
  
                return true;  
            }  
        });  
    }  
  
    private void updateViewPosition() {  
        wmParams.x = (int) (x - mTouchStartX);  
        wmParams.y = (int) (y - mTouchStartY);  
        Log.d(TAG, "updateViewPosition params.x = " + wmParams.x + ",params.y = " + wmParams.y);
        wm.updateViewLayout(view, wmParams);  
    } 
}
