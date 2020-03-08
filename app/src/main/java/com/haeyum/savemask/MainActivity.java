package com.haeyum.savemask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.haeyum.savemask.APIs.Models.MaskInfo.MaskStore;
import com.haeyum.savemask.APIs.Models.MaskInfo.MaskStores;
import com.haeyum.savemask.APIs.NetClient;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;

import static com.haeyum.savemask.APIs.NetClient.MASK_BASE_URL;
import static com.haeyum.savemask.NoticeManager.createNotice;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NaverMap.OnLocationChangeListener, NaverMap.OnCameraChangeListener, NaverMap.OnCameraIdleListener {
    private final String TAG = "MainActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    // UI
    private ConstraintLayout clStore;

    private TextView tvStoreName, tvStoreAddr;
    private TextView tvStoreStock, tvStoreSale, tvStoreTime;

    // MAP
    private NaverMap naverMap;
    private FusedLocationSource locationSource;

    // Marker
    private Marker currentMarker;
    private ArrayList<InfoWindow> infoWindows = new ArrayList<>();
//    private ArrayList<Marker> markers = new ArrayList<>();

    private boolean isLoadedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initMap();
    }

    private void initUI() {
        clStore = findViewById(R.id.cl_main_store);
        clStore.setAlpha(0);

        tvStoreName = findViewById(R.id.tv_main_storeName);
        tvStoreAddr = findViewById(R.id.tv_main_storeAddr);

        tvStoreStock = findViewById(R.id.tv_main_storeStock);
        tvStoreSale = findViewById(R.id.tv_main_storeSale);
        tvStoreTime = findViewById(R.id.tv_main_storeTime);

        new Handler().postDelayed(() -> {
            clStore.setAlpha(1);
            clStore.animate().translationYBy(clStore.getHeight()).start();
        }, 100);
    }

    private void getMaskStores(LatLng latLng) {
//        if(1==1) {
//            return;
//        }

        String query = MASK_BASE_URL + "storesByGeo/json?lat=" + latLng.latitude + "&lng=" + latLng.longitude + "&m=5000";
        Call<MaskStores> res = NetClient.NetClientNaver().getMaskStores(query);

        res.enqueue(new Callback<MaskStores>() {
            @Override
            public void onResponse(Call<MaskStores> call, Response<MaskStores> response) {
                Toast.makeText(MainActivity.this, "getMaskStores", Toast.LENGTH_SHORT).show();

                if(response.body() == null) {
                    Toast.makeText(getApplicationContext(), "서버 연결에 실패하였습니다... 관리자에게 문의바랍니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(InfoWindow iw : infoWindows) {
                    iw.close();
                }

//                markers.clear();
                infoWindows.clear();

                int cnt = 1;

                for(MaskStore maskStore : response.body().getStores()) {
                    LatLng pos = new LatLng(maskStore.getLat(), maskStore.getLng());

//                    Marker marker = new Marker();
//                    marker.setPosition(pos);
//                    marker.setMap(naverMap);

//                    cnt++;
//                    String count = cnt < 10 ? "00" + cnt : (cnt < 100) ? "0" + cnt : cnt + "";
                    cnt = maskStore.getRemain_cnt();
                    String count = cnt < 10 ? "00" + cnt : (cnt < 100) ? "0" + cnt : cnt + "";

                    InfoWindow.Adapter adapter;
                    adapter = new InfoWindow.Adapter() {
                        @NonNull
                        @Override
                        public OverlayImage getImage(@NonNull InfoWindow infoWindow) {
                            return OverlayImage.fromAsset("00" + count + ".png");
                        }
                    };

                    InfoWindow infoWindow = new InfoWindow();
                    infoWindow.setAdapter(adapter);
                    infoWindow.setPosition(pos);
                    infoWindow.setMap(naverMap);

                    infoWindow.setOnClickListener(overlay -> {
                        tvStoreName.setText(maskStore.getName());
                        tvStoreAddr.setText(maskStore.getAddr());

                        tvStoreStock.setText(maskStore.getStock_cnt());
                        tvStoreSale.setText(maskStore.getSold_cnt());
                        tvStoreTime.setText(maskStore.getStock_t());
                        
                        showStore();
                        return false;
                    });

//                    markers.add(marker);
                    infoWindows.add(infoWindow);
                }
            }

            @Override
            public void onFailure(Call<MaskStores> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(getApplicationContext(), "알 수 없는 오류가 발생하였습니다ㅠㅠ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMap() {
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void showStore() {
        clStore.animate().translationYBy(-clStore.getHeight()).setDuration(300).start();
    }

    private void hideStore() {
        clStore.animate().translationYBy(clStore.getHeight()).setDuration(300);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        currentMarker = new Marker();
        currentMarker.setIcon(MarkerIcons.BLACK);
        currentMarker.setIconTintColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        currentMarker.setPosition(naverMap.getCameraPosition().target);
        currentMarker.setWidth(70);
        currentMarker.setHeight(90);
        currentMarker.setMap(naverMap);

        naverMap.addOnLocationChangeListener(this);
        naverMap.addOnCameraChangeListener(this);
        naverMap.addOnCameraIdleListener(this);
    }

    @Override
    public void onLocationChange(@NonNull Location location) {
        if(!isLoadedMap) {
            isLoadedMap = true;
            getMaskStores(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    public void onCameraChange(int i, boolean b) {
        currentMarker.setPosition(naverMap.getCameraPosition().target);
    }

    @Override
    public void onCameraIdle() {
//        Toast.makeText(this, "onCameraIdle", Toast.LENGTH_SHORT).show();
//        getMaskStores(naverMap.getCameraPosition().target);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_search:
//                createNotice(this, "알려드립니다", "안녕하세요.\n저는 덜렁덜렁 빛나는 유남이입니다.\n유남이 너무 멋있어 너무 상스러워\n내가 소유해도 될까..? \n그럼 왜 안되겠어");
                getMaskStores(currentMarker.getPosition());
                break;

            case R.id.btn_main_currentLocation:
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(locationSource.getLastLocation().getLatitude(), locationSource.getLastLocation().getLongitude())).animate(CameraAnimation.Linear); //new LatLng(37.5666102, 126.9783881)
                naverMap.moveCamera(cameraUpdate);

                showStore();
                break;

            case R.id.btn_main_storeClose:
                hideStore();
                break;
        }
    }

    private long backTimer = SystemClock.uptimeMillis() - 2000;

    @Override
    public void onBackPressed() {
        long result = SystemClock.uptimeMillis() - backTimer;

        if(result > 2000)
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        else {
            finish();
        }
        backTimer = SystemClock.uptimeMillis();
    }

    private void trash() {
        //                    adapter = new InfoWindow.Adapter() {
//                        @NonNull
//                        @Override
//                        public OverlayImage getImage(@NonNull InfoWindow infoWindow) {
//                            return OverlayImage.fromResource(R.drawable.splash_logo);
//                        }
//                    };
//
//                    adapter = new InfoWindow.ViewAdapter() {
//                        @NonNull
//                        @Override
//                        public View getView(@NonNull InfoWindow infoWindow) {
//                            View view = findViewById(R.id.test);
//
//                            return view;
//                        }
//                    };
//
//                    adapter = new InfoWindow.ViewAdapter() {
//                        @NonNull
//                        @Override
//                        public View getView(@NonNull InfoWindow infoWindow) {
//                            return null;
//                        }
//                    };

//                    adapter = new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
//                        @NonNull
//                        @Override
//                        public CharSequence getText(@NonNull InfoWindow infoWindow) {
//                            return maskStore.getRemain_cnt() + "";
//                        }
//                    };

//                    infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
//                        @NonNull
//                        @Override
//                        public CharSequence getText(@NonNull InfoWindow infoWindow) {
//                            return maskStore.getName();
//                        }
//                    });
    }
}
