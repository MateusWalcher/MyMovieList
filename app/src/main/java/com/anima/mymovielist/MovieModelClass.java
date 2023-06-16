package com.anima.mymovielist;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModelClass implements Parcelable {

    String id;
    String name;
    String img;

    String overview;



    public MovieModelClass(String id, String name, String img, String over) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.overview = over;
    }

    public MovieModelClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    protected MovieModelClass(Parcel in) {
        id = in.readString();
        name = in.readString();
        img = in.readString();
        overview = in.readString();
    }

    public static final Creator<MovieModelClass> CREATOR = new Creator<MovieModelClass>() {
        @Override
        public MovieModelClass createFromParcel(Parcel in) {
            return new MovieModelClass(in);
        }

        @Override
        public MovieModelClass[] newArray(int size) {
            return new MovieModelClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(img);
        dest.writeString(overview);
    }
}
