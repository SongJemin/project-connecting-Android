package com.example.jamcom.connecting.Jemin.Calendar;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * Created by JAMCOM on 2018-04-14.
 */

public class SundayDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();


    public SundayDecorator() {
    }


    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SUNDAY;
    }
}