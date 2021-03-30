package com.example.cafestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class noticeAdd extends Fragment {
    private Context context; // Toast를 사용하기 위한 선언
    private TextView dateNow; // 시간을 담을 텍스트뷰

    //////////파이어베이스를 사용하기 위한 선언/////////////
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    /////////////////////////////////////
    TextView Title;// 제목이 들어갈곳
    TextView Sub;// 내용이 들어갈곳

    ///////////////////// 현재시간을 계산하기 위한 변수 ///////////////////
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    private SimpleDateFormat mFormat = new SimpleDateFormat("   yyyy년 M월 d일"); // 날짜 초기화
    String formatDate = mFormat.format(date);
    ////////////////////////////////////////////////////////////////////

    public noticeAdd(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notice_add, null);// 뷰를 사용하기 위함
        Title=view.findViewById(R.id.Title); // 제목 선언
        Sub=view.findViewById(R.id.Sub);// 내용 선언
        Button AddBtn = (Button) view.findViewById(R.id.AddBtn);//버튼 이벤트를 위한 선언
        context=container.getContext();
        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Title.getText().length()==0)//제목에 아무것도 안적혀있을때 공지사항 등록 못하게 막음
                {
                    Toast.makeText(context,"제목을 입력하세요..", Toast.LENGTH_SHORT).show();
                }
                else if(Sub.getText().length()==0)//내용에 아무것도 안적혀있을때 공지사항 등록 못하게 막음
                {
                    Toast.makeText(context,"내용을 적으세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(Title.getText().toString().contains("\\n")||Sub.getText().toString().contains("\\n"))// /n으로 구분자를 사용해서 제목과 내용을 판별하기 때문에 /n이 포함되면 실패여부를 표시
                    {

                        Toast.makeText(context,"특수문자가 포함되어있습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else//성공상태로 데이터베이스 공지사항 child에 내용을 입력한다.
                    {
                        myRef.child("공지사항").push().setValue(Title.getText() + "\n" + Sub.getText());
                        getFragmentManager().beginTransaction().replace(R.id.frame,new Fragment3()).commit();//입력 성공시 Fragment3로 이동
                    }

                }
            }
        });

        // 현재시간을 dateText에 출력
        dateNow = (TextView) view.findViewById(R.id.dateText);
        dateNow.setText(formatDate);
        ////////////////////////////

        return view;
    }
}
