package com.example.mycomputer.voicemobileapp;

import android.Manifest;
import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;

import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;

import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognitionListener;

import android.speech.RecognizerIntent;

import android.speech.SpeechRecognizer;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;


public class voiceAction extends AppCompatActivity
{
    SpeechRecognizer recognizer;
    Intent intent;
    ImageButton bt1;
    int songno=0;
    TextToSpeech tts;
    WifiManager wm;
    BluetoothAdapter bm;

    Camera camera;
    Camera.Parameters pm;
    boolean isFlashOn;
    String phonename,phonenum,search,sender="",subject,message,song="",path="";
    boolean flage2=false;
    boolean flage1=false;
    boolean flage3=false;
    boolean flag4=true;
    boolean flag1=false;
    boolean flag2=false;
    boolean flag3=false;
    boolean found = false;
    //boolean flagplay=false;
    String hrs,min, ampm;
    boolean value;

    TextView tv1;
    Cursor cursor;
    MediaPlayer mp;
    HashMap<String,String> hashMap;
    LinearLayout lv,lv2,lv3,lv4;
    ImageButton im,im2,im3,im4;
    ArrayList<String> al;
    ArrayList<String> my;
    ListView lw;
    myadapter ad;
    String msg="";
    String mess[],af[],ev[],mm[];
    int num1,num2;

    Thread t;
    RelativeLayout rt;
    ArrayList<Apps> als;
    GridView gv1;



