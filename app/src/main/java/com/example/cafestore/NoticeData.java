package com.example.cafestore;

import java.util.HashMap;

public class NoticeData {
    HashMap<String,String> map=new HashMap<>();//제목과 내용이 들어갈곳
    HashMap<String,String> KeyMap=new HashMap<>();//제목과 키값이 들어갈곳 나중에 제목으로 키값을 얻어올때 사용
    String Position="";//클릭한 포지션
    public String getPosition(){
        return Position;
    }//포지션 get
    public void setPosition(String position){
        Position=position;
    }//포지션 set
    public void PositionClear(){ String Position=""; }//포지션 clear
    public void setMap(String key,String Content){
        map.put(key,Content);
    }//map set
    public String getMap(String key){
        return map.get(key);
    }//map get
    public void ClearMap(){
        map.clear();
    }// map clear
    public void setKeyMap(String Content,String key){KeyMap.put(Content,key);}//keyMap set
    public String getKeyMap(String Content){ return KeyMap.get(Content); } //keyMap get

}
