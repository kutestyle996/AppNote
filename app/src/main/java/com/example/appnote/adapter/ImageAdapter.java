package com.example.appnote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.appnote.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    public LayoutInflater layoutInflater;
    private List<String> mUrls;
    private ItemImageClickListener mItemImageClickListener;

    public ImageAdapter(Context context, ItemImageClickListener itemImageClickListener) {
        mItemImageClickListener = itemImageClickListener;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    @NonNull
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        View view = layoutInflater.inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.setItemImageClickListener(mItemImageClickListener);
        holder.binData(mUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return mUrls != null ? mUrls.size() : 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder
            implements  View.OnClickListener {
        private ImageView mItemImage;
        private ImageView mButtonRemove;
        private ItemImageClickListener mItemImageClickListener;
        private String mUrl;

        public ImageViewHolder(View view) {
            super(view);
            initViews(view);
        }

        public void setItemImageClickListener(ItemImageClickListener itemImageClickListener) {
            mItemImageClickListener = itemImageClickListener;
        }

        private void initViews(View view) {
            mItemImage = view.findViewById(R.id.itemImage);
            mButtonRemove = view.findViewById(R.id.buttonRemove);
            mItemImage.setOnClickListener(this);
            mButtonRemove.setOnClickListener(this);

        }

        private void binData(String url) {
            mUrl = url;
            Picasso.get().load(mUrl).into(mItemImage);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.itemImage) {
                mItemImageClickListener.onClick(mUrl);
            } else if (v.getId() == R.id.buttonRemove) {
                mItemImageClickListener.onRemove(getAdapterPosition());
            }
        }

    }

    public void setData(List<String> urls) {
        mUrls = urls;
    }


    public interface ItemImageClickListener {
        void onRemove(int position);

        void onClick(String url);
    }

}
