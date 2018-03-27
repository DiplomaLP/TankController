package com.app.yura.tankcontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import TCPClient.Client;
import TCPClient.Command;
import TCPClient.CommandFactory;
import TCPClient.Commands;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLeft = (Button)findViewById(R.id.buttonLeft);
        Button buttonRight = (Button)findViewById(R.id.buttonRight);

        final Client client = ConnectActivity.getClient();

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command command = null;
                try {
                    command = CommandFactory.CreateCommand(Commands.MOVE_LEFT, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client.sendCommand(command);
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command command = null;
                try {
                    command = CommandFactory.CreateCommand(Commands.MOVE_RIGHT, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client.sendCommand(command);
            }
        });
    }
}
