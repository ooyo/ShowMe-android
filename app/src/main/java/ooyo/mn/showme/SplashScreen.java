package ooyo.mn.showme;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseLongArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

/**
 * Created by appleuser on 11/5/16.
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        final ImageButton btnLogo = (ImageButton) findViewById(R.id.btnLogo);
        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogo.setVisibility(View.INVISIBLE);
                ImageView image= (ImageView) findViewById(R.id.imageView2);
                image.setVisibility(View.INVISIBLE);
                try {
                    VideoView videoHolder = (VideoView) findViewById(R.id.videoView); //new VideoView(SplashScreen.this);
                //    setContentView(videoHolder);
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.master2);
                    videoHolder.setVideoURI(video);
                    videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            jump();
                        }
                    });
                    videoHolder.start();
                } catch (Exception ex) {
                    jump();
                }
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        Intent intent = new Intent(SplashScreen.this, WelcomeActivity.class);
        intent.putExtra("clicked", "welcome");
        startActivity(intent);
        finish();
    }
}