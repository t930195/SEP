package com.mark.pocketmanager.Setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mark.pocketmanager.R;

public class BackUpActivity extends AppCompatActivity {
    Button back;
    RadioGroup backup_group;
    RadioButton month_backup,week_backup,day_backup;
    SharedPreferences sharedPreferences;
    SharedPreferences preferences;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Toast.makeText(this, "按下左上角返回鍵", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.backup_page);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        preferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);

        month_backup = findViewById(R.id.month_backup);
        week_backup = findViewById(R.id.week_backup);
        day_backup = findViewById(R.id.day_backup);
        backup_group = findViewById(R.id.backup_group);
        int backUp = sharedPreferences.getInt("backUp",1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(backUp==0){
            month_backup.setChecked(true);
        }
        else if(backUp==1){
            week_backup.setChecked(true);
        }
        else{
            day_backup.setChecked(true);
        }
        backup_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.month_backup){
                    editor.putInt("backUp",0);
                }
                else if(checkedId == R.id.week_backup){
                    editor.putInt("backUp",1);
                }
                else{
                    editor.putInt("backUp",2);
                }
                editor.commit();
            }
        });
    }
}
