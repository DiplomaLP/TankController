package com.app.yura.tankcontroller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import TCPClient.Commands;

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

        SeekBar seekBarLeft = (SeekBar)findViewById(R.id.seekBarMoveLeft);
        seekBarLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    Command command = CommandFactory.CreateCommand(Commands.MOVE_LEFT, progress);
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

        SeekBar seekBarRight = (SeekBar)findViewById(R.id.seekBarMoveRight);
        seekBarRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    Command command = CommandFactory.CreateCommand(Commands.MOVE_RIGHT, progress);
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
