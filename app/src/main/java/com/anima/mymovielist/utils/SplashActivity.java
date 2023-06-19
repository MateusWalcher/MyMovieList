package com.anima.mymovielist.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.anima.mymovielist.R;
import com.anima.mymovielist.ui.activities.LoginActivity;
import com.anima.mymovielist.ui.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    new Handler(getMainLooper()).postDelayed(this::verificaLogin, 3000);
  }

  private void verificaLogin(){
    finish();
    if (FirebaseAuth.getInstance().getCurrentUser() != null){
      startActivity(new Intent(this, MainActivity.class));
    }else {
      startActivity(new Intent(this, LoginActivity.class));
    }
  }

}
