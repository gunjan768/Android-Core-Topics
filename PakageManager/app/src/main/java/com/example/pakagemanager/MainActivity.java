package com.example.pakagemanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pakagemanager.utils.Permissions;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button appListBtn = findViewById(R.id.list_app_btn);

        appListBtn.setOnClickListener(this);

        if(!checkPermissionsArray(Permissions.PERMISSIONS))
        {
            verifyPermissions(Permissions.PERMISSIONS);
        }
        else
        {
            Toast.makeText(this, "You have granted all the permissions", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, AppListActivity.class);
        startActivity(intent);
    }

    // Verify all the permissions passed to the array.
    public void verifyPermissions(String[] permissions)
    {
        // Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(MainActivity.this, permissions, VERIFY_PERMISSIONS_REQUEST);
    }

    // Check an array of permissions.
    public boolean checkPermissionsArray(String[] permissions)
    {
        // Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(String check : permissions)
        {
            if(!checkPermissions(check))
            {
                return false;
            }
        }

        return true;
    }

    // Check a single permission is it has been verified.
    public boolean checkPermissions(String permission)
    {
        // Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(MainActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED)
        {
            // Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else
        {
            // Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }
}