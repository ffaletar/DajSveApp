package hr.foi.air.dajsve.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import hr.foi.air.dajsve.Helpers.Baza;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hr.foi.air.dajsve.R.layout.activity_splash_screen);

        ImageView slika = (ImageView) findViewById(hr.foi.air.dajsve.R.id.splash_screen_logo);
        TextView tekst = (TextView) findViewById(hr.foi.air.dajsve.R.id.splash_screen_tekst);


        Thread splashScreenThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2500);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        splashScreenThread.start();



        TranslateAnimation anim = new TranslateAnimation(0,0,0,1200);
        TranslateAnimation anim2 = new TranslateAnimation(0,0,1200,0);
        anim.setDuration(800);
        anim2.setDuration(1000);

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setFillAfter(true);
        anim.setFillEnabled(true);

        slika.startAnimation(anim);
        tekst.startAnimation(anim2);


    }
}
