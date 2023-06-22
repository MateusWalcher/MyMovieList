package com.anima.mymovielist.model;

import android.content.Context;
import android.os.AsyncTask;

import com.anima.mymovielist.R;
import com.anima.mymovielist.ui.adapters.FilmesAdaptery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModelTMDB {
  private String JSON_URL = "";

  private String jsonUrl;
  private List<MovieModelClass> movieList;
  private FilmesAdaptery filmesAdaptery;
  private Context context;

  public ModelTMDB(Context context, List<MovieModelClass> movieList, FilmesAdaptery filmesAdaptery) {

    this.JSON_URL = context.getString(R.string.now_playing_completo);

    this.context  = context;
    this.movieList = movieList;
    this.filmesAdaptery = filmesAdaptery;
    this.jsonUrl = JSON_URL;
  }

  public void setJsonUrl(String jsonUrl) {
    this.jsonUrl = jsonUrl;
  }

  public void getData() {
    GetData getData = new GetData();
    getData.execute();
  }

  private class GetData extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
      String current = "";
      try {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
          .url(jsonUrl)
          .addHeader("accept", "application/json")
          .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5MWNlZmRhZmExNjk5MDY0N2VkMTc3NDM0NzYxNmU1NyIsInN1YiI6IjY0N2NhZTVlMTc0OTczMDBhODE5ZWZjZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ba96m-zhQgDAg9YsOHcjVOc17PIDP0e5LyoDS7FmTg0")
          .build();

        Response response = client.newCall(request).execute();
        current = response.body().string();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return current;
    }

    @Override
    protected void onPostExecute(String s) {
      try {
        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        movieList.clear();

        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject jsonObject1 = jsonArray.getJSONObject(i);

          MovieModelClass model = new MovieModelClass();
          model.setId(jsonObject1.getString("vote_average"));
          model.setName(jsonObject1.getString("title"));
          model.setImg(jsonObject1.getString("poster_path"));
          model.setOverview(jsonObject1.getString("overview"));

          movieList.add(model);
        }

        filmesAdaptery.notifyDataSetChanged();
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void clearMovieList() {
    movieList.clear();
    filmesAdaptery.notifyDataSetChanged();
  }

  public void updateJsonUrl(int page) {
    String jsonUrl = this.jsonUrl;

    if (jsonUrl.contains("now_playing")) {
      jsonUrl = context.getString(R.string.now_playing_sem_pagina) + page;
    } else if (jsonUrl.contains("popular")) {
      jsonUrl = context.getString(R.string.popular_sem_pagina) + page;
    } else if (jsonUrl.contains("top_rated")) {
      jsonUrl = context.getString(R.string.top_rated_sem_pagina) + page;
    }
    setJsonUrl(jsonUrl);
  }

  public void setJsonUrlNowPlaying(){
    String jsonUrl = context.getString(R.string.now_playing_completo);
    setJsonUrl(jsonUrl);
  }

  public void setJsonUrlPopular(){
    String jsonUrl = context.getString(R.string.popular_completo);
    setJsonUrl(jsonUrl);
  }
  public void setJsonUrlTopRated(){
    String jsonUrl = context.getString(R.string.top_rated_completo);
    setJsonUrl(jsonUrl);
  }



}
