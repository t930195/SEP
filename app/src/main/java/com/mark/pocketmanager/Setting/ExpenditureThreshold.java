package com.mark.pocketmanager.Setting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.mark.pocketmanager.R;

public class ExpenditureThreshold extends AppCompatActivity {
    Switch newMonthNoticeSwitch;
    EditText buget;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_threshold);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        buget = findViewById(R.id.budget);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("buget",buget);
//        editor.apply();

        newMonthNoticeSwitch=findViewById(R.id.newMonthNoticeSwitch);
        newMonthNoticeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean newNotice = newMonthNoticeSwitch.isChecked();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("newNotice",newNotice);
                editor.apply();
            }
        });
        newMonthNoticeSwitch.setChecked(sharedPreferences.getBoolean("newNotice",false));
    }
}