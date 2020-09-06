package com.example.root.mobilequizz;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketHandler {

    private static Socket socket;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
      SocketHandler.socket = socket;
    }

    public static synchronized ObjectOutputStream getObjectOutputStream(){
        return oos;
    }

    public static synchronized void setObjectOutputStream(ObjectOutputStream oos){
        SocketHandler.oos = oos;
    }

    public static synchronized ObjectInputStream getObjectInputStream(){
        return ois;
    }

    public static synchronized void setObjectInputStream(ObjectInputStream ois){
        SocketHandler.ois = ois;
    }
}

