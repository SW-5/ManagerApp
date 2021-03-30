package com.example.cafestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class Fragment3 extends Fragment  {
    private ListView list; // 공지사항을 담기위한 리스트객체
    private TextView dateNow; // 현재시간을 담는 텍스트뷰
    ArrayAdapter<String> adapter;//리스트뷰를 연결할 어뎁터

    ///////파이어베이스를 사용하기 위한 선언/////////
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    ////////////////////////////////////////////

    ///////////////////// 현재시간을 계산하기 위한 변수 ///////////////////
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    private SimpleDateFormat mFormat = new SimpleDateFormat("   yyyy년 M월 d일"); // 날짜 초기화
    String formatDate = mFormat.format(date);
    ////////////////////////////////////////////////////////////////////

    List<String> data = new ArrayList<>();//내용이 들어갈 어레이 리스트
    String Title="";//역순 배치를 위한 Title
    StringTokenizer st;//스트링 토크나이저
    String TitleShow;//제목이 들어갈곳
    String Sub="";//내용이 들어갈곳

    public Fragment3() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, null);//뷰를 설정
        list = (ListView) view.findViewById(R.id.listView);//리스트 가져오기
        initDatabase();//초기화

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,data);//어뎁터 연결
        list.setAdapter(adapter);//어뎁터 연결


        mReference = mDatabase.getReference("공지사항"); // 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)//데이터 스냅샷을 이용한 데이터베이스 읽어오기
            {
                adapter.clear();//어뎁터 클리어

                for (DataSnapshot messageData : dataSnapshot.getChildren())//데이터스냅샷의 children의 데이터를 다 가져옴
                {
                    String Pull=messageData.getValue().toString();//가져온 문자열을 Pull로 가져옴
                    st=new StringTokenizer(Pull,"\n");// 토크나이져로 자름
                    TitleShow=st.nextToken();//제목을 담음
                    while(st.hasMoreTokens())//내용을 담는다
                    {
                        Sub+=("\n"+st.nextToken());//\n을 이용해서 짤랐으므로 다시 \n을 붙여준다
                    }
                    NoticeAdd(TitleShow);//리스트뷰 배치를 한다(어레이리스트 이용)
                    //공지사항데이터를 NoticeData에 넣어놓는다
                    MainActivity.NoticeData.setKeyMap(TitleShow,messageData.getKey());
                    MainActivity.NoticeData.setMap(TitleShow,Sub);
                    /////////////////////////////////////////
                    Sub="";//Sub 초기화 (내용을 초기화)
                }

                adapter.notifyDataSetChanged();//아이템 추가
                list.setSelection(adapter.getCount()-1);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

         list.setOnItemClickListener(listener);//클릭 리스너
        Button button = (Button) view.findViewById(R.id.btnAdd);//추가 버튼을 클릭했을때 공지사항을 만드는 창으로 넘어간다
        button.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame,new noticeAdd()).commit();

            }
        });

        // 현재시간을 dateText에 출력
        dateNow = (TextView) view.findViewById(R.id.dateText);
        dateNow.setText(formatDate);
        ////////////////////////////

        return view;
    }
    //각 리스트뷰를 클릭할때 각 포지션을 찾아서 그값을 NoticeData에 저장하고, 공지사항 내용을 표시하는 NoticeContent 클래스로 넘어간다
    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {

        @Override

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MainActivity.NoticeData.setPosition(data.get(position));
            getFragmentManager().beginTransaction().replace(R.id.frame,new NoticeContent()).commit();

        }
    };

    public void NoticeAdd(String New) //역순 배치를 위한 메소드
    {
        if(data.isEmpty()){
            data.add(New);
            Title=New;
        }
        else{
            int a=data.indexOf(Title);
            data.add(a,New);
            Title=New;
        }
    }


    private void initDatabase() //초기화 하는 메소드
    {

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("공지사항");//경로설정
        // mReference.child("공지사항").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }


}
