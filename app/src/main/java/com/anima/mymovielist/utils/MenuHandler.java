package com.anima.mymovielist.utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.anima.mymovielist.R;
import com.anima.mymovielist.model.ModelTMDB;
import com.anima.mymovielist.model.MovieModelClass;
import com.anima.mymovielist.ui.activities.ActivityFavoritos;
import com.anima.mymovielist.ui.activities.LoginActivity;
import com.anima.mymovielist.ui.activities.MainActivity;
import com.anima.mymovielist.ui.adapters.FilmesAdaptery;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MenuHandler {

  public static boolean handleMenuItemSelection(AppCompatActivity activity, MenuItem item, List<MovieModelClass> favoritesList) {
    int id = item.getItemId();


    if (id == R.id.action_expand) {
      return true;
    } else if (id == R.id.action_home) {
      activity.startActivity(new Intent(activity, MainActivity.class));
      activity.finish();
      return true;
    } else if (id == R.id.action_favorites) {
      showFavoritesList(activity, favoritesList);
      return true;
    } else if (id == R.id.action_logoff) {
      FirebaseAuth.getInstance().signOut();
      activity.finish();
      activity.startActivity(new Intent(activity, LoginActivity.class));
      return true;
    }

    return false;
  }

  private static void showFavoritesList(Context context, List<MovieModelClass> favoritesList) {
    Intent intent = new Intent(context, ActivityFavoritos.class);
    intent.putParcelableArrayListExtra("favoritesList", new ArrayList<>(favoritesList));
    context.startActivity(intent);
  }

}
