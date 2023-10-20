package com.mark.pocketmanager.Setting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import com.mark.pocketmanager.Account.AccountViewModel;
import com.mark.pocketmanager.Category.Category;
import com.mark.pocketmanager.Category.CategoryViewModel;
import com.mark.pocketmanager.R;

public class CategoryEditActivity extends AppCompatActivity {
    private Button save, delete;
    private EditText categoryEditText;
    private CategoryViewModel categoryViewModel;
    private AccountViewModel accountViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        int id = intent.getIntExtra("id", 0);
        String category = intent.getStringExtra("category");

        View actionBar = findViewById(R.id.my_actionBar);
        ImageButton backButton = actionBar.findViewById(R.id.backButton);
        TextView title = actionBar.findViewById(R.id.title);
        title.setText("編輯" + type + "類別");
        backButton.setOnClickListener(v -> {
            finish();
        });

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        categoryEditText = findViewById(R.id.categoryEditText);
        categoryEditText.setText(category);

        save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            if(TextUtils.isEmpty(categoryEditText.getText())) {
                //如果輸入為空
                showToast(v.getContext(),"請輸入類別名稱");
            } else if(categoryEditText.getText().toString().equals(category)) {
                //如果是一樣的名稱
                finish();
            } else {
                //如果輸入不為空也不是一樣的名稱
                if(categoryViewModel.getCategory(type, categoryEditText.getText().toString()).size() != 0){
                    showToast(v.getContext(),"此類別名稱已存在");
                } else if(accountViewModel.getAccountCategory(type, categoryEditText.getText().toString()).size() != 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("警告");
                    alertDialog.setMessage("發現有帳目中有相同的類別名稱\n確定要整合兩筆不同類別的資料嗎？");
                    alertDialog.setPositiveButton("確定",((dialog, which) -> {}));
                    alertDialog.setNeutralButton("取消",((dialog, which) -> {}));
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v2 -> {
                        categoryViewModel.updateCategories(new Category(id, type, categoryEditText.getText().toString()));
                        accountViewModel.afterCategoryEdit(type, category, categoryEditText.getText().toString());
                        finish();
                        dialog.dismiss();
                    });
                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v2 -> {
                        dialog.dismiss();
                    });
                } else {
                    categoryViewModel.updateCategories(new Category(id, type, categoryEditText.getText().toString()));
                    accountViewModel.afterCategoryEdit(type, category, categoryEditText.getText().toString());
                    finish();
                }
            }
        });

        delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(v -> {
            categoryViewModel.deleteCategories(new Category(id));
            finish();
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