package com.example.root.mobilequizz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class StudentPanel extends AppCompatActivity {
    Question[] questions;
    int cursor;
    SharedPreferences preferences;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_panel);

        preferences = getSharedPreferences("answer",MODE_PRIVATE);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Quiz");

        questions = (Question[])getIntent().getSerializableExtra("ques_array");

        String[] quno = new String[questions.length];
        String[] ques = new String[questions.length];
        for(int i=0 ; i<questions.length ; i++){
            quno[i] = questions[i].id;
            ques[i] = questions[i].question;
        }

        StudentPanelAdapter adapter = new StudentPanelAdapter(this,quno,ques);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                showQuestion(position);
            }
        });


        Button finish = toolbar.findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReport();
            }
        });

    }

    protected void showQuestion(int index){
        cursor = index;
        final Dialog show = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
        show.setContentView(R.layout.questionpopup);
        show.setTitle("Question "+cursor);

        final TextView question = show.findViewById(R.id.textView11);
        final RadioButton op1 = show.findViewById(R.id.radioButton);
        final RadioButton op2 = show.findViewById(R.id.radioButton2);
        final RadioButton op3 = show.findViewById(R.id.radioButton3);
        final RadioButton op4 = show.findViewById(R.id.radioButton4);
        final Button next = show.findViewById(R.id.button9);
        final Button prev = show.findViewById(R.id.button11);
        final RadioGroup group = show.findViewById(R.id.radioGroup);

        question.setText(questions[cursor].question);
        op1.setText(questions[cursor].op1);
        op2.setText(questions[cursor].op2);
        op3.setText(questions[cursor].op3);
        op4.setText(questions[cursor].op4);

        String answer = preferences.getString(String.valueOf(cursor),null);
        if(answer!=null){
            if(answer.equals(op1.getText()))
                op1.setChecked(true);
            if(answer.equals(op2.getText()))
                op2.setChecked(true);
            if(answer.equals(op3.getText()))
                op3.setChecked(true);
            if(answer.equals(op4.getText()))
                op4.setChecked(true);
        }

        Button back = show.findViewById(R.id.button10);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });

        if(cursor==questions.length-1)
            next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor++;

                showQuestion(cursor);
                show.dismiss();
            }
        });


        if(cursor==0)
            prev.setEnabled(false);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor--;

                showQuestion(cursor);
                show.dismiss();
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                RadioButton button = radioGroup.findViewById(checked);
                preferences.edit().putString(String.valueOf(cursor), button.getText().toString()).commit();
            }
        });

        show.show();
    }

    protected void showReport(){
       new Thread(){
           @Override
           public void run() {
               try {
                   Answers toSend = new Answers();
                   toSend.id = new String[preferences.getAll().size()];
                   toSend.ans = new String[preferences.getAll().size()];
                   int flag=0;
                   for(int i=0 ; i<questions.length ; i++){
                       if(preferences.getString(String.valueOf(i),null)!=null){
                           toSend.id[flag]=Integer.toString(i);
                           toSend.ans[flag]=preferences.getString(String.valueOf(i),null);
                           flag++;
                       }
                   }

                   SocketHandler.getObjectOutputStream().writeObject(toSend);

                   Report report = (Report) SocketHandler.getObjectInputStream().readObject();

                   SocketHandler.getSocket().close();

                   Intent intent = new Intent(activity ,ReportActivity.class);
                   intent.putExtra("report" ,report);
                   startActivity(intent);
               } catch (IOException | ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
       }.start();
    }
}