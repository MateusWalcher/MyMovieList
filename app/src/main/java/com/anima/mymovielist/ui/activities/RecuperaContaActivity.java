package com.anima.mymovielist.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.anima.mymovielist.databinding.ActivityRecuperaContaBinding;
import com.anima.mymovielist.utils.FirebaseFunctions;
import com.anima.mymovielist.utils.ValidaDados;

public class RecuperaContaActivity extends AppCompatActivity {

  private ActivityRecuperaContaBinding binding;
  private FirebaseFunctions firebaseFunctions;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityRecuperaContaBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    firebaseFunctions = new FirebaseFunctions(this);

    binding.btnRecuperaConta.setOnClickListener(v -> ValidaDados.validaDadosRecuperaConta(this, binding, binding.editEmail.getText().toString().trim(), firebaseFunctions));
  }
}
