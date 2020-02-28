package com.example.technote.Network.RetrofitExample;

import java.util.ArrayList;

public class MovieListVO {

    private String category;
    private ArrayList<Movie> list;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Movie> getList() {
        return list;
    }

    public void setList(ArrayList<Movie> list) {
        this.list = list;
    }

    public class Movie {
        private String movie_no;
        private String title;
        private String thumb_url;
        public String getMovie_no() {
            return movie_no;
        }

        public void setMovie_no(String movie_no) {
            this.movie_no = movie_no;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getThumb_url() {
            return thumb_url;
        }
        public void setThumb_url(String thumb_url) {
            this.thumb_url = thumb_url;
        }
    }
}
