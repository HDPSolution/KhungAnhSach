package com.hdpsolution.khunganhtrangsach.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hdpsolution.khunganhtrangsach.Model.Image;
import com.hdpsolution.khunganhtrangsach.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/17/2018.
 */

public class ListHistoryAdapter extends RecyclerView.Adapter<ListHistoryAdapter.ViewHolder>{

    private ArrayList<Image> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;
    private Context context;


    public ListHistoryAdapter(ArrayList<Image> list,Context context) {
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            Glide.with(context).load(Uri.parse(list.get(position).getUri()))
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageload);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());

        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) mLongClickListener.onItemLongClick(v, getAdapterPosition());
            return true;
        }
    }

    Image getItem(int id) {
        return list.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public void setLongClickListener(ItemLongClickListener itemlongClickListener) {
        this.mLongClickListener = itemlongClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

}
