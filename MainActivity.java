package com.example.mycomputer.voicemobileapp;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void go11(View v)
    {
        Intent in=new Intent(this,Main3Activity.class);
        startActivity(in);

    }

    public void go15(View v)
    {
        Intent in=new Intent(this,speechtotext.class);
        startActivity(in);

    }
    public void go16(View v)
    {
        Intent in=new Intent(this,musiclist.class);
        startActivity(in);

    }
    public void go17(View v)
    {
        Intent in=new Intent(this,camera.class);
        startActivity(in);

    }
    public void go18(View v)
    {
        Intent in=new Intent(this,mapActivity.class);
        startActivity(in);
    }
    public void go19(View v)
    {
        Intent in=new Intent(this,voiceAction.class);
        startActivity(in);
    }
    public void go20(View v)
    {
        Intent in=new Intent(this,listvoiceaction.class);
        startActivity(in);
    }
    public void go21(View v)
    {
        Intent in=new Intent(this,setwallpaper.class);
        startActivity(in);
    }


}
