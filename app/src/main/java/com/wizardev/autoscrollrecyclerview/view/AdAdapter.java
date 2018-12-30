package com.wizardev.autoscrollrecyclerview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wizardev.autoscrollrecyclerview.R;

import java.util.List;

/**
 * founter：符乃辉
 * time：2018/12/6
 * email:wizarddev@163.com
 * description:
 */
public class AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {
    private List<String> mDynamicAdsDetails;
    private Context mContext;
    private long between = 0;

    public AdAdapter(List<String> dynamicAdsDetails, Context context) {
        mDynamicAdsDetails = dynamicAdsDetails;
        mContext = context;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_ad, parent, false);
        return new AdViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdViewHolder holder,  int position) {

        if (mDynamicAdsDetails.size() != 0) {
            String media1 = mDynamicAdsDetails.get(position % mDynamicAdsDetails.size());
            holder.ivFlipperItem.setTag(media1);

            Picasso.get()
                    .load(media1)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(new com.squareup.picasso.Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (mDynamicAdsDetails.get(holder.getAdapterPosition() % mDynamicAdsDetails.size()).equals(holder.ivFlipperItem.getTag())) {
                                holder.ivFlipperItem.setScaleType(ImageView.ScaleType.FIT_XY);
                                holder.ivFlipperItem.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //控制点击频率
                if ((System.currentTimeMillis() - between) / 1000 < 1) {
                    return;
                }
                between = System.currentTimeMillis();
                Toast.makeText(mContext,"点击了第"+holder.getAdapterPosition() % mDynamicAdsDetails.size()+"个",Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public int getItemCount() {

        return Integer.MAX_VALUE;
    }

    class AdViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFlipperItem;

        private AdViewHolder(View itemView) {
            super(itemView);
            ivFlipperItem = itemView.findViewById(R.id.ivAdPic);
        }
    }

    @Override
    public void onViewRecycled(AdViewHolder holder) {
        Picasso.get().cancelRequest(holder.ivFlipperItem);
        super.onViewRecycled(holder);
    }
}
