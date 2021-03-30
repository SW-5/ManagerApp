package com.example.cafestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private static final String TAG="SIGN";
    /////////파이어베이스를 사용하기 위한 선언/////////
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user= mAuth.getCurrentUser();
    /////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (user != null)//사용자가 있는지 확인
        {
            signOut();//있는 경우 사용자 초기화
        }
        else {

        }
    }
    private void signOut()//사용자를 초기화 시키는 메소드
    {
        FirebaseAuth.getInstance().signOut();
    }
    public void Login(){
        try {
            String email = ((EditText) findViewById(R.id.ID)).getText().toString();//email값을 String으로 가져옴
            String password = ((EditText) findViewById(R.id.PASS)).getText().toString();//Password를 String으로 가져옴



            if(email.length()==0||password.length()==0)//이메일과 패스워드에 아무것도 입력이 안되어있을때
            {
                Toast.makeText(getApplicationContext(), "이메일 혹은 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();

            }
            else {
                //파이어베이스 오픈소스를 이용한 로그인과정
                if(email.equals("manager1@naver.com")==true||email.equals("manager2@naver.com")==true||email.equals("manager3@naver.com")==true)//지정된 매장아이디로만 로그인할수있게 함
                {

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");//로그인 성공시 성공로그 띄움
                            user = mAuth.getCurrentUser();//유저 내용 가져옴

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class); //매장 화면  이동
                                startActivity(intent);//매장화면 이동

                        }
                        else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());//실패시 실패 로그
                            Toast.makeText(getApplicationContext(), "이메일 혹은 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                }else{
                    Toast.makeText(getApplicationContext(), "관리자 아이디로 접속하세요", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void Login(View view) {
        Login();//로그인 메소드 실행
    }
}