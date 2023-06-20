package com.anima.mymovielist.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anima.mymovielist.ui.adapters.FilmesAdaptery;
import com.anima.mymovielist.model.MovieModelClass;
import com.anima.mymovielist.R;
import com.anima.mymovielist.model.ModelTMDB;
import com.anima.mymovielist.utils.MenuHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private List<MovieModelClass> movieList;
  private List<MovieModelClass> favoritesList;
  private RecyclerView recyclerView;
  private FilmesAdaptery filmesAdaptery;
  private ModelTMDB modelTMDB;
  private int currentPage = 1;
  private int totalPages = 5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    movieList = new ArrayList<>();
    favoritesList = new ArrayList<>();

    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    filmesAdaptery = new FilmesAdaptery(this, movieList, favoritesList);
    recyclerView.setAdapter(filmesAdaptery);

    modelTMDB = new ModelTMDB(movieList, filmesAdaptery);

    View paginationLayout = findViewById(R.id.layout_pagination);
    ImageButton btnPrevious = paginationLayout.findViewById(R.id.btnPrevious);
    ImageButton btnNext = paginationLayout.findViewById(R.id.btnNext);
    TextView tvPageNumber = paginationLayout.findViewById(R.id.tvPageNumber);

    updatePageNumber(tvPageNumber, currentPage, totalPages);

    btnPrevious.setOnClickListener(v -> {
      if (currentPage > 1) {
        currentPage--;
        updatePageNumber(tvPageNumber, currentPage, totalPages);
        movieList.clear();
        modelTMDB.updateJsonUrl(currentPage);
        modelTMDB.getData();
      }
    });

    btnNext.setOnClickListener(v -> {
      if (currentPage < totalPages) {
        currentPage++;
        updatePageNumber(tvPageNumber, currentPage, totalPages);
        movieList.clear();
        modelTMDB.updateJsonUrl(currentPage);
        modelTMDB.getData();
      }
    });

    modelTMDB.getData();
  }

  private void updatePageNumber(TextView tvPageNumber, int currentPage, int totalPages) {
    String pageNumberText = getString(R.string.page_number, currentPage, totalPages);
    tvPageNumber.setText(pageNumberText);
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
