package com.example.root.mobilequizz;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ManageQuestions extends AppCompatActivity {

    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);
        final DatabaseHandler db = new DatabaseHandler(this);
        activity = this;

        int total = (int) db.getQuestionTotal();
        final Question[] questions = new Question[total];
        for(int i=0 ; i<total; i++)
            questions[i] = db.getQuestion(i+1);

        String[] quno = new String[questions.length];
        final String[] ques = new String[questions.length];
        for(int i=0 ; i<questions.length ; i++){
            quno[i] = questions[i].id;
            ques[i] = questions[i].question;
        }

        StudentPanelAdapter adapter = new StudentPanelAdapter(this,quno,ques);
        ListView listView = findViewById(R.id.list2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, final View v, final int position,
                                    long id) {

                final Dialog update_question = new Dialog(activity);
                update_question.setContentView(R.layout.addquestionpopup);
                update_question.setTitle("Edit Question");

                final EditText question = update_question.findViewById(R.id.editText11);
                final EditText op1 = update_question.findViewById(R.id.editText12);
                final EditText op2 = update_question.findViewById(R.id.editText13);
                final EditText op3 = update_question.findViewById(R.id.editText14);
                final EditText op4 = update_question.findViewById(R.id.editText15);
                final Button submit = update_question.findViewById(R.id.button7);

                final Spinner spinner = update_question.findViewById(R.id.spinner);
                String[] answer_choice = { "Option 1", "Option 2", "Option 3", "Option 4"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,android.R.layout.simple_spinner_item,answer_choice);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                question.setText(questions[position].question);
                op1.setText(questions[position].op1);
                op2.setText(questions[position].op2);
                op3.setText(questions[position].op3);
                op4.setText(questions[position].op4);

                if(questions[position].answer.equals(questions[position].op1))
                    spinner.setSelection(0);
                if(questions[position].answer.equals(questions[position].op2))
                    spinner.setSelection(1);
                if(questions[position].answer.equals(questions[position].op3))
                    spinner.setSelection(2);
                if(questions[position].answer.equals(questions[position].op4))
                    spinner.setSelection(3);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(question.getText().toString().equals("") ||
                                op1.getText().toString().equals("") ||
                                op2.getText().toString().equals("") ||
                                op3.getText().toString().equals("") ||
                                op4.getText().toString().equals("")) {
                            Toast.makeText(activity, "Fill all Fields",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Question update = new Question();
                            update.id = questions[position].id;
                            update.question = question.getText().toString();
                            update.op1 = op1.getText().toString();
                            update.op2 = op2.getText().toString();
                            update.op3 = op3.getText().toString();
                            update.op4 = op4.getText().toString();

                            questions[position].question = question.getText().toString();
                            questions[position].op1 = op1.getText().toString();
                            questions[position].op2 = op2.getText().toString();
                            questions[position].op3 = op3.getText().toString();
                            questions[position].op4 = op4.getText().toString();

                            String string = spinner.getSelectedItem().toString();
                            if(string.equals("Option 1")) {
                                update.answer = op1.getText().toString();
                                questions[position].answer = op1.getText().toString();
                            }
                            if(string.equals("Option 2")) {
                                update.answer = op2.getText().toString();
                                questions[position].answer = op2.getText().toString();
                            }
                            if(string.equals("Option 3")) {
                                update.answer = op3.getText().toString();
                                questions[position].answer = op3.getText().toString();
                            }
                            if(string.equals("Option 4")){
                                update.answer = op4.getText().toString();
                                questions[position].answer = op4.getText().toString();
                            }

                            db.updateQuestion(update);
                            update_question.dismiss();
                            ((TextView)v.findViewById(R.id.ques)).setText(update.question);

                        }
                    }
                });
                update_question.show();
            }
        });
    }
}
