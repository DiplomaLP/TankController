package com.app.yura.tankcontroller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import TCPClient.Client;

public class ConnectActivity extends AppCompatActivity {
    ProgressBar progressBarStatus;
    TextView textViewStatus;
    static Client client;
    ConnectToServer asyncConnect;
    Button buttonConnect;

    public static Client getClient() {
        return client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        client = new Client();
        progressBarStatus = (ProgressBar)findViewById(R.id.progressBarStatus);
        textViewStatus = (TextView)findViewById(R.id.textViewStatus);

        progressBarStop();

        buttonConnect = findViewById(R.id.buttonConnect);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncConnect = new ConnectToServer();
                asyncConnect.execute();
            }
        });
    }

    private void progressBarStart() {
        progressBarStatus.setVisibility(View.VISIBLE);
    }

    private void progressBarStop() {
        progressBarStatus.setVisibility(View.GONE);
    }

    private void setTextViewStatus(String status) {
        textViewStatus.setText(status);
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    class ConnectToServer extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarStart();
            buttonConnect.setEnabled(false);
            setTextViewStatus("Connecting to server...");
        }
        @Override
        protected Integer doInBackground(Void... voids) {
            String ip = getResources().getString(R.string.server_ip);
            int port = getResources().getInteger(R.integer.server_port);
            return client.connect(ip, port);
        }

        @Override
        protected void onPostExecute(Integer resultOfConnect) {
            super.onPostExecute(resultOfConnect);
            progressBarStop();
            buttonConnect.setEnabled(true);

            if (resultOfConnect == null || resultOfConnect.intValue() == Client.ERROR) {
                setTextViewStatus("Do not connected!");

            } else if (resultOfConnect.intValue() == Client.OK) {
                setTextViewStatus("Connected!");
                startMainActivity();
                setTextViewStatus("Tap to connect to the tank server");

            } else if (resultOfConnect.intValue() == Client.ALREADY_CONNECTED){
                setTextViewStatus("The client already connected!");
            }
        }
    }
}
