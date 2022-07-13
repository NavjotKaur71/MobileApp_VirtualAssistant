package com.example.mycomputer.voicemobileapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

public class speechtotext extends AppCompatActivity {
    Button bt1;
    EditText et;
    int songno=0;
    TextToSpeech tts;
    String s;
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
    boolean found = false;
    TextView tv1;
    Cursor cursor;
    MediaPlayer mp;
    HashMap<String,String> hashMap;
    LinearLayout lv,lv2,lv3;
    ImageButton im,im2,im3,im4;
    ArrayList<String> al;
    ArrayList<String> my;
    ListView lw;
    myadapter ad;
    RelativeLayout rt;
    String msg="";
    int num1,num2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechtotext);
        bt1=(Button)findViewById(R.id.bt1);
        et=(EditText)findViewById(R.id.et);
        tv1=(TextView)findViewById(R.id.tv1);
        tts= new TextToSpeech(this,new MyListener());
        bm = BluetoothAdapter.getDefaultAdapter();
        lv=(LinearLayout)findViewById(R.id.LL1);
        lv2=(LinearLayout)findViewById(R.id.LL2);
        rt=(RelativeLayout)findViewById(R.id.rt);

        lw=(ListView)findViewById(R.id.lv1);
        im=(ImageButton)findViewById(R.id.ib2);
        im2=(ImageButton)findViewById(R.id.ib3);
        im3=(ImageButton)findViewById(R.id.ib4);
        im4=(ImageButton)findViewById(R.id.ib5);
        my=new ArrayList<>();
        ad=new myadapter();
        final String mess[]= getResources().getStringArray(R.array.hello);
        final String mm[]= getResources().getStringArray(R.array.morning);
        final String af[]= getResources().getStringArray(R.array.afternoon);
        final String ev[]= getResources().getStringArray(R.array.evening);

        tts.setOnUtteranceProgressListener(new MyUtteranceListener());
        al= new ArrayList<>();
        hashMap=new HashMap<>();


        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        final String[] projection =
                {MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA,};
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
            hashMap.put(p,s);
            al.add(p);
            //son= hashMap.get(p);
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
                    try {
                        songno = songno - 1;
                        mp.setDataSource(hashMap.get(al.get(songno)));
                        mp.prepare();
                        mp.start();
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
                    try {
                        songno = songno + 1;
                        play(hashMap.get(al.get(songno)));
                        mp.prepare();
                        mp.start();
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



                    }
                }


            }
        });


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                s = et.getText().toString().toLowerCase();
                ad.notifyDataSetChanged();
                if (s.equals("wifi on")) {
                    wm = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
                    wm.setWifiEnabled(true);
                    speak("wifi on");

                } else if (s.equals("wifi off")) {
                    wm = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
                    wm.setWifiEnabled(false);
                    speak("wifi off");

                } else if (s.equals("bluetooth on")) {
                    if (!bm.isEnabled())

                    {
                        bm.enable();
                        speak("Bluetooth-on");

                    }
                } else if (s.equals("bluetooth off")) {
                    bm.disable();
                    speak("Bluetooth-off");

                } else if (s.equals("flash on")) {

                    trunOnFlash();

                    speak("Flash On");

                } else if (s.equals("flash off")) {
                    trunOffFlash();

                    speak("Flash Off");

                } else if (s.startsWith("call")) {

                        phonename = s.substring(s.indexOf(" ")+1);

                    // String phonename = et.getText().toString();
                    phonenum = getPhoneNumber(phonename);
                    call(phonenum);
                }
                else if (s.startsWith("message")) {
                    StringTokenizer st = new StringTokenizer(s, " ");
                    st.nextToken();
                    st.nextToken();
                    while (st.hasMoreTokens()) {
                        phonename = st.nextToken();
                    }
                    phonenum = getPhoneNumber(phonename);
                    speak("Enter message");
                    flage2 = true;
                    et.setText("");
                }
                else if (flage2 == true)
                {
                    String msg = et.getText().toString();
                    sendSMSMessage(phonenum, msg);
                    speak("Message Sent to" + phonename);
                    et.setText("");

                } else if (s.startsWith("Enter")) {
                    StringTokenizer st = new StringTokenizer(s, " ");
                    while (st.hasMoreTokens()) {
                        search = st.nextToken();
                    }
                    speak(" " + search);
                    googlesearch(search);
                    et.setText(" ");
                }
                else if (s.startsWith("location")){
                    StringTokenizer st=new StringTokenizer(s," ");
                    while (st.hasMoreTokens()) {
                        search =search+" "+st.nextToken();
                    }
                    googlemap(search);
                    et.setText("");

                }
                else if(s.contains("mail"))
                {
                        StringTokenizer st = new StringTokenizer(s, " ");
                    System.out.println(st.countTokens() + "ABC");
                        while (st.hasMoreTokens())
                        {
                            String temp=st.nextToken();
                            if(!temp.equals("Mail"))
                            sender = temp;
                        }
                        speak("Enter subject");
                        flage1 = true;
                        et.setText("");
                }
                else if (flage1 == true)
                {
                        subject = et.getText().toString();
                        speak("enter message");
                        flage1 = false;
                        flage3 = true;
                        et.setText("");

                } else if (flage3 == true)
                {
                        message = et.getText().toString();
                        sendEmail(sender, subject, message);
                        //tv1.setText("to=" + sender + "\nSubject=" + subject + "\nMessage=" + message + "");
                        et.setText("");
                }
                else if(s.contains("calculate"))
                {
                    if (s.contains("sum") || s.contains("+")||s.contains("plus") || s.contains("add") || s.contains("addition")) {
                        StringTokenizer st = new StringTokenizer(s, " ");
                        ArrayList<Integer> number = new ArrayList<>();
                        while (st.hasMoreTokens())
                        {

                            try
                            {
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

                    } else if (s.contains("%")&& s.equals("of") ) {
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

                    }
                    else if ((s.contains("divided") && s.contains("by")) || s.contains("division") || s.contains("/"))
                    {
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

                    }
                }
                else if (s.equals("brightness low")) {
                        changeBrightness(0);
                        speak("brightness low");
                    et.setText("");
                    } else if (s.equals("brightness medium")) {
                        changeBrightness(0.5f);
                    speak("brightness medium");
                    et.setText("");
                    } else if (s.equals("brightness full")) {
                        changeBrightness(1);
                    speak("brightness full");
                    et.setText("");
                    } else if (s.startsWith("play")) {
                        song = "";
                        StringTokenizer st = new StringTokenizer(s, " ");
                        st.nextToken();
                    while (st.hasMoreTokens()) {
                            song = song + " " + st.nextToken();
                            //al.add(song);
                        }
                    song = song.trim();
                    for(int i=0;i<al.size();i++) {
                        if (al.get(i).contains(song))
                        {
                            songno=i;
                            path = hashMap.get(al.get(i));

                            play(path);
                            path = "";
                            et.setText("");
                            break;
                        }
                    }



                    }
                else if(s.equals("hello")||s.equals("hi")||s.equals("hey"))
                {
                    msg=et.getText().toString();
                    lw.setAdapter(ad);
                    int random=new Random().nextInt(mess.length);
                    String reponse=mess[random];
                    my.add(msg);
                    my.add(reponse);
                    speak(reponse.substring(reponse.indexOf(" ") + 1));
                    lv.setVisibility(View.GONE);
                    lv2.setVisibility(View.VISIBLE);
                    //speak(reponse);
                    et.setText(null);
                }
                else if(s.equals("good morning"))
                {
                    msg=et.getText().toString();
                    lw.setAdapter(ad);
                    int random=new Random().nextInt(mm.length);
                    String reponse=mm[random];
                    my.add(msg);
                    my.add(reponse);
                    speak(reponse.substring(reponse.indexOf(" ") + 1));
                    lv.setVisibility(View.GONE);
                    lv2.setVisibility(View.VISIBLE);
                    //speak(reponse);
                    et.setText(null);
                }
                else if(s.equals("good afternoon"))
                {
                    msg=et.getText().toString();
                    lw.setAdapter(ad);
                    int random=new Random().nextInt(af.length);
                    String reponse=af[random];
                    my.add(msg);
                    my.add(reponse);
                    speak(reponse.substring(reponse.indexOf(" ") + 1));
                    lv.setVisibility(View.GONE);
                    lv2.setVisibility(View.VISIBLE);
                    //speak(reponse);
                    et.setText(null);
                }
                else if(s.equals("good evening"))
                {
                    msg=et.getText().toString();
                    lw.setAdapter(ad);
                    int random=new Random().nextInt(ev.length);
                    String reponse=ev[random];
                    my.add(msg);
                    my.add(reponse);
                    speak(reponse.substring(reponse.indexOf(" ")+1));
                    lv.setVisibility(View.GONE);
                    lv2.setVisibility(View.VISIBLE);
                    //speak(reponse);
                    et.setText(null);
                }
                else if(s.equals("my camera"))
                {
                    Intent in=new Intent(getApplicationContext(),camera.class);
                    startActivity(in);
                    finish();
                }
                else if (s.contains("vibrate")) {
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
                else {
                        Uri u = Uri.parse("http:" + s);
                        Intent i = new Intent(Intent.ACTION_VIEW, u);
                        startActivity(i);
                    }
                }
        });

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
    private void trunOnFlash(){getcamera();
        System.out.println("ABC flash on");
        if(!isFlashOn){
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
    }
    private void trunOffFlash()
    {getcamera();
        System.out.println("ABC flash off");
        if(isFlashOn){
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

    }
    public void googlesearch(String search){
        Uri u=Uri.parse("http:"+search);
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
