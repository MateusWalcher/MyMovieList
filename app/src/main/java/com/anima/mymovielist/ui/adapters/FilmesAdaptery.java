package com.anima.mymovielist.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.anima.mymovielist.R;
import com.anima.mymovielist.model.MovieModelClass;
import com.anima.mymovielist.data.remote.Firestore;
import com.bumptech.glide.Glide;

import java.util.List;

public class FilmesAdaptery extends RecyclerView.Adapter<FilmesAdaptery.MyViewHolder> {

  private Context mContext;
  private List<MovieModelClass> mData;
  private List<MovieModelClass> favoritesList;
  private Firestore firestore;

  public FilmesAdaptery(Context mContext, List<MovieModelClass> mData, List<MovieModelClass> favoritesList) {
    this.mContext = mContext;
    this.mData = mData;
    this.favoritesList = favoritesList;
    this.firestore = new Firestore();
    getFavoriteMovies();
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v;
    LayoutInflater inflater = LayoutInflater.from(mContext);
    v = inflater.inflate(R.layout.movie_item, parent, false);

    return new MyViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    MovieModelClass movie = mData.get(position);

    holder.id.setText(movie.getId());
    holder.name.setText(movie.getName());

    // https://image.tmdb.org/t/p/w500
    Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + movie.getImg()).into(holder.img);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showMovieDescriptionDialog(movie);
      }
    });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  private void showMovieDescriptionDialog(MovieModelClass movie) {
    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    builder.setTitle(movie.getName());
    builder.setMessage(movie.getOverview());
    builder.setPositiveButton("Fechar", null);
    builder.setNegativeButton(getFavoriteButtonText(movie), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (favoritesList.contains(movie)) {
          removeFavoriteMovie(movie);
        } else {
          addFavoriteMovie(movie);
        }
      }
    });

    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private String getFavoriteButtonText(MovieModelClass movie) {
    if (favoritesList.contains(movie)) {
      return "Favorito";
    } else {
      return "Favoritar";
    }
  }

  private void addFavoriteMovie(MovieModelClass movie) {
    firestore.addFavoriteMovie(movie);
    favoritesList.add(movie);
    Toast.makeText(mContext, "Filme adicionado aos favoritos", Toast.LENGTH_SHORT).show();
  }

  private void removeFavoriteMovie(MovieModelClass movie) {
    firestore.removeFavoriteMovie(movie);
    favoritesList.remove(movie);
    Toast.makeText(mContext, "Filme removido dos favoritos", Toast.LENGTH_SHORT).show();
  }

  private void getFavoriteMovies() {
    firestore.getFavoriteMovies(new Firestore.FavoriteMoviesCallback() {
      @Override
      public void onFavoriteMovies(List<MovieModelClass> favoriteMovies) {
        favoritesList.clear();
        favoritesList.addAll(favoriteMovies);
        notifyDataSetChanged();
      }

      @Override
      public void onFailure(Exception e) {
        Toast.makeText(mContext, "Erro ao obter filmes favoritos", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView id;
    TextView name;
    ImageView img;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      id = itemView.findViewById(R.id.id_txt);
      name = itemView.findViewById(R.id.name_txt);
      img = itemView.findViewById(R.id.imageView);
    }
  }
}
