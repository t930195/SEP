package com.mark.pocketmanager.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.SignInButton;
import com.mark.pocketmanager.Account.AccountViewModel;
import com.mark.pocketmanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SettingFragment extends Fragment {
    private static ProgressDialog progress;
    private final GoogleDriveService mGDS = new GoogleDriveService();

    private SharedPreferences googleDriveData;
    SignInButton connectGoogle;
    Button income, expenditure, remind, backup, restore, logOut, account;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch noticeSwitch;
    SharedPreferences settingData;
    Calendar calendar = Calendar.getInstance();
    TextView backupdate;
    //boolean notification;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    LinearLayout linearLayout;
    String userEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        googleDriveData = this.getContext().getSharedPreferences("GoogleDrive_Data", MODE_PRIVATE);
        settingData = this.getContext().getSharedPreferences("Setting_Data", MODE_PRIVATE);
        progress = new ProgressDialog(this.getContext());
        //使用 accountData.getString(INPUTA, INPUTB) 回傳email(String 型態)
        // INPUTA 是keyword,可以是 email, givenName, displayName
        // INPUTB 是預設的文字
        account = view.findViewById(R.id.account);
        connectGoogle = view.findViewById(R.id.connectGoogle);
        linearLayout = view.findViewById(R.id.linearLayout);
        backupdate = view.findViewById(R.id.backupdate);
        backupdate.setText("最新備份紀錄:\n"+settingData.getString("backupdate", "沒有備份紀錄"));
        //判斷是否已經登入，若登入則自動點擊連結帳號button
        if(ifLogInBefore() && mGDS.ifConnected()){
            Log.w("auto","已經登入");
            connectGoogle.setVisibility(View.GONE);
            account.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            backupdate.setVisibility(View.VISIBLE);
            account.setText(userEmail);
        }else if(ifLogInBefore() && !mGDS.ifConnected()){
            //曾經登入過（有email紀錄），但沒有登入數據
            Log.w("auto","自動登入");
            Intent intent = mGDS.getSignInIntent(this.getActivity());
            startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
        }else{
            Log.w("auto","完全登出");
            connectGoogle.setVisibility(View.VISIBLE);
            account.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            backupdate.setVisibility(View.GONE);
            //登出了（沒有email紀錄），也沒有登入數據
            //connectGoogle.setText("連結帳號");
        }

        connectGoogle.setOnClickListener(v -> {
            Log.i("ifLogInBefore", ifLogInBefore().toString());
            Log.i("onclick", "start sign in");
            Intent intent = mGDS.getSignInIntent(this.getActivity());
            startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
        });

        account.setOnClickListener(v -> {
            showToast(v.getContext(),"已連結帳號" + userEmail);
        });

        backup = view.findViewById(R.id.backup);
        backup.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("警告");
            alertDialog.setMessage("確定要進行備份嗎？\n之前的備份將被覆蓋");
            alertDialog.setPositiveButton("確定",((dialog, which) -> {}));
            alertDialog.setNeutralButton("取消",((dialog, which) -> {}));
            AlertDialog dialog = alertDialog.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v2 -> {
                mGDS.backUpToDrive(this.getContext());
                String now = dateFormat.format(calendar.getTime());
                backupdate.setText("最新備份紀錄:"+now);
                SharedPreferences.Editor editor = settingData.edit();
                editor.putString("backupdate", now);
                editor.apply();
                dialog.dismiss();
            });
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v2 -> {
                dialog.dismiss();
            });
        });

        AccountViewModel accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        restore = view.findViewById(R.id.restore);
        restore.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("警告");
            alertDialog.setMessage("確定要進行還原嗎？\n目前的紀錄將被覆蓋");
            alertDialog.setPositiveButton("確定",((dialog, which) -> {}));
            alertDialog.setNeutralButton("取消",((dialog, which) -> {}));
            AlertDialog dialog = alertDialog.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v2 -> {
                mGDS.restoreFileFromDrive(this.getContext(),accountViewModel);
                dialog.dismiss();
            });
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v2 -> {
                dialog.dismiss();
            });
        });

        logOut = view.findViewById(R.id.logOut);
        logOut.setOnClickListener(v -> {
            mGDS.logOut();
            mGDS.clearAccountData(googleDriveData);
            updateIsLogIn(false);
            showToast(this.getContext(),"已登出");
            //Toast.makeText(this.getContext(), "登出", Toast.LENGTH_SHORT).show();
            connectGoogle.setVisibility(View.VISIBLE);
            account.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            backupdate.setVisibility(View.GONE);
        });

        income = view.findViewById(R.id.income);
        income.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type","收入");
            intent.setClass(this.getContext(), CategoryListActivity.class);
            startActivity(intent);
        });

        expenditure = view.findViewById(R.id.expenditure);
        expenditure.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type","支出");
            intent.setClass(this.getContext(), CategoryListActivity.class);
            startActivity(intent);
        });

        remind = view.findViewById(R.id.remind);
        remind.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this.getContext(), BudgetActivity.class);
            startActivity(intent);
        });

        noticeSwitch=view.findViewById(R.id.noticeSwitch);
        noticeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean listener = noticeSwitch.isChecked();

            SharedPreferences.Editor editor = settingData.edit();
            editor.putBoolean("listener",listener);
            editor.apply();
        });
        noticeSwitch.setChecked(settingData.getBoolean("listener",false));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == GoogleDriveService.RC_SIGN_IN) {
            if (mGDS.handleSignInResult(data, this.getContext())) {
                mGDS.requestStoragePremission(this.getActivity());
//                    mGDS.deleteAllBackupFromDrive(SettingActivity.this);
//                    mGDS.backUpToDrive(SettingActivity.this);
//                    mGDS.restoreFileFromDrive(SettingActivity.this);
                googleDriveData = mGDS.setAccountData(googleDriveData);
                userEmail = googleDriveData.getString("email", "已登入");
                //connectGoogle.setText(userEmail);
                updateIsLogIn(true);
                showToast(this.getContext(),"已連結帳號" + userEmail);
                //Toast.makeText(this.getContext(), "已連結帳號" + userEmail, Toast.LENGTH_SHORT).show();
                Log.i("sign in", "Sign in success");
                connectGoogle.setVisibility(View.GONE);
                account.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                backupdate.setVisibility(View.VISIBLE);
                account.setText(userEmail);
            } else {
                showToast(this.getContext(),"登入失敗");
                //Toast.makeText(this.getContext(), "登入失敗", Toast.LENGTH_SHORT).show();
                updateIsLogIn(false);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void updateIsLogIn(Boolean TorF){
        SharedPreferences.Editor editor = googleDriveData.edit();
        editor.putBoolean("isLogIn", TorF);
        editor.apply();
    }
    private Boolean ifLogInBefore(){
        return googleDriveData.getBoolean("isLogIn", false);
    }

    private static Toast toast;
    public static void showToast(Context context, String msg) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static ProgressDialog getProgressDialog(){
        return progress;
    }

}