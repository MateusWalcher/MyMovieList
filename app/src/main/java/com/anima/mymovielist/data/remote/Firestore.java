package com.anima.mymovielist.data.remote;

import com.anima.mymovielist.model.MovieModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Firestore {

  private static final String COLLECTION_USERS = "MobileUsers";
  private static final String COLLECTION_FAVORITES = "favorites";

  private FirebaseFirestore db;
  private FirebaseAuth mAuth;

  public Firestore() {
    db = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
  }

  public void addFavoriteMovie(MovieModelClass movie) {
    String userId = mAuth.getCurrentUser().getUid();

    Map<String, Object> movieData = new HashMap<>();
    movieData.put("id", movie.getId());
    movieData.put("name", movie.getName());
    movieData.put("img", movie.getImg());
    movieData.put("overview", movie.getOverview());

    DocumentReference userRef = db.collection(COLLECTION_USERS).document(userId);
    DocumentReference favoriteRef = userRef.collection(COLLECTION_FAVORITES).document(movie.getName());

    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            favoriteRef.set(movieData, SetOptions.merge())
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                  if (task.isSuccessful()) {
                    System.out.println("\n\n\n\ndeu certo\n\n\n\n\n");
                  } else {
                    System.out.println("\n\n\n\ndeu errado\n\n\n\n\n");
                  }
                }
              });
          } else {
            userRef.set(new HashMap<>())
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                  if (task.isSuccessful()) {
                    favoriteRef.set(movieData, SetOptions.merge())
                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                          if (task.isSuccessful()) {
                            System.out.println("\n\n\n\ndeu certo\n\n\n\n\n");
                          } else {
                            System.out.println("\n\n\n\ndeu errado\n\n\n\n\n");
                          }
                        }
                      });
                  } else {
                    System.out.println("\n\n\n\ndeu errado ao criar o documento do usuário\n\n\n\n\n");
                  }
                }
              });
          }
        } else {
          System.out.println("\n\n\n\ndeu errado ao buscar o documento do usuário\n\n\n\n\n");
        }
      }
    });
  }

  public void removeFavoriteMovie(MovieModelClass movie) {
    String userId = mAuth.getCurrentUser().getUid();
    DocumentReference favoriteRef = db.collection(COLLECTION_USERS)
      .document(userId)
      .collection(COLLECTION_FAVORITES)
      .document(movie.getName());

    favoriteRef.delete()
      .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(Task<Void> task) {
          if (task.isSuccessful()) {
            System.out.println("\n\n\n\ndeu certo\n\n\n\n\n");
          } else {
            System.out.println("\n\n\n\ndeu errado\n\n\n\n\n");
          }
        }
      });
  }

  public interface FavoriteMoviesCallback {
    void onFavoriteMovies(List<MovieModelClass> favoriteMovies);
    void onFailure(Exception e);
  }

  public void getFavoriteMovies(FavoriteMoviesCallback callback) {
    String userId = mAuth.getCurrentUser().getUid();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection(COLLECTION_USERS)
      .document(userId)
      .collection(COLLECTION_FAVORITES)
      .get()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          List<MovieModelClass> favoriteMovies = new ArrayList<>();
          for (DocumentSnapshot document : task.getResult()) {
            MovieModelClass movie = document.toObject(MovieModelClass.class);
            favoriteMovies.add(movie);
          }
          callback.onFavoriteMovies(favoriteMovies);
        } else {
          callback.onFailure(task.getException());
        }
      });
  }

}
