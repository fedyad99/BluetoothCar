package com.dff.bluetoothapp;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class AppLoop extends Thread{

    private app app;
    private SurfaceHolder surfaceHolder;

    private boolean isRunning = false;

    public AppLoop(app app, SurfaceHolder surfaceHolder) {
        this.app = app;
        this.surfaceHolder = surfaceHolder;
    }


    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();


        Canvas canvas = null;
        while(isRunning) {

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    app.update();
                    app.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
