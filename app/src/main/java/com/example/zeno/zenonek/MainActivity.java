package com.example.zeno.zenonek;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textHighScore;
    Button btnPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActionBar actionBar=getSupportActionBar();
            actionBar.hide();
            setContentView(R.layout.activity_main);


            textHighScore=(TextView)findViewById(R.id.textHighScore);
            btnPlay = (Button)findViewById(R.id.btnPlay);
            btnPlay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,GameActivity.class);
        startActivity(i);
        finish();

    }
}
