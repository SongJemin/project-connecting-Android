package com.example.jamcom.connecting.Jemin.Calendar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.example.jamcom.connecting.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectedDecorator implements DayViewDecorator {
    //private static final int color = Color.parseColor("#228BC34A");
    private static final int color = Color.parseColor("#CCCCFF");
    private final Drawable highlightDrawable = new ColorDrawable(color);;
    private final Calendar calendar = Calendar.getInstance();
    private ArrayList<CalendarDay> dates;

    public SelectedDecorator(ArrayList<CalendarDay> dates) {
        this.dates = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.setBackgroundDrawable(highlightDrawable);
    }
}