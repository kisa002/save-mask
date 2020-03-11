package com.haeyum.savemask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private Activity act;

    public NearbyAdapter(Activity act, ArrayList<MaskStore> list) {
        this.act = act;
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

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");

            String cnt = "";
            switch (maskStores.get(position).getRemain_stat()) {
                case "plenty":
                    cnt = "100개 이상";
                    break;

                case "some":
                    cnt = "30개에서 100개 미만";
                    break;

                case "few":
                    cnt = "1개에서 30개 미만";
                    break;

                default:
                    cnt = "품절";
                    break;
            }

            String text;
            if(cnt.equals("품절")) {
                text = "세이브 마스크에서 안내드립니다\n\n현재 " + maskStores.get(position).getName() + "에서 판매중인 마스크는 모두 판매되었습니다...\n세이브 마스크를 통해 다른 판매처를 찾아보세요..!\n\n[세이브 마스크 앱 설치]\nhttps://play.google.com/store/apps/details?id=com.haeyum.savemask";
            } else {
                text = "세이브 마스크에서 안내드립니다!\n\n현재 " + maskStores.get(position).getName() + "에서 판매중인 마스크의 재고 수가 " + cnt + " 입니다!\n늦기 전에 방문하여 마스크를 구매하세요!\n\n마스크 입고 시간: " + maskStores.get(position).getStock_at() + "\n\n주소: " + maskStores.get(position).getAddr() + "\n\n길찾기: https://map.kakao.com/?q=" + maskStores.get(position).getAddr().replaceAll(" ", "+") + "\n\n[세이브 마스크 앱 설치]\nhttps://play.google.com/store/apps/details?id=com.haeyum.savemask";
            }
            intent.putExtra(Intent.EXTRA_TEXT, text);

            // Title of intent
            Intent chooser = Intent.createChooser(intent, "세이브 마스크 공유하기");
            act.startActivity(chooser);
        });
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
