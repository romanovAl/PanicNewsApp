package ru.romanoval.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

    private int currentPageNumber = 1;

    private int totalPageCount;

    private int currentPageCount;

    private static int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        width = size.x;
        height = size.y;

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
        Call<ResponseItem> call = service.getResponseItem("Путин",1,"publishedAt","fc0602937a6748c7af5987eaf7dfd530");

        call.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                progressBar.setVisibility(View.INVISIBLE);

                ArrayList<Article> articles = response.body().getArticles();

                totalPageCount = response.body().getTotalResults();

                currentPageCount = articles.size();

                adapter.addAll(articles);

                if(totalPageCount != currentPageCount){
                    currentPageNumber++;
                    adapter.addLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<ResponseItem> call, Throwable t) {
                isLoading = false;
                System.err.println("Я сегодня не могу " + t.getMessage());
            }
        });
    }

    private void loadNextPage(){

        Call<ResponseItem> call = service.getResponseItem("Путин",currentPageNumber,"publishedAt","fc0602937a6748c7af5987eaf7dfd530");

        call.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {

                ArrayList<Article> articles = response.body().getArticles();

                adapter.removeLoadingFooter();

                currentPageCount += articles.size();
                adapter.addAll(articles);

                if(totalPageCount > currentPageCount){
                    adapter.addLoadingFooter();
                    currentPageNumber++;
                }

                isLoading = false;
            }

            @Override
            public void onFailure(Call<ResponseItem> call, Throwable t) {
                isLoading = false;
                System.err.println("Я сегодня не могу " + t.getMessage());
            }
        });
    }

    @Override
    public void openNews(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private interface GetDataService{
        @GET("everything")
        Call<ResponseItem> getResponseItem(@Query("q") String q,
                                           @Query("page") int page,
                                           @Query("sortBy") String sortBy,
                                           @Query("apiKey") String apiKey);
    }

    @BindingAdapter({"url"})
    public static void loadImage(ImageView view, String url) {
        Picasso
                .get()
                .load(url)
                .resize(width, view.getHeight())
                .centerCrop()
                .into(view);
    }
}