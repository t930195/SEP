package com.mark.pocketmanager.Setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mark.pocketmanager.Category.Category;
import com.mark.pocketmanager.Category.CategoryViewModel;
import com.mark.pocketmanager.R;

public class CategoryAddActivity extends AppCompatActivity {
    private Button save;
    private CategoryViewModel categoryViewModel;
    private EditText categoryEditText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        View actionBar = findViewById(R.id.my_actionBar);
        ImageButton backButton = actionBar.findViewById(R.id.backButton);
        TextView title = actionBar.findViewById(R.id.title);
        title.setText("新增" + type + "類別");
        backButton.setOnClickListener(v -> {
            finish();
        });

        categoryEditText = findViewById(R.id.categoryEditText);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            if(TextUtils.isEmpty(categoryEditText.getText())){
                showToast(v.getContext(),"請輸入類別名稱");
            } else if(categoryViewModel.getCategory(type, categoryEditText.getText().toString()).size() != 0){
                showToast(v.getContext(),"此類別名稱已存在");
            } else{
                categoryViewModel.insertCategories(new Category(type, categoryEditText.getText().toString()));
                finish();
            }
        });
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