package com.app.yura.tankcontroller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import TCPClient.Client;
import TCPClient.Command;
import TCPClient.CommandFactory;
import TCPClient.CommandMove;
import TCPClient.Commands;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Client client = ConnectActivity.getClient();

        final TextView textViewMoveCamera = (TextView)findViewById(R.id.textViewMoveCamera);

        final SeekBar seekBarMoveCamera = (SeekBar)findViewById(R.id.seekBarMoveCamera);
        seekBarMoveCamera.setMin(getResources().getInteger(R.integer.move_camera_min));
        seekBarMoveCamera.setMax(getResources().getInteger(R.integer.move_camera_max));

        seekBarMoveCamera.setProgress(seekBarMoveCamera.getMax() / 2);

        seekBarMoveCamera.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    Command command = CommandFactory.CreateCommand(Commands.MOVE_CAMERA, progress);
                    client.sendCommand(command, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        JoystickView joystick = (JoystickView) findViewById(R.id.JoystickMoveView);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                
                double angle45 = Math.toRadians(45);
                double leftVectorX = Math.cos(angle45) / 2.0;
                double leftVectorY = Math.sin(angle45) / 2.0;

                double rightVectorX = -Math.cos(angle45) / 2.0;
                double rightVectorY = Math.sin(angle45) / 2.0;


                double resultVectorX = Math.cos(Math.toRadians(angle)) / 2.0 * strength / 100.0;
                double resultVectorY = Math.sin(Math.toRadians(angle)) / 2.0 * strength / 100.0;

//                double leftPower = resultVectorX
                double rightPower = (resultVectorY - resultVectorX) / (rightVectorY - rightVectorX);
                double leftPower = (resultVectorX - rightVectorX * rightPower) / leftVectorX;

                rightPower = rightPower * CommandMove.POWER_RATIO + CommandMove.POWER_RATIO;
                leftPower = leftPower * CommandMove.POWER_RATIO + CommandMove.POWER_RATIO;

                Log.d("tank", "angle = " + angle + " strength = " + strength + "left = " + leftPower + " right = " + rightPower +
                "resVx = " + resultVectorX + " resVy = " + resultVectorY);

                try {
                    Command leftCommand = null;
                    leftCommand = CommandFactory.CreateCommand(Commands.MOVE_LEFT, (int)leftPower);
                    client.sendCommand(leftCommand, false);
                    Command rightCommand = CommandFactory.CreateCommand(Commands.MOVE_RIGHT, (int) rightPower);
                    client.sendCommand(rightCommand , false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 50);

        Switch swithcLight = (Switch)findViewById(R.id.switchLight);
        swithcLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    Command command = CommandFactory.CreateCommand(Commands.LIGHT_TURN, isChecked?1:0);
                    client.sendCommand(command, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        web = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = web.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:callFromActivitySetPassword(\""+"12345678"+"\")");
            }
        });

        web.loadUrl("http://192.168.8.1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        web.postDelayed(new Runnable() {
            @Override
            public void run() {
                showVideo();

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TextView textViewGetVideoStream = (TextView)findViewById(R.id.textViewGetVideoStream);
                textViewGetVideoStream.setVisibility(TextView.INVISIBLE);

                ProgressBar progressBarStream = (ProgressBar)findViewById(R.id.progressBarLoadVideo);
                progressBarStream.setVisibility(ProgressBar.INVISIBLE);

                web.setVisibility(WebView.VISIBLE);
            }
        }, 10000);

    }

    private void showVideo()
    {
        web.loadUrl("http://192.168.8.1:8080");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to close connection?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectActivity.client.disconnect();
    }
}
