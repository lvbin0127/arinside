package com.googlecode.arinside;

import java.io.IOException;


import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

	/**	Camera **/
    class CameraView extends SurfaceView implements SurfaceHolder.Callback{ 
      
    	SurfaceHolder mHolder;
    	Camera mCamera;

      CameraView(Context context) {
          super(context);
          
          // Install a SurfaceHolder.Callback so we get notified when the
          // underlying surface is created and destroyed.
          mHolder = getHolder();
          mHolder.addCallback(this);
          mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
      }

      public void surfaceCreated(SurfaceHolder holder) {
          // The Surface has been created, acquire the camera and tell it where
          // to draw.
          mCamera = Camera.open();
          try {
             mCamera.setPreviewDisplay(holder);
          } catch (IOException exception) {
              mCamera.release();
              mCamera = null;
              Log.e("Camera", "Error en SurfaceCreated", exception);
          }
      }

      public void surfaceDestroyed(SurfaceHolder holder) {
          // Surface will be destroyed when we return, so stop the preview.
          // Because the CameraDevice object is not a shared resource, it's very
          // important to release it when the activity is paused.
          mCamera.stopPreview();
          mCamera.release();
          mCamera = null;
      }
      
      public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
          // Now that the size is known, set up the camera parameters and begin
          // the preview.
          Camera.Parameters parameters = mCamera.getParameters();
          parameters.setPreviewSize(w, h);
          mCamera.setParameters(parameters);
          mCamera.startPreview();
      }
}//end class CameraView