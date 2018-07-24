package com.ansh.mobile.nearby;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.widget.Toast;


public class Splash extends Activity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.splash);


        checkPermision();

    }

    //permission only for 6.0 on word for allowing settings

    public void checkPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        } else {
            Thread background = new Thread() {
                public void run() {

                    try {
                        // Thread will sleep for 5 seconds
                        sleep(3000);

                        // After 5 seconds redirect to another intent
                        Intent i=new Intent(getBaseContext(),MainActivityNearby.class);
                        startActivity(i);

                        //Remove activity
                        finish();

                    } catch (Exception e) {

                    }
                }
            };

            // start thread
            background.start();
            Toast.makeText(getApplicationContext(),"Without Permissions we cannot move",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 2 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED ) {

                Thread background = new Thread() {
                    public void run() {

                        try {
                            // Thread will sleep for 5 seconds
                            sleep(3000);

                            // After 5 seconds redirect to another intent
                            Intent i=new Intent(getBaseContext(),MainActivityNearby.class);
                            startActivity(i);

                            //Remove activity
                            finish();

                        } catch (Exception e) {

                        }
                    }
                };

                // start thread
                background.start();
                // Success Stuff here

            }
            else{
                // Failure Stuff
            }
        }

    }
    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}