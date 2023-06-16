package com.anima.mymovielist;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.anima.mymovielist.Adaptery;
import com.anima.mymovielist.MovieModelClass;
import com.anima.mymovielist.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;


public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL = "https://api.themoviedb.org/3/movie/top_rated?language=pt-BR&page=1";

    private List<MovieModelClass> movieList;
    private List<MovieModelClass> favoritesList;
    private RecyclerView recyclerView;
    private Adaptery adaptery;

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

        adaptery = new Adaptery(this, movieList, favoritesList);
        recyclerView.setAdapter(adaptery);

        GetData getData = new GetData();
        getData.execute();

        // Verificar se o usuário está logado
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // Buscar os filmes favoritos do usuário no Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = auth.getCurrentUser().getUid();
            DocumentReference userRef = db.collection("users").document(userId);

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        List<HashMap<String, Object>> favoriteMovies = (List<HashMap<String, Object>>) documentSnapshot.get("favoriteMovies");
                        if (favoriteMovies != null) {
                            favoritesList.clear(); // Limpar a lista antes de adicionar os filmes favoritos
                            for (HashMap<String, Object> movieData : favoriteMovies) {
                                // Mapear os dados do filme para a classe MovieModelClass
                                MovieModelClass movie = new MovieModelClass();
                                movie.setId((String) movieData.get("id"));
                                movie.setName((String) movieData.get("name"));
                                movie.setImg((String) movieData.get("img"));
                                movie.setOverview((String) movieData.get("overview"));

                                favoritesList.add(movie);
                            }
                            adaptery.notifyDataSetChanged();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Tratar falha na obtenção dos filmes favoritos
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_expand) {
            return true; // Lidar com o clique no botão de expansão
        } else if (id == R.id.action_home) {
            // Lidar com o clique no item "Início"
            Toast.makeText(this, "Clicou em Início", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_favorites) {
            // Lidar com o clique no item "Favoritos"
            showFavoritesList();
            return true;
        } else if (id == R.id.action_logoff) {
            // Lidar com o clique no item "Logoff"
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFavoritesList() {
        // Passar a lista de favoritos para a ActivityFavoritos
        Intent intent = new Intent(MainActivity.this, ActivityFavoritos.class);
        intent.putParcelableArrayListExtra("favoritesList", new ArrayList<>(favoritesList));
        startActivity(intent);
    }



    private class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(JSON_URL)
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

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MovieModelClass model = new MovieModelClass();
                    model.setId(jsonObject1.getString("vote_average"));
                    model.setName(jsonObject1.getString("title"));
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setOverview(jsonObject1.getString("overview"));

                    movieList.add(model);
                }

                adaptery.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

