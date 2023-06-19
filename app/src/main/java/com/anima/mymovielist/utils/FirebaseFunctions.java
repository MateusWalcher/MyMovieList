package com.anima.mymovielist.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.anima.mymovielist.databinding.ActivityLoginBinding;
import com.anima.mymovielist.ui.activities.CadastroActivity;
import com.anima.mymovielist.databinding.ActivityCadastroBinding;
import com.anima.mymovielist.ui.activities.LoginActivity;
import com.anima.mymovielist.ui.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseFunctions {
  private FirebaseAuth mAuth;
  private Context context;

  public FirebaseFunctions(Context context) {
    mAuth = FirebaseAuth.getInstance();
    this.context = context;
  }

  public void recuperaContaFirebase(String email) {
    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        Toast.makeText(context, "Verifique seu email", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void loginFirebase(String email, String senha) {
    mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
      } else {
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(((LoginActivity) context).getLayoutInflater());
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void criarContaFirebase(String email, String senha) {
    mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
      } else {
        ActivityCadastroBinding binding = ActivityCadastroBinding.inflate(((CadastroActivity) context).getLayoutInflater());
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
