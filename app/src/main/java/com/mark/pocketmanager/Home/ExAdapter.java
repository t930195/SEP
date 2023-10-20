package com.mark.pocketmanager.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.pocketmanager.Account.Account;
import com.mark.pocketmanager.Account.AccountViewModel;
import com.mark.pocketmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExAdapter extends RecyclerView.Adapter<ExAdapter.MyViewHolder> {
    private final AccountViewModel accountViewModel;
    private final List<List<Account>> dataGroupByDay = new ArrayList<>();
    private final Context context;

    public ExAdapter(List<Account> data, Context context, AccountViewModel accountViewModel){
        this.context = context;
        this.accountViewModel = accountViewModel;
        int day = 0;
        int index = -1;
        for(int i=0;i<data.size();i++){
            if(data.get(i).getDay() != day) {
                this.dataGroupByDay.add(new ArrayList<>());
                day = data.get(i).getDay();
                index++;
            }
            this.dataGroupByDay.get(index).add(data.get(i));
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView date, dayOfTheWeek, yearAndMonth, dailyInAmount, dailyOutAmount;
        public RecyclerView internalRecyclerView;

        public MyViewHolder(View v){
            super(v);
            itemView = v;
            date = itemView.findViewById(R.id.date);
            dayOfTheWeek = itemView.findViewById(R.id.dayOfTheWeek);
            yearAndMonth = itemView.findViewById(R.id.yearAndMonth);
            dailyInAmount = itemView.findViewById(R.id.dailyInAmount);
            dailyOutAmount = itemView.findViewById(R.id.dailyOutAmount);
            internalRecyclerView = itemView.findViewById(R.id.internalRecyclerView);
        }
    }

    @NonNull
    @Override
    public ExAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.daily_records, parent,false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ExAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM");
        Calendar time = Calendar.getInstance();
        time.set(Calendar.YEAR,dataGroupByDay.get(position).get(0).getYear());
        time.set(Calendar.MONTH,dataGroupByDay.get(position).get(0).getMonth());
        time.set(Calendar.DAY_OF_MONTH,dataGroupByDay.get(position).get(0).getDay());
        holder.itemView.setOnClickListener(v-> {
            Intent intent = new Intent(context, AddOrEditActivity.class);
            intent.putExtra("mode", "addByDate");
            intent.putExtra("Year", dataGroupByDay.get(position).get(0).getYear());
            intent.putExtra("Month", dataGroupByDay.get(position).get(0).getMonth());
            intent.putExtra("Day", dataGroupByDay.get(position).get(0).getDay());
            context.startActivity(intent);
        });
        holder.date.setText(Integer.toString(time.get(Calendar.DAY_OF_MONTH)));
        holder.dayOfTheWeek.setText(weekDays[time.get(Calendar.DAY_OF_WEEK)-1]);
        holder.yearAndMonth.setText(dateFormat.format(time.getTime()));
        holder.dailyInAmount.setText(String.format("$ %d", accountViewModel.getDayAmount(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), "收入")));
        holder.dailyOutAmount.setText(String.format("$ %d", accountViewModel.getDayAmount(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), "支出")));
        holder.internalRecyclerView.setAdapter(new InAdapter(dataGroupByDay.get(position), context));
    }

    @Override
    public int getItemCount() {
        return dataGroupByDay.size();
    }
}
