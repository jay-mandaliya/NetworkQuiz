package com.example.root.mobilequizz;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Student extends AppCompatActivity {

    EditText username, password,ip1,ip2,ip3,ip4;
    Button submit;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        username = findViewById(R.id.editText6);
        password = findViewById(R.id.editText7);
        submit = findViewById(R.id.submit);
        ip1 = findViewById(R.id.ip1);
        ip2 = findViewById(R.id.ip2);
        ip3 = findViewById(R.id.ip3);
        ip4 = findViewById(R.id.ip4);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String ip = ip1.getText().toString()+"."+ip2.getText().toString()+"."+ip3.getText().toString()+"."+ip4.getText().toString();
                Client myClient = new Client(username.getText()
                        .toString(), password.getText().toString(),ip, activity);

                myClient.start();
            }
        });
    }
}