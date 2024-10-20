package com.example.userr_bus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;


public class routechoose extends AppCompatActivity {

    private FloatingActionButton fab;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mFirebaseUser;
    private TextView scholl_num; // 이메일을 표시할 TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routechoose);

        // Firebase 인증 객체 초기화
        mFirebaseAuth = FirebaseAuth.getInstance();

        // SharedPreferences에서 이메일 정보 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "No Email Found");

        // 이메일을 TextView에 설정
        TextView School_num = findViewById(R.id.scholl_num);
        School_num.setText(userEmail);

        // FloatingActionButton 초기화 및 클릭 리스너 설정
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // 이동 버튼들 초기화 및 클릭 리스너 설정
        Button moveButton=findViewById(R.id.gyonea);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), gyonea.class);
                startActivity(intent);
            }
        });

        Button moveButton2=findViewById(R.id.hayang);
        moveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(getApplicationContext(), hayang.class);
                startActivity(intent2);
            }
        });

        Button moveButton3=findViewById(R.id.ansim);
        moveButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(getApplicationContext(), ansimstation.class);
                startActivity(intent3);
            }
        });

        Button moveButton4=findViewById(R.id.sawel);
        moveButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4=new Intent(getApplicationContext(), sawel.class);
                startActivity(intent4);
            }
        });

        Button moveButton5=findViewById(R.id.sawel_ansim);
        moveButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5=new Intent(getApplicationContext(), ansim_sawel.class);
                startActivity(intent5);
            }
        });

        Button moveButton6=findViewById(R.id.btn_back);
        moveButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent6=new Intent(getApplicationContext(), login.class);
                startActivity(intent6);
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
                    startActivity(new Intent(routechoose.this, selectbuslist.class));
                    return true;
                } else if (itemId == R.id.menu_item_3) {
                    startActivity(new Intent(routechoose.this, login.class));
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }
}
