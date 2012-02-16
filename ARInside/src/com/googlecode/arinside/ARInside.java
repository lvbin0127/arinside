package com.googlecode.arinside;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class ARInside extends Activity {
	private CameraView mCameraView;
    private CanvasView mCanvasView;;
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mMagneticSensor;
    //private Sensor mOrientationSensor;
    
    
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Hide the window title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mCanvasView = new CanvasView(this);
        mCanvasView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        
        setContentView(mCanvasView);
        
        mCameraView = new CameraView(this);
        addContentView(mCameraView, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        
        /*
        //SENSORS
        */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Accelerometer
        mAccelerometerSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if ( mAccelerometerSensor != null){//If the device has a Accelerometer Sensor
        	mSensorManager.registerListener(mCanvasView, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        	Log.i("-----Accelerometer Sensor","YES");
        }
        //Magnetic
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if( mMagneticSensor.getType() == Sensor.TYPE_MAGNETIC_FIELD ){//If the device has a Magnetic Sensor
        	mSensorManager.registerListener(mCanvasView,mMagneticSensor, SensorManager.SENSOR_DELAY_UI);
        	Log.i("-----Magnetic Sensor","YES");
        }
        //Orientation Sensor
        /*mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if ( mOrientationSensor.getType() == Sensor.TYPE_ORIENTATION){//If the device has a Orientation Sensor
        	mSensorManager.registerListener(mCanvasView, mOrientationSensor, SensorManager.SENSOR_DELAY_UI);
        	Log.i("-----Orientation Sensor","YES");
        }*/
    }//end oneCreate
    
    @Override
    protected void onResume() {
        super.onResume();
        //Register Sensors Again
        if(mAccelerometerSensor.getType() == Sensor.TYPE_ACCELEROMETER)
        	mSensorManager.registerListener(mCanvasView, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        if(mMagneticSensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        	mSensorManager.registerListener(mCanvasView, mMagneticSensor, SensorManager.SENSOR_DELAY_UI);
       // if(mOrientationSensor.getType() == Sensor.TYPE_ORIENTATION)
        //	mSensorManager.registerListener(mCanvasView, mOrientationSensor, SensorManager.SENSOR_DELAY_UI);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mCanvasView);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    protected void onStop(){
        super.onStop();
    }
    
}