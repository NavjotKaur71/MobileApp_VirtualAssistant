package com.example.mycomputer.voicemobileapp;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

public class camera extends AppCompatActivity {
    Preview preview;
    FrameLayout myframelayout;
    TextView tv1;
    Camera.PictureCallback jpegCallback;
    int n=0;
    String pic="";
    EditText et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        et1=(EditText)findViewById(R.id.et2);
        preview = new Preview(this);
                Log.d("MYMSG", "all set");

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
                myframelayout = (FrameLayout)findViewById(R.id.fm1);

                myframelayout.addView(preview);


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
                        camera.setPreviewCallback(new Camera.PreviewCallback() {
                            @Override
                            public void onPreviewFrame(byte[] data, Camera camera) {

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
                    Log.d("Release"," ");
                    camera.stopPreview();
                    camera.setPreviewCallback(null);
                    camera.release();
                    camera=null;
                }
            }

    public void ClickPhoto(View v)
    {
        pic=et1.getText().toString();
        if(pic.equals("Click"))
        {
        preview.camera.takePicture(null,null,jpegCallback);
    }

        }}







