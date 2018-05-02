package com.hdpsolution.khunganhtrangsach.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.hdpsolution.khunganhtrangsach.R;
import com.hdpsolution.khunganhtrangsach.hdp_utils.FirebaseStorageUtils;

import java.util.List;

/**
 * Created by Administrator on 3/17/2018.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    private List<String> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;


    public ListAdapter(List<String> list,Context context) {
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

        FirebaseStorageUtils.displayImageFromFirebaseStoragenoLoad(context,holder.imageView,list.get(position),R.drawable.ic_load);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageload);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());

        }
    }

    String getItem(int id) {
        return list.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
