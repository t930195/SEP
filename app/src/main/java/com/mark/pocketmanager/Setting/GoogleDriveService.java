package com.mark.pocketmanager.Setting;

import static android.content.Context.MODE_PRIVATE;

import static com.mark.pocketmanager.Setting.ZipManager.unzip;
import static com.mark.pocketmanager.Setting.ZipManager.zip;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.mark.pocketmanager.Account.Account;
import com.mark.pocketmanager.Account.AccountViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GoogleDriveService {

    public static final int RC_SIGN_IN = 400;
    private GoogleSignInClient client;
    private GoogleSignInAccount account;
    private String backupDBPath = GoogleDriveUtil.rootPath + "/data/com.mark.pocketmanager/databases";
    private Drive driveService;

//    public ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
//    GoogleDriveService(){
//        threadLocal.set(0);
//    }
//    return sign in intent
    public Intent getSignInIntent(Activity activity) {
        Log.i("log", "get sign in intent");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                        new Scope(DriveScopes.DRIVE),
                        new Scope(DriveScopes.DRIVE_APPDATA),
                        new Scope(DriveScopes.DRIVE_READONLY),
                        new Scope(DriveScopes.DRIVE_METADATA),
                        new Scope(DriveScopes.DRIVE_SCRIPTS),
                        new Scope(DriveScopes.DRIVE_METADATA_READONLY),
                        new Scope(DriveScopes.DRIVE_PHOTOS_READONLY))
                .requestProfile()
                .build();

        client = GoogleSignIn.getClient(activity, gso);
        return client.getSignInIntent();
    }
    public void requestStoragePremission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    public void logOut(){
        client.signOut();
        Log.i("isLogOut?", "yes!");
    }
