package com.barackbao.electricmonitoring.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.barackbao.electricmonitoring.R;
import com.barackbao.electricmonitoring.bean.DetailItem;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wangtianrui on 2018/6/2.
 */

public class DetailHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.linechart)
    LineChart mLineChart;
    @BindView(R.id.piechart)
    PieChart pieChart;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.value)
    TextView value;

    public DetailHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bindView(DetailItem item) {
        setPie(item.getPieList());
        setLinechart();
        setLineData(item.getLineList(),item);
        title.setText(item.getTitle());
        value.setText(item.getValue());
    }


    private void setPie(ArrayList<PieEntry> entries) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        pieChart.setDragDecelerationFrictionCoef(0.95f);//设置滑动减速摩擦系数，在0~1之间
        //设置中间文件
        // pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawSliceText(false);//设置隐藏饼图上文字，只显示百分比
        pieChart.setDrawHoleEnabled(false);//设置为TRUE时，饼中心透明
        pieChart.setHoleColor(Color.WHITE);//设置饼中心颜色

        pieChart.setTransparentCircleColor(Color.WHITE);//透明的圆
        pieChart.setTransparentCircleAlpha(110);//透明度

        //pieChart.setHoleRadius(58f);//中间圆的半径占总半径的百分数
        pieChart.setHoleRadius(0);//实心圆
        //pieChart.setTransparentCircleRadius(61f);//// 半透明圈

        pieChart.setDrawCenterText(true);//绘制显示在饼图中心的文本

        pieChart.setRotationAngle(0);//设置一个抵消RadarChart的旋转度
        // 触摸旋转
        pieChart.setRotationEnabled(true);//通过触摸使图表旋转
        pieChart.setHighlightPerTapEnabled(true);//通过点击手势突出显示的值
        pieSetData(entries);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

//        Legend l = pieChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);

        // 输入标签样式
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
        /**
         * 设置比例图
         */
        Legend mLegend = pieChart.getLegend();
        mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);  //在左边中间显示比例图
        mLegend.setFormSize(14f);//比例块字体大小
        mLegend.setXEntrySpace(4f);//设置距离饼图的距离，防止与饼图重合
        mLegend.setYEntrySpace(4f);
        //设置比例块换行...
        mLegend.setWordWrapEnabled(true);
        mLegend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//设置字跟图表的左右顺序

        //mLegend.setTextColor(getResources().getColor(R.color.alpha_80));
        mLegend.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块
    }

    //设置数据
    private void pieSetData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(2f);//饼图区块之间的距离
        dataSet.setSelectionShift(5f);//

        //数据和颜色
        Integer[] colors = new Integer[]{Color.parseColor("#ffb980"), Color.parseColor("#2ec7c9"), Color.parseColor("#FFFF0900"),
                Color.parseColor("#d87a80"), Color.parseColor("#ffb980"), Color.parseColor("#8d98b3")};
        //添加对应的颜色值
        List<Integer> colorSum = new ArrayList<>();
        for (Integer color : colors) {
            colorSum.add(color);
        }
        dataSet.setColors(colorSum);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.highlightValues(null);//在给定的数据集中突出显示给定索引的值
        //刷新
        //pieChart.setDrawSliceText(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.invalidate();
    }

    private void setLinechart() {
        //描述信息
//        Description description = new Description();
//        description.setText("我是描述信息");
//        //设置描述信息
//        mLineChart.setDescription(description);
        //设置没有数据时显示的文本
        mLineChart.setNoDataText("没有数据喔~~");
        //设置是否绘制chart边框的线
        mLineChart.setDrawBorders(true);
        //设置chart边框线颜色
        mLineChart.setBorderColor(Color.GRAY);
        //设置chart边框线宽度
        mLineChart.setBorderWidth(1f);
        //设置chart是否可以触摸
        mLineChart.setTouchEnabled(true);
        //设置是否可以拖拽
        mLineChart.setDragEnabled(true);
        //设置是否可以缩放 x和y，默认true
        mLineChart.setScaleEnabled(false);
        //设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setDoubleTapToZoomEnabled(false);
        //设置chart动画
        mLineChart.animateXY(1000, 1000);

        //=========================设置图例=========================
        // 像"□ xxx"就是图例
        Legend legend = mLineChart.getLegend();
        //设置图例显示在chart那个位置 setPosition建议放弃使用了
        //设置垂直方向上还是下或中
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置水平方向是左边还是右边或中
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //设置所有图例位置排序方向
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //设置图例的形状 有圆形、正方形、线
        legend.setForm(Legend.LegendForm.CIRCLE);
        //是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
        legend.setWordWrapEnabled(true);


        //=======================设置X轴显示效果==================
        XAxis xAxis = mLineChart.getXAxis();
        //是否启用X轴
        xAxis.setEnabled(true);
        //是否绘制X轴线
        xAxis.setDrawAxisLine(true);
        //设置X轴上每个竖线是否显示
        xAxis.setDrawGridLines(true);
        //设置是否绘制X轴上的对应值(标签)
        xAxis.setDrawLabels(true);
        //设置X轴显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置竖线为虚线样式
        // xAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置x轴标签数
        xAxis.setLabelCount(5, true);
        //图表第一个和最后一个label数据不超出左边和右边的Y轴
        // xAxis.setAvoidFirstLastClipping(true);

    }

    private void setLineData(ArrayList<Entry> pointValues,DetailItem item) {
        //点构成的某条线
        LineDataSet lineDataSet = new LineDataSet(pointValues, item.getTitle());
        //设置该线的颜色
        lineDataSet.setColor(Color.RED);
        //设置每个点的颜色
        lineDataSet.setCircleColor(Color.YELLOW);
        //设置该线的宽度
        lineDataSet.setLineWidth(1f);
        //设置每个坐标点的圆大小
        //lineDataSet.setCircleRadius(1f);
        //设置是否画圆
        lineDataSet.setDrawCircles(false);
        // 设置平滑曲线模式
        //  lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //设置线一面部分是否填充颜色
        lineDataSet.setDrawFilled(true);
        //设置填充的颜色
        lineDataSet.setFillColor(Color.BLUE);
        //设置是否显示点的坐标值
        lineDataSet.setDrawValues(false);

        //线的集合（可单条或多条线）
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        //把要画的所有线(线的集合)添加到LineData里
        LineData lineData = new LineData(dataSets);
        //把最终的数据setData
        mLineChart.setData(lineData);
    }
}
