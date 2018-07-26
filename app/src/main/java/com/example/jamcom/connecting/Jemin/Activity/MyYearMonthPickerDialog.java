package com.example.jamcom.connecting.Jemin.Activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.jamcom.connecting.R;

import java.util.Calendar;

/**
 * Created by JAMCOM on 2018-05-22.
 */

public class MyYearMonthPickerDialog extends DialogFragment {
    private OnConfirmDateListener onConfirmDateListener;
    Calendar calendar = Calendar.getInstance();
    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 1980;
    int cDay = calendar.get(Calendar.DAY_OF_MONTH);
    public Calendar cal = Calendar.getInstance();
    int startMonthValue, finishMonthValue;
    int startYearValue, finishYearValue;
    int startDayValue, finishDayValue;
    static int returnValue2;

    public void setOnConfirmDateListener(OnConfirmDateListener onConfirmDateListener) {
        this.onConfirmDateListener = onConfirmDateListener;
    }

    Button btnConfirm;
    Button btnCancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.year_month_picker, null);

        btnConfirm = dialog.findViewById(R.id.btn_date_picker_dlg_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker monthStartPicker = (NumberPicker) dialog.findViewById(R.id.picker_start_month);
        final NumberPicker monthFinishPicker = (NumberPicker) dialog.findViewById(R.id.picker_finish_month);
        final NumberPicker yearStartPicker = (NumberPicker) dialog.findViewById(R.id.picker_start_year);
        final NumberPicker yearFinishPicker = (NumberPicker) dialog.findViewById(R.id.picker_finish_year);
        final NumberPicker dayStartPicker = (NumberPicker) dialog.findViewById(R.id.picker_start_day);
        final NumberPicker dayFinishPicker = (NumberPicker) dialog.findViewById(R.id.picker_finish_day);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyYearMonthPickerDialog.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startMonthValue = monthStartPicker.getValue();
                finishMonthValue = monthFinishPicker.getValue();
                startYearValue = yearStartPicker.getValue();
                finishYearValue = yearFinishPicker.getValue();
                startDayValue = dayStartPicker.getValue();
                finishDayValue = dayFinishPicker.getValue();
                Log.v("asdf", "시작 년 : "+startYearValue + ", 시작 월 : "  +startMonthValue + ", 종료 년 : "+ finishYearValue + ", 종료 월 : " +finishMonthValue );

                onConfirmDateListener.onConfirmDateListener(startYearValue, startMonthValue, startDayValue, finishYearValue, finishMonthValue, finishDayValue);
                MyYearMonthPickerDialog.this.getDialog().cancel();
                returnValue2 = finishMonthValue;
            }
        });

        monthStartPicker.setMinValue(1);
        monthStartPicker.setMaxValue(12);
        monthStartPicker.setValue(cal.get(Calendar.MONTH) + 1);

        int firstYear = cal.get(Calendar.YEAR);
        yearStartPicker.setMinValue(MIN_YEAR);
        yearStartPicker.setMaxValue(MAX_YEAR);
        yearStartPicker.setValue(firstYear);

        String[] firstStringDate = new String[31];
        for (int i = 0; i < 31; i++) {
            firstStringDate[i] = Integer.toString(i + 1);
        }
        dayStartPicker.setDisplayedValues(firstStringDate);
        dayStartPicker.setMaxValue(30);
        dayStartPicker.setMinValue(0);
        dayStartPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        dayStartPicker.setWrapSelectorWheel(true);
        dayStartPicker.setValue(cDay - 1);


        monthFinishPicker.setMinValue(1);
        monthFinishPicker.setMaxValue(12);
        monthFinishPicker.setValue(cal.get(Calendar.MONTH) + 1);

        int secondYear = cal.get(Calendar.YEAR);
        yearFinishPicker.setMinValue(MIN_YEAR);
        yearFinishPicker.setMaxValue(MAX_YEAR);
        yearFinishPicker.setValue(secondYear);

        String[] secondStringDate = new String[31];
        for (int i = 0; i < 31; i++) {
            secondStringDate[i] = Integer.toString(i + 1);
        }
        dayFinishPicker.setDisplayedValues(secondStringDate);
        dayFinishPicker.setMaxValue(30);
        dayFinishPicker.setMinValue(0);
        dayFinishPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        dayFinishPicker.setWrapSelectorWheel(true);
        dayFinishPicker.setValue(cDay - 1);

        builder.setView(dialog);


        return builder.create();
    }


    interface OnConfirmDateListener{
        public void onConfirmDateListener(int startYear, int startMonth, int startDay, int finishYear, int finishMonth, int finishDay);
    }
}
