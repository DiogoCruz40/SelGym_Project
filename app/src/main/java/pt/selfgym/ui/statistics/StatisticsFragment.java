package pt.selfgym.ui.statistics;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.StatInterface;
import pt.selfgym.MainActivity;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.databinding.FragmentStatisticsBinding;
import pt.selfgym.dtos.DateDTO;
import pt.selfgym.dtos.EventDTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.ui.workouts.WorkoutViewModel;

public class StatisticsFragment extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    private FragmentStatisticsBinding binding;
    private ActivityInterface activityInterface;
    private View view;

    private SharedViewModel mViewModel;

    private ActivityInterface a;

    private PieChart piePolyLineChart;
    private LineChart lineChart;
    private BarChart barChart;

    private  DateDTO trimester;
    private int trimesterWeek;

    private Dictionary<String, Integer> eventsPerWeek = new Hashtable<String, Integer>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        a = (ActivityInterface) context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(a.getMainActivity()).get(SharedViewModel.class);

        binding = FragmentStatisticsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart();
        piePolyLineChart();
        lineChart();

        mViewModel.getWorkoutsTop5().observe(getViewLifecycleOwner(), top5 -> {

            if(!top5.isEmpty()){
                barChartData(top5);
            }

        });

        mViewModel.getStats().observe(getViewLifecycleOwner(), stats -> {

            if(!stats.isEmpty()){
                piePolyLineChartData(stats);
            }
        });

        mViewModel.getEventsCa().observe(getViewLifecycleOwner(), events -> {

            if(events.isEmpty()){
                DateDTO today = new DateDTO(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
                trimesterWeek = week(trimester(today));
            }
            else{
                trimesterWeek = week(trimester(events.get(events.size()-1).getDate()));

                eventsPerWeek = new Hashtable<String, Integer>();

                for(EventDTO e: events){

                    int week = week(e.getDate());
                    String key = week + "_" + e.getDate().getYear();
                    Integer value = eventsPerWeek.get(key);

                    if(value==null){
                        eventsPerWeek.put(key, 1);
                    }
                    else{
                        eventsPerWeek.put(key, value + 1);
                    }
                }

                //lineChartData();

            }

        });

    }

    public int week(DateDTO date){

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, date.getDay());
        c.set(Calendar.MONTH, date.getMonth() - 1);
        c.set(Calendar.YEAR, date.getYear());

        c.getWeekYear();

        return c.get(Calendar.WEEK_OF_YEAR);

    }

    public DateDTO trimester(DateDTO date){

        int month = date.getMonth();

        if (month >= Calendar.JANUARY && month <= Calendar.MARCH) {
          return new DateDTO(1, 1, date.getYear());
        } else if (month >= Calendar.APRIL && month <= Calendar.JUNE) {
          return new DateDTO(1, 4, date.getYear());
        } else if (month >= Calendar.JULY && month <= Calendar.SEPTEMBER) {
          return new DateDTO(1, 7, date.getYear());
        } else {
          return new DateDTO(1, 10, date.getYear());
        }
    }

    public void barChart(){

        BarChart chartUI = a.getMainActivity().findViewById(R.id.barChart);
        chartUI.setOnChartValueSelectedListener(this);

        chartUI.setDrawGridBackground(false);
        chartUI.getDescription().setEnabled(false);
        chartUI.setDrawBorders(false);

        chartUI.setDrawValueAboveBar(true);

        chartUI.setPinchZoom(false);

        chartUI.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        XAxis xAxis = chartUI.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(5);

        chartUI.getAxisLeft().setEnabled(false);
        chartUI.getAxisRight().setEnabled(false);

        Legend l = chartUI.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        barChart = chartUI;

    }

    public void barChartData(List<WorkoutDTO> top5){

        BarDataSet set;
        BarData data = new BarData();
        ArrayList<BarEntry> values;
        WorkoutDTO workout;

        for(int i=0; i<top5.size(); i++){

            workout = top5.get(i);

            values = new ArrayList<>();
            values.add(new BarEntry(i,workout.getNrOfConclusions()));

            set = new BarDataSet(values, workout.getName());
            set.setColor(colors[ i%colors.length ]);
            set.setDrawIcons(false);

            data.addDataSet(set);
        }

        data.setValueFormatter(new LargeValueFormatter());

        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        barChart.setData(data);
    }

    public void piePolyLineChart(){

        piePolyLineChart = a.getMainActivity().findViewById(R.id.piePolyLineChart);
        piePolyLineChart.setUsePercentValues(true);
        piePolyLineChart.getDescription().setEnabled(false);
        piePolyLineChart.setExtraOffsets(5, 10, 5, 5);

        piePolyLineChart.setDragDecelerationFrictionCoef(0.95f);

        piePolyLineChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        piePolyLineChart.setDrawHoleEnabled(true);
        //piePolyLineChart.setHoleColor(Color.WHITE);

        //piePolyLineChart.setTransparentCircleColor(Color.WHITE);
        piePolyLineChart.setTransparentCircleAlpha(110);

        piePolyLineChart.setHoleRadius(58f);
        piePolyLineChart.setTransparentCircleRadius(61f);

        piePolyLineChart.setRotationAngle(0);
        piePolyLineChart.setRotationEnabled(true);
        piePolyLineChart.setHighlightPerTapEnabled(true);

        piePolyLineChart.setOnChartValueSelectedListener(this);

        piePolyLineChart.animateY(1400, Easing.EaseInOutQuad);

        piePolyLineChart.setDrawCenterText(true);
        piePolyLineChart.setDrawEntryLabels(false);
        piePolyLineChart.setCenterText("Workout Distribution");
        piePolyLineChart.setCenterTextColor(Color.BLACK);

        Legend l = piePolyLineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextColor(Color.BLACK);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void piePolyLineChartData(Dictionary stats) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (Enumeration<String> k = stats.keys(); k.hasMoreElements();) {
            String key = k.nextElement();
            int value = (int) stats.get(key);
            if(!(value==0)){
                entries.add(new PieEntry((float) value/ 5, key));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);

        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);

        piePolyLineChart.setData(data);
        piePolyLineChart.invalidate();
    }

    public void lineChart(){

        LineChart chartUI;

        chartUI = a.getMainActivity().findViewById(R.id.lineChart);

        chartUI.setOnChartValueSelectedListener(this);

        chartUI.setDrawGridBackground(false);
        chartUI.getDescription().setEnabled(false);
        chartUI.setDrawBorders(false);

        chartUI.getAxisLeft().setEnabled(false);
        chartUI.getAxisRight().setDrawAxisLine(false);
        chartUI.getAxisRight().setDrawGridLines(false);
        chartUI.getXAxis().setDrawAxisLine(false);
        chartUI.getXAxis().setDrawGridLines(false);
        chartUI.getXAxis().setAvoidFirstLastClipping(true);
        chartUI.setTouchEnabled(true);
        chartUI.setDragEnabled(true);
        chartUI.setScaleEnabled(true);

        chartUI.setPinchZoom(false);

        Legend l = chartUI.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYOffset(10f);
        l.setTextSize(15);
        l.setTextColor(Color.rgb(0, 0, 0));

        chartUI.getXAxis().setCenterAxisLabels(true);
        //chartUI.getXAxis().setGranularity(1f);
        /*
        chartUI.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yy");

            @Override
            public String getFormattedValue(float value) {

                long millis = TimeUnit.DAYS.toMillis((long) value);
                long actualDate = (long) millis;// + firstDate*1000;
                return mFormat.format(new Date(actualDate));
            }
        });*/

        lineChart  = chartUI;

    }

    public void lineChartData(){

        ArrayList<Entry> weeks = new ArrayList<>();

        for (Enumeration<String> k = eventsPerWeek.keys(); k.hasMoreElements();) {
            String key = k.nextElement();
            int value = (int) eventsPerWeek.get(key);
            int week;

            if(key.length() == 6){
                week = Character.getNumericValue(key.charAt(0));
            }
            else{
                week = Character.getNumericValue(key.charAt(0))*10 + Character.getNumericValue(key.charAt(1));
            }

            if(trimesterWeek>50){
                trimesterWeek = 0;
            }

            if(week >= trimesterWeek && week < trimesterWeek + 12){
                weeks.add(new Entry((float) week, (float) value));
            }
        }

        LineDataSet line = new LineDataSet(weeks, "OI");

        line.setLineWidth(2f);
        line.setCircleRadius(3f);
        line.setColor(colors[2]);
        line.setCircleColor(colors[2]);
        line.setDrawValues(false);

        LineData data = new LineData(line);

        lineChart.resetTracking();
        lineChart.setData(data);
        lineChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private final int[] colors = new int[] {
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2],
            ColorTemplate.VORDIPLOM_COLORS[3],
            ColorTemplate.VORDIPLOM_COLORS[4]
            //more colors
            //ColorTemplate.JOYFUL_COLORS
            //ColorTemplate.COLORFUL_COLORS
            //ColorTemplate.LIBERTY_COLORS
            // ColorTemplate.PASTEL_COLORS
    };

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        /*
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            chart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)*/

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart long pressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart fling. VelocityX: " + velocityX + ", VelocityY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {}
}