package com.example.mycomputer.voicemobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    ArrayList<Apps> al;
    GridView gv1;
    PackageManager pm;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        gv1=(GridView)(findViewById(R.id.gv1));
        al=new ArrayList<>();
        List<PackageInfo> packageInfos=new ArrayList<>();
        packageInfos=getPackageManager().getInstalledPackages(0);
       final Context cn= getApplicationContext();
            for(int i=0;i<packageInfos.size();i++)
        {

            PackageInfo packageInfo=packageInfos.get(i);
            String pname=packageInfo.applicationInfo.packageName;
            if(cn.getPackageManager().getLaunchIntentForPackage(pname)!=null)
            {
                String lable = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();

                Drawable photo = packageInfo.applicationInfo.loadIcon(getPackageManager());
                al.add(new Apps(pname, lable, photo));
            }
            //OnResult else if
            /*for(int j=0;j<al.size();j++)
            {
                String s="open abc";
                String name=al.get(j).lable;
                if(s.equalsIgnoreCase(name))
                {
                    Intent intent=getPackageManager().getLaunchIntentForPackage(al.get(j).pname);
                    startActivity(intent);
                }
            }*/

        }



        gv1=(GridView)(findViewById(R.id.gv1));
        myadapter ad=new myadapter();
        gv1.setAdapter(ad);
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

    }

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
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater= LayoutInflater.from(parent.getContext());

            convertView=inflater.inflate(R.layout.list1, parent, false);

            Apps st= al.get(i);

            TextView tv1;
            ImageView imv1;

            tv1=(TextView)(convertView.findViewById(R.id.tv1));

            imv1=(ImageView)(convertView.findViewById(R.id.imv1));

            tv1.setText(st.label);
            imv1.setImageDrawable(st.photo);
            return convertView;
        }
    }



}
