package com.anima.mymovielist.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.anima.mymovielist.databinding.ActivityCadastroBinding;
import com.anima.mymovielist.databinding.ActivityLoginBinding;
import com.anima.mymovielist.databinding.ActivityRecuperaContaBinding;
import com.anima.mymovielist.ui.activities.CadastroActivity;
import com.anima.mymovielist.ui.activities.LoginActivity;
import com.anima.mymovielist.ui.activities.RecuperaContaActivity;
import com.anima.mymovielist.utils.FirebaseFunctions;

public class ValidaDados {

  public static void validaDadosLogin(Context context, ActivityLoginBinding binding, String email, String senha, FirebaseFunctions firebaseFunctions) {
    if (!email.isEmpty()) {
      if (!senha.isEmpty()) {
        binding.progressBar.setVisibility(View.VISIBLE);
        firebaseFunctions.loginFirebase(email, senha);
      } else {
        Toast.makeText(context, "Informe uma senha.", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(context, "Informe seu e-mail.", Toast.LENGTH_SHORT).show();
    }
  }

  public static void validaDadosRecuperaConta(Context context, ActivityRecuperaContaBinding binding, String email, FirebaseFunctions firebaseFunctions) {
    if (!email.isEmpty()) {
      binding.progressBar.setVisibility(View.VISIBLE);
      firebaseFunctions.recuperaContaFirebase(email);
    } else {
      Toast.makeText(context, "Informe seu e-mail.", Toast.LENGTH_SHORT).show();
    }
  }

  public static void validaDadosCadastro(Context context, ActivityCadastroBinding binding, String email, String senha, FirebaseFunctions firebaseFunctions) {
    if (!email.isEmpty()) {
      if (!senha.isEmpty()) {
        binding.progressBar.setVisibility(View.VISIBLE);
        firebaseFunctions.criarContaFirebase(email, senha);
      } else {
        Toast.makeText(context, "Informe uma senha.", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(context, "Informe seu e-mail.", Toast.LENGTH_SHORT).show();
    }
  }
}
