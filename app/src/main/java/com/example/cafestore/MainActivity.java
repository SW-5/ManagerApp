package com.example.cafestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn1,btn2,btn3,btn4; //버튼 4개 선언
    static NoticeData NoticeData=new NoticeData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {//main화면에서 실행될 때(생명주기)

        super.onCreate(savedInstanceState);//activity 실행구문
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//안드로이드화면제어

        setContentView(R.layout.activity_main); //visible 상태만들기



//
        btn1 = (Button)findViewById(R.id.btn_1); //xml파일에서의 버튼들과의 변수들의 id값을 매칭시키기
        btn2 = (Button)findViewById(R.id.btn_2);
        btn3 = (Button)findViewById(R.id.btn_3);
        btn4 = (Button)findViewById(R.id.btn_4);


        btn1.setOnClickListener(new View.OnClickListener() { //버튼1클릭시 fragment1페이지를 frame창에 띄우는 기능
            @Override
            public void onClick(View v) {// 각 버튼을 클릭했을 시의 이벤트를 만들어줌
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                com.example.cafestore.Fragment1 fragment1 = new com.example.cafestore.Fragment1();
                transaction.replace(R.id.frame, fragment1);
                //transcation 객체를 사용하여 fragment1페이지 주소값을 통한 fragment1객체를 frame에 해당하는 부분을 fragment1으로 교체해라란 코드
                transaction.commit();

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                com.example.cafestore.Fragment2 fragment2 = new com.example.cafestore.Fragment2();
                transaction.replace(R.id.frame, fragment2);
                transaction.commit();

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                com.example.cafestore.Fragment3 fragment3 = new com.example.cafestore.Fragment3();
                transaction.replace(R.id.frame, fragment3);
                transaction.commit();

            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              System.exit(0);

            }
        });


    }
    @Override
    public void onBackPressed() {


    }
}