package com.googlecode.arinside;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

	/** Thread Canvas **/
    public class CanvasThread extends Thread {

    	private SurfaceHolder sh;
    	private CanvasView view;
    	private boolean run;

    	public CanvasThread(SurfaceHolder sh, CanvasView view) {
    		this.sh = sh;
    		this.view = view;
    		run = false;
    	}

		public void setRunning(boolean run) {
    		this.run = run;
    	}

    	public void run() {
    		Canvas canvas;
    		while(run) {
    			canvas = null;
    			try {
    				canvas = sh.lockCanvas(null);
    				synchronized(sh) {
    					view.onDraw(canvas);
    				}
    			} finally {
    				if(canvas != null)
    					sh.unlockCanvasAndPost(canvas);
    			}
    		}
    	}
    }//end class CanvasThread