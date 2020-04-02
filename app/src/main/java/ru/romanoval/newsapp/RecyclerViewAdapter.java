package ru.romanoval.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.romanoval.newsapp.databinding.NewsCardBinding;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded;

    private ArrayList<Article> list = new ArrayList<>();

    private RecyclerViewAdapterCallback mCallback;

    public RecyclerViewAdapter(Context context){
        this.mCallback = (RecyclerViewAdapterCallback) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType){

            case ITEM:

                NewsCardBinding binding = NewsCardBinding.inflate(layoutInflater,parent,false);

                viewHolder = new ItemViewHolder(binding);

                return viewHolder;

            case LOADING:

                View viewLoading = layoutInflater.inflate(R.layout.item_loading,parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);

                return viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == ITEM){

            Article curArticle = list.get(position);

            ((ItemViewHolder) holder).bind(curArticle);

        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == list.size() - 1 && isLoadingAdded)
            return LOADING;
        else
            return ITEM;
    }

    public void addAll(ArrayList<Article> list){

        for (Article article: list) {
            this.list.add(article);
            notifyItemChanged(list.size() - 1);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        list.add(new Article());
        notifyItemInserted(list.size() - 1);
    }

    public void removeLoadingFooter() {
        if(!isLoadingAdded)
            return;

        isLoadingAdded = false;

        int position = list.size() - 1;
        Article article = list.get(position);

        if (article != null) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll(){
        removeLoadingFooter();
        list.clear();
        notifyDataSetChanged();
    }

    public boolean isLoadingAdded() {
        return isLoadingAdded;
    }

    public void setLoadingAdded(boolean isLoadingAdded) {
        this.isLoadingAdded = isLoadingAdded;
    }

    public ArrayList<Article> getList(){
        return list;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final NewsCardBinding binding;

        ItemViewHolder(NewsCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Article article) {
            binding.setArticle(article);
            binding.constraint.setOnClickListener(v -> mCallback.openNews(article.getUrl()));

//           ConstraintLayout constraintLayout = binding.constraint;
           binding.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            binding.executePendingBindings();
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder{

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface RecyclerViewAdapterCallback {
        void openNews(String url);
        int[] getScreenWidth();
    }

}
