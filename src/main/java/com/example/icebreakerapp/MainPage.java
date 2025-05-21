package com.example.icebreakerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;

import ovh.karewan.knble.KnBle;
import ovh.karewan.knble.interfaces.BleGattCallback;
import ovh.karewan.knble.interfaces.BleScanCallback;
import ovh.karewan.knble.scan.ScanFilters;
import ovh.karewan.knble.scan.ScanSettings;
import ovh.karewan.knble.struct.BleDevice;

public class MainPage extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT=1;
    private final static int REQUEST_ACCESS_FINE_LOCATION =2;
    private static final String TAG = "1";
    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Setup Code:

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getSupportActionBar().hide();
        final Handler animationtimelogo = new Handler();
        final Handler animationtimebuttons = new Handler();
        final Handler animationtimepicture = new Handler();
        final Handler notifications = new Handler();
        KnBle.getInstance().init(getApplicationContext());
        ProgressDialog dialog = new ProgressDialog(MainPage.this);
        ProgressDialog dialog2 = new ProgressDialog(MainPage.this);
        AlertDialog.Builder Scanagainbuild = new AlertDialog.Builder(MainPage.this);
        ImageView logoImage = findViewById(R.id.imageView);
        ImageView productImage = findViewById(R.id.imageView2);
        Button start =(Button) findViewById(R.id.buttonstart);
        Button about  = (Button) findViewById(R.id.buttonabout);
        Vibrator notify = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        start.setVisibility(View.INVISIBLE);
        about.setVisibility(View.INVISIBLE);
        productImage.setVisibility(View.INVISIBLE);
        KnBle.getInstance().init(getApplicationContext());
        ScanFilters filters = new ScanFilters.Builder()//
                .addDeviceName("ICE BREAKER V1")//
                .build();
        KnBle.getInstance().setScanFilter(filters);
        ScanSettings settings = new ScanSettings.Builder()//
                .setScanTimeout(5000)//
                .setScanMode(0)//
                .build();
        KnBle.getInstance().setScanSettings(settings);
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        //Main Code:

        animationtimelogo.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoImage.startAnimation(AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.move_down
                ));

            }
        }, 2500);


        animationtimepicture.postDelayed(new Runnable() {
        @Override
        public void run() {
            productImage.startAnimation(AnimationUtils.loadAnimation(
                    getApplicationContext(),
                    R.anim.fade_in
            ));

        }
    }, 3500);


        animationtimebuttons.postDelayed(new Runnable() {
            @Override
            public void run() {
                start.startAnimation(AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.fade_in

                ));

                about.startAnimation(AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.fade_in
                ));

            }
        }, 4500);

        notifications.postDelayed(new Runnable() {
            public void run() {
                notify.vibrate(500);
                AlertDialog.Builder notificationalert = new AlertDialog.Builder(MainPage.this);
                notificationalert.setMessage("Allow App to use Bluetooth and Location Services");
                notificationalert.setCancelable(true);
                notificationalert.setPositiveButton(
                        "Proceed",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ActivityCompat.requestPermissions(MainPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
                                result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                                    @Override
                                    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                                        try {
                                            task.getResult(ApiException.class);
                                        } catch (ApiException exception) {
                                            switch (exception.getStatusCode()) {
                                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                                    try {
                                                        ResolvableApiException resolvable = (ResolvableApiException) exception;
                                                        resolvable.startResolutionForResult(MainPage.this,100);
                                                    } catch (IntentSender.SendIntentException e) {
                                                        Log.d(TAG, e.getMessage());
                                                    } catch (ClassCastException e) {
                                                        Log.d(TAG, e.getMessage());
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                });
                                if (!adapter.isEnabled()) {
                                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                                }
                            }
                        });
                AlertDialog alert2 = notificationalert.create();
                alert2.show();
            }

        }, 5000);




        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KnBle.getInstance().startScan(new BleScanCallback() {
                    @Override
                    public void onScanStarted() {
                        dialog.setMessage("Locating ICE BREAKER...");
                        dialog.setCancelable(true);
                        dialog.show();


                    }

                    @Override
                    public void onScanFailed(int error) {

                    }


                    @Override
                    public void onScanResult(@NonNull BleDevice bleDevice) {
                        BleDevice device = KnBle.getInstance().getBleDeviceFromMac("12:34:56:01:AA:AA");
                        KnBle.getInstance().connect(device,new BleGattCallback() {
                            @Override
                            public void onConnecting() {
                                dialog.dismiss();
                                dialog2.setMessage("Initializing...");
                                dialog2.setCancelable(false);
                                dialog2.show();

                            }

                            @Override
                            public void onConnectFailed() {
                                Toast.makeText(MainPage.this, "Connection failed", Toast.LENGTH_LONG).show();
                                dialog2.dismiss();
                            }

                            @Override
                            public void onConnectSuccess(List<BluetoothGattService> services) {
                                dialog2.dismiss();
                                Toast.makeText(MainPage.this, "Connection Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainPage.this,ControlPage.class));

                            }

                            @Override
                            public void onDisconnected() {

                            }
                        });
                    }

                    @Override
                    public void onScanFinished(@NonNull HashMap<String, BleDevice> scanResult) {
                        Scanagainbuild.setMessage("ICEBRKR not found, make sure to turn device on before scanning");
                        Scanagainbuild.setCancelable(false);
                        Scanagainbuild.setPositiveButton(
                                        "locate again",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface scanagaindialog, int id) {
                                                scanagaindialog.cancel();
                                                dialog.dismiss();

                                            }
                                        });
                        AlertDialog alert1 = Scanagainbuild.create();
                        alert1.show();
                    }
                });
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPage.this,AboutPage.class));
            }
        });




    }
}

//ICEBRKR APP BY JOSHUA TARGETE