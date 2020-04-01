package ru.romanoval.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterCallback {

    private RecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;

    private GetDataService service;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int currentPageNumber = 1, currentCount, currentTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = RetrofitInstance.getRetrofitInstance().create(GetDataService.class);

        adapter = new RecyclerViewAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        progressBar = findViewById(R.id.progressBar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;

                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();
    }

    private void loadFirstPage(){
        Call<ResponseItem> call = service.getResponseItem("Путин",1,"fc0602937a6748c7af5987eaf7dfd530");

        call.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                progressBar.setVisibility(View.INVISIBLE);

                System.err.println("Что-то получили");

                ArrayList<Article> articles = response.body().getArticles();

                adapter.addAll(articles);
                adapter.addLoadingFooter();
            }

            @Override
            public void onFailure(Call<ResponseItem> call, Throwable t) {
                isLoading = false;
                System.err.println("Я сегодня не могу " + t.getMessage());

            }
        });
    }

    private void loadNextPage(){

    }

    @Override
    public void openFullUserInformation(String login) {

    }

    private interface GetDataService{
        @GET("everything")
        Call<ResponseItem> getResponseItem(@Query("q") String q, @Query("page") int page, @Query("apiKey") String apiKey);
    }
}