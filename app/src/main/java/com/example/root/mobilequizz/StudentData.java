package com.example.root.mobilequizz;

import java.io.Serializable;

class StudentData implements Serializable {

    String username;
    String password;
    String status;

    StudentData(){}

    StudentData(String username, String password){
        this.username = username;
        this.password = password;
    }
}