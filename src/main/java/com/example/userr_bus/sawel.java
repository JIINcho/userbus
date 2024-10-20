package com.example.userr_bus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sawel extends AppCompatActivity {

    private FloatingActionButton fab;
    private static final String TAG = "sawel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawel);

        // 스피너 초기화
        Spinner timeSpinner = findViewById(R.id.time_spinner);
        Spinner placeSpinner = findViewById(R.id.station_spinner);

        // 드롭다운 메뉴에 표시할 항목들 배열
        String[] item_time = new String[]{"시간을 선택하세요", "08:00", "08:05", "08:20", "08:40", "09:00"
                , "09:30", "09:50", "10:00"};

        String[] item_place = new String[]{"장소를 선택하세요", "사월역(3번출구)", "정문", "B1", "C7", "C13", "D6", "A2(건너편)"};

        // 현재 시간 가져오기
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        // 문자열 형식으로 다시 받아오기
        List<String> timeList = new ArrayList<>();

        // 현재 시간 이후의 시간만 추가
        for (String time : item_time) {
            if (time.equals("시간을 선택하세요")) {
                timeList.add(time);
            } else {
                int hour = Integer.parseInt(time.substring(0, 2));
                int minute = Integer.parseInt(time.substring(3));

                if (hour > currentHour || (hour == currentHour && minute >= currentMinute)) {
                    timeList.add(time);
                }
            }
        }

        // 시간 목록을 문자열 배열로 변환
        String[] finalTimeList = new String[timeList.size()];
        timeList.toArray(finalTimeList);

        // ArrayAdapter를 사용하여 스피너에 항목들을 표시
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, finalTimeList);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapterTime);

        ArrayAdapter<String> adapterPlace = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_place);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placeSpinner.setAdapter(adapterPlace);

        // 예약 버튼 클릭 리스너 설정
        final Button reservationButton = findViewById(R.id.reservation);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // Spinner 항목 선택에 따라 버튼 활성화/비활성화
                    int selectedTimePosition = timeSpinner.getSelectedItemPosition();
                    int selectedPlacePosition = placeSpinner.getSelectedItemPosition();

                    if (selectedTimePosition != 0 && selectedPlacePosition != 0) {
                        // 시간과 장소를 모두 선택한 경우
                        reservationButton.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "예약이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        // Firebase Firestore 인스턴스 가져오기
                        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

                        String userId = currentUser.getUid();
                        String selectedRoute = "사월역 출발";

                        // 예약 정보 저장을 위한 Map 생성
                        Map<String, Object> dataToSave = new HashMap<>();
                        dataToSave.put("userId", userId);
                        dataToSave.put("route", selectedRoute);
                        dataToSave.put("time", timeSpinner.getSelectedItem().toString());
                        dataToSave.put("place", placeSpinner.getSelectedItem().toString());
                        dataToSave.put("reservationDate", new Timestamp(new Date()));

                        // Firestore에 예약 정보 추가
                        mFirestore.collection("Reservation").add(dataToSave)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });

                        // 예약 완료 후 예약 리스트 화면으로 이동
                        Intent intent = new Intent(sawel.this, selectbuslist.class);
                        startActivity(intent);
                        finish(); // 현재 액티비티 종료
                    } else {
                        // "시간을 선택하세요" 또는 "장소를 선택하세요"를 선택한 경우
                        if (selectedTimePosition == 0) {
                            Toast.makeText(getApplicationContext(), "유효한 시간을 선택해주세요", Toast.LENGTH_SHORT).show();
                        }
                        if (selectedPlacePosition == 0) {
                            Toast.makeText(getApplicationContext(), "유효한 장소를 선택해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        // 뒤로 가기 버튼 클릭 리스너 설정
        final Button button = findViewById(R.id.btn_back);
        Button moveButton=findViewById(R.id.btn_back);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 노선 선택 화면으로 이동
                Intent intent=new Intent(getApplicationContext(), routechoose.class);
                startActivity(intent);
            }
        });

        // 플로팅 액션 버튼 클릭 리스너 설정
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    // 팝업 메뉴 표시하는 메소드
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_item_1) {
                    // 원하는 웹사이트로 이동하는 Intent 생성
                    String url = "https://www.cu.ac.kr/life/welfare/schoolbus"; // 여기에 이동하려는 웹사이트 주소 입력
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.menu_item_2) {
                    startActivity(new Intent(sawel.this, selectbuslist.class));
                    return true;
                } else if (itemId == R.id.menu_item_3) {
                    startActivity(new Intent(sawel.this, login.class));
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }
}
