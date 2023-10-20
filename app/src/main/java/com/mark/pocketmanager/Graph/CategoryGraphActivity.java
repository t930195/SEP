package com.mark.pocketmanager.Graph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mark.pocketmanager.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class CategoryGraphActivity extends AppCompatActivity {
    private RecyclerView categoryIncomeRecyclerView,categoryExpenseRecyclerView;
    private RecyclerView.LayoutManager categoryIncomeLayoutManager,categoryExpenseLayoutManager;
    //private GraphActivity.categoryIncomeGraphAdapter categoryIncomeGraphAdapter;
    //private GraphActivity.categoryExpenseGraphAdapter categoryExpenseGraphAdapter;
    ToggleButton toggleButton;
    PieChart categoryIncomePieChart,categoryExpensePieChart;
    BarChart categoryIncomeBarChart,categoryExpenseBarChart;
    List<PieEntry> CatInPielist = new ArrayList<>() ;
    List<PieEntry> CatExPielist = new ArrayList<>() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_graph);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("CatergoryChart");


        toggleButton = findViewById(R.id.categoryChartToggleButton);//圖表切換按鈕
        categoryIncomePieChart = findViewById(R.id.categoryIncomePieChart);
        categoryIncomeRecyclerView = findViewById(R.id.categoryIncomeRecyclerView);

        //income圖表
        categoryIncomeRecyclerView.setHasFixedSize(true);
        categoryIncomeLayoutManager = new LinearLayoutManager(this);
        categoryIncomeRecyclerView.setLayoutManager(categoryIncomeLayoutManager);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()){
                    case R.id.categoryChartToggleButton:
                        if(compoundButton.isChecked()) {
                            Toast.makeText(CategoryGraphActivity.this,"長條圖模式", Toast.LENGTH_SHORT).show();
                            /*inCatPieChart.setVisibility(View.GONE);
                            exCatPieChart.setVisibility(View.GONE);
                            inCatBarChart.setVisibility(View.VISIBLE);
                            exCatBarChart.setVisibility(View.VISIBLE);*/
                        }
                        else{
                            Toast.makeText(CategoryGraphActivity.this,"圓餅圖模式",Toast.LENGTH_SHORT).show();
                            /*inCatPieChart.setVisibility(View.VISIBLE);
                            exCatPieChart.setVisibility(View.VISIBLE);
                            inCatBarChart.setVisibility(View.GONE);
                            exCatBarChart.setVisibility(View.GONE);*/
                        }
                        break;
                }
            }
        });

        categoryIncomeShow();

    }

    //圓餅圖
    private void categoryIncomeShow() {
        //如果啟用此選項,則圖表中的值將以百分比形式繪製,而不是以原始值繪製
        categoryIncomePieChart.setUsePercentValues(true);
        //如果這個元件應該啟用(應該被繪製)FALSE如果沒有。如果禁用,此元件的任何內容將被繪製預設
        categoryIncomePieChart.getDescription().setEnabled(false);
        //將額外的偏移量(在圖表檢視周圍)附加到自動計算的偏移量
        categoryIncomePieChart.setExtraOffsets(5, 10, 5, 5);
        //較高的值表明速度會緩慢下降 例如如果它設定為0,它會立即停止。1是一個無效的值,並將自動轉換為0.999f。
        categoryIncomePieChart.setDragDecelerationFrictionCoef(0.95f);
        //設定中間字型
        categoryIncomePieChart.setCenterText("收入");
        //設定為true將餅中心清空
        categoryIncomePieChart.setDrawHoleEnabled(true);
        //套孔,繪製在PieChart中心的顏色
        categoryIncomePieChart.setHoleColor(Color.WHITE);
        //設定透明圓應有的顏色。
        categoryIncomePieChart.setTransparentCircleColor(Color.WHITE);
        //設定透明度圓的透明度應該有0 =完全透明,255 =完全不透明,預設值為100。
        categoryIncomePieChart.setTransparentCircleAlpha(110);
        //設定在最大半徑的百分比餅圖中心孔半徑(最大=整個圖的半徑),預設為50%
        categoryIncomePieChart.setHoleRadius(58f);
        //設定繪製在孔旁邊的透明圓的半徑,在最大半徑的百分比在餅圖*(max =整個圖的半徑),預設55% -> 5%大於中心孔預設
        categoryIncomePieChart.setTransparentCircleRadius(61f);
        //將此設定為true,以繪製顯示在pie chart
        categoryIncomePieChart.setDrawCenterText(true);
        //集度的radarchart旋轉偏移。預設270f -->頂(北)
        categoryIncomePieChart.setRotationAngle(0);
        //設定為true,使旋轉/旋轉的圖表觸控。設定為false禁用它。預設值:true
        categoryIncomePieChart.setRotationEnabled(true);
        //將此設定為false,以防止由抽頭姿態突出值。值仍然可以通過拖動或程式設計高亮顯示。預設值:真
        categoryIncomePieChart.setHighlightPerTapEnabled(true);
        //建立Legend物件
        Legend l = categoryIncomePieChart.getLegend();
        //設定垂直對齊of the Legend
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //設定水平of the Legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //設定方向
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        //其中哪一個將畫在圖表或外
        l.setDrawInside(false);
        //設定水平軸上圖例項之間的間距
        l.setXEntrySpace(7f);
        //設定在垂直軸上的圖例項之間的間距
        l.setYEntrySpace(0f);
        //設定此軸上標籤的所使用的y軸偏移量 更高的偏移意味著作為一個整體的Legend將被放置遠離頂部。
        l.setYOffset(0f);
        //設定入口標籤的顏色。
        categoryIncomePieChart.setEntryLabelColor(Color.WHITE);
        //設定入口標籤的大小。預設值:13dp
        categoryIncomePieChart.setEntryLabelTextSize(12f);

        list_set(0);//圖表資料設定
        //設定到PieDataSet物件
        PieDataSet set = new PieDataSet(CatInPielist , "表一") ;
        set.setDrawValues(false);//設定為true,在圖表繪製y
        set.setAxisDependency(YAxis.AxisDependency.LEFT);//設定Y軸,這個資料集應該被繪製(左或右)。預設值:左
        set.setAutomaticallyDisableSliceSpacing(false);//當啟用時,片間距將是0時,最小值要小於片間距本身
        set.setSliceSpace(5f);//間隔
        set.setSelectionShift(10f);//點選伸出去的距離
        /**
         * 設定該資料集前應使用的顏色。顏色使用只要資料集所代表的條目數目高於顏色陣列的大小。
         * 如果您使用的顏色從資源, 確保顏色已準備好(通過呼叫getresources()。getColor(…))之前,將它們新增到資料集
         * */
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        set.setColors(colors);
        //傳入PieData
        PieData data = new PieData(set);
        //為圖表設定新的資料物件
        categoryIncomePieChart.setData(data);
        //重新整理
        categoryIncomePieChart.invalidate();
        //動畫圖上指定的動畫時間軸的繪製
        //incomePieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }

    private void categoryExpenseShow() {
        //如果啟用此選項,則圖表中的值將以百分比形式繪製,而不是以原始值繪製
        categoryExpensePieChart.setUsePercentValues(true);
        //如果這個元件應該啟用(應該被繪製)FALSE如果沒有。如果禁用,此元件的任何內容將被繪製預設
        categoryExpensePieChart.getDescription().setEnabled(false);
        //將額外的偏移量(在圖表檢視周圍)附加到自動計算的偏移量
        categoryExpensePieChart.setExtraOffsets(5, 10, 5, 5);
        //較高的值表明速度會緩慢下降 例如如果它設定為0,它會立即停止。1是一個無效的值,並將自動轉換為0.999f。
        categoryExpensePieChart.setDragDecelerationFrictionCoef(0.95f);
        //設定中間字型
        categoryExpensePieChart.setCenterText("收入");
        //設定為true將餅中心清空
        categoryExpensePieChart.setDrawHoleEnabled(true);
        //套孔,繪製在PieChart中心的顏色
        categoryExpensePieChart.setHoleColor(Color.WHITE);
        //設定透明圓應有的顏色。
        categoryExpensePieChart.setTransparentCircleColor(Color.WHITE);
        //設定透明度圓的透明度應該有0 =完全透明,255 =完全不透明,預設值為100。
        categoryExpensePieChart.setTransparentCircleAlpha(110);
        //設定在最大半徑的百分比餅圖中心孔半徑(最大=整個圖的半徑),預設為50%
        categoryExpensePieChart.setHoleRadius(58f);
        //設定繪製在孔旁邊的透明圓的半徑,在最大半徑的百分比在餅圖*(max =整個圖的半徑),預設55% -> 5%大於中心孔預設
        categoryExpensePieChart.setTransparentCircleRadius(61f);
        //將此設定為true,以繪製顯示在pie chart
        categoryExpensePieChart.setDrawCenterText(true);
        //集度的radarchart旋轉偏移。預設270f -->頂(北)
        categoryExpensePieChart.setRotationAngle(0);
        //設定為true,使旋轉/旋轉的圖表觸控。設定為false禁用它。預設值:true
        categoryExpensePieChart.setRotationEnabled(true);
        //將此設定為false,以防止由抽頭姿態突出值。值仍然可以通過拖動或程式設計高亮顯示。預設值:真
        categoryExpensePieChart.setHighlightPerTapEnabled(true);
        //建立Legend物件
        Legend l = categoryExpensePieChart.getLegend();
        //設定垂直對齊of the Legend
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //設定水平of the Legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //設定方向
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        //其中哪一個將畫在圖表或外
        l.setDrawInside(false);
        //設定水平軸上圖例項之間的間距
        l.setXEntrySpace(7f);
        //設定在垂直軸上的圖例項之間的間距
        l.setYEntrySpace(0f);
        //設定此軸上標籤的所使用的y軸偏移量 更高的偏移意味著作為一個整體的Legend將被放置遠離頂部。
        l.setYOffset(0f);
        //設定入口標籤的顏色。
        categoryExpensePieChart.setEntryLabelColor(Color.WHITE);
        //設定入口標籤的大小。預設值:13dp
        categoryExpensePieChart.setEntryLabelTextSize(12f);
        list_set(1);
        //設定到PieDataSet物件
        PieDataSet set = new PieDataSet(CatExPielist , "表二") ;
        set.setDrawValues(false);//設定為true,在圖表繪製y
        set.setAxisDependency(YAxis.AxisDependency.LEFT);//設定Y軸,這個資料集應該被繪製(左或右)。預設值:左
        set.setAutomaticallyDisableSliceSpacing(false);//當啟用時,片間距將是0時,最小值要小於片間距本身
        set.setSliceSpace(5f);//間隔
        set.setSelectionShift(10f);//點選伸出去的距離
        /**
         * 設定該資料集前應使用的顏色。顏色使用只要資料集所代表的條目數目高於顏色陣列的大小。
         * 如果您使用的顏色從資源, 確保顏色已準備好(通過呼叫getresources()。getColor(…))之前,將它們新增到資料集
         * */
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        set.setColors(colors);
        //傳入PieData
        PieData data = new PieData(set);
        //為圖表設定新的資料物件
        categoryExpensePieChart.setData(data);
        //重新整理
        categoryExpensePieChart.invalidate();
        //動畫圖上指定的動畫時間軸的繪製
        //incomePieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }

    private void list_set(int flag){
        int i;
        //模擬的資料來源
        if(flag==0)//income
        {
            for(i=0;i<10;i++) {
                PieEntry row = new PieEntry(15.8f, "N" + i);
                CatInPielist.add(row);
            }
        }
        else if(flag==1)
        {
            for(i=0;i<10;i++) {
                PieEntry row = new PieEntry(15.8f, "M" + i);
                CatExPielist.add(row);
            }
        }
    }

    private class categoryIncomeGraphAdapter extends RecyclerView.Adapter<categoryIncomeGraphAdapter.MyViewHolder>{
        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public TextView description, money, category;

            public MyViewHolder(View v){
                super(v);
                itemView = v;
                category = itemView.findViewById(R.id.category);
                description = itemView.findViewById(R.id.asset);
                money = itemView.findViewById(R.id.amount);
            }
        }
        @NonNull
        @Override
        public categoryIncomeGraphAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.single_account, parent,false);

            return new categoryIncomeGraphAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull categoryIncomeGraphAdapter.MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Toast.makeText(this, "按下左上角返回鍵", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}