//    handle the result of sign in intent, and reset account data
    public Boolean handleSignInResult(Intent data, Context context) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(googleSignInAccount -> {

                        GoogleAccountCredential credential = GoogleAccountCredential
                                .usingOAuth2(context, Collections.singleton(DriveScopes.DRIVE_FILE));
                        credential.setSelectedAccount(googleSignInAccount.getAccount());

                        driveService = new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName("PocketManager")
                                .build();
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
            account = task.getResult(ApiException.class);
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            if(e.getStatusCode() == 7){
                Log.i("signin repo", "no internet connect");
                Toast.makeText(context, "失去網路連接", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

//    return account info to Activity
    public SharedPreferences setAccountData(SharedPreferences accountData){
        try{
            if(account != null) {
                SharedPreferences.Editor editor = accountData.edit();
                editor.putString("email", account.getEmail());
                editor.putString("giveName", account.getGivenName());
                editor.putString("displayName", account.getDisplayName());
//                editor.putString("displayPhoto", account.getPhotoUrl().toString());
                editor.apply();
            }
        }catch(Exception e){
            Log.w("TAG", e);
        }

        return accountData;
    }
//    clear account info form Activity
    public SharedPreferences clearAccountData(SharedPreferences accountData){
        try{
            SharedPreferences.Editor editor = accountData.edit();
            editor.clear();
            editor.apply();

        }catch(Exception e){
            Log.w("TAG", e);
        }

        return accountData;
    }
    public void backUpToDrive(Context context){
        ProgressDialog progress = createProgressing("備份資料中...", 1);
        driveService= getDriveService(context);
        progress.show();
        progress.setCancelable(false);

        final File backupDBFolder = new File(backupDBPath);
        backupDBFolder.mkdirs();
        String[] s = new String[6];
        for(int i = 0; i<GoogleDriveUtil.dbFileNames.size(); i= i+1){
            final File backupDB = new File(backupDBFolder, GoogleDriveUtil.dbFileNames.get(i));
            s[i]= backupDB.getAbsolutePath();
        }
        try{
            zip(s, backupDBPath + "/pocketmanager_all_db.zip");
        }catch (IOException e) {
            e.printStackTrace();
        }
        //progress.setCanceledOnTouchOutside(false);
        Thread thread = new Thread(() -> {
            progress.setProgress(0);
            int counter = 0;
            String filename = "pocketmanager_all_db.zip";
            ArrayList<String> ids = GoogleDriveUtil.searchFileFromDrive(driveService, filename);
            if(ids == null){
                GoogleDriveUtil.createFileToDrive(driveService, filename);
            }else{
                GoogleDriveUtil.uploadFileToDrive(driveService, ids.get(0), filename);
            }
            progress.setProgress(1);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
            progress.cancel();
            Looper.prepare();
            Toast.makeText(context, "備份完成", Toast.LENGTH_SHORT).show();
            Looper.loop();
        });
        thread.start();
    }
    public void deleteAllBackupFromDrive(Context context){
//        ProgressDialog progress = createProgressing("刪除資料中...", 6);
//        Map<String, String> foundedId =  GoogleDriveUtil.checkIsIntegrity(driveService);
        Thread thr = new Thread(() -> {
//            progress.show();
            for(String filename: GoogleDriveUtil.dbFileNames) {
                int counter = 0;
                try {
                    ArrayList<String> ids = GoogleDriveUtil.searchFileFromDrive(driveService, filename);
                    for (String s : ids) {
                        Log.i("delete id", s);
                        GoogleDriveUtil.deleteFileFromDrive(driveService, s);
                    }
                } catch (NullPointerException e) {
                    Log.w("delete id", "there isn't exist file");
                }
                counter = counter + 1;
//                progress.setProgress(counter);
            }

        });
        thr.start();

    }
    public void restoreFileFromDrive(Context context, AccountViewModel accountViewModel){
        SharedPreferences googleDriveData = context.getSharedPreferences("GoogleDrive_Data", MODE_PRIVATE);
        ProgressDialog progress = createProgressing("還原資料中...", 2);
        progress.show();
        progress.setCancelable(false);

        Thread thread = new Thread(() -> {
            Looper.prepare();
            progress.setProgress(0);
            int counter = 0;
            String filename = "pocketmanager_all_db.zip";
            ArrayList<String> ids = GoogleDriveUtil.searchFileFromDrive(driveService, filename);
            if(ids == null){
                Toast.makeText(context, "找不到備份", Toast.LENGTH_SHORT).show();
            }else{
                GoogleDriveUtil.downloadFileFromDrive(driveService, ids.get(0), filename);
                File dbPath = new File(backupDBPath);
                if (dbPath.canWrite()) {
                    try {
                        Toast.makeText(context, "解壓縮中", Toast.LENGTH_SHORT).show();
                        progress.setProgress(1);
                        unzip(backupDBPath + "/pocketmanager_all_db.zip", backupDBPath);
                        progress.setProgress(2);
                        Toast.makeText(context, "還原完成", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
            progress.cancel();
            accountViewModel.insertAccounts(new Account(-1));
            accountViewModel.deleteAccounts(new Account(-1));
            Looper.loop();
//                Toast.makeText(context, "雲端資料不完整！（終止還原）", Toast.LENGTH_SHORT).show();
        });
        thread.start();
    }
    private Boolean checkIfIntegrity(){
        for(String filename: GoogleDriveUtil.dbFileNames){
            ArrayList<String> foundedIds =  GoogleDriveUtil.searchFileFromDrive(driveService, filename);
            if(foundedIds == null) {
                Log.i("check if integrity", "FALSE");
                return false;
            }
        }
        Log.w("check if integrity", "TRUE");
        return true;
    }

    private Boolean isLogIn(Drive drive){
        return drive != null;
    }
    private Drive getDriveService(Context context){
        GoogleAccountCredential credential = GoogleAccountCredential
                .usingOAuth2(context, Collections.singleton(DriveScopes.DRIVE_FILE));
        if(account == null) {
            Log.e("getDrive", "account is null");
            return null;
        }
        credential.setSelectedAccount(account.getAccount());

        driveService = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("PocketManager")
                .build();
        return driveService;
    }
    public Boolean ifConnected(){
        return client != null;
    }

    private ProgressDialog createProgressing(String title, int maxValue){
        ProgressDialog progress = SettingFragment.getProgressDialog();
        progress.setMax(maxValue);
        progress.setMessage(title);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgress(0);
        return progress;
    }
}
