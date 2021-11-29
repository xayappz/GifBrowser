package com.xay.gifbrowser;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.xay.gifbrowser.utils.InternetConnection;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static String rootpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        requestAppPermissions();
        if (!InternetConnection.checkConnection(getApplicationContext())) {
         //   OnInternet();
        } else {

        }
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 0); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    File directoryToStore;
                    directoryToStore = getBaseContext().getExternalFilesDir(".Gifs");
                    if (!directoryToStore.exists()) {
                        if (directoryToStore.mkdir()) ;
                    }
                    rootpath = directoryToStore.getAbsolutePath();

                    if (!InternetConnection.checkConnection(getApplicationContext())) {
                        //OnInternet();
                    }
                } else {
                    Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void OnInternet() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertDialogBuilder
                .setMessage("No internet connection on your device. Please Enable?")
                .setTitle("No Internet Connection")
                .setCancelable(false)
                .setPositiveButton(" Enable  ",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplication().startActivity(dialogIntent);
                            }
                        });

        alertDialogBuilder.setNegativeButton(" Cancel ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!InternetConnection.checkConnection(getApplicationContext())) {
           // OnInternet();
        } else {
       //     FragmentChangeer.Frags(this,new NoInternet());
        }
    }

//   public void loadFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.nav_host_fragment, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}