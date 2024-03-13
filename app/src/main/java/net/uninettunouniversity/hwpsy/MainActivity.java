package net.uninettunouniversity.hwpsy;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import net.uninettunouniversity.hwpsy.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String msg = "Android : ";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_otp, R.id.navigation_log)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }

    @Override
    protected void onStart() {
        super.onStart();
        class BackgroundThread extends Thread {
            @Override
            public void run() {

                try {

                    while (true){
                        addOtp();
                        Thread.sleep(10000);
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        BackgroundThread backgroundThread = new BackgroundThread();
        backgroundThread.start();
    }

    public void addOtp() {

        Long tsLong = System.currentTimeMillis();
        String timestamp = tsLong.toString();

        ContentValues values = new ContentValues();

        values.put(OtpProvider.OTP, generateOTP(8,timestamp));
        values.put(OtpProvider.TIMESTAMP, timestamp);

        Uri uri = getContentResolver().insert(
                OtpProvider.CONTENT_URI, values
        );

    }

    private String generateOTP(int length, String base){
        Random random_method = new Random();
        char[] otp = new char[length];
        for (int i = 0; i < length; i++) {
            otp[i] = base.charAt(random_method.nextInt(base.length()));
        }
        Log.d(msg, "Generato OTP " + Arrays.toString(otp));
        return new String(otp);
    }

    @SuppressLint("Range")
    public void retrieveLastOtp(View v) {
        String URL = "content://net.uninettunouniversity.hwpsy.OtpProvider";

        Uri otpbucket = Uri.parse(URL);

        try (Cursor c = managedQuery(otpbucket, null, null, null, "timestamp")) {

            c.moveToLast();

            TextView t = (TextView)v.getRootView().findViewById(R.id.textView2);
            t.setText(c.getString(c.getColumnIndex(OtpProvider.OTP)));

        }
    }
    @SuppressLint("Range")
    public void retrieveOtps() {
        String URL = "content://net.uninettunouniversity.hwpsy.OtpProvider";

        Uri otpbucket = Uri.parse(URL);

        try (Cursor c = managedQuery(otpbucket, null, null, null, "otp")) {

            if (c.moveToFirst()) {
                do {
                    Toast.makeText(this,
                            c.getString(c.getColumnIndex(OtpProvider._ID)) + "," +
                                    c.getString(c.getColumnIndex(OtpProvider.OTP)) + "," +
                                    c.getString(c.getColumnIndex(OtpProvider.TIMESTAMP))
                            , Toast.LENGTH_SHORT).show();
                } while (c.moveToNext());
            }


        }
    }

}