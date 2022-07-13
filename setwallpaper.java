package com.example.mycomputer.voicemobileapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class setwallpaper extends AppCompatActivity {

    int a[]={R.drawable.black1,R.drawable.black3,R.drawable.black4,R.drawable.black5,R.drawable.microphone,R.drawable.black7,R.drawable.black8};
    ImageView imv;
    int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setwallpaper);
        imv=(ImageView)(findViewById(R.id.imd1));
    }

    public void Next(View v)
    {
        if(pos==6)
        {
            Toast.makeText(this,"already of last tmg",Toast.LENGTH_SHORT).show();
        }
        else
        {
            pos++;
            Bitmap bmp= BitmapFactory.decodeResource(getResources(),a[pos]);
            imv.setImageBitmap(bmp);

        }

    }
    public void Pre(View v)
    {
        if(pos==0)
        {
            Toast.makeText(this,"already of first tmg", Toast.LENGTH_SHORT).show();
        }
        else
        {
            pos--;
            Bitmap bmp= BitmapFactory.decodeResource(getResources(), a[pos]);
            imv.setImageBitmap(bmp);

        }

    }
    public void stv(View v)
    {
        globalapp.num=a[pos];
        Intent in=new Intent(getApplicationContext(),voiceAction.class);
        startActivity(in);
    }

}
