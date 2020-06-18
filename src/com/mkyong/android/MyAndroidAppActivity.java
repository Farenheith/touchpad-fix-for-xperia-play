package com.mkyong.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.InputDevice;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.DataOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyAndroidAppActivity extends Activity {

    private Button btnDisplay;
    private Button btnAct;
    private TextView txtDisplay;
    private TextView txtPointerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        txtDisplay = (TextView) findViewById(R.id.txtTouchpadState);
        txtPointerId = (TextView) findViewById(R.id.txtPointerId);
        int t = getTouchpadState(false);
        changeLabel(t);
        t = getPointerIdStart(false);
        changeLabel2(t);
        btnDisplay = (Button) findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int t = getTouchpadState(true);
                changeLabel(t);
                Toast.makeText(MyAndroidAppActivity.this, "behavior switched",
                        Toast.LENGTH_SHORT).show();

            }
        });

        btnDisplay = (Button) findViewById(R.id.btnPointerIdFrom1);

        btnDisplay.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                int t = getPointerIdStart(true);
                changeLabel2(t);
                Toast.makeText(MyAndroidAppActivity.this, "pointerID start switched",
                        Toast.LENGTH_SHORT).show();

            }
        });
        
        btnAct = (Button) findViewById(R.id.btnAppActivation);
        
        btnAct.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Object viewRoot = getWindow().getDecorView().getRootView().getParent();
                Class localClass;
                try {
                    localClass = Class.forName("android.view.ViewRoot");
                } catch (ClassNotFoundException ex) {
                    Toast.makeText(MyAndroidAppActivity.this, "Error 1: "+ex.getMessage(), RESULT_OK).show();
                    return;
                }
                Class[] arrayOfClass = new Class[1];
                arrayOfClass[0] = Boolean.TYPE;
                Method localMethod;
                try {
                    localMethod = localClass.getMethod("setProcessPositionEvents", arrayOfClass);
                } catch (NoSuchMethodException ex) {
                    Toast.makeText(MyAndroidAppActivity.this, "Error 2: "+ex.getMessage(), RESULT_OK).show();
                    return;
                } catch (SecurityException ex) {
                    Toast.makeText(MyAndroidAppActivity.this, "Error 3: "+ex.getMessage(), RESULT_OK).show();
                    return;
                }
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = true;
                try {
                    localMethod.invoke(viewRoot, arrayOfObject);
                } catch (IllegalAccessException ex) {
                    Toast.makeText(MyAndroidAppActivity.this, "Error 4: "+ex.getMessage(), RESULT_OK).show();
                    return;
                } catch (IllegalArgumentException ex) {
                    Toast.makeText(MyAndroidAppActivity.this, "Error 5: "+ex.getMessage(), RESULT_OK).show();
                    return;
                } catch (InvocationTargetException ex) {
                    Toast.makeText(MyAndroidAppActivity.this, "Error 6: "+ex.getMessage(), RESULT_OK).show();
                    return;
                }
                Toast.makeText(MyAndroidAppActivity.this, "Touchpad activated!", RESULT_OK).show();
            }
        });
    }

    private void changeLabel(int t) {
        if (t == 1)
            txtDisplay.setText("TOUCHPAD ALWAYS ACTIVATED");
        else
            txtDisplay.setText("touchpad needs app side activation");
    }
    
    private void changeLabel2(int t) {
        if (t == 1)
            txtPointerId.setText("POINTER ID START FROM 1 (USEFUL FOR SOME OLD GAMES)");
        else
            txtPointerId.setText("Pointer ID start from 0 (DEFAULT)");
    }

    public int getTouchpadState(boolean setit) {
        return getProp("mod.touchpad.activated", setit);
    }

    public int getPointerIdStart(boolean setit) {
        return getProp("mod.touchpad.startfrom1", setit);
    }
    public int getProp(String property, boolean setit) {
        int value = 0;
        try {
            Class sysprops = Class.forName("android.os.SystemProperties");
            Class[] pTypes = new Class[2];
            pTypes[0] = String.class;
            pTypes[1] = int.class;
            Method method = sysprops.getMethod("getInt", pTypes);
            Object[] pValues = new Object[2];
            pValues[0] = property;
            pValues[1] = 0;
            value = (Integer)method.invoke(null, pValues);
            if (setit) {
               Process p = Runtime.getRuntime().exec("su");
               // Attempt to write a file to a root-only  
               DataOutputStream os = new DataOutputStream(p.getOutputStream());  
               value = (value == 0 ? 1 : 0);
               os.writeBytes("setprop "+property+" "+value+"\n");
               // Close the terminal  
               os.writeBytes("exit\n");
               os.flush();
            }
        } catch (Exception ex) {
        }
        return value;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getSource() == InputDevice.SOURCE_TOUCHPAD){
            Log.i("TOUCHPADACTIVATOR", "*******************BEGIN******************");
            Log.i("TOUCHPADACTIVATOR", "Action: "+event.getAction());
            Log.i("TOUCHPADACTIVATOR", "Device id: "+event.getDeviceId());
            Log.i("TOUCHPADACTIVATOR", "Masked action: "+event.getActionMasked());
            Log.i("TOUCHPADACTIVATOR", "Action index: "+event.getActionIndex());
            Log.i("TOUCHPADACTIVATOR", "Action index: "+event.getFlags());
            Log.i("TOUCHPADACTIVATOR", "Pointer count: "+event.getPointerCount());
            Log.i("TOUCHPADACTIVATOR", "*********POINTERS*****************");
            StringBuilder b = new StringBuilder("TOUCHPAD ACTIVATED ");
            for (int i = 0; i < event.getPointerCount(); i++){
                try {
                    Log.i("TOUCHPADACTIVATOR", "Pointer id: "+event.getPointerId(i));
                    Log.i("TOUCHPADACTIVATOR", "Pointer X: "+event.getX(i));
                    Log.i("TOUCHPADACTIVATOR", "Pointer Y: "+event.getY(i));
                    int x = (int)event.getX(i);
                    int y = (int)event.getY(i);
                    b.append("(").append(x)
                            .append(",")
                            .append(y)
                            .append(")");
                } catch (Exception ex){}
            }
            Log.i("TOUCHPADACTIVATOR", "*********END POINTERS*****************");
            Log.i("TOUCHPADACTIVATOR", "*******************END******************");
            txtDisplay.setText(b.toString());
        }
        return true;
    }
}