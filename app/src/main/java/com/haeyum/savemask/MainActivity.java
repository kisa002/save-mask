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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Calendar;

import static com.haeyum.savemask.APIs.NetClient.MASK_BASE_URL;
import static com.haeyum.savemask.NoticeManager.createNotice;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NaverMap.OnLocationChangeListener, NaverMap.OnCameraChangeListener, NaverMap.OnCameraIdleListener {
    private final String TAG = "MainActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    // UI
    private ConstraintLayout clStore;

    private ConstraintLayout clOptionPlenty, clOptionSome, clOptionFew, clOptionEmpty;
    private ImageView ivOptionPlentyCheck, ivOptionSomeCheck, ivOptionFewCheck, ivOptionEmptyCheck;

    private TextView tvStoreName, tvStoreAddr, tvStoreTime;
    private ImageView ivStoreStatus;

    // MAP
    private NaverMap naverMap;
    private FusedLocationSource locationSource;

    // Marker
    private Marker currentMarker;
    private ArrayList<InfoWindow> infoWindows = new ArrayList<>();
    private ArrayList<MaskStore> maskStores = new ArrayList<>();
//    private ArrayList<Marker> markers = new ArrayList<>();

    private boolean isLoadedMap;
    private boolean isShowStore;

    private AppPref appPref;

    private MaskStore selectMaskStore;

    private Boolean isOptionPlenty = true, isOptionSome = true, isOptionFew = true, isOptionEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initMap();

        appPref = new AppPref(this);

        if(!appPref.getTermsAgree())
            startActivity(new Intent(getApplicationContext(), TermsActivity.class));

        int birth = appPref.getBirth();
        if(birth != -1) {
            boolean result = false;
            int x = -1, y = -1;

            switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                case 2:
                    x = 1;
                    y = 6;
                    if(getCheckBirth(birth, x, y))
                        result = true;
                    break;

                case 3:
                    x = 2;
                    y = 7;
                    if(getCheckBirth(birth, x, y))
                        result = true;
                    break;

                case 4:
                    x = 3;
                    y = 8;
                    if(getCheckBirth(birth, x, y))
                        result = true;
                    break;

                case 5:
                    x = 4;
                    y = 9;
                    if(getCheckBirth(birth, x, y))
                        result = true;
                    break;

                case 6:
                    x = 5;
                    y = 0;

                    if(getCheckBirth(birth, x, y))
                        result = true;
                    break;

                default:
                    result = true;
            }

            if(result) {
                if(x == -1) {
                    createNotice(this, "잠깐, 마스크를 사셨나요?", "주중에 마스크를 구매하신 못한 분들은 주말에 구매가 가능합니다!\n세이브 마스크를 이용하여 재고가 있는 판매처에 방문하여 마스크를 구해세요!\n\n마스크는 1주일에 2장만 구매 가능하며 신분증을 꼭 챙겨주세요!");
                } else {
                    createNotice(this, "마스크 사셔야겠네요!", "오늘은 " + x + "년생과 " + y + "년 생이 사는 날 입니다.\n\n세이브 마스크를 이용하여 재고가 있는 판매처에 방문하여 마스크를 구해세요!\n\n마스크는 1주일에 2장만 구매 가능하며 신분증을 꼭 챙겨주세요!");
                }
            } else {
                createNotice(this, "오늘은 패스...", "오늘은 " + x + "년생과 " + y + "년 생이 사는 날 입니다.\n아쉽게도 다음에 사셔야겠네요...\n\n세이브 마스크 기능은 자유롭게 이용하실 수 있습니다!");
            }
        }

        connectServer();
    }

    private void initUI() {
        clStore = findViewById(R.id.cl_main_store);
        clStore.setAlpha(0);

        clOptionPlenty = findViewById(R.id.cl_main_optionPlenty);
        ivOptionPlentyCheck = findViewById(R.id.iv_main_optionPlentyCheck);

        clOptionSome = findViewById(R.id.cl_main_optionSome);
        ivOptionSomeCheck = findViewById(R.id.iv_main_optionSomeCheck);

        clOptionFew = findViewById(R.id.cl_main_optionFew);
        ivOptionFewCheck = findViewById(R.id.iv_main_optionFewCheck);

        clOptionEmpty = findViewById(R.id.cl_main_optionEmpty);
        ivOptionEmptyCheck = findViewById(R.id.iv_main_optionEmptyCheck);

        tvStoreName = findViewById(R.id.tv_main_storeName);
        tvStoreAddr = findViewById(R.id.tv_main_storeAddr);
        tvStoreTime = findViewById(R.id.tv_main_storeTime);

        ivStoreStatus = findViewById(R.id.iv_main_storeStatus);

        new Handler().postDelayed(() -> {
            clStore.setAlpha(1);
            clStore.animate().translationYBy(clStore.getHeight()).start();
        }, 100);
    }

    private void connectServer() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseManager.getInstance().sign(androidId);
    }

    private boolean getCheckBirth(int birth, int x, int y) {
        return birth == x || birth == y ? true : false;
    }

    private void getMaskStores(LatLng latLng) {
//        if(1==1) {
//            return;
//        }

        String query = MASK_BASE_URL + "storesByGeo/json?lat=" + latLng.latitude + "&lng=" + latLng.longitude + "&m=3000";
        Call<MaskStores> res = NetClient.NetClientNaver().getMaskStores(query);

        res.enqueue(new Callback<MaskStores>() {
            @Override
            public void onResponse(Call<MaskStores> call, Response<MaskStores> response) {
                if(response.body() == null) {
                    Toast.makeText(getApplicationContext(), "서버 연결에 실패하였습니다... 관리자에게 문의바랍니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(InfoWindow iw : infoWindows) {
                    iw.close();
                }

                infoWindows.clear();
                maskStores.clear();

                for(MaskStore maskStore : response.body().getStores()) {
                    if(maskStore.getRemain_stat() == null)
                        continue;

                    LatLng pos = new LatLng(maskStore.getLat(), maskStore.getLng());

                    InfoWindow.Adapter adapter;
                    adapter = new InfoWindow.Adapter() {
                        @NonNull
                        @Override
                        public OverlayImage getImage(@NonNull InfoWindow infoWindow) {
                            return OverlayImage.fromAsset(maskStore.getRemain_stat() + ".png");
                        }
                    };

                    tvStoreName.setText(maskStore.getName());
                    tvStoreAddr.setText(maskStore.getAddr());
                    tvStoreTime.setText(maskStore.getStock_at());

                    InfoWindow infoWindow = new InfoWindow();
                    infoWindow.setAdapter(adapter);
                    infoWindow.setPosition(pos);
                    infoWindow.setMap(naverMap);

                    infoWindow.setOnClickListener(overlay -> {
                        tvStoreName.setText(maskStore.getName());
                        tvStoreAddr.setText(maskStore.getAddr());

                        int remainId = -1;
                        if(maskStore.getRemain_stat() != null)
                            switch (maskStore.getRemain_stat()) {
                                case "plenty":
                                    remainId = R.drawable.plenty;
                                    break;

                                case "some":
                                    remainId = R.drawable.some;
                                    break;

                                case "few":
                                    remainId = R.drawable.few;
                                    break;

                                default:
                                    remainId = R.drawable.empty;
                                    break;
                            }
                        else
                            remainId = R.drawable.empty;

                        selectMaskStore = maskStore;
                        ivStoreStatus.setImageResource(remainId);

                        showStore();
                        return false;
                    });

//                    markers.add(marker);
                    maskStores.add(maskStore);
                    infoWindows.add(infoWindow);
                }

                changeOption();
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
        if(isShowStore) {
//            reopenStore();
            return;
        }
        clStore.animate().translationYBy(-clStore.getHeight()).setDuration(300).withEndAction(() -> {
            isShowStore = true;
        });
    }

    private void hideStore() {
        if(!isShowStore)
            return;

        clStore.animate().translationYBy(clStore.getHeight()).setDuration(300).withEndAction(() -> {
            isShowStore = false;
        });
    }

    private void reopenStore() {
        clStore.animate().translationYBy(clStore.getHeight()).setDuration(300).withEndAction(() -> {
            clStore.animate().translationYBy(-clStore.getHeight()).setDuration(300).start();
        });
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
        currentMarker.setGlobalZIndex(1);
        currentMarker.setZIndex(100000);

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
        getMaskStores(naverMap.getCameraPosition().target);
    }

    private void changeOption() {
        for(int i=0; i<maskStores.size(); i++) {
            if(maskStores.get(i) == null)
                continue;
            else
                if(maskStores.get(i).getRemain_stat() == null)
                    continue;

            MaskStore maskStore = maskStores.get(i);

            if(maskStore.getRemain_stat().equals("plenty"))
                infoWindows.get(i).setVisible(isOptionPlenty);

            if(maskStore.getRemain_stat().equals("some"))
                infoWindows.get(i).setVisible(isOptionSome);

            if(maskStore.getRemain_stat().equals("few"))
                infoWindows.get(i).setVisible(isOptionFew);

            if(maskStore.getRemain_stat().equals("empty"))
                infoWindows.get(i).setVisible(isOptionEmpty);
        }
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.cl_main_optionPlenty:
                isOptionPlenty = !isOptionPlenty;

                if(isOptionPlenty) {
                    ivOptionPlentyCheck.setVisibility(View.VISIBLE);
                    clOptionPlenty.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5d65dc")));
                } else {
                    ivOptionPlentyCheck.setVisibility(View.GONE);
                    clOptionPlenty.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#979797")));
                }

                changeOption();
                break;

            case R.id.cl_main_optionSome:
                isOptionSome = !isOptionSome;

                if(isOptionSome) {
                    ivOptionSomeCheck.setVisibility(View.VISIBLE);
                    clOptionSome.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5d65dc")));
                } else {
                    ivOptionSomeCheck.setVisibility(View.GONE);
                    clOptionSome.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#979797")));
                }

                changeOption();
                break;

            case R.id.cl_main_optionFew:
                isOptionFew = !isOptionFew;

                if(isOptionFew) {
                    ivOptionFewCheck.setVisibility(View.VISIBLE);
                    clOptionFew.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5d65dc")));
                } else {
                    ivOptionFewCheck.setVisibility(View.GONE);
                    clOptionFew.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#979797")));
                }

                changeOption();
                break;

            case R.id.cl_main_optionEmpty:
                isOptionEmpty = !isOptionEmpty;

                if(isOptionEmpty) {
                    ivOptionEmptyCheck.setVisibility(View.VISIBLE);
                    clOptionEmpty.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5d65dc")));
                } else {
                    ivOptionEmptyCheck.setVisibility(View.GONE);
                    clOptionEmpty.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#979797")));
                }

                changeOption();
                break;

            case R.id.btn_main_search:
//                Toast.makeText(this, "마스크 정보를 조회합니다!", Toast.LENGTH_SHORT).show();
//                createNotice(this, "뚝딱뚝딱 만들고 있어요!", "산업기능요원으로 근무하다보니 시간이 부족하여 완성되지 못했습니다ㅜㅜ\n\n퇴근 시간을 모아 조금씩 만들고 있으니 며칠내로 유용한 기능이 생길꺼에요!!\n\n개발 문의: vnycall74@naver.com");
                intent = new Intent(getApplicationContext(), NearbyActivity.class);
                intent.putExtra("lat", locationSource.getLastLocation().getLatitude());
                intent.putExtra("lng", locationSource.getLastLocation().getLongitude());

                startActivity(intent);
                break;

            case R.id.btn_main_currentLocation:
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(locationSource.getLastLocation().getLatitude(), locationSource.getLastLocation().getLongitude())).animate(CameraAnimation.Linear); //new LatLng(37.5666102, 126.9783881)
                naverMap.moveCamera(cameraUpdate);

//                showStore();
                break;

            case R.id.btn_main_share:
                intent = new Intent(android.content.Intent.ACTION_SEND);

                intent.setType("text/plain");

                String cnt = "";
                switch (selectMaskStore.getRemain_stat()) {
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
                    text = "세이브 마스크에서 안내드립니다\n\n현재 " + selectMaskStore.getName() + "에서 판매중인 마스크는 모두 판매되었습니다...\n세이브 마스크를 통해 다른 판매처를 찾아보세요..!\n\n[세이브 마스크 앱 설치]\nhttps://play.google.com/store/apps/details?id=com.haeyum.savemask";
                } else {
                    text = "세이브 마스크에서 안내드립니다!\n\n현재 " + selectMaskStore.getName() + "에서 판매중인 마스크의 재고 수가 " + cnt + " 입니다!\n늦기 전에 방문하여 마스크를 구매하세요!\n\n마스크 입고 시간: " + selectMaskStore.getStock_at() + "\n\n주소: " + selectMaskStore.getAddr() + "\n\n길찾기: https://map.kakao.com/?q=" + selectMaskStore.getAddr().replaceAll(" ", "+") + "\n\n[세이브 마스크 앱 설치]\nhttps://play.google.com/store/apps/details?id=com.haeyum.savemask";
                }
                intent.putExtra(Intent.EXTRA_TEXT, text);

                // Title of intent
                Intent chooser = Intent.createChooser(intent, "세이브 마스크 공유하기");
                startActivity(chooser);

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
