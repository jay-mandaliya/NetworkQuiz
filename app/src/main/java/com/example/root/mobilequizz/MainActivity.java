package com.example.root.mobilequizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendAdmin(View view){
        Intent intent = new Intent(this,Admin.class);
        startActivity(intent);
    }

    public void sendStudent(View view){
        Intent intent = new Intent(this,Student.class);
        startActivity(intent);
    }
}
