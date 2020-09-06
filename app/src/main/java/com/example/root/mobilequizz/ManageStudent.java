package com.example.root.mobilequizz;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class ManageStudent extends AppCompatActivity {

    SharedPreferences stupref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_student);
        stupref = getSharedPreferences("studentpreference",MODE_PRIVATE);

        Map<String,?> allEntries = stupref.getAll();
        ArrayList<String> studentlist = new ArrayList<>();

        for(Map.Entry<String, ?>entry : allEntries.entrySet()){
//            studentlist.add("Username : "+entry.getKey()+" Password : "+entry.getValue());
            studentlist.add(entry.getKey());
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.manage_student_layout,R.id.textView13, studentlist);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    public void onDelete(View view){

        LinearLayout parentRow = (LinearLayout) view.getParent();

        TextView username = (TextView) parentRow.getChildAt(0);

        stupref.edit().remove(username.getText().toString()).apply();
    }
}
