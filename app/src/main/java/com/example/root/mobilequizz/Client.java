package com.example.root.mobilequizz;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Client extends Thread{

    private String ip,username,password;
    private Activity activity;

    Client(String username, String password , String ip ,Activity activity) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.activity = activity;
    }

    @Override
    public void run() {

        try {
            StudentData toSend = new StudentData(username,password);

            SocketHandler.setSocket(new Socket(ip , 38421));
            SocketHandler.setObjectOutputStream(new ObjectOutputStream(SocketHandler.getSocket().getOutputStream()));

            SocketHandler.getObjectOutputStream().writeObject(toSend);
//            oos.close();

            SocketHandler.setObjectInputStream(new ObjectInputStream(SocketHandler.getSocket().getInputStream()));
            StudentData received = (StudentData) SocketHandler.getObjectInputStream().readObject();
//            ois.close();

            if(received.status.equals("accepted")){

                final Question[] questions = (Question[]) SocketHandler.getObjectInputStream().readObject();
                Intent intent = new Intent(activity, StudentPanel.class);
                intent.putExtra("ques_array", questions);
                activity.startActivity(intent);
            }
            else{
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                });
                SocketHandler.getSocket().close();
            }
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
