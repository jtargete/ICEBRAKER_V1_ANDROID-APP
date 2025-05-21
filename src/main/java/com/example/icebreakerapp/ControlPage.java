package com.example.icebreakerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import ovh.karewan.knble.KnBle;
import ovh.karewan.knble.interfaces.BleWriteCallback;
import ovh.karewan.knble.struct.BleDevice;

public class ControlPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_page);

        AlertDialog.Builder INSTRUCTIONS = new AlertDialog.Builder(ControlPage.this);

        ImageView btnforward = (ImageView) findViewById(R.id.forward);
        ImageView btnback = (ImageView) findViewById(R.id.back);
        ImageView btnleft = (ImageView) findViewById(R.id.left);
        ImageView btnright = (ImageView) findViewById(R.id.right);
        ImageView btnstop = (ImageView) findViewById(R.id.stop);

        String UUIDCHAR = "0000FFE1-0000-1000-8000-00805F9B34FB";
        String UUIDSER = "0000FFE0-0000-1000-8000-00805F9B34FB";
        String goforward = "b";
        String goback = "e";
        String turnright = "c";
        String turnleft = "d";
        String stop = "a";

        byte[] byteforward = goforward.getBytes();
        byte[] byteback = goback.getBytes();
        byte[] byteright = turnright.getBytes();
        byte[] byteleft = turnleft.getBytes();
        byte[] bytestop = stop.getBytes();

        INSTRUCTIONS.setMessage("UP = FORWARD\n"+ "DOWN = REVERSE\n" + "RIGHT = RIGHT\n" + "LEFT = LEFT\n"+"SYMBOL = STOP\n"+"ENJOY!");
        INSTRUCTIONS.setCancelable(false);
        INSTRUCTIONS.setPositiveButton(
                "PROCEED",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface INSTRUCTIONS, int id) {
                        INSTRUCTIONS.cancel();
                    }
                });
        AlertDialog instructions = INSTRUCTIONS.create();
        instructions.show();


        BleDevice device = KnBle.getInstance().getBleDeviceFromMac("12:34:56:01:AA:AA");
        BleWriteCallback CALLBACK = new BleWriteCallback() {
            @Override
            public void onWriteFailed() {

            }

            @Override
            public void onWriteProgress(int current, int total) {

            }

            @Override
            public void onWriteSuccess() {

            }
        };

        btnforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KnBle.getInstance().write(device, UUIDSER, UUIDCHAR, byteforward, CALLBACK);
            }

        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KnBle.getInstance().write(device, UUIDSER, UUIDCHAR, byteback, CALLBACK);
            }

        });

        btnleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KnBle.getInstance().write(device, UUIDSER, UUIDCHAR, byteleft, CALLBACK);

            }

        });

        btnright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KnBle.getInstance().write(device, UUIDSER, UUIDCHAR, byteright, CALLBACK);

            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KnBle.getInstance().write(device, UUIDSER, UUIDCHAR, bytestop, CALLBACK);

            }

        });
    }
    }
