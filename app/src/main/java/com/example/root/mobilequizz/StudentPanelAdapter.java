package com.example.root.mobilequizz;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StudentPanelAdapter extends ArrayAdapter {

    private final Activity activity;
    private final String[] quno;
    private final String[] ques;

    public StudentPanelAdapter(Activity activity, String[] quno, String[] ques){
        super(activity, R.layout.student_panel_layout, quno);

        this.activity = activity;
        this.quno = quno;
        this.ques = ques;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.student_panel_layout, null,true);

        TextView qunoTextField = rowView.findViewById(R.id.quno);
        TextView quesTextField = rowView.findViewById(R.id.ques);

        qunoTextField.setText("Q-"+quno[position]);
        quesTextField.setText(ques[position]);
        return rowView;
    }
}
