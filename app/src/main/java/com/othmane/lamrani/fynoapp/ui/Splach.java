package com.othmane.lamrani.fynoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.othmane.lamrani.fynoapp.API.models.TempUserInfo;
import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Methods;

public class Splach extends AppCompatActivity {

    //private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);

        // set the context value
        if(Methods.context == null){
            Methods.context= getApplicationContext();
        }

        // Hide action bar
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //tv = (TextView)  findViewById(R.id.tv_splach);
        iv = (ImageView) findViewById(R.id.iv_splach);

        Animation myAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_transition);
        //tv.startAnimation(myAnimation);
        iv.startAnimation(myAnimation);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    TempUserInfo user = Methods.getUser();
                    Intent intent;
                    if(user.getRemember_me()){
                        // Log the user into the application
                         intent = new Intent(getApplicationContext(), MainActivity.class);
                    }
                    else{
                         intent = new Intent(Splach.this, LoginActivity.class);
                    }

                    startActivity(intent);
                    finish();
                }
            }
        };

        timer.start();

    }
}
