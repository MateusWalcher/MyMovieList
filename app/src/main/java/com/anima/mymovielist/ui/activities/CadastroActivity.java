package com.anima.mymovielist.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anima.mymovielist.databinding.ActivityCadastroBinding;
import com.anima.mymovielist.ui.activities.MainActivity;
import com.anima.mymovielist.utils.FirebaseFunctions;
import com.anima.mymovielist.utils.ValidaDados;

public class CadastroActivity extends AppCompatActivity {

  private ActivityCadastroBinding binding;
  private FirebaseFunctions firebaseFunctions;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityCadastroBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    firebaseFunctions = new FirebaseFunctions(this);

    binding.btnCriarConta.setOnClickListener(v -> ValidaDados.validaDadosCadastro(this, binding, binding.editEmail.getText().toString().trim(), binding.editSenha.getText().toString().trim(), firebaseFunctions));
  }
}
