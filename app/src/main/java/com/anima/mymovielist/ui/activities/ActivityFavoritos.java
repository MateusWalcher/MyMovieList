package com.anima.mymovielist.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anima.mymovielist.R;
import com.anima.mymovielist.model.MovieModelClass;
import com.anima.mymovielist.ui.adapters.FilmesAdaptery;
import com.anima.mymovielist.utils.MenuHandler;

import java.util.ArrayList;
import java.util.List;


public class ActivityFavoritos extends AppCompatActivity {

  private List<MovieModelClass> favoritesList;
  private RecyclerView recyclerView;
  private FilmesAdaptery filmesAdaptery;
  private List<MovieModelClass> movieList;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favoritos);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    movieList = new ArrayList<>();

    ArrayList<MovieModelClass> favoritesArrayList = getIntent().getParcelableArrayListExtra("favoritesList");
    favoritesList = new ArrayList<>(favoritesArrayList);

    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    filmesAdaptery = new FilmesAdaptery(this, favoritesList, favoritesList);
    recyclerView.setAdapter(filmesAdaptery);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    return MenuHandler.handleMenuItemSelection(this, item, favoritesList);
  }

}
