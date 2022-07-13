package com.example.mycomputer.voicemobileapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyCamera extends Activity
{
    Preview preview;
    FrameLayout myframelayout;
    TextView tv1;
    Camera.PictureCallback jpegCallback;
    int n=0;
    SpeechRecognizer recognizer;
    Intent intent;
    AlertDialog ad;
    AlertDialog.Builder builder;
    Handler handler;
    @Override
    protected void onPause()
    {
        super.onPause();
        stop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);
        tv1 = (TextView) findViewById(R.id.tv1);


        builder=new AlertDialog.Builder(this);
        builder.setTitle("Error Message!");
        builder.setMessage("Please connect to Internet");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()  //Dialog Box uses this class
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        ad = builder.create();
        preview = new Preview(this);
        Log.d("MYMSG","all set");

        File f = new File(Environment.getExternalStorageDirectory().getPath()+"/MyCamera/");
        f.mkdir();

        jpegCallback = new Camera.PictureCallback()
        {
            public void onPictureTaken(byte[] data, Camera camera)
            {
                FileOutputStream outStream = null;
                try
                {
                    String timestmp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    outStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath()+"/MyCamera/img"+timestmp+".jpg"));
                    outStream.write(data);
                    outStream.close();
                    Toast.makeText(getBaseContext(), "Picture Clicked", Toast.LENGTH_LONG).show();
                    tv1.setText("Picture Clicked");;
                    camera.startPreview();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        myframelayout = (FrameLayout)findViewById(R.id.myframelayout);

        myframelayout.addView(preview);

        handler=new Handler();
        Log.d("MYMSG","all set");
        start();

    }
    void start()
    {
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());

        MyRecognitionListener listener = new MyRecognitionListener();
        recognizer.setRecognitionListener(listener);
        ConnectivityManager conn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        try
        {

            if (conn.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || conn.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED )
            {
                Log.d("MYMSG","start listening camera ");
                recognizer.startListening(intent);
            }
            else
            {
                ad.setMessage("Internet Access Not Available, Please connect to Internet");
                ad.show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    void stop()
    {
        if (recognizer != null)
        {
            recognizer.stopListening();
            recognizer.setRecognitionListener(null);
            intent = null;
            recognizer = null;
            System.gc();
        }
    }
    class Preview extends SurfaceView implements SurfaceHolder.Callback
    {
        SurfaceHolder holder;
        Camera camera;
        public Preview(Context context)
        {
            super(context);
            holder = getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            camera = Camera.open();
            camera.setDisplayOrientation(90);

            camera.setPreviewCallback(new Camera.PreviewCallback()
            {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera)
                {

                }
            });
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            try
            {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder)
        {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera=null;
        }
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
            start();
        }

        @Override
        public void onResults(Bundle results)
        {
            stop();
            ArrayList<String> al1 = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final String s = al1.get(0);

            if(s.equalsIgnoreCase("take photo"))
            {
                preview.camera.takePicture(null, null, jpegCallback);
            }
            else
            {
                ad.setMessage("Command Not Recognized, Try Again");
                ad.show();
            }
            start();
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

}




