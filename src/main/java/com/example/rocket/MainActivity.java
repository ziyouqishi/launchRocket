package com.example.rocket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import utitls.DensityUtitl;

public class MainActivity extends Activity {
    private ImageView ivRocket,box,fire;
    private AnimationDrawable animationDrawable;
    private int screenWidth,screenHeight;
    private RelativeLayout relativeLayout;
    private int rocketWidth,rocketHeight;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(null!=msg.obj){
                int y=(Integer)msg.obj;
                //ivRocket.setTranslationY(y);
                ivRocket.layout(ivRocket.getLeft(),y,ivRocket.getRight(),y+ivRocket.getHeight());

            }


            if(msg.what==0){
                fire.setVisibility(View.INVISIBLE);
                ivRocket.layout(0,0,rocketWidth,rocketHeight);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenWidth=new DensityUtitl().getScreenParams(this,DensityUtitl.WIDTH);
        screenHeight=new DensityUtitl().getScreenParams(this,DensityUtitl.HEIGHT);
        initView();
    }

    void initView(){
        relativeLayout=(RelativeLayout)findViewById(R.id.activity_main);
        ivRocket=(ImageView)findViewById(R.id.iv_rocket);
        box=(ImageView)findViewById(R.id.box);
        fire=(ImageView)findViewById(R.id.fire);
        ivRocket.setImageResource(R.drawable.rocket);
        animationDrawable = (AnimationDrawable) ivRocket.getDrawable();
        animationDrawable.start();

        fire.setImageResource(R.drawable.fire);
        animationDrawable = (AnimationDrawable) fire.getDrawable();
        animationDrawable.start();


        ivRocket.setOnTouchListener(new View.OnTouchListener() {
            int startX=0;
            int startY=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX=(int)event.getRawX();
                        startY=(int)event.getRawY();
                        displayBox();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int newX=(int)event.getRawX();
                        int newY=(int)event.getRawY();

                        int dx=newX-startX;
                        int dy=newY-startY;
                        /*ivRocket.layout(ivRocket.getLeft()+dx,ivRocket.getTop()+dy,
                                ivRocket.getRight()+dx,ivRocket.getBottom()+dy);*/
                        ivRocket.setX(ivRocket.getX()+dx);
                        ivRocket.setY(ivRocket.getY()+dy);
                       // ivRocket.setTranslationX(dx);
                       /* startX=(int)event.getRawX();
                        startY=(int)event.getRawY();*/

                        startX=newX;
                        startY=newY;


                        break;
                    case MotionEvent.ACTION_UP:
                        /**
                         * 定义一个矩形区域，以火光为基础进行定义
                         */
                        int newl=fire.getLeft();
                        int newt=fire.getTop()-200;
                        int newr=fire.getRight();
                        int tranX=(int)ivRocket.getX();
                        int tranY=(int)ivRocket.getY();


                        /*if(newl>210&&newr>540&&newt<900){
                            Toast.makeText(MainActivity.this,"哈哈哈哈",Toast.LENGTH_SHORT).show();
                        }*/

                        Log.d("tag", ivRocket.getLeft()+" ivRocket.getLeft()");
                        Log.d("tag", ivRocket.getRight()+" ivRocket.getRight()");
                        Log.d("tag", newl+" newl");
                        Log.d("tag", newr+" newr");
                        Log.d("tag", newt+" newt");

                        Log.d("tag", tranY+" tranY");
                        Log.d("tag", tranX+" tranX");

                        int offsetLeft=(screenWidth-rocketWidth)/2;
                        int offsetRight=(screenWidth+rocketWidth)/2;

                        if(offsetLeft>newl&&offsetRight<newr&&tranY>newt){
                            Toast.makeText(MainActivity.this,"哈哈哈哈",Toast.LENGTH_SHORT).show();
                            showFire();
                            launch2();
                        }


                         hideBox();
                        break;
                }
                return true;
            }
        });

        /**
         * 控件的宽高不能直接获取,需要在线程中获取。
         */

        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                rocketWidth=ivRocket.getWidth();
                rocketHeight=ivRocket.getHeight();
                int boxHeight=box.getHeight();
                box.setTranslationY(boxHeight);
                /**
                 * 将火光垂直上移至发射口，乘以0.31135f是通过图片的宽高比例算出来的
                 */
                fire.setTranslationY(-boxHeight*0.31135f);
            }
        });

    }

    void displayBox(){
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(box,"translationY",300f,0);
        ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(box,"alpha",0.2f,1.0f);
        AnimatorSet set=new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(objectAnimator,objectAnimator2);
        set.start();
    }

    void hideBox(){
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(box,"translationY",0,300f);
        ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(box,"alpha",1.0f,0.0f);
        AnimatorSet set=new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(objectAnimator,objectAnimator2);
        set.start();

    }

    /**
     * 显示火光
     */

    void showFire(){
        fire.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(box,"alpha",0.2f,1.0f);
        objectAnimator.setDuration(600);
        objectAnimator.start();


    }

    /**
     * 发射火箭
     */
    void launch(){
        ivRocket.setX((fire.getRight()-fire.getLeft())/2);
        ivRocket.setY(box.getTop());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message;
                for(int i=0;i<10;i++){
                    SystemClock.sleep(10);
                     message=Message.obtain();
                    int y=1250-i*100;
                    message.obj=y;
                    handler.sendMessage(message);
                    Log.d("tag", "run: "+y);
                }
                message=Message.obtain();
                message.what=0;
                handler.sendMessage(message);


            }
        }).start();
    }

    void launch2(){
       /* ivRocket.setX((fire.getRight()-fire.getLeft())/2);
        ivRocket.setY(box.getTop());*/
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(ivRocket,"translationY",ivRocket.getTranslationY()-1100f);
        objectAnimator.setDuration(1500);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fire.setVisibility(View.INVISIBLE);
               // ivRocket.layout(0,0,rocketWidth,rocketHeight);
            }
        });
        objectAnimator.start();
        Toast.makeText(this,"方法已经执行",Toast.LENGTH_SHORT).show();
    }

}
