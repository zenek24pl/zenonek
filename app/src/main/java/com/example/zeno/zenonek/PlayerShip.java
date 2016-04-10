package com.example.zeno.zenonek;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by zeno on 2016-04-03.
 */
public class PlayerShip {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 0;
    private boolean boosting;
    private final int GRAVITY=-12;

    //tarcza
    private int shieldStrength;



    //KOLIZYJA
    private Rect hitBox;

    //// STOPSHIP:
    private int maxY;
    private int minY;

    //ograniczenie predkosci
    private final int MIN_SPEED=1;
    private final int MAX_SPEED=20;


    public PlayerShip(Context context,int screenX, int screenY) {
        x = 50;
        y = 50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        boosting=false;
        maxY=screenY-bitmap.getHeight();
        minY=0;
        hitBox=new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
        shieldStrength=20;
    }
    public void update(){
      if(boosting){
          speed+=2;
      }
        else {
          speed-=5;
      }
        if(speed>MAX_SPEED){
            speed=MAX_SPEED;
        }
        if(speed<MIN_SPEED){
            speed=MIN_SPEED;
        }
        //poruszanie gora dol
        y-=speed+GRAVITY;
        if(y<minY){
            y=minY;
        }
        if(y>maxY){
            y=maxY;
        }
        // odnawianie czytania pozycji hitbox

        hitBox.left=x;
        hitBox.top=y;
        hitBox.right=x+bitmap.getWidth();
        hitBox.bottom=y+bitmap.getWidth();
    }

    //getery
    public Bitmap getBitmap(){
        return bitmap;
    }
    public int getSpeed(){
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void setBoosting(){
        boosting=true;
    }
    public void stopBoosting(){
        boosting=false;
    }

    public Rect getHitbox() {
        return hitBox;
    }
    public int getShieldStrength() {
        return shieldStrength;
    }

    //metoda do obnizania shielda

    public void reduceShieldStrength(){
        shieldStrength --;
    }
}
