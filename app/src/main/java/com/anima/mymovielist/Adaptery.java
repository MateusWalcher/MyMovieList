package com.anima.mymovielist;

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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {

    private Context mContext;
    private List<MovieModelClass> mData;
    private List<MovieModelClass> favoritesList;

    public Adaptery(Context mContext, List<MovieModelClass> mData, List<MovieModelClass> favoritesList) {
        this.mContext = mContext;
        this.mData = mData;
        this.favoritesList = favoritesList;
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
        // Adicionar o filme à lista de favoritos local
        favoritesList.add(movie);

        // Obter a referência do documento do usuário logado no Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = db.collection("users").document(userId);

        // Atualizar a matriz favoriteMovies no documento do usuário
        userRef.update("favoriteMovies", FieldValue.arrayUnion(movie));

        // Exibir uma mensagem para o usuário
        Toast.makeText(mContext, "Filme adicionado aos favoritos", Toast.LENGTH_SHORT).show();
    }

    private void removeFavoriteMovie(MovieModelClass movie) {
        favoritesList.remove(movie);
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
