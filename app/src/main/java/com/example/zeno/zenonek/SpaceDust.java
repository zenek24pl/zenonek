package com.example.zeno.zenonek;

import java.util.Random;

/**
 * Created by zeno on 2016-04-05.
 */
public class SpaceDust {

    private int x,y;
    private int speed;

    //wykrywanie pylu ktory opuszcza screen

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;


    public SpaceDust(int screenX,int screenY) {

        maxX=screenX;
        maxY=screenY;
        minX=0;
        minY=0;

        //ustawienie szybkosci pomiedzy 0-9

        Random generator=new Random();
        speed=generator.nextInt(10);


        //ustawienie polozenia poczatkowego
        x=generator.nextInt(maxX);
        y=generator.nextInt(maxY);

    }
    public void update(int playerSpeed){
        //przyspieszenie gdy player robi cos
        x-=playerSpeed;
        x-=speed;


        //respawn pylu
        if(x<0){
            x=maxX;
            Random generator =new Random();
            y=generator.nextInt(maxY);
            speed=generator.nextInt(15);

        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
