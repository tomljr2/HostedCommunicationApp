package com.tomlinson.justin.groupapp;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client extends AsyncTask<Void, Void, Void> {
    private Socket socket;
    private String IP;
    private int port;
    private OutputStream outputStream;
    private volatile boolean msgFlag;
    private volatile boolean aliveFlag;

    Client(String IP, int port)
    {
        this.IP = IP;
        this.port = port;
        socket = null;
        msgFlag = false;
        aliveFlag = true;
    }

    @Override
    protected Void doInBackground(Void... arg0)
    {
        try {
            socket = new Socket(IP,port);
        }
        catch(Exception e)
        { e.printStackTrace(); }

        while(aliveFlag)
        {
            if(msgFlag)
            {
                sendMessage("Hi losers.");
                msgFlag = false;
            }

        }
        return null;
    }

    void sendMessage(String msg)
    {
        msgFlag = true;
        sendMsg(msg);
    }

    private void sendMsg(String msg)
    {
        if(socket == null)
            return;

        try {
            outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(msg);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void close()
    {
        try {
            if(socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
