package com.mark.pocketmanager.Home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mark.pocketmanager.Account.Account;
import com.mark.pocketmanager.Account.AccountViewModel;
import com.mark.pocketmanager.Category.CategoryViewModel;
import com.mark.pocketmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddOrEditActivity extends AppCompatActivity {
    private Button inButton, outButton, inButtonLine, outButtonLine;
    private Button datePickButton, timePickButton;
    private Button deleteButton, saveButton, addButton;
    private String mode;
    private Spinner assetPicker, categoryPicker;
    private EditText noteEditor, amountEditor;
    private AccountViewModel accountViewModel;
    private CategoryViewModel categoryViewModel;
    private Calendar calendar = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat time = new SimpleDateFormat("a hh:mm");
    private List<String> assets = Arrays.asList("現金", "帳戶");
    private List<String> types = Arrays.asList("收入", "支出", "轉帳");
    private List<String> inCategories = new ArrayList<>();
    private List<String> outCategories = new ArrayList<>();
    private ArrayAdapter inCategoryAdapter, outCategoryAdapter;
    private String type;
    private SharedPreferences settingData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_add_or_edit);
        settingData = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        View actionBar = findViewById(R.id.my_actionBar);
        ImageButton backButton = actionBar.findViewById(R.id.backButton);
        TextView title = actionBar.findViewById(R.id.title);
        title.setText("");
        backButton.setOnClickListener(v -> finish());
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        inButton = findViewById(R.id.inButton);
        outButton = findViewById(R.id.outButton);
        inButtonLine = findViewById(R.id.inButtonLine);
        outButtonLine = findViewById(R.id.outButtonLine);
        assetPicker = findViewById(R.id.assetPicker);
        categoryPicker = findViewById(R.id.categoryPicker);
        datePickButton = findViewById(R.id.datePickButton);
        timePickButton = findViewById(R.id.timePickButton);
        noteEditor = findViewById(R.id.noteEditor);
        amountEditor = findViewById(R.id.amountEditor);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        addButton = findViewById(R.id.addButton);
        calendar.set(Calendar.YEAR, intent.getIntExtra("Year",Calendar.getInstance().get(Calendar.YEAR)));
        calendar.set(Calendar.MONTH, intent.getIntExtra("Month",Calendar.getInstance().get(Calendar.MONTH)));
        calendar.set(Calendar.DAY_OF_MONTH, intent.getIntExtra("Day",Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        calendar.set(Calendar.HOUR_OF_DAY, intent.getIntExtra("Hour",Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        calendar.set(Calendar.MINUTE, intent.getIntExtra("Minute",Calendar.getInstance().get(Calendar.MINUTE)));
        datePickButton.setText(date.format(calendar.getTime()));  //set initial value
        timePickButton.setText(time.format(calendar.getTime()));  //set initial value

        ArrayAdapter assetAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, assets);
        assetPicker.setAdapter(assetAdapter);
        assetPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        inCategories = categoryViewModel.getCategoriesList("收入");
        outCategories = categoryViewModel.getCategoriesList("支出");
        inCategoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, inCategories);
        outCategoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, outCategories);
        type = intent.getStringExtra("Type");
        if(type == null){
            type = "支出";
        }
        //點進頁面
        if (mode.equals("edit")) {  //edit mode
            if(type.equals("收入")) {
                if(!inCategories.contains(intent.getStringExtra("Category"))){
                    inCategories.add(0,intent.getStringExtra("Category"));
                    inCategoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, inCategories) {
                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent)
                        {
                            View v = null;
                            // If this is the initial dummy entry, make it hidden
                            if (position == 0) {
                                TextView tv = new TextView(getContext());
                                tv.setHeight(0);
                                tv.setVisibility(View.GONE);
                                v = tv;
                            }
                            else {
                                // Pass convertView as null to prevent reuse of special case views
                                v = super.getDropDownView(position, null, parent);
                            }
                            // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                            parent.setVerticalScrollBarEnabled(false);
                            return v;
                        }
                    };
                    inCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                categoryPicker.setAdapter(inCategoryAdapter);
                categoryPicker.setSelection(inCategories.indexOf(intent.getStringExtra("Category")));
            } else if(type.equals("支出")) {
                if(!outCategories.contains(intent.getStringExtra("Category"))){
                    outCategories.add(0,intent.getStringExtra("Category"));
                    outCategoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, outCategories) {
                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent)
                        {
                            View v = null;
                            // If this is the initial dummy entry, make it hidden
                            if (position == 0) {
                                TextView tv = new TextView(getContext());
                                tv.setHeight(0);
                                tv.setVisibility(View.GONE);
                                v = tv;
                            }
                            else {
                                // Pass convertView as null to prevent reuse of special case views
                                v = super.getDropDownView(position, null, parent);
                            }
                            // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                            parent.setVerticalScrollBarEnabled(false);
                            return v;
                        }
                    };
                    outCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                categoryPicker.setAdapter(outCategoryAdapter);
                categoryPicker.setSelection(outCategories.indexOf(intent.getStringExtra("Category")));
            }
            selectButton(type);
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.GONE);
            assetPicker.setSelection(assets.indexOf(intent.getStringExtra("Asset")));
            noteEditor.setText(intent.getStringExtra("Note"));
            amountEditor.setText(intent.getStringExtra("Amount"));
        } else if (mode.equals("add")) {
            saveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);
            selectButton(type);
            categoryPicker.setAdapter(outCategoryAdapter);
        } else if (mode.equals("addByDate")){
            saveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);
            selectButton(type);
            categoryPicker.setAdapter(outCategoryAdapter);
        }
        //設置type,asset,category綁定的資源，並可於頁面中更改選取的物件

        inButton.setOnClickListener(v -> {
            selectButton("收入");
            type = "收入";
            categoryPicker.setAdapter(inCategoryAdapter);
        });

        outButton.setOnClickListener(v -> {
            selectButton("支出");
            type = "支出";
            categoryPicker.setAdapter(outCategoryAdapter);
        });

        //設置返回、完成更動、刪除按鈕的功能
        //刪除現有資料
        deleteButton.setOnClickListener(v -> {
            accountViewModel.deleteAccounts(new Account(
                    intent.getIntExtra("Id",0)));
            finish();
        });

        //新增完成
        addButton.setOnClickListener(v -> {
            if(TextUtils.isEmpty(amountEditor.getText())) {
                showToast(v.getContext(),"請輸入金額");
            } else{
                try {
                    Integer.parseInt(amountEditor.getText().toString());
                }catch (Exception e) {
                    showToast(v.getContext(),"請輸入合法金額");
                    return;
                }
                if(Integer.parseInt(amountEditor.getText().toString())<0)
                    showToast(v.getContext(),"請輸入大於0的金額");
                else{
                    accountViewModel.insertAccounts(new Account(
                            assetPicker.getSelectedItem().toString(),
                            type,
                            Integer.parseInt(amountEditor.getText().toString()),
                            categoryPicker.getSelectedItem().toString(),
                            "",
                            calendar,
                            noteEditor.getText().toString()));
                    amountEditor.getBackground().clearColorFilter();
                    noteEditor.getBackground().clearColorFilter();
                    finish();
                    if(settingData.getBoolean("ifRemind", false)){
                        if(accountViewModel.getDayAmount(calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH),
                                "支出") >= settingData.getInt("budget",0))
                            showToast(v.getContext(),"注意！已超過每日預算！");
                    }
                }
            }
        });

        //編輯完成
        saveButton.setOnClickListener(v -> {
            if(TextUtils.isEmpty(amountEditor.getText())) {
                showToast(v.getContext(),"請輸入金額");
            } else{
                try {
                    Integer.parseInt(amountEditor.getText().toString());
                }catch (Exception e) {
                    showToast(v.getContext(),"請輸入合法金額");
                    return;
                }
                if(Integer.parseInt(amountEditor.getText().toString())<0)
                    showToast(v.getContext(),"請輸入大於0的金額");
                else{
                    accountViewModel.updateAccounts(new Account(
                            intent.getIntExtra("Id", 0),
                            assetPicker.getSelectedItem().toString(),
                            type,
                            Integer.parseInt(amountEditor.getText().toString()),
                            categoryPicker.getSelectedItem().toString(),
                            "",
                            calendar,
                            noteEditor.getText().toString()));
                    amountEditor.getBackground().clearColorFilter();
                    noteEditor.getBackground().clearColorFilter();
                    finish();
                    if(settingData.getBoolean("ifRemind", false)){
                        if(accountViewModel.getDayAmount(calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH),
                                "支出") >= settingData.getInt("budget",0))
                            showToast(v.getContext(),"注意！已超過每日預算！");
                    }
                }
            }
        });
    }

    // 選日期，選完自動跳去選時間
    public void datePicker(View v){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), (view, year1, month1, day1) -> {
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.DAY_OF_MONTH, day1);
            datePickButton.setText(date.format(calendar.getTime()));  //set initial value
        }, year, month, day).show();
    }

    // 選時間
    public void timePicker(View v){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(v.getContext(), (view, hour1, minute1) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour1);
            calendar.set(Calendar.MINUTE, minute1);
            timePickButton.setText(time.format(calendar.getTime()));  //set initial value
        }, hour, minute, false).show();
    }

    @SuppressLint("ResourceAsColor")
    public void selectButton(String type){
        if(type.equals("收入")){
            inButton.setTextColor(this.getResources().getColor(R.color.blue)); //藍
            inButton.setBackgroundColor(this.getResources().getColor(R.color.white)); //白
            inButton.setSelected(true);
            outButton.setSelected(false);
            outButton.setTextColor(this.getResources().getColor(R.color.deepGray)); //深灰
            outButton.setBackgroundColor(this.getResources().getColor(R.color.lightGray)); //淺灰
            saveButton.setBackgroundColor(this.getResources().getColor(R.color.blue)); //藍
            addButton.setBackgroundColor(this.getResources().getColor(R.color.blue)); //藍
            inButtonLine.setVisibility(View.VISIBLE);
            outButtonLine.setVisibility(View.GONE);
        }
        else if(type.equals("支出")){
            outButton.setTextColor(this.getResources().getColor(R.color.red)); //紅
            outButton.setBackgroundColor(this.getResources().getColor(R.color.white)); //白
            outButton.setSelected(true);
            inButton.setSelected(false);
            inButton.setTextColor(this.getResources().getColor(R.color.deepGray)); //深灰
            inButton.setBackgroundColor(this.getResources().getColor(R.color.lightGray)); //淺灰
            saveButton.setBackgroundColor(this.getResources().getColor(R.color.red)); //紅
            addButton.setBackgroundColor(this.getResources().getColor(R.color.red)); //紅
            inButtonLine.setVisibility(View.GONE);
            outButtonLine.setVisibility(View.VISIBLE);
        }
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
