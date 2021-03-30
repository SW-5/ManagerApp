package com.example.cafestore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


    public class Fragment2 extends Fragment {
        private ListView list; // 공지사항을 담기위한 리스트객체
        private TextView dateNow; // 현재시간을 담는 텍스트뷰
        ArrayAdapter<String> adapter;

        ///////////////////// 현재시간을 계산하기 위한 변수 ///////////////////

        long now = System.currentTimeMillis();

        Date date = new Date(now);
        private SimpleDateFormat mFormat = new SimpleDateFormat("   yyyy년 M월 d일"); // 날짜 초기화
        String formatDate = mFormat.format(date);
        ////////////////////////////////////////////////////////////////////
        /////파이어베이스를 사용하기 위한 선언////////////
        private FirebaseDatabase mDatabase;
        private DatabaseReference mReference;
        private ChildEventListener mChild;
        ////////////////////////////////
        List<String> data = new ArrayList<>();//내용이 들어갈 어레이 리스트
        List<String> Keydata=new ArrayList<>();//키값이 들어갈 어레이 리스트
        String Title=""; //역순 배치를 위한 Title
        StringTokenizer st;//스트링 토크나이저
        public Fragment2() {

        }


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment2, null);//view를 연결하기 위함
            list = (ListView) view.findViewById(R.id.list);//리스트 가져오기
            initDatabase();//초기화

            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,data);//어뎁터 연결
            list.setAdapter(adapter);//어뎁터 연결

            mReference = mDatabase.getReference("주문정보"); // 변경값을 확인할 child 이름

            mReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot)//데이터 스냅샷을 이용한 데이터베이스 읽어오기
                {
                    adapter.clear();//어뎁터 클리어
                    Keydata.clear();//키 어레이 클리어

                    for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                        st= new StringTokenizer(messageData.getValue().toString(),"\n");// \n로 문자열을 자름
                        if(st.nextToken().equals("주문완료"))//첫 단어가 주문완료일경우에만 화면에 보이게 셋팅
                        {
                            NoticeAdd(messageData.getValue().toString(),messageData.getKey());//화면에 역순으로 보여야하기때문에 만든 NoticAdd를 이용해서 리스트와 data셋팅

                        }
                      //  MainActivity.StoreData.setMap(messageData.getKey(),messageData.getValue().toString());//키값과 내용을 StoreData에 저장해놓음

                    }

                    adapter.notifyDataSetChanged();//아이템 추가
                    list.setSelection(adapter.getCount()-1);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            // 현재시간을 dateText에 출력
            dateNow = (TextView) view.findViewById(R.id.dateText);
            dateNow.setText(formatDate);

            return view;
        }

        public void NoticeAdd(String New,String Key)//역순 배치와 리스트 삽입을 위한 메소드
        {
            if(data.isEmpty())//data가 비었을때는 그냥 add시킴
            {
                data.add(New);//데이터 어레이에 추가
                Keydata.add(Key);//키어레이에 추가
                Title=New;
            }
            else//아닌 경우 index를 가져와서 그 위치에 생성
            {
                int a=data.indexOf(Title);//타이틀을 가지고있는 어레이 인덱스를 가져옴
                data.add(a,New);//그 위치에 데이터 추가
                Keydata.add(a,Key);//그 위치에 데이터 추가
                Title=New;//New로 초기화
            }
        }

        private void initDatabase() //초기화 하는 메소드
        {

            mDatabase = FirebaseDatabase.getInstance();
            mReference = mDatabase.getReference("공지사항");//경로설정

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

