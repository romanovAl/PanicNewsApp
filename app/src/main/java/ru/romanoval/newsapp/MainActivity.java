package ru.romanoval.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    private interface GetDataService{
        @GET("/everything")
        Call<ResponseItem> getResponseItem(@Query("q") String q, @Query("page") int pageNum);
    }

}

