package com.example.mycomputer.voicemobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class listvoiceaction extends AppCompatActivity {

    ArrayList<voiceappclass> al;
    ListView lv1;
    ScrollView sv;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listvoiceaction);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sv=(ScrollView)findViewById(R.id.sv);





        al=new ArrayList<>();

        al.add(new voiceappclass("Play Song from your music Library", "\"say Play song_name\"", R.drawable.music1));
        al.add(new voiceappclass("Change Screen Brightness", "\"say brightness low/full/medium\"",R.drawable.bright1));
        al.add(new voiceappclass("Surf Internet and open web sites","\"say browse google.com\"",R.drawable.browse1));
        al.add(new voiceappclass("Make call","\"call dad\"",R.drawable.call1));
        al.add(new voiceappclass("Send Text messages ","\"say  message abhishek\"",R.drawable.message2));
        al.add(new voiceappclass("Send E-Mails","\"say email navjot@gmail.com\"",R.drawable.email1));
        al.add(new voiceappclass("Find places on Google maps","\"say location of/locate golden temple\"",R.drawable.maps1));
        al.add(new voiceappclass("Turn wifi on/off","\"say wifi on/off\"",R.drawable.wifi1));
        al.add(new voiceappclass("Turn Bluetooth on/off","\"say bluetooth on/off\"",R.drawable.bluetooth1));
        al.add(new voiceappclass("Turn Flash light on/off","\"say Flashlight on/off\"",R.drawable.flashlight1));
        al.add(new voiceappclass("Open installed Application","\"say  open application_name\"",R.drawable.launch1));
        al.add(new voiceappclass("Click Photo using own camera","\"say my camera\"",R.drawable.camera1));
        al.add(new voiceappclass("perform Basic Calculation","\"say calculate sum/multiplication/divison/difference of 2 andb3\" ",R.drawable.calc1));
        al.add(new voiceappclass("Google search","\"say search \"",R.drawable.search1));
        al.add(new voiceappclass("Control Robot","\"say hello\"",R.drawable.robot1));









        lv1=(ListView)(findViewById(R.id.lv1));

        myadapter ad=new myadapter();

        lv1.setAdapter(ad);
        new Thread(new Runnable() {
            public void run() {
                int listViewSize = lv1.getAdapter().getCount();
                for (int index = 0; index < listViewSize ; index++) {
                    lv1.smoothScrollToPositionFromTop(lv1.getLastVisiblePosition() + 100, 0, 10000);
                    try {
                          Thread.sleep(600);
                        }
                    catch (InterruptedException e)
                    {


                    }

                }

            }

        }).start();







lv1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(listvoiceaction.this, al.get(position).name + " selected"
                        , Toast.LENGTH_LONG).show();
            }
        });

        //lv1.setScrollY(300);

    }

    /// Inner Class /////
    class myadapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return al.size();
        }

        @Override
        public Object getItem(int i)
        {
            return al.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i*10;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater= LayoutInflater.from(parent.getContext());

            convertView=inflater.inflate(R.layout.voiceapp,parent,false);

            voiceappclass st= al.get(i);

            TextView tv1,tv2;
            ImageView imv1;

            tv1=(TextView)(convertView.findViewById(R.id.tv1));
            tv2=(TextView)(convertView.findViewById(R.id.tv2));
            imv1=(ImageView)(convertView.findViewById(R.id.imv1));

            tv1.setText(st.name2);
            tv2.setText(st.name);
            imv1.setImageResource(st.photo);
            return convertView;
        }
    }
}
