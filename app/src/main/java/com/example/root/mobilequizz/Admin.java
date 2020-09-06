package com.example.root.mobilequizz;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Admin extends AppCompatActivity {

    SharedPreferences admpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        admpref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = admpref.edit();
        boolean firstTime = admpref.getBoolean("first",true);

        if (firstTime) {

            final Dialog myDialog = new Dialog(this);
            myDialog.setContentView(R.layout.adminloginpopup);
            final EditText usrnm = myDialog.findViewById(R.id.editText);
            final EditText pass = myDialog.findViewById(R.id.editText2);
            final EditText cnfpass = myDialog.findViewById(R.id.editText3);
            Button submit = myDialog.findViewById(R.id.button2);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (usrnm.getText().toString().equals(""))
                        showToast("Enter UserName");
                    else {
                        if (pass.getText().toString().equals(""))
                            showToast("Enter Password");
                        else {
                            if (cnfpass.getText().toString().equals("") || !(cnfpass.getText().toString().equals(pass.getText().toString())))
                                showToast("Password not match");
                            else {
                                editor.putBoolean("first", false).apply();
                                editor.putString(usrnm.getText().toString(),pass.getText().toString()).apply();
                                myDialog.dismiss();
                            }
                        }
                    }
                }
            });
            myDialog.show();
        }
    }

    public void onSubmit(View view){
        admpref = PreferenceManager.getDefaultSharedPreferences(this);
        EditText usrname = findViewById(R.id.editText4);
        EditText pass = findViewById(R.id.editText5);

        if(usrname.getText().toString().equals(""))
            showToast("Enter UserName");
        else{
            if(pass.getText().toString().equals(""))
                showToast("Enter Password");
            else{
                if(pass.getText().toString().equals(admpref.getString(usrname.getText().toString(),null))){
                    Intent intent = new Intent(this,AdminPanel.class);
                    startActivity(intent);
                }else{
                    showToast("Wrong Username Or Password");
                }
            }
        }
    }

    public void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
