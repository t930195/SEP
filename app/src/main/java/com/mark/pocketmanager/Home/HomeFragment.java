package com.mark.pocketmanager.Home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.mark.pocketmanager.Account.Account;
import com.mark.pocketmanager.Account.AccountViewModel;
import com.mark.pocketmanager.Category.Category;
import com.mark.pocketmanager.Category.CategoryViewModel;
import com.mark.pocketmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private Button monthPicker;
    private TextView inAmount, outAmount, sumAmount, noData;
    private RecyclerView externalRecyclerView;
    private ExAdapter exAdapter;
    private List<Account> data = new ArrayList<>();
    private AccountViewModel accountViewModel;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM月");
    private final Calendar date = Calendar.getInstance();
    private LiveData<List<Account>> listLiveData = null;
    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        CategoryViewModel categoryViewModel;
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.insertCategories(new Category(-1));
        categoryViewModel.deleteCategories(new Category(-1));
        ImageButton lastMonth = view.findViewById(R.id.lastMonth);
        ImageButton nextMonth = view.findViewById(R.id.nextMonth);
        inAmount = view.findViewById(R.id.inAmount);
        outAmount = view.findViewById(R.id.outAmount);
        sumAmount = view.findViewById(R.id.sumAmount);
        noData = view.findViewById(R.id.noData);
        monthPicker = view.findViewById(R.id.monthPicker);
        monthPicker.setText(dateFormat.format(date.getTime()));
        externalRecyclerView = view.findViewById(R.id.externalRecyclerView);
        externalRecyclerView.setHasFixedSize(true);
        externalRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        context = externalRecyclerView.getContext();
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        resetLiveData();

        monthPicker.setOnClickListener(v -> {
            new RackMonthPicker(this.getActivity())
                    .setLocale(Locale.TRADITIONAL_CHINESE)
                    .setNegativeText("取消")
                    .setPositiveText("確認")
                    .setPositiveButton((month, startDate, endDate, year, monthLabel) -> {
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, month-1);
                        monthPicker.setText(dateFormat.format(date.getTime()));
                        resetLiveData();
                    })
                    .setNegativeButton(Dialog::cancel).show();
        });

        lastMonth.setOnClickListener(v -> {
            date.add(Calendar.MONTH,-1);
            monthPicker.setText(dateFormat.format(date.getTime()));
            resetLiveData();
        });

        nextMonth.setOnClickListener(v -> {
            date.add(Calendar.MONTH,1);
            monthPicker.setText(dateFormat.format(date.getTime()));
            resetLiveData();
        });

        FloatingActionButton previousStep = view.findViewById(R.id.previousStep);
        previousStep.setOnClickListener(v -> {
            //TODO
        });

        FloatingActionButton nextStep = view.findViewById(R.id.nextStep);
        nextStep.setOnClickListener(v -> {
            //TODO
        });

        FloatingActionButton editor = view.findViewById(R.id.editor);
        editor.setOnClickListener(v -> {
            //TODO
        });

        FloatingActionButton adder = view.findViewById(R.id.adder);
        adder.setOnClickListener(v -> {
            Intent intent = new Intent(HomeFragment.this.getContext(), AddOrEditActivity.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
        });
        return view;
    }
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void resetLiveData(){
        if(listLiveData != null && listLiveData.hasActiveObservers()){
            listLiveData.removeObservers(HomeFragment.this.getViewLifecycleOwner());
        }
        listLiveData = accountViewModel.getAccountsLive(date.get(Calendar.YEAR), date.get(Calendar.MONTH));
        listLiveData.observe(HomeFragment.this.getViewLifecycleOwner(), accounts -> {
            long inAmountValue = accountViewModel.getMonthAmount(date.get(Calendar.YEAR), date.get(Calendar.MONTH),"收入");
            long outAmountValue = accountViewModel.getMonthAmount(date.get(Calendar.YEAR), date.get(Calendar.MONTH),"支出");
            long sumAmountValue = inAmountValue - outAmountValue;
            inAmount.setText(Long.toString(inAmountValue));
            outAmount.setText(Long.toString(outAmountValue));
            sumAmount.setText(Long.toString(sumAmountValue));
            data = accounts;
            Log.e("size",Integer.toString(accounts.size()));
            if(accounts.size() != 0)
                noData.setVisibility(View.GONE);
            else
                noData.setVisibility(View.VISIBLE);
            exAdapter = new ExAdapter(data, context, accountViewModel);
            externalRecyclerView.setAdapter(exAdapter);
            exAdapter.notifyDataSetChanged();
        });
    }

}