package com.example.mycomputer.voicemobileapp;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

public class musiclist extends AppCompatActivity {
    Cursor cursor;
    MediaPlayer mp;
    HashMap<String,String>hashMap;
    TextView tv1;
    ScrollView sv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist);
        sv=(ScrollView)findViewById(R.id.sv);
        tv1=(TextView)findViewById(R.id.tv1);
        hashMap=new HashMap<>();
        String selection= MediaStore.Audio.Media.IS_MUSIC+"!=0";
        String[]projection={MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA};
        cursor=this.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
        String son="";
        while (cursor.moveToNext()){
            String p=cursor.getString(0);
            String s=cursor.getString(1);

            son=p;
            hashMap.put(p,s);
            tv1.append("song:->"+p+""+"\n---------------------------------------------------------------------------------------\n");
        }
       /* mp=new MediaPlayer();
        try
        {
            mp.setDataSource(hashMap.get(son));
            mp.prepare();
            mp.start();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }*/


    }
}
