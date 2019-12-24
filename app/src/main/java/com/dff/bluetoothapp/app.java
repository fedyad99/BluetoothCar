package com.dff.bluetoothapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;




class app extends SurfaceView implements SurfaceHolder.Callback {

    private final int TimeSendDelay = 220;
    private int joystickPointerId = 0;
    public final Joystick joystick;
    private AppLoop appLoop;
    long time = System.currentTimeMillis();

    public app(Context context) {
        super(context);


        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        appLoop = new AppLoop(this, surfaceHolder);


        joystick = new Joystick(350, 480, 220, 90);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {

                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystickPointerId == event.getPointerId(event.getActionIndex())) {

                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;

        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        appLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        canvas.drawColor(color);
        drawXY(canvas);


        joystick.draw(canvas);

    }





    private void drawXY(Canvas canvas){
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.text);
        paint.setColor(color);
        paint.setTextSize(40);

        canvas.drawText("X: " + CorStr(joystick.getActuatorX()*100), 20, 100, paint);
        canvas.drawText("Y: " + CorStr((joystick.getActuatorY() == 0) ? joystick.getActuatorY() : -joystick.getActuatorY()*100), 20, 150, paint);

    }

    public String CorStr(double x){
        String str="";
        int cor = (int)x;
        str = Integer.toString(cor);
        return str;
    }

    public String XYtoStr(double x, double y){
        String str="";
        int corx = (int)x;
        int cory = (int)y;
        str = '#' + Integer.toString(corx) + ':' + cory + ';';
        return str;
    }

    public void update() {
        joystick.update();
        if(System.currentTimeMillis() - time > TimeSendDelay) {
            String coordinatesXY = XYtoStr(joystick.getActuatorX() * 100, -joystick.getActuatorY() * 100);
            MainActivity.say(coordinatesXY);
            time = System.currentTimeMillis();
        }
    }


}
