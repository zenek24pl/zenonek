package com.example.zeno.zenonek;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Collection;
import java.util.Random;

/**
 * Created by zeno on 2016-04-03.
 */
public class EnemyShip {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;



    //kolizyja
    private Rect hitBox;

    //wykrwyanie  wychodzenia poza ekran

    private int maxX;
    private int minX;

    //spawn przeciwnikow
    private int maxY;
    private int minY;



    public EnemyShip(Context context, int screenX, int screenY) {
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
        maxX=screenX;
        maxY=screenY;
        minX=0;
        minY=0;

        Random generator =new Random();
        speed=generator.nextInt(6)+10;

        x=screenX;
        y=generator.nextInt(maxY)-bitmap.getHeight();
        hitBox=new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update(int playerSpeed){
        x-=playerSpeed;
        x-=speed;

        if(x<minX-bitmap.getWidth()){
            Random generator=new Random();
            speed=generator.nextInt(10)+10;
            x=maxX;
            y=generator.nextInt(maxY)-bitmap.getHeight();

        }

        hitBox.left=x;
        hitBox.top=y;
        hitBox.right=x+bitmap.getWidth();
        hitBox.bottom=y+bitmap.getWidth();
    }
    public Rect getHitBox() {
        return hitBox;
    }
    public void setX(int x) {
        this.x = x;
    }


}
