package com.example.technote.TN_Database;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.example.technote.TN_Database.Adapter.RealCallLogsAdapter;
import com.example.technote.R;

import java.util.List;

public class RealCallLog_Test extends AppCompatActivity {
    private static final int READ_LOGS = 725;
    private ListView logList;
    private Runnable logsRunnable;
    private String[] requiredPermissions = {Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS};
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_real_call_log);

        logList = (ListView) findViewById(R.id.LogsList);
        searchView = (SearchView)findViewById(R.id.search_real_call_log);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length()==0){
                    logsRunnable.run();
                }else{
                    loadSelectName(s);
                }
                return false;
            }
        });

        logsRunnable = new Runnable() {
            @Override
            public void run() {
                loadLogs();
            }
        };

        // Checking for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionToExecute(requiredPermissions, READ_LOGS, logsRunnable);
        } else {
            logsRunnable.run();
        }
    }
    // This is to be run only when READ_CONTACTS and READ_CALL_LOG permission are granted
    private void loadLogs() {
        RealCallLogsManager realCallLogsManager = new RealCallLogsManager(this);
        List<LogObject> callLogs = realCallLogsManager.getLogs(RealCallLogsManager.ALL_CALLS);
        RealCallLogsAdapter realCallLogsAdapter = new RealCallLogsAdapter(this, R.layout.log_layout, callLogs);
        logList.setAdapter(realCallLogsAdapter);
    }
    private void loadSelectName(String s) {
        RealCallLogsManager realCallLogsManager = new RealCallLogsManager(this);
        List<LogObject> callLogs = realCallLogsManager.getLogs(RealCallLogsManager.SELECT_NAME,s);
        RealCallLogsAdapter realCallLogsAdapter = new RealCallLogsAdapter(this, R.layout.log_layout, callLogs);
        logList.setAdapter(realCallLogsAdapter);
    }
    // A method to check if a permission is granted then execute tasks depending on that particular permission
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissionToExecute(String permissions[], int requestCode, Runnable runnable) {
        boolean logs = ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED;
        boolean contacts = ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED;

        if (logs || contacts) {
            requestPermissions(permissions, requestCode);
        } else {
            runnable.run();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_LOGS && permissions[0].equals(Manifest.permission.READ_CALL_LOG) && permissions[1].equals(Manifest.permission.READ_CONTACTS)) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED && grantResults[1] == PermissionChecker.PERMISSION_GRANTED) {
                logsRunnable.run();
            } else {
                new AlertDialog.Builder(RealCallLog_Test.this)
                        .setMessage("The app needs these permissions to work, Exit?")
                        .setTitle("Permission Denied")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkPermissionToExecute(requiredPermissions, READ_LOGS, logsRunnable);
                            }
                        })
                        .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        }
    }
}