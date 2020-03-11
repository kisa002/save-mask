package com.haeyum.savemask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haeyum.savemask.APIs.Models.MaskInfo.MaskStore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {
    private ArrayList<MaskStore> maskStores = null;

    public NearbyAdapter(ArrayList<MaskStore> list) {
        maskStores = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_nearby, parent, false) ;
        ViewHolder vh = new NearbyAdapter.ViewHolder(view) ;

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(maskStores.get(position).getName());
        holder.tvAddr.setText(maskStores.get(position).getAddr());
        holder.tvTime.setText(maskStores.get(position).getStock_at());

        int stat = -1;
        switch (maskStores.get(position).getRemain_stat()) {
            case "plenty":
                stat = R.drawable.plenty;
                break;

            case "some":
                stat = R.drawable.some;
                break;

            case "few":
                stat = R.drawable.few;
                break;

            case "empty":
                stat = R.drawable.empty;
                break;
        }
        holder.ivStat.setImageResource(stat);
    }

    @Override
    public int getItemCount() {
        return maskStores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvAddr, tvTime;
        private ImageView ivStat;

        ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_itemNearby_name);
            tvAddr = itemView.findViewById(R.id.tv_itemNearby_addr);
            tvTime = itemView.findViewById(R.id.tv_itemNearby_time);

            ivStat = itemView.findViewById(R.id.iv_itemNearby_stat);
        }
    }
}
