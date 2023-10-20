package com.mark.pocketmanager.Setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mark.pocketmanager.Category.Category;
import com.mark.pocketmanager.Category.CategoryViewModel;
import com.mark.pocketmanager.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {
    FloatingActionButton add;
    private RecyclerView categoryRecyclerView;
    private RecyclerView.LayoutManager caLayoutManager;
    private CaAdapter caAdapter;
    private List<Category> categoryData = new ArrayList<>();
    private CategoryViewModel categoryViewModel;
    private LiveData<List<Category>> categoryLiveData = null;
    private String type;

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        type = getIntent().getStringExtra("type");
        View actionBar = findViewById(R.id.my_actionBar);
        ImageButton backButton = actionBar.findViewById(R.id.backButton);
        TextView title = actionBar.findViewById(R.id.title);
        title.setText(type + "類別");
        backButton.setOnClickListener(v -> {
            finish();
        });

        Log.e("size:", Integer.toString(categoryData.size()));
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryLiveData = categoryViewModel.getCategoriesLive(type);
        categoryLiveData.observe(this, categories -> {
            categoryData = categories;
            caAdapter.notifyDataSetChanged();
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type", type);
            intent.setClass(CategoryListActivity.this, CategoryAddActivity.class);
            startActivity(intent);
        });

        categoryRecyclerView = findViewById(R.id.categoryRecyclerview);
        categoryRecyclerView.setHasFixedSize(true);
        caLayoutManager = new LinearLayoutManager(this);
        categoryRecyclerView.setLayoutManager(caLayoutManager);

        caAdapter = new CaAdapter();
        categoryRecyclerView.setAdapter(caAdapter);
    }



    private class CaAdapter extends RecyclerView.Adapter<CaAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public TextView category;

            public MyViewHolder(View v){
                super(v);
                itemView = v;
                category = itemView.findViewById(R.id.category);
            }
        }

        @NonNull
        @Override
        public CaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.single_category, parent,false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull CaAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.category.setText(categoryData.get(position).getCategory());
            holder.itemView.setOnClickListener(v -> {
                Integer id = categoryData.get(position).getId();
                String category = categoryData.get(position).getCategory();
                Intent intent = new Intent(CategoryListActivity.this, CategoryEditActivity.class);
                intent.putExtra("type",type);
                intent.putExtra("category", category);
                intent.putExtra("id", id);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return categoryData.size();
        }
    }
}