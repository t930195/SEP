package com.mark.pocketmanager.Setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mark.pocketmanager.R;

/*
    SharedPpreference's Data:
    {"budget" : String 預算金額, "ifRemind" : Boolean是否提醒}
*/
public class BudgetActivity extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch remindSwitch;
    private EditText budgetEdit;
    private SharedPreferences settingData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buget);

        View actionBar = findViewById(R.id.my_actionBar);
        ImageButton backButton = actionBar.findViewById(R.id.backButton);
        TextView title = actionBar.findViewById(R.id.title);
        title.setText("預算設定");
        backButton.setOnClickListener(v -> finish());

        settingData = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        budgetEdit = findViewById(R.id.budgetEdit);
        remindSwitch = findViewById(R.id.remindSwitch);
        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            if(TextUtils.isEmpty(budgetEdit.getText())){
                showToast(v.getContext(),"請輸入預算");
            } else{
                try {
                    Integer.parseInt(budgetEdit.getText().toString());
                }catch (Exception e) {
                    showToast(v.getContext(),"請輸入合法預算");
                    return;
                }
                if(Integer.parseInt(budgetEdit.getText().toString()) < 0){
                    showToast(v.getContext(),"請輸入大於0的預算");
                } else {
                    SharedPreferences.Editor editor = settingData.edit();
                    editor.putInt("budget", Integer.parseInt(budgetEdit.getText().toString()));
                    editor.putBoolean("ifRemind", remindSwitch.isChecked());
                    editor.apply();
                    finish();
                }
            }
        });
        remindSwitch.setChecked(settingData.getBoolean("ifRemind",false));
        budgetEdit.setText(Integer.toString(settingData.getInt("budget",0)));
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
}