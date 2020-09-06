package com.example.root.mobilequizz;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Report report = (Report) getIntent().getSerializableExtra("report");

        TextView right = findViewById(R.id.textView15);
        TextView wrong = findViewById(R.id.textView16);
        TextView attempt = findViewById(R.id.textView17);
        TextView total = findViewById(R.id.textView18);

        right.setText("Correct Answers : "+String.valueOf(report.correct_questions));
        wrong.setText("Wrong Answers : "+String.valueOf(report.attempted_questions-report.correct_questions));
        attempt.setText("Total Attempted : "+String.valueOf(report.attempted_questions));
        total.setText("Total Questions : "+String.valueOf(report.total_questions));

        SharedPreferences preferences = getSharedPreferences("answer",MODE_PRIVATE);
        preferences.edit().clear().apply();
    }
}
