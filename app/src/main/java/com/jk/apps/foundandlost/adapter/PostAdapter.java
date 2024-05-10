package com.jk.apps.foundandlost.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jk.apps.foundandlost.databinding.ItemPostBinding;
import com.jk.apps.foundandlost.databinding.ItemSpaceBinding;
import com.jk.apps.foundandlost.model.PostModel;
import com.jk.apps.foundandlost.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<PostModel> models = new ArrayList<>();
    onItemClickListener listener;
    int TYPE_POST = 100, TYPE_SPACE = 101;

    public interface onItemClickListener {
        public void onItemClick(int pos, PostModel model);
    }

    public PostAdapter(Context context, List<PostModel> data, onItemClickListener listener) {
        this.context = context;
        this.models = data;
        this.listener = listener;
    }

    public void refresh(List<PostModel> data) {
        this.models = data;
        if (!this.models.isEmpty()) {
            this.models.add(null);
        }
        notifyDataSetChanged();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        ItemPostBinding binding;

        public PostHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class SpaceHolder extends RecyclerView.ViewHolder {

        ItemSpaceBinding binding;

        public SpaceHolder(@NonNull ItemSpaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (models.get(position) == null)
            return TYPE_SPACE;
        else return TYPE_POST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_POST)
            return new PostHolder(ItemPostBinding.inflate(LayoutInflater.from(context), parent, false));
        else
            return new SpaceHolder(ItemSpaceBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SpaceHolder) {

        } else if (holder instanceof PostHolder) {
            PostHolder holder1 = (PostHolder) holder;
            PostModel postModel = models.get(position);
            Log.e("TAG", postModel.postType + " HELL");
            String adType = postModel.postType == Constant.LOST_POST ? "Lost" : "Found";
            holder1.binding.txtName.setText(adType + " " + postModel.postName + " ...");
            holder1.itemView.setOnClickListener(v -> {
                listener.onItemClick(position, postModel);
            });
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
