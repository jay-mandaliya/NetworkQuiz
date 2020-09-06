package com.example.root.mobilequizz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminPanel extends AppCompatActivity {

    SharedPreferences stupref;
    SharedPreferences answer;
    Server server;
    DatabaseHandler db = new DatabaseHandler(this);
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        stupref = getSharedPreferences("studentpreference",MODE_PRIVATE);
        answer = getSharedPreferences("answer",MODE_PRIVATE);
        setTitle("Admin Panel");
    }

    protected void onDestroy(){
        super.onDestroy();
        server.onDestroy();
    }

    public void onNewStudent(View view){
        final Dialog newstudent = new Dialog(this);
        newstudent.setContentView(R.layout.newstudentpopup);
        newstudent.setTitle("New Student");


        final EditText usname = newstudent.findViewById(R.id.editText8);
        final EditText pass = newstudent.findViewById(R.id.editText10);
        Button submit = newstudent.findViewById(R.id.button4);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usname.getText().toString().equals(""))
                   showToast("Enter Username");
                else{
                    if(pass.getText().toString().equals(""))
                        showToast("Enter Password");
                    else{
                        if(stupref.getString(usname.getText().toString(),null)!=null)
                            showToast("Username Already Exist");
                        else {
                            SharedPreferences.Editor editor = stupref.edit();
                            editor.putString(usname.getText().toString(),pass.getText().toString()).apply();
                            newstudent.dismiss();
                        }
                    }
                }

            }
        });
        newstudent.show();
    }

    public void onManageStudents(View view){
        Intent intent = new Intent(this,ManageStudent.class);
        startActivity(intent);
    }

    public void onAddQuestion(View view){

        final Dialog addquestion = new Dialog(this);
        addquestion.setContentView(R.layout.addquestionpopup);
        addquestion.setTitle("Add Question");

        final EditText question = addquestion.findViewById(R.id.editText11);
        final EditText op1 = addquestion.findViewById(R.id.editText12);
        final EditText op2 = addquestion.findViewById(R.id.editText13);
        final EditText op3 = addquestion.findViewById(R.id.editText14);
        final EditText op4 = addquestion.findViewById(R.id.editText15);
        final Button submit = addquestion.findViewById(R.id.button7);

        final Spinner spinner = addquestion.findViewById(R.id.spinner);
        String[] answer_choice = { "Option 1", "Option 2", "Option 3", "Option 4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,answer_choice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question.getText().toString().equals("") ||
                        op1.getText().toString().equals("") ||
                        op2.getText().toString().equals("") ||
                        op3.getText().toString().equals("") ||
                        op4.getText().toString().equals("")) {
                    showToast("Fill all Fields");
                }
                else{
                    Question ques = new Question();
                    ques.question = question.getText().toString();
                    ques.op1 = op1.getText().toString();
                    ques.op2 = op2.getText().toString();
                    ques.op3 = op3.getText().toString();
                    ques.op4 = op4.getText().toString();

                    String string = spinner.getSelectedItem().toString();
                    if(string.equals("Option 1"))
                        ques.answer = op1.getText().toString();
                    if(string.equals("Option 2"))
                        ques.answer = op2.getText().toString();
                    if(string.equals("Option 3"))
                        ques.answer = op3.getText().toString();
                    if(string.equals("Option 4"))
                        ques.answer = op4.getText().toString();

                    db.addQuestion(ques);
                    addquestion.dismiss();
                }
            }
        });
        addquestion.show();
    }

    public void onManageQuestions(View view){
        Intent intent = new Intent(this, ManageQuestions.class);
        startActivity(intent);
    }

    public void onStartQuiz(View view){

        int total = (int) db.getQuestionTotal();
        Question[] questions = new Question[total];
        for(int i=0 ; i<total; i++){
            questions[i] = db.getQuestion(i+1);
            answer.edit().putString(String.valueOf(i),questions[i].answer).apply();
        }

        server = new Server(this, stupref, db, answer);
        TextView infoip = findViewById(R.id.textView4);
        String resource = "Quiz Running At IP : " + server.getIpAddress() + " Port : " + server.getPort();
        infoip.setText(resource);
    }

    public void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}