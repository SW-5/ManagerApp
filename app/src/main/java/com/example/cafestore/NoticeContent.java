package com.example.cafestore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoticeContent extends Fragment {
    TextView content; //내용이 들어갈곳
    //////파이어베이스를 사용하기 위한 선언/////////
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    //////////////////////////////////////////////
    public NoticeContent() {

    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notice_content, null);//뷰를 사용하기 위함
        content=view.findViewById(R.id.content);//content를 선언

        content.setText(MainActivity.NoticeData.getMap(MainActivity.NoticeData.getPosition()));//NoticeData에 저장되어있는 내용을 가져옴
        Button DeleteBtn = (Button) view.findViewById(R.id.DeleteBtn);//삭제 이벤트 버튼

        Button BackBtn = (Button) view.findViewById(R.id.BackBtn);
        //삭제 버튼을 클릭할시에 키값을 가져와서 그 키값을 가진 하위 데이터를 지우고 Fragment3으로 이동
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                myRef.child("공지사항").child(MainActivity.NoticeData.getKeyMap(MainActivity.NoticeData.getPosition())).removeValue();
                getFragmentManager().beginTransaction().replace(R.id.frame,new Fragment3()).commit();
            }
        });
        //뒤로가기 버튼
        BackBtn.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame,new Fragment3()).commit();

            }
        });
     return view;
    }
}
