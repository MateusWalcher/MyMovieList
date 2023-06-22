package com.anima.mymovielist.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.ImageButton;

import com.anima.mymovielist.R;

import java.util.Locale;

public class ChangeLanguage {
  public static void changeLanguage(Context context) {
    Configuration config = new Configuration(context.getResources().getConfiguration());

    if (config.locale.getLanguage().equals("en")) {
      config.locale = new Locale("pt", "BR");
    } else {
      config.locale = Locale.ENGLISH;
    }

    context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
  }

  public static void changeFlag(Context context, ImageButton button) {
    Configuration config = new Configuration(context.getResources().getConfiguration());

    if (config.locale.getLanguage().equals("en")) {
      button.setImageResource(R.drawable.usa_flag);
    } else {
      button.setImageResource(R.drawable.brazil_flag);
    }

    context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
  }
}
