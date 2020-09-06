package com.example.root.mobilequizz;

import android.app.Activity;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

class Server{
    Activity activity;
    ServerSocket serverSocket;
    static int socketServerPORT;
    SharedPreferences stupref,answer_pref;
    DatabaseHandler db;

    public Server(Activity a, SharedPreferences stupref, DatabaseHandler db, SharedPreferences answer) {
        activity = a;
        this.stupref = stupref;
        this.answer_pref = answer;
        this.db = db;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread{

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(38421);
                socketServerPORT = serverSocket.getLocalPort();

                while(true) {
                    Socket socket = serverSocket.accept();

                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    StudentData received = (StudentData) ois.readObject();

                    String pass = received.password;
                    if(pass.equals(stupref.getString(received.username,null))){

                        StudentData toSend = new StudentData();
                        toSend.status="accepted";

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(toSend);

                        int total = (int) db.getQuestionTotal();
                        final Question[] questions = new Question[total];
                        for(int i=0 ; i<total; i++){
                            questions[i] = db.getQuestion(i+1);
                            questions[i].answer=null;
                        }
                        oos.writeObject(questions);

                        Report report = generateReport((Answers) ois.readObject());
                        oos.writeObject(report);
                    }
                    else{
                        StudentData toSend = new StudentData();
                        toSend.status="notaccepted";

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(toSend);
                        oos.close();
                    }

                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }

    public Report generateReport(final Answers received){

        Report report = new Report();

        int total = received.id.length;
        for (int i=0 ; i<total ; i++){

            if(received.ans[i].equals(
                    answer_pref.getString(received.id[i],null)
            )) {
                report.correct_questions++;
            }
        }

        report.total_questions = (int) db.getQuestionTotal();
        report.attempted_questions = received.id.length;
        return report;
    }
}