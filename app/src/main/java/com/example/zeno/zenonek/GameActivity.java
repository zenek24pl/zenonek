package com.example.zeno.zenonek;

import android.graphics.Point;
import android.os.Bundle;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;


public class GameActivity extends AppCompatActivity {

    private TDView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();


        setContentView(R.layout.activity_game);
      Display display=getWindowManager().getDefaultDisplay();

        //ladowanie rozdzielczosci d punktu obiektu
        Point size=new Point();
        display.getSize(size);
       gameView = new TDView(this,size.x,size.y);
        setContentView(gameView);


    }
    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause();
    }
    protected  void onResume(){
        super.onResume();
        gameView.resume();
    }

}
