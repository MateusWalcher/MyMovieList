package com.anima.mymovielist.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.anima.mymovielist.utils.FirebaseFunctions;
import com.anima.mymovielist.utils.ValidaDados;

public class LoginActivity extends AppCompatActivity {

  private com.anima.mymovielist.databinding.ActivityLoginBinding binding;
  private FirebaseFunctions firebaseFunctions;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = com.anima.mymovielist.databinding.ActivityLoginBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    firebaseFunctions = new FirebaseFunctions(this);

    binding.textCadastro.setOnClickListener(view -> {
      startActivity(new Intent(this, CadastroActivity.class));
    });

    binding.textRecuperaConta.setOnClickListener(v ->
      startActivity(new Intent(this, RecuperaContaActivity.class)));

    binding.btnLogin.setOnClickListener(v -> ValidaDados.validaDadosLogin(this, binding, binding.editEmail.getText().toString().trim(), binding.editSenha.getText().toString().trim(), firebaseFunctions));
  }
}
