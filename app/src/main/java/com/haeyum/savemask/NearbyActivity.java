package com.haeyum.savemask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haeyum.savemask.APIs.Models.MaskInfo.MaskStore;
import com.haeyum.savemask.APIs.Models.MaskInfo.MaskStores;
import com.haeyum.savemask.APIs.NetClient;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;

import static com.haeyum.savemask.APIs.NetClient.MASK_BASE_URL;

public class NearbyActivity extends AppCompatActivity {
    private final String TAG = "[ Nearby Act ]";

    private RecyclerView rvNearby;
    private ArrayList<MaskStore> maskStores = new ArrayList<>();

    private TextView tvTitle, tvContext;
    private ConstraintLayout clFail;
    private ImageView ivFail;
    private View vFail;

    private Button btnMore;

    private NearbyAdapter nearbyAdapter;

    private LatLng latLng;
    private int m = 500;

    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        initUI();
        initData();
    }

    private void initUI () {
        rvNearby = findViewById(R.id.rv_nearby);
        rvNearby.setLayoutManager(new LinearLayoutManager(this));

        tvTitle = findViewById(R.id.tv_nearby_title);
        tvContext = findViewById(R.id.tv_nearby_context);

        clFail = findViewById(R.id.cl_nearby_fail);
        ivFail = findViewById(R.id.iv_nearby_fail);
        vFail = findViewById(R.id.v_nearby_shadow);

        btnMore = findViewById(R.id.btn_nearby_more);
        btnMore.setText("1km 거리로 찾아보기");
        tvTitle.setText("현재위치 500m 반경");
    }

    private void initData() {
        act = this;

        latLng = new LatLng(getIntent().getDoubleExtra("lat", -1), getIntent().getDoubleExtra("lng", -1));
        getMaskStores(latLng);
    }

    private void getMaskStores(LatLng latLng) {
        String query = MASK_BASE_URL + "storesByGeo/json?lat=" + latLng.latitude + "&lng=" + latLng.longitude + "&m=" + m;
        Call<MaskStores> res = NetClient.NetClientNaver().getMaskStores(query);

        res.enqueue(new Callback<MaskStores>() {
            @Override
            public void onResponse(Call<MaskStores> call, Response<MaskStores> response) {
                if(response.body() == null) {
                    Toast.makeText(getApplicationContext(), "서버 연결에 실패하였습니다... 관리자에게 문의바랍니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(MaskStore maskStore : response.body().getStores()) {
                    if(maskStore.getRemain_stat() == null)
                        continue;

                    if(maskStore.getRemain_stat().equals("empty"))
                        continue;

                    maskStores.add(maskStore);
                }

                if(maskStores.size() == 0) {
                    tvContext.setText("마스크가 남아있는 약국이 없네요.");

                    clFail.setVisibility(View.VISIBLE);

                    ivFail.post(()->{
                        ivFail.animate().rotation(-40).setDuration(300).withEndAction(() -> {
                            ivFail.animate().rotation(40).setDuration(300).withEndAction(() -> {
                                ivFail.animate().rotation(0).setDuration(300).withStartAction(() -> {
                                    ivFail.animate().rotation(-20).setDuration(200).withEndAction(() -> {
                                        ivFail.animate().rotation(20).setDuration(200).withEndAction(() -> {
                                            ivFail.animate().rotation(0).setDuration(300);
                                        });
                                    });
                                });
                            });
                        });
                    });

                    clFail.post(() -> {
                        vFail.setVisibility(View.GONE);
                        clFail.animate().translationYBy(clFail.getHeight() / 20).setDuration(400).withEndAction(() -> {
                            clFail.animate().translationYBy(-clFail.getHeight() / 20).setDuration(400).withEndAction(() -> {
                                clFail.animate().translationYBy(clFail.getHeight() / 30).setDuration(250).withEndAction(() -> {
                                    clFail.animate().translationYBy(-clFail.getHeight() / 30).setDuration(250).withEndAction(() -> {
                                        vFail.setVisibility(View.VISIBLE);
                                    });
                                });
                            });
                        });
                    });
                }
                else {
                    clFail.setVisibility(View.GONE);
                    tvContext.setText("마스크 남아있는 약국을\n모았습니다.");

                    nearbyAdapter = null;
                    nearbyAdapter = new NearbyAdapter(act, maskStores);

                    rvNearby.setAdapter(nearbyAdapter);
                }
            }

            @Override
            public void onFailure(Call<MaskStores> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(getApplicationContext(), "알 수 없는 오류가 발생하였습니다ㅠㅠ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_nearby_back:
                finish();
                break;

            case R.id.btn_nearby_more:
                if(m == 500) {
                    m = 1000;
                    tvTitle.setText("현재위치 1km 반경");
                    btnMore.setText("3km 거리로 찾아보기");
                }
                else if(m == 1000) {
                    m = 3000;
                    tvTitle.setText("현재위치 3km 반경");
                    btnMore.setText("최대 거리 입니다.");
                    btnMore.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#878787")));
                    btnMore.setEnabled(false);
                }

                getMaskStores(latLng);
                break;
        }
    }
}