    class mygridadapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return als.size();
        }

        @Override
        public Object getItem(int i)
        {
            return als.get(i);
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

            Apps st= als.get(i);

            TextView tv1;
            ImageView imv1;

            tv1=(TextView)(convertView.findViewById(R.id.tv1));

            imv1=(ImageView)(convertView.findViewById(R.id.imv1));

            tv1.setText(st.label);
            imv1.setImageDrawable(st.photo);
            return convertView;
        }
    }

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_action);
        gv1=(GridView)(findViewById(R.id.gv1));
        als=new ArrayList<>();
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
                als.add(new Apps(pname, lable, photo));
            }
            //OnResult else if

        }



        gv1=(GridView)(findViewById(R.id.gv1));
        mygridadapter adg=new mygridadapter();
        gv1.setAdapter(adg);

        tv1 = (TextView) findViewById(R.id.tv1);
        bt1=(ImageButton)findViewById(R.id.bt1);
        tts= new TextToSpeech(this,new MyListener());
        bm = BluetoothAdapter.getDefaultAdapter();
        lv=(LinearLayout)findViewById(R.id.LL1);
        lv2=(LinearLayout)findViewById(R.id.LL2);
        lv4=(LinearLayout)findViewById(R.id.linearLayout2);

        lw=(ListView)findViewById(R.id.lv1);

        im=(ImageButton)findViewById(R.id.ib2);
        im2=(ImageButton)findViewById(R.id.ib3);
        im3=(ImageButton)findViewById(R.id.ib4);
        im4=(ImageButton)findViewById(R.id.ib5);
        rt=(RelativeLayout)findViewById(R.id.rt);


        al= new ArrayList<>();
        my=new ArrayList<>();
        ad=new myadapter();
        mess= getResources().getStringArray(R.array.hello);
        mm= getResources().getStringArray(R.array.morning);
        af= getResources().getStringArray(R.array.afternoon);
        ev= getResources().getStringArray(R.array.evening);

        tts.setOnUtteranceProgressListener(new MyUtteranceListener());
        hashMap=new HashMap<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        final String[] projection ={MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA,};
        cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);
        //String son="";
        while(cursor.moveToNext())
        {   String p=cursor.getString(0);
            String s =cursor.getString(1);
            hashMap.put(p, s);
            al.add(p);
            song= hashMap.get(p);

        }



        im2.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       if (flag4) {
                                           mp.pause();
                                           im2.setImageResource(R.drawable.play3);
                                           Toast.makeText(getApplicationContext(), "Song play", Toast.LENGTH_SHORT).show();
                                           flag4 = false;
                                       } else if (flag4 == false) {
                                           if (mp != null) {
                                               mp.start();
                                               im2.setImageResource(R.drawable.bpause);
                                               Toast.makeText(getApplicationContext(), "Song Pause", Toast.LENGTH_SHORT).show();
                                               flag4 = true;


                                           }
                                       }

                                   }
                               }
        );
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (al.size() == 0) {
                    Toast.makeText(getApplicationContext(), "already at first one", Toast.LENGTH_SHORT).show();
                } else {
                    mp.reset();
                    Toast.makeText(getApplicationContext(), "Back song", Toast.LENGTH_SHORT).show();
                    try {
                        songno = songno - 1;

                        mp.setDataSource(hashMap.get(al.get(songno)));
                        mp.prepare();
                        mp.start();

                        im2.setImageResource(R.drawable.bpause);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int s = al.size() - 1;
                if (songno == s) {
                    Toast.makeText(getApplicationContext(), "already at last one", Toast.LENGTH_SHORT).show();
                } else {
                    mp.reset();
                    Toast.makeText(getApplicationContext(), "Next Song", Toast.LENGTH_SHORT).show();
                    try {
                        songno = songno + 1;

                        play(hashMap.get(al.get(songno)));
                        tv1.setText(songno);
                        mp.prepare();
                        mp.start();
                        tv1.setText(songno);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Music Player Off", Toast.LENGTH_SHORT).show();
                if (flag4) {
                    if (mp != null) {
                        mp.stop();
                        im2.setImageResource(R.drawable.bpause);
                        flag4= false;
                        lv.setVisibility(View.GONE);
                        rt.setVisibility(View.VISIBLE);
                        tv1.setText(" ");
                    }
                }


            }
        });


        Intent it = new Intent();
        int id=it.getIntExtra("pos",0);
        System.out.println(id+"HELLO");
        System.out.println("pos" + id);

        rt.setBackgroundResource(globalapp.num);


    }
    public void help(View v)
    {
        Intent in=new Intent(this,speechtotext.class);
        startActivity(in);
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.hello, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(item.getItemId()==R.id.m3)
        {
            Intent it=new Intent(getApplicationContext(),listvoiceaction.class);
            startActivity(it);
        }
        else if(item.getItemId()==R.id.refresh)
        {
            Intent it=getIntent();
            finish();
            startActivity(it);
        }
        else if(item.getItemId()== R.id.share)
        {
            Intent shareIntent =
                    new Intent(android.content.Intent.ACTION_SEND);

            //set the type
            shareIntent.setType("text/plain");

            //add a subject
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "Insert Subject Here");

            //build the body of the message to be shared
            String shareMessage = "https://drive.google.com/file/d/0B01wskeDQtOfTzNqTFAxQ3R3T2M/view?usp=sharing";
            //add the message
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    shareMessage);

          //start the chooser for sharing
            startActivity(Intent.createChooser(shareIntent,
                    "Share via"));
        }
        else if(item.getItemId()== R.id.m5)
        {
            Intent it=new Intent(getApplicationContext(),setwallpaper.class);
            startActivity(it);
        }
        else if(item.getItemId()== R.id.app)
        {
            Intent it=new Intent(getApplicationContext(),Main3Activity.class);
            startActivity(it);
        }
        else if(item.getItemId()== R.id.m6)
        {
            speak("as your pleasure");
            t=new Thread();

            try {
                t.sleep(1000);
                Intent i=new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    class MyRecognitionListener implements RecognitionListener
    {

        @Override
        public void onReadyForSpeech(Bundle bundle)
        {
        }

        @Override
        public void onBeginningOfSpeech()
        {
        }

        @Override
        public void onRmsChanged(float v)
        {
        }

        @Override
        public void onBufferReceived(byte[] bytes)
        {
        }

        @Override
        public void onEndOfSpeech()
        {
        }

        @Override
        public void onError(int i)
        {
            System.out.println("ERROR");
        }


        @Override

        public void onResults(Bundle results)
        {

            stop();
            bt1.setImageResource(R.drawable.voice);

            ArrayList<String> al1 = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            final String s = al1.get(0).toLowerCase();

            tv1.setText(s);
            if (s.contains("wifi on")) {
                wm = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
                wm.setWifiEnabled(true);
                speak("wifi on");


            } else if (s.contains("wifi off")) {
                wm = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
                wm.setWifiEnabled(false);
                speak("wifi off");


            } else if (s.contains("bluetooth on")) {
                if (bm.isEnabled()){

                        Toast.makeText(getApplicationContext(), "Bluetooth Already on", Toast.LENGTH_LONG).show();
                        speak("Bluetooth already on");
                }
                else
                {
                    bm.enable();
                    speak("bluetooth-on");


                }
            } else if (s.contains("bluetooth off")) {
                bm.disable();
                speak("Bluetooth-off");


            } else if (s.contains("flashlight on")) {

                trunOnFlash();
                speak("Flashlight On");


            }
            else if (s.contains("flashlight off"))
            {

                trunOffFlash();
                speak("Flashlight Off");


            }

            else if (s.startsWith("call"))
            {
                String temp=s.substring(s.indexOf(" ")+1);

                        Intent it=new Intent(Intent.ACTION_CALL);
                        phonenum=getPhoneNumber(temp);
                        it.setData(Uri.parse("tel:"+phonenum));
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                            return;
                        }
                        startActivity(it);




                }




            else if (s.startsWith("message")) {
                StringTokenizer st = new StringTokenizer(s, " ");
                st.nextToken();
                st.nextToken();
                String sphone="";
                while (st.hasMoreTokens()) {
                    sphone = st.nextToken();
                }
                phonenum = getPhoneNumber(sphone);
                speak("enter message");
                start();
                flage2 = true;

            } else if (flage2 == true) {
                String msg = tv1.getText().toString();
                sendSMSMessage(phonenum, msg);
                speak("Message Sent to" + phonename);



            } else if (s.startsWith("search"))
            {
                Uri uri = Uri.parse("http://www.google.com/#q="+s);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

//                StringTokenizer st = new StringTokenizer(s, " ");
//                    search = st.nextToken();
//                speak(" " + search);
//                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent.putExtra(SearchManager.QUERY, s); // query contains search string
//                startActivity(intent);

            }

            else if(s.contains("location")||s.contains("locate")||s.contains("Navigate to")){
                String temp=s.substring(s.indexOf(" ")+1);
                globalapp.loc=temp;
                Intent it=new Intent(getApplicationContext(),mapActivity.class);
                startActivity(it);

            }
            else if (s.replaceAll("-", "").toLowerCase().startsWith("email")) {

                if (s.replaceAll("-", "").toLowerCase().equalsIgnoreCase("email")) {
                    speak("Whom do you want to mail");
                } else {
                    sender=s.substring(s.indexOf(" ") + 1);
                    sender = sender.replaceAll(" ", "");
                    sender = sender.toLowerCase();
                    if (sender.contains("at")) {
                        int pos = sender.lastIndexOf("at");
                        sender.replace(sender.charAt(pos), '@');
                        sender.replace(sender.charAt(pos + 1), '\0');
                    }
                    start();
                    speak("Enter subject");

                    flage1 = true;
                    tv1.setText("");
                }
            }
            else if (flage1 == true) {

                    subject = tv1.getText().toString();
                start();
                speak("enter message");

                flage1 = false;
                flage3 = true;
                tv1.setText("");

            } else if (flage3 == true) {
                stop();
                message = tv1.getText().toString();
                Toast.makeText(getApplicationContext(), "Select Email Browser", Toast.LENGTH_SHORT).show();
                sendEmail(sender, subject, message);
                tv1.setText("");

            }

            else if (s.contains("set alarm")) {

                if (s.equals("set alarm")) {
                    speak("at what time should i set alarm?");
                } else {
                    hrs = s.substring(s.indexOf(" ") + 1);
                    start();
                    flag1 = true;
                    speak("Enter minutes");
                    tv1.setText("");
                }
            }
            else if (flag1 == true)
            {

                min = tv1.getText().toString();
                start();
                speak("A M or P M?");

                flag1 = false;
                flag3 = true;
                tv1.setText("");

            }
            else if (flag3 == true)
            {
                stop();
                ampm = tv1.getText().toString();
                if(ampm.equalsIgnoreCase("AM"))
                {
                    value=false;
                }
                else
                {
                    value=true;
                }
                setalarm(hrs,min,value);
                tv1.setText("");

            }
            else if(s.contains("calculate"))
            {
                if (s.contains("sum") || s.contains("+")||s.contains("plus") || s.contains("add") || s.contains("addition"))
                {
                    StringTokenizer st = new StringTokenizer(s, " ");
                    ArrayList<Integer> number = new ArrayList<>();
                    while (st.hasMoreTokens()) {

                        try {
                            int num = Integer.parseInt(st.nextToken());
                            number.add(num);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    int ans = 0;
                    for (int i = 0; i < number.size(); i++) {
                        ans = ans + number.get(i);
                    }
                    speak("Answer is " + ans);

                } else if (s.contains("subtract") || s.contains("minus") || s.contains("subtraction") || s.contains("reduce") || s.contains("difference")||s.contains("-") ){
                    StringTokenizer st = new StringTokenizer(s, " ");
                    ArrayList<Integer> number = new ArrayList<>();
                    while (st.hasMoreTokens()) {

                        try {
                            int num = Integer.parseInt(st.nextToken());
                            number.add(num);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    int a,b;
                    int ans=0;


                    for (int i=0;i<number.size();i++)
                    {
                        a=number.get(0);
                        b=number.get(1);
                        ans =a-b;
                    }
                    speak("Answer is " + ans);

                } else if (s.contains("percentage")||s.contains("percent")||s.contains("%")){
                    StringTokenizer st = new StringTokenizer(s, " ");
                    ArrayList<Double> number = new ArrayList<>();
                    while (st.hasMoreTokens()) {

                        try {
                            double num = Double.parseDouble(st.nextToken());
                            number.add(num);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    double ans = 1.0;
                    double num1 = number.get(0);
                    double num2 = number.get(1);
                    ans = (num1 * num2) / 100;

                    speak("Answer is " + ans);

                } else if ((s.contains("multiply") && s.contains("by"))||s.contains("multiplication") ||  s.contains("multiply") || s.contains("into")||s.contains("*")) {
                    StringTokenizer st = new StringTokenizer(s, " ");
                    ArrayList<Integer> number = new ArrayList<>();
                    while (st.hasMoreTokens()) {

                        try {
                            int num = Integer.parseInt(st.nextToken());
                            number.add(num);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    int ans = 1;
                    num1 = number.get(0);
                    num2 = number.get(1);
                    ans = num1 * num2;

                    speak("Answer is " + ans);

                } else if ((s.contains("divide") && s.contains("by")) || s.contains("division") || s.contains("/")) {
                    StringTokenizer st = new StringTokenizer(s, " ");
                    ArrayList<Double> number = new ArrayList<>();
                    while (st.hasMoreTokens()) {

                        try {
                            double num = Double.parseDouble(st.nextToken());
                            number.add(num);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    double ans = 1.0;
                    double num1 = number.get(0);
                    double num2 = number.get(1);
                    ans = num1 / num2;
                    speak("Answer is " + ans);

                }}

            else if (s.equals("brightness low")||s.contains("minimum brightness")||s.contains("brightness minimum")) {
                changeBrightness(0);
            speak("brightness low");
                Toast.makeText(getApplicationContext(), "Brightness Low", Toast.LENGTH_SHORT).show();



            } else if (s.contains("brightness medium")||s.contains("medium brightness")) {
                changeBrightness(0.5f);
                speak("brightness medium");
                Toast.makeText(getApplicationContext(), "Brightness medium", Toast.LENGTH_SHORT).show();


            } else if (s.equals("brightness full")||s.contains("maximize brightness")||s.contains("maximum brightness")||s.contains("brightness maximum")) {
                changeBrightness(1);
            speak("brightness full");
                Toast.makeText(getApplicationContext(), "Brightness Full", Toast.LENGTH_SHORT).show();


            } else if (s.contains("play")) {
                song = "";
                StringTokenizer st = new StringTokenizer(s, " ");
                st.nextToken();
                while (st.hasMoreTokens()) {
                    song = song + " " + st.nextToken();
                    //al.add(song);
                }
                song = song.trim();
                for(int i=0;i<al.size();i++)
                {
                    if (al.get(i).equals(song))
                    {
                        songno=i;
                        path = hashMap.get(al.get(i));

                        play(path);
                        lv2.setVisibility(View.GONE);
                       path = "";
                        tv1.setText(s+path);
                        break;
                    }
                }



            }
            else if(s.contains("hello")||s.contains("hi")||s.contains("hey"))
            {
                msg=tv1.getText().toString();
                lw.setAdapter(ad);
                int random=new Random().nextInt(mess.length);
                String reponse=mess[random];
                my.add(msg);
                my.add(reponse);
                lv4.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);
                lv2.setVisibility(View.VISIBLE);
                //speak(reponse);
                tv1.setText(null);
            }
            else if (s.contains("good morning"))
            {
                msg=tv1.getText().toString();
                lw.setAdapter(ad);
                int random=new Random().nextInt(mm.length);
                String reponse=mm[random];
                my.add(msg);
                my.add(reponse);
                lv4.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);
                lv2.setVisibility(View.VISIBLE);
                //speak(reponse);
                tv1.setText(null);
            }
            else if (s.contains("good afternoon"))
            {
                msg=tv1.getText().toString();
                lw.setAdapter(ad);
                int random=new Random().nextInt(af.length);
                String reponse=af[random];
                my.add(msg);
                my.add(reponse);
                lv4.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);
                lv2.setVisibility(View.VISIBLE);
                //speak(reponse);
                tv1.setText(null);
            }
            else if(s.contains("good evening"))
            {
                msg=tv1.getText().toString();
                lw.setAdapter(ad);
                int random=new Random().nextInt(ev.length);
                String reponse=ev[random];
                my.add(msg);
                my.add(reponse);
                lv4.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);
                lv2.setVisibility(View.VISIBLE);
                //speak(reponse);
                tv1.setText(null);
            }
            else if(s.contains("my camera"))
            {
                Intent in=new Intent(getApplicationContext(),MyCamera.class);
                startActivity(in);
                finish();
            }
            else if(s.equals("exit")||s.contains("goodbye")||s.contains("buy")) {
                speak("As your pleasure");
                t=new Thread();


                try {
                    t.sleep(5000);
                    Intent i=new Intent(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            else if (s.contains("vibrate")||s.contains("vibration")) {
                AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            }



            else if (s.contains("silent"))
            {
                AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            }


            else if (s.contains("normal"))
            {
                AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            }
            else if(s.contains("open")||s.contains("launch"))
            {


                String mys=s.substring(s.indexOf(" ")+1);
                for(int i=0;i<als.size();i++)
                {
                    if(mys.equalsIgnoreCase(als.get(i).label))
                    {

                        startActivity(getPackageManager().getLaunchIntentForPackage(als.get(i).pname));
                    }
                }

            }
            else if (s.equals("refresh"))
            {
                Intent it=getIntent();
                finish();
                startActivity(it);
            }
            else if (s.equals("help"))
            {
                Intent it=new Intent(getApplicationContext(),listvoiceaction.class);
                startActivity(it);
            }
            else if (s.contains("share")||s.contains("share my virtual assistant"))
            {
                Intent shareIntent =
                        new Intent(android.content.Intent.ACTION_SEND);

                //set the type
                shareIntent.setType("text/plain");

                //add a subject
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Insert Subject Here");

                //build the body of the message to be shared
                String shareMessage = "https://drive.google.com/file/d/0B01wskeDQtOfTzNqTFAxQ3R3T2M/view?usp=sharing";
                //add the message
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        shareMessage);

                //start the chooser for sharing
                startActivity(Intent.createChooser(shareIntent,
                        "Share via"));
            }
            else if (s.equals("set wallpaper")||s.equals("change wallpaper"))
            {
                Intent it=new Intent(getApplicationContext(),setwallpaper.class);
                startActivity(it);
            }
            else if(s.contains("set reminder"))
            {
                Calendar cal=Calendar.getInstance();
                Intent intent=new Intent(Intent.ACTION_EDIT);
                intent.setType(("vnd.android.cursor.item/event"));
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", true);
                intent.putExtra("rrule","FREQ=YEARLY");
                intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
                intent.putExtra("title","A Test Event from android app");
                startActivity(intent);
            }


            else {
                speak("Let me search web for this..");
                Uri uri = Uri.parse("http://www.google.com/#q="+s);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
//                Uri u = Uri.parse("http:" + s);
//                Intent i = new Intent(Intent.ACTION_VIEW, u);
//                startActivity(i);
//                StringTokenizer st = new StringTokenizer(s, " ");
//                while (st.hasMoreTokens()) {
//                    search = st.nextToken();
//                }
//                speak(" " + search);
//                googlesearch(search);
            }

        }


        @Override

        public void onPartialResults(Bundle bundle)
        {


        }


        @Override

        public void onEvent(int i, Bundle bundle)
        {


        }

    }


    void stop()
    {

        System.out.println("In STOP");
        tv1.setText("stopped...");
        tv1.setText("Press Mic icon to speak");

        if (recognizer != null)
        {

            recognizer.stopListening();

            recognizer.setRecognitionListener(null);

            intent = null;

            recognizer = null;

            System.gc();

        }

    }



    void start()
    {

        System.out.println("In START");

        recognizer = SpeechRecognizer.createSpeechRecognizer(this);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());


        MyRecognitionListener listener = new MyRecognitionListener();

        recognizer.setRecognitionListener(listener);


        recognizer.startListening(intent);

    }


    public void listen(View v)
    {

        if (isNetworkConnected())
        {
            tv1.setText("Listening....");
            start();
            bt1.setImageResource(R.drawable.micbol2);

        }
        else
        {

            Toast.makeText(getApplicationContext(), "Connect To Internet", Toast.LENGTH_LONG).show();

        }

    }

    public void music(View v)

    {

        startActivity(new Intent(this,musiclist.class));

    }


    private boolean isNetworkConnected()
    {

        boolean haveConnectedWifi = false;

        boolean haveConnectedMobile = false;


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo)
        {

            if (ni.getTypeName().equalsIgnoreCase("WIFI"))

                if (ni.isConnected())

                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))

            if (ni.isConnected())

                haveConnectedMobile = true;
        }

        return haveConnectedWifi || haveConnectedMobile;

    }
    public void speak(String s) {
        HashMap<String, String> mymap = new HashMap<>();
        mymap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "myid");
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, mymap);
    }


    class MyListener implements TextToSpeech.OnInitListener {

        @Override
        public void onInit(int status) {

        }
    }

    class MyUtteranceListener extends UtteranceProgressListener {

        @Override
        public void onStart(String utteranceId)

        {
            Log.d("MYMESSAGE", "on start");
        }

        @Override
        public void onDone(String utteranceId) {
            Log.d("MYMESSAGE", "on Done");
        }

        @Override
        public void onError(String utteranceId) {

        }
    }
    private void getcamera(){

        if(camera==null){
            try {

                camera= Camera.open();
                pm=camera.getParameters();

            }
            catch (RuntimeException rn)
            {

            }
        }
    }
    private void trunOnFlash(){
        System.out.println("ABC flash on");

        if(!isFlashOn){getcamera();
            if(camera==null|| pm==null){
                return;
            }

            pm=camera.getParameters();
            pm.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(pm);
            camera.startPreview();
            System.out.println("ABC flash on 2");
            isFlashOn=true;

        }
        else if(pm.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH))
        {
            Toast.makeText(getApplicationContext(), "Flash  Already on", Toast.LENGTH_LONG).show();
            speak("Flash already on");
        }
    }
    private void trunOffFlash()
    {
        System.out.println("ABC flash off");

        if(isFlashOn){ getcamera();

            if(camera==null|| pm==null){
                return;
            }


            pm=camera.getParameters();
            pm.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

            camera.setParameters(pm);
            camera.stopPreview();
            System.out.println("ABC flash off 2");
            isFlashOn=false;
            camera.release();

        }
        else if(pm.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH))
        {
            Toast.makeText(getApplicationContext(), "Flash  Already off", Toast.LENGTH_LONG).show();
            speak("Flash already off");
        }
    }
    public String getPhoneNumber(String name)
    {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone ="";

        Log.d("MYMSG", name);
        while (cur.moveToNext())
        {
            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
            final String name1 = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            phonename=name1;
            if(name1.equalsIgnoreCase(name))
            {
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " ="+id, null, null);
                    if(pCur.moveToNext())
                    {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        found = true;
                    }
                    pCur.close();
                }
            }
        }

        if(found)
            return phone;
        else
            return "";

    }
    private void call(String phonenum)
    {

        Intent in = new Intent(Intent.ACTION_CALL);
        in.setData(Uri.parse("tel:" + phonenum));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(in);
    }
    protected void sendSMSMessage(String phonenum,String msg) {
        Log.i("Send SMS", "");

        System.out.println("HELLO " + phonenum + " " + phonename);
        try
        {
            if(phonenum.equals("unsaved"))
            {
                Toast.makeText(getApplicationContext(), "Contact Not Found " + phonenum, Toast.LENGTH_SHORT).show();
            }
            else
            {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phonenum, null, msg, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent to "+phonenum, Toast.LENGTH_SHORT).show();
            }
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        stop();

    }
    public void googlesearch(String search){
        Uri u=Uri.parse("http://www.google.co.in/"+search);
        Intent i = new Intent(Intent.ACTION_VIEW,u);
        startActivity(i);

    }

    public void googlemap(String search) {

        String url="http://maps.google.com/maps?daddr=" + search;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(url));
        startActivity(intent);

    }
    protected void sendEmail( String to,String subject,String message){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
        email.setType("message/rfc822");
        email.setType("text/plain");
        startActivity(Intent.createChooser(email, "Select Email Client"));


    }
    public void setalarm(String hrs,String min,boolean value)
    {
        Intent i=new Intent((AlarmClock.ACTION_SET_ALARM));
        i.putExtra(AlarmClock.EXTRA_HOUR,hrs);
        i.putExtra(AlarmClock.EXTRA_MINUTES, min);
        i.putExtra(AlarmClock.EXTRA_IS_PM,value);
    }
    public void changeBrightness( float bright)
    {
        WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
        layoutParams.screenBrightness=bright;
        getWindow().setAttributes(layoutParams);}
    public void play(String mysong)
    {

        Log.d("my","function");

        mp=new MediaPlayer();
        try
        { Log.d("song","Palying song");
            System.out.println("Palying song" + hashMap.get(mysong) + " " + mysong);
            mp.setDataSource(mysong);//Write your location here
            mp.prepare();
            mp.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        lv2.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);


    }
    class myadapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return my.size();
        }

        @Override
        public Object getItem(int i)
        {
            return my.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent)
        {
            String al=my.get(i).toLowerCase();
            LayoutInflater inflater= LayoutInflater.from(parent.getContext());

            convertView=inflater.inflate(R.layout.mylist2, parent, false);
            TextView tv2=(TextView)(convertView.findViewById(R.id.tv2));
            lv3=(LinearLayout)convertView.findViewById(R.id.design);
            if(al.startsWith("robot"))
            {
                al=al.substring(al.indexOf(" ")+1);
                speak(al);
                lv3.setGravity(Gravity.RIGHT);

            }
            else
            {
                lv3.setGravity(Gravity.LEFT);
            }
            tv2.setText(al);
            return convertView;
        }

}

}

