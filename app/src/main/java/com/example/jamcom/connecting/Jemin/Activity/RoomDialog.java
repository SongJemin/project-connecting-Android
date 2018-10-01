package com.example.jamcom.connecting.Jemin.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jamcom.connecting.R;
import com.example.jamcom.connecting.Jemin.Calendar.OneDayDecorator;
import com.example.jamcom.connecting.Jemin.Calendar.SaturdayDecorator;
import com.example.jamcom.connecting.Jemin.Calendar.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

public class RoomDialog extends Dialog{
    MaterialCalendarView materialCalendarView;
    private ImageButton confirmBtn, cancelBtn;
    private ImageView setLocBtn;
    AppCompatActivity roomActivity;
    TextView rangeTv;
    String startZeroMonth;
    String startZeroDay;
    String finishZeroMonth;
    String finishZeroDay;

    public RoomDialog(AppCompatActivity activity) {
        super(activity);
        roomActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_setting);
        materialCalendarView= (MaterialCalendarView) findViewById(R.id.m_calendarView);
        confirmBtn = (ImageButton) findViewById(R.id.setting_btn_confirm);
        cancelBtn = (ImageButton) findViewById(R.id.setting_btn_cancel);
        rangeTv = (TextView) findViewById(R.id.setting_tv_range);

        setLocBtn = (ImageView) findViewById(R.id.setting_btn_setloc);


        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator());

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent3 = new Intent(roomActivity, RoomViewActivity.class);
                roomActivity.startActivity(intent3);

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        setLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(roomActivity, MapViewActivity.class);
                roomActivity.startActivity(intent2);
            }
        });

        materialCalendarView. setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        rangeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyYearMonthPickerDialog pd = new MyYearMonthPickerDialog();
                pd.setOnConfirmDateListener(new MyYearMonthPickerDialog.OnConfirmDateListener() {
                    @Override
                    public void onConfirmDateListener(int startYear, int startMonth, int startDay, int finishYear, int finishMonth, int finishDay) {
                        if(startMonth <10)
                        {
                            startZeroMonth = "0"+startMonth;
                        }
                        else
                        {
                            startZeroMonth=Integer.toString(startMonth);
                        }
                        if(startDay <10)
                        {
                            startDay+=1;
                            startZeroDay = "0"+startDay;
                        }
                        else
                        {
                            startZeroDay=Integer.toString(startDay+1);
                        }
                        if(finishMonth <10)
                        {
                            finishZeroMonth = "0"+finishMonth;
                        }
                        else
                        {
                            finishZeroMonth=Integer.toString(finishMonth);
                        }
                        if(finishDay <10)
                        {
                            finishDay+=1;
                            finishZeroDay = "0"+finishDay;
                        }
                        else
                        {
                            finishZeroDay=Integer.toString(finishDay+1);
                        }
                        String startMonthStr = startYear + ". " + startZeroMonth + ". " + startZeroDay;
                        String finishMonthStr = finishYear + ". " + finishZeroMonth + ". " + finishZeroDay;
                        rangeTv.setText(startMonthStr + " ~ " + finishMonthStr);
                    }
                });
                pd.show(roomActivity.getFragmentManager(), "YearMonthPickerTest");

            }
        });

    }
}
