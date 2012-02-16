package com.googlecode.arinside;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

	/** Canvas View **/
    class CanvasView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
 
    	private CanvasThread thread;
    	private ArrayList<Capa> Capas;
    	private Bitmap bmpAudio; 
		private Bitmap bmpImage; 
		private Bitmap bmpInfo; 
	
    	//Values of Sensors
		private float mAccelerometerValues[] = new float[3];
    	private float mMagneticValues[] = new float[3];
    	//private float mOrientationValues[] = new float[3];
    	private float rotationMatrix[] = new float[16];
        private float remappedRotationMatrix[] = new float[16];
    	    
        Float azimut;  // View to draw a compass
        //CanvasView mCanvasView = new CanvasView(this.getContext());
        
    	CanvasView(Context context){
    		super(context);
    		getHolder().addCallback(this);
    	}
    	
    	/** SURFACEVIEW METHODS **/
    	@Override
    	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    	}

    	@Override
    	public void surfaceCreated(SurfaceHolder arg0) {
    		
    		bmpAudio = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
    		bmpImage = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
    		bmpInfo = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
    		
    		Capas = new ArrayList<Capa>();
    		Capas.add(new Capa(100,50, bmpAudio.getWidth(), bmpAudio.getHeight())); //CapaAudio
    		Capas.add(new Capa(100,150,bmpImage.getWidth(), bmpImage.getHeight())); //CapaImage
    		Capas.add(new Capa(100,250,bmpInfo.getWidth(), bmpInfo.getHeight())); //CapaInfo
    		
    		thread = new CanvasThread(getHolder(), this);
    		thread.setRunning(true);
    		thread.start();
    	}

    	@Override
    	public void surfaceDestroyed(SurfaceHolder arg0) {
    		boolean retry = true;
    		thread.setRunning(false);
    		while(retry){
    				try{
    					thread.join();
    					retry = false;
    				}catch(InterruptedException e){
    					
    				}//end try-catch
    		}//end while
    	}
    	
    	@Override
    	public void onDraw(Canvas canvas){
    		// Get rotation matrix from the sensor
    	    SensorManager.getRotationMatrix(rotationMatrix, null, mAccelerometerValues, mMagneticValues);
    	    // As the documentation says, we are using the device as a compass in landscape mode
    	    SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, remappedRotationMatrix);

    		Capa CAudio = Capas.get(0);
    		Capa CImage = Capas.get(1);
    		Capa CInfo = Capas.get(2);
    		
    		canvas.drawColor( 0, PorterDuff.Mode.CLEAR);
    		if (azimut != null)
    			canvas.rotate(-azimut*360/(2*3.14159f), (int) getWidth()/2, (int) getHeight()/2);
    		canvas.drawBitmap(bmpAudio, CAudio.getX(), CAudio.getY(), null);
    		canvas.drawBitmap(bmpImage, CImage.getX(), CImage.getY(), null);
    		canvas.drawBitmap(bmpInfo, CInfo.getX(), CInfo.getY(), null);
    	}

        /** SENSOR IMPLEMENTS METHODS **/
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy){
      	  
        }
        
        @Override
        public void onSensorChanged(SensorEvent event){
        	//Synchronized the threads by sensors
        	synchronized (this){
        		switch(event.sensor.getType()){
        		case Sensor.TYPE_ORIENTATION:
        			/*for(int i=0; i<3; i++){
        				//mOrientationValues[i] = event.values[i];
        				Log.d("---ORIENTATION SENSOR","Value:"+ i +" : "+ mOrientationValues[i]);        
        			}*/
        			break;
        		case Sensor.TYPE_ACCELEROMETER:
        				mAccelerometerValues = event.values;
        				//Log.d("---ACCELEROMETER SENSOR","Value:"+ i +" : "+ mAccelerometerValues[i]);        
        			break;
        		case Sensor.TYPE_MAGNETIC_FIELD:
        				mMagneticValues = event.values;
        				//Log.d("---MAGNETIC SENSOR","Value:"+ i +" : "+ mMagneticValues[i]);
        			break;
        		default:
        		}//end switch*/
        		
        		float R[] = new float[9];
        	    float I[] = new float[9];
        	    boolean success = SensorManager.getRotationMatrix(R, I, mAccelerometerValues, mMagneticValues);
        	    if (success) {
        	       float orientation[] = new float[3];
        	       SensorManager.getOrientation(R, orientation);
        	       azimut = orientation[0]; // orientation contains: azimut, pitch and roll
        	      }
        	}//end synchronized
    	}
    	
    	@Override
        public boolean onTouchEvent(MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                
                switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	Capa CAudio = Capas.get(0);
                	Capa CImage = Capas.get(1);
                	Capa CInfo = Capas.get(2);
                	
                	if( ( x > CAudio.getX() && x < CAudio.getX()+CAudio.getWidth()) && (y > CAudio.getY() && y < CAudio.getY() +CAudio.getHeight())){
                		Toast.makeText(this.getContext(), "Capa de Audio Seleccionada",Toast.LENGTH_SHORT).show();
                		
                	}
                	else if((x > CImage.getX() && x < CImage.getX()+CImage.getWidth() ) && (y > CImage.getY() && y < CImage.getY()+CImage.getHeight())){
                		Toast.makeText(this.getContext(), "Capa de Imagen Seleccionada",Toast.LENGTH_SHORT).show();
                	}
                	else if((x > CInfo.getX() && x < CInfo.getX()+CInfo.getWidth()) && (y > CInfo.getY() && y < CInfo.getY()+CInfo.getHeight())){
                		Toast.makeText(this.getContext(), "Capa de Informacion Seleccionada",Toast.LENGTH_SHORT).show();
                	}
              	  break;
                case MotionEvent.ACTION_MOVE:
              	  break;
                case MotionEvent.ACTION_UP:
              	  break;
                }
                return true;
    		}
}//end class CanvasView