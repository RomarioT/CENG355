package com.fouronesixplayer.fouronesixplayer;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    Button playBtn;
    SeekBar positionBar, volumeBar;
    ProgressBar progressBar;
    TextView elapsedTimeLabel, remainingTimeLabel, textView;
    MediaPlayer mp;
    int totalTime;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button mFirebaseBtn, mFirebaseBtn2, mFirebaseBtn3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        //Play and StopButton
       mFirebaseBtn = (Button) findViewById(R.id.firebase_btn);

       mDatabase = FirebaseDatabase.getInstance().getReference();

        mFirebaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Play").setValue(1);
            }
        });

        mFirebaseBtn2 = (Button) findViewById(R.id.firebase_btn2);
        mFirebaseBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Play").setValue(0);
            }
        });


        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Media Player
        mp = MediaPlayer.create(this, R.raw.song);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        //Buttons & Time
       // playBtn = findViewById(R.id.playBtn);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);

        //Position Bar
        positionBar = findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser){
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Volume Bar
        textView = (TextView) findViewById(R.id.textView);
        progressBar =(ProgressBar)findViewById(R.id.progressBar);
        volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressBar.setProgress(progress);
                        textView.setText(""+progress);
                        float volumeNum=progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Volume Upload
        mFirebaseBtn3 = (Button) findViewById(R.id.uploadVolume);
        mFirebaseBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Volume").setValue(textView.getText());
            }
        });

        //Thread (Update positionBar and timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp!=null){
                    try {
                        Message msg = new Message();
                        msg.what=mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e){}
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Update positionBar
            positionBar.setProgress(currentPosition);

            //Update Labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("-" + remainingTime);
        }
    };

    public String createTimeLabel(int time){

        String timeLabel = "";
        int min = time / 1000/ 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel +=sec;
        return timeLabel;
    }



  /* public void playBtnClick (View view){

          if (!mp.isPlaying()){
            //Stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        } else {
            //Playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }

    } */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_upload) {
            Intent intent4 = new Intent(MainActivity.this, Upload.class);
            startActivity(intent4);
        }

        if (id == R.id.action_signout) {
            mAuth.signOut();
            Intent intent5 = new Intent(MainActivity.this, Login.class);
            startActivity(intent5);
        }

        if (id == R.id.action_songlist) {
            Intent intent = new Intent(MainActivity.this, MyRecyclerViewActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_aboutus) {
            Intent intent = new Intent(MainActivity.this, AboutUs.class);
            startActivity(intent);
        }

        if (id == R.id.action_quit) {
            finishAndRemoveTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


   }
