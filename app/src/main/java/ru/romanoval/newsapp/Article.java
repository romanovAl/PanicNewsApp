package ru.romanoval.newsapp;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

public class Article {

    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("urlToImage")
    @Expose
    private String urlToImage;
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @SerializedName("content")
    @Expose
    private String content;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return getRandomEndAndStart(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String getRandomEndAndStart(String conc){

        String[] ends = {
                ". И всё, пиздец!",
                ". Так сказал Путин, блять!",
                ". Что будет дальше, хуй его знает…",
                ". В Кремле уже решают этот вопрос.",
                ". Пу взял вопрос на карандашик.",
                ". А Ванга говорила, блять...   "
        };

        String[] starts = {
                "Началось... ",
                "Сбылось древнее предсказание Ванги: ",
                "Новость прямо из преисподни: ",
                "Путин был в ярости, когда узнал: ",
                "Путин проболтался: ",
                "Ученые оцепенели, когда узнали... ",

        };

        double endOrStar = Math.random();

        if(endOrStar <= 0.5){
            return starts[(int)(Math.random()) * starts.length] + conc;
        }else{
            return conc + ends[(int)(Math.random() * ends.length)];
        }

    }


}
