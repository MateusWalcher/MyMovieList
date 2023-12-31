package com.anima.mymovielist.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anima.mymovielist.ui.adapters.FilmesAdaptery;
import com.anima.mymovielist.model.MovieModelClass;
import com.anima.mymovielist.R;
import com.anima.mymovielist.model.ModelTMDB;
import com.anima.mymovielist.utils.ChangeLanguage;
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

    modelTMDB = new ModelTMDB(MainActivity.this,movieList, filmesAdaptery);

    View paginationLayout = findViewById(R.id.layout_pagination);
    ImageButton btnPrevious = paginationLayout.findViewById(R.id.btnPrevious);
    ImageButton btnNext = paginationLayout.findViewById(R.id.btnNext);
    TextView tvPageNumber = paginationLayout.findViewById(R.id.tvPageNumber);
    ImageButton btnCategory = paginationLayout.findViewById(R.id.btncategory);
    ImageButton btnLanguage = paginationLayout.findViewById(R.id.btnLanguage);

    View optionsLayout = findViewById(R.id.layout_options);
    Button btnCartaz = optionsLayout.findViewById(R.id.btnCartaz);
    Button btnPopular = optionsLayout.findViewById(R.id.btnPopular);
    Button btnVotado = optionsLayout.findViewById(R.id.btnVotado);

    ChangeLanguage.changeFlag(MainActivity.this, btnLanguage);

    btnCategory.setOnClickListener(v -> {
        if (optionsLayout.getVisibility() == View.VISIBLE){
          optionsLayout.setVisibility(View.GONE);
        }else{
          optionsLayout.setVisibility(View.VISIBLE);
        }
      });

    btnCartaz.setOnClickListener(v ->{
      movieList.clear();
      filmesAdaptery.notifyDataSetChanged();
      currentPage = 1;
      updatePageNumber(tvPageNumber, currentPage, totalPages);
      modelTMDB.setJsonUrlNowPlaying();
      modelTMDB.getData();
    });

    btnPopular.setOnClickListener(v -> {
      movieList.clear();
      filmesAdaptery.notifyDataSetChanged();
      currentPage = 1;
      updatePageNumber(tvPageNumber, currentPage, totalPages);
      modelTMDB.setJsonUrlPopular();
      modelTMDB.getData();
    });

    btnVotado.setOnClickListener(v -> {
      movieList.clear();
      filmesAdaptery.notifyDataSetChanged();
      currentPage = 1;
      updatePageNumber(tvPageNumber, currentPage, totalPages);
      modelTMDB.setJsonUrlTopRated();
      modelTMDB.getData();
    });

    btnLanguage.setOnClickListener(v ->{
      ChangeLanguage.changeLanguage(MainActivity.this);
      recreate();
    });



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
