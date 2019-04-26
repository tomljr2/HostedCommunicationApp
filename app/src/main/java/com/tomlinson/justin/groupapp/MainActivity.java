package com.tomlinson.justin.groupapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Socket socket;

    EditText username;
    EditText serverIP;
    EditText port;

    String user;
    String IP;
    int p;

    Button connect;

    String message;

    Client client;

    PrintWriter printWriter;
    private volatile boolean msgFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.et1);
        serverIP = findViewById(R.id.et2);
        port = findViewById(R.id.et3);

        msgFlag = false;

        connect = findViewById(R.id.connect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty() || serverIP.getText().toString().isEmpty()
                        || port.getText().toString().isEmpty())
                {
                    connect.setTextColor(Color.RED);
                    Toast.makeText(getBaseContext(),
                            "Please fill in all fields.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        connect.setTextColor(Color.DKGRAY);
                        sendMessage("Hello.");
                    }
                    catch(Exception e)
                    {
                        connect.setTextColor(Color.RED);
                        Toast.makeText(getBaseContext(),
                                "Could not connect to host.",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }

                    connect.setTextColor(Color.DKGRAY);
                }
            }
        });
    }

    private void sendMessage(String msg)
    {
        msgFlag = true;
        user = username.getText().toString();
        IP = serverIP.getText().toString();
        p = Integer.parseInt(port.getText().toString());
        message = msg;
        client = new Client();
        client.execute();
    }

    private class Client extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try {
                socket = new Socket(IP,p);
                if(msgFlag)
                {
                    printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.write(user + ": " + message);
                    printWriter.flush();
                    printWriter.close();
                    msgFlag = false;
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
