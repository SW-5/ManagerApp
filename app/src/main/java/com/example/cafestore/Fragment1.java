package com.example.cafestore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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


public class Fragment1 extends Fragment {
    private ListView list; // 공지사항을 담기위한 리스트객체
    private TextView dateNow; // 현재시간을 담는 텍스트뷰
    ArrayAdapter<String> adapter; //후에 리스트뷰와 연결할 어뎁터

    ///////////////////// 현재시간을 계산하기 위한 변수 ///////////////////
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    private SimpleDateFormat mFormat = new SimpleDateFormat("   yyyy년 M월 d일"); // 날짜 초기화
    String formatDate = mFormat.format(date);
    ////////////////////////////////////////////////////////////////////

    /////////파이어베이스를 사용하기 위한 선언////////
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;//하위 이벤트를 수신하기 위하여 선언
    /////////////////////////////////
    List<String> data = new ArrayList<>();//내용이 들어갈 어레이 리스트
    List<String> Keydata=new ArrayList<>();//키값이 들어갈 어레이 리스트
    String Title="";
    StringTokenizer st;//스트링 토크나이저
    public Fragment1() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);//view를 연결하기 위함
        list = (ListView) view.findViewById(R.id.list);//리스트 가져오기
        initDatabase();//초기화
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,data);//어뎁터 연결
        list.setAdapter(adapter);//어뎁터 연결


      //  HomeActivity.NoticeData.PositionClear();
        mReference = mDatabase.getReference("주문정보"); // 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() //데이터베이스에 있는 값을 가져오기 위함
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)//데이터 스냅샷을 이용한 데이터베이스 읽어오기
            {
                adapter.clear();//어뎁터 클리어
                Keydata.clear();//키 어레이 클리어

                for (DataSnapshot messageData : dataSnapshot.getChildren())//데이터스냅샷이 가지고있는 데이터의 모든 children이 포함하고있는 데이터를 하나씩 가져옴
                {
                    st= new StringTokenizer(messageData.getValue().toString(),"\n");// \n로 문자열을 자름
                   if(st.nextToken().equals("주문처리중"))//첫 단어가 주문처리중일경우에만 화면에 보이게 셋팅
                   {
                       NoticeAdd(messageData.getValue().toString(),messageData.getKey());//화면에 역순으로 보여야하기때문에 만든 NoticAdd를 이용해서 리스트와 data셋팅

                   }

                }

                adapter.notifyDataSetChanged();//아이템 추가
                list.setSelection(adapter.getCount()-1);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        list.setOnItemClickListener(listener);//클릭 리스너
        // 현재시간을 dateText에 출력
        dateNow = (TextView) view.findViewById(R.id.dateText);
        dateNow.setText(formatDate);
        ////////////////////////////
        return view;
    }
    // 어뎁터와 연결한 리스트뷰를 클릭할떄 이벤트
    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {

        @Override

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try{

            String nomal=data.get(position);//클릭한 리스트의 위치의 내용을 nomal에 담아둠
            if(data.isEmpty()==false&&Keydata.isEmpty()==false)//키어레이와 데이터 어레이가 비어있지않을때
            {

                st = new StringTokenizer(nomal, "\n");// 문자열을 잘라서 주문처리중인지 확인
                String statu = st.nextToken();//다음 토큰을 저장
                if (statu.equals("주문처리중"))//주문처리중인경우
                {

                    String after = nomal.replaceFirst("주문처리중", "주문완료");//주문처리중인 문자열을 주문완료로 바꿈
                    mReference.child(Keydata.get(position)).setValue(after);//데이터베이스 set

                    data.remove(position);//선택한 리스트뷰의 포지션의 숫자로 data어레이를 지움
                    Keydata.remove(position);//선택한 리스트뷰의 포지션의 숫자로 Keydata어레이 지움

                }
            }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };
    public void NoticeAdd(String New,String Key)//역순 배치와 리스트 삽입을 위한 메소드
    {
        if(data.isEmpty())//data가 비었을때는 그냥 add시킴
        {
            data.add(New);//데이터어레이에 추가
            Keydata.add(Key);//키어레이에 추가
            Title=New;//타이틀을 NEW로 추가
        }
        else//아닌 경우 index를 가져와서 그 위치에 생성
            {
            int a=data.indexOf(Title);//현재 TITLE이 저장되어있는 인덱스를 가져와서 역순으로 만들게 함
            data.add(a,New);//데이터어레이에 추가
            Keydata.add(a,Key);//키어레이에 추가
            Title=New;//타이틀을 NEW로 추가
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
