/**
 * Created by zeno on 2016-04-01.
 */
package com.example.zeno.zenonek;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class TDView extends SurfaceView implements Runnable {
   ///kopia kontextu
    private Context context;
    //rozmiary monitora do
    private int screenY;
    private int screenX;

    volatile boolean playing;

    //watkek
    Thread gameThread=null;
    //obiekt gry
    //deklaracja randomowego pylu w tab array
    public ArrayList<SpaceDust> dustList=new ArrayList<SpaceDust>();


    private PlayerShip player;
    public EnemyShip enemy1;
    public EnemyShip enemy2;
    public EnemyShip enemy3;
    
    //zmienne odpowiedzialne za czas dystas itp
    
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;


    //rysowanie
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    //zmienna odpowiedzialna za koniec gry
    private boolean gameEnded;

    //muza
    private SoundPool soundPool;
    int start=-1;
    int bump=-1;
    int destroyed=-1;
    int win=-1;

    public TDView(Context context,int x,int y) {
        super(context);
        this.context=context;

        soundPool=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        try{
            AssetManager assetManager=context.getAssets();
            AssetFileDescriptor descriptor;

            //tworzenia
            descriptor=assetManager.openFd("bump.wav");
            bump=soundPool.load(descriptor,0);

            descriptor=assetManager.openFd("bomb.mp3");
            start=soundPool.load(descriptor,0);

            descriptor=assetManager.openFd("destroyed.wav");
            destroyed=soundPool.load(descriptor,0);

            descriptor=assetManager.openFd("win.wav");
            win=soundPool.load(descriptor,0);

        }catch (IOException e){
            //wyswietlanie bledu
            Log.e("error", "failed to load sound ");
        }

        screenX=x;
        screenY=y;
        startGame();


       ourHolder=getHolder();
        paint=new Paint();

    }



    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }
    private void update() {
        //detekcja kolizji  na nowych pozycjach
        boolean hitDetected=false;
        if(Rect.intersects(player.getHitbox(),enemy1.getHitBox()))
        {
            hitDetected=true;
            enemy1.setX(-100);
        }
        if(Rect.intersects(player.getHitbox(),enemy2.getHitBox()))
        {
            hitDetected=true;
            enemy2.setX(-100);
        }
        if(Rect.intersects(player.getHitbox(),enemy3.getHitBox()))
        {
            hitDetected=true;
            enemy3.setX(-100);
        }

        //redukcja shielda
        if(hitDetected){
            soundPool.play(bump,1,1,0,0,1);
            player.reduceShieldStrength();
            if(player.getShieldStrength()<0){
                soundPool.play(destroyed,1,1,0,0,1);
            //gra skonczona
            gameEnded=true;
        }}

        //update kazdego z elemetow dust
        for(SpaceDust sd : dustList){
            sd.update(player.getSpeed());
        }


        player.update();

        //enemies
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());

        //zakonczenie gry

        if(!gameEnded){
            //zmniejszanie sie odleglosci
            distanceRemaining -=player.getSpeed();
            soundPool.play(win,1,1,0,0,1);

            //czas gry/lotu
            timeTaken=System.currentTimeMillis()-timeStarted;
        }
        if(distanceRemaining<0){
            //sprawdzenie czy nie pobilismy rekordu czasowego
            if(timeTaken<fastestTime){
                fastestTime=timeTaken;
            }

            //pominiecie negatywnych numerow :D
          //  distanceRemaining=0;
            //koniec gry
            gameEnded=true;
        }

    }

    private void draw() {
        if(ourHolder.getSurface().isValid()) {
            //blokujemy obszar pamieci na ktorym bedziemy rysowac canvas
            canvas = ourHolder.lockCanvas();
            //wyrzucani ostatniej klatki
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            //biale plamki
            paint.setColor(Color.argb(255, 255, 255, 254));

            paint.setStrokeWidth(5.5f);

            //rysowanie bialego pylu z listy
            for (SpaceDust sd : dustList) {
                canvas.drawPoint(sd.getX(), sd.getY(), paint);

            }
                //do debugowania i sprawdzenia obszaru kolizji
                //zmiana na biale pixele

                //rysowanie gracza
                canvas.drawBitmap(player.getBitmap(),
                        player.getX(),
                        player.getY(),
                        paint);
                canvas.drawBitmap(enemy1.getBitmap(),
                        enemy1.getX(),
                        enemy1.getY(),
                        paint);
                canvas.drawBitmap(enemy2.getBitmap(),
                        enemy2.getX(),
                        enemy2.getY(),
                        paint);
                canvas.drawBitmap(enemy3.getBitmap(),
                        enemy3.getX(),
                        enemy3.getY(),
                        paint);


                //tworzenie HUD
                if (!gameEnded) {
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setColor(Color.argb(255, 25, 255, 255));
                    paint.setTextSize(25);

                    canvas.drawText("Fastest:" + fastestTime + "s", 10, 20, paint);

                    canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 20, paint);

                    canvas.drawText("Distance" + distanceRemaining / 1000 + " KM", screenX / 3, screenY - 20, paint);
                    canvas.drawText("Shield:" + player.getShieldStrength(), 10, screenY - 20, paint);
                    canvas.drawText("Speed:" + player.getSpeed() * 60 + " MPS", (screenX / 3) * 2, screenY - 20, paint);
                } else {
                    //pokaz screen
                    paint.setTextSize(80);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText("GAME OVER", screenX / 2, 100, paint);
                    paint.setTextSize(25);
                    canvas.drawText("Fastest:" + fastestTime + "s", screenX / 2, 160, paint);
                    canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 200, paint);
                    canvas.drawText("DistanceRemaining:" + distanceRemaining / 100 + " KM", screenX / 2, 240, paint);
                    paint.setTextSize(80);
                    canvas.drawText("TAP to REPLAY!", screenX / 2, 350, paint);


                }

                //odblokowanie i rysowanie sceny
                ourHolder.unlockCanvasAndPost(canvas);

        }


    }

    private void startGame(){
        //inicjalizacja obiektow
        //inicjalizacja metody game end
        gameEnded=false;
        player=new PlayerShip(context,screenX,screenY);
        enemy1=new EnemyShip(context,screenX,screenY);
        enemy2=new EnemyShip(context,screenX,screenY);
        enemy3=new EnemyShip(context,screenX,screenY);

        int numSpecs=20;
        for(int i=0;i<numSpecs;i++){
            //SPawn pylu
            SpaceDust spec=new SpaceDust(screenX,screenY);
            dustList.add(spec);
        }

        //reset czasu i dystansu
        distanceRemaining=10000; //10km
        timeTaken=0;

        //pobranie czasu startowego
        timeStarted=System.currentTimeMillis();

        soundPool.play(start,1,1,0,0,1);


    }
    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {

        }
    }


    public void pause(){
        playing=false;
        try{
            gameThread.join();

        }catch (InterruptedException e){

        }
    }
    public void resume(){
        playing=true;
        gameThread=new Thread(this);
        gameThread.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        //rozne zdarzenia

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            //odcisniecie :D do gory
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            //nacisniecie
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                //jezeli jestesmy na screenie koncowym zacznij nowa gre
                if(gameEnded){
                    startGame();
                }
                break;


        }
        return true;
    }
}
