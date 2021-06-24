package com.example.musicalbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {
    Button btnNext,btnPrev,btnPause,btnPlay;
    TextView songText,currtime,maxtime;
    SeekBar songSeekbar;
    BarVisualizer visualizer;
    ImageView imageView;

    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekBar;
    String sname;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if(visualizer!=null){
            visualizer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnNext = (Button) findViewById(R.id.next);
        btnPause = (Button) findViewById(R.id.pause);

        visualizer = findViewById(R.id.blast);
        btnPrev = (Button) findViewById(R.id.prev);

        songText = (TextView) findViewById(R.id.songLable);

        songSeekbar = (SeekBar) findViewById(R.id.seekbar);

        imageView = findViewById(R.id.imageView);
        currtime = findViewById(R.id.txtStart);
        maxtime = findViewById(R.id.txtstop);


        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration =mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration){
                    try{
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);
                    }catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }

            }
        };

        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songName");
        songText.setText(sname);

        songText.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri uri  = Uri.parse(mySongs.get(position).toString());




        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        songSeekbar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();

        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_500),PorterDuff.Mode.MULTIPLY);
        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.purple_500),PorterDuff.Mode.SRC_IN);




        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        maxtime.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                currtime.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        },delay);


        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekbar.setMax(mediaPlayer.getDuration());

                if(mediaPlayer.isPlaying()){
                    btnPause.setBackgroundResource(R.drawable.icon_play);
                    mediaPlayer.pause();
                }else{
                    btnPause.setBackgroundResource(R.drawable.icon_pause);
                    mediaPlayer.start();
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnNext.performClick();
            }
        });
        int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId != -1)
            visualizer.setAudioSessionId(audioSessionId);





        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%mySongs.size());

                Uri uri = Uri.parse(mySongs.get(position).toString());

                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                sname = mySongs.get(position).getName().toString();
                songText.setText(sname);

                btnPause.setBackgroundResource(R.drawable.icon_pause);

                mediaPlayer.start();
                startAnimation(imageView);
                int audioSessionId = mediaPlayer.getAudioSessionId();
                if (audioSessionId != -1)
                    visualizer.setAudioSessionId(audioSessionId);
                songSeekbar.setMax(mediaPlayer.getDuration());

                String endTime = createTime(mediaPlayer.getDuration());
                maxtime.setText(endTime);
               int  currentPosition = mediaPlayer.getCurrentPosition();
                songSeekbar.setProgress(currentPosition);



            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);

                Uri uri = Uri.parse(mySongs.get(position).toString());

                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                sname = mySongs.get(position).getName().toString();
                songText.setText(sname);



                int audioSessionId = mediaPlayer.getAudioSessionId();
                if (audioSessionId != -1)
                   visualizer.setAudioSessionId(audioSessionId);

                String endTime = createTime(mediaPlayer.getDuration());
                maxtime.setText(endTime);



                mediaPlayer.start();
                btnPause.setBackgroundResource(R.drawable.icon_pause);
                startAnimation(imageView);
                songSeekbar.setMax(mediaPlayer.getDuration());
                int  currentPosition = mediaPlayer.getCurrentPosition();
                songSeekbar.setProgress(currentPosition);

            }
        });
    }
    public void startAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,"rotation",0f ,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;
        time+= min + ":" ;
        if(sec<10){
            time += "0" ;
        }
        time +=sec;
        return time;
    }
}