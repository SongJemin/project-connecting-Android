package com.example.jamcom.connecting.Jemin.Fragment;

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

public class DatePickerDialog extends DialogFragment {
    private OnConfirmDateListener onConfirmDateListener;
    Calendar calendar = Calendar.getInstance();
    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 1980;
    int cDay = calendar.get(Calendar.DAY_OF_MONTH);
    public Calendar cal = Calendar.getInstance();
    int selectMonthValue;
    int selectYearValue;
    int selectDayValue;

    public void setOnConfirmDateListener(OnConfirmDateListener onConfirmDateListener) {
        this.onConfirmDateListener = onConfirmDateListener;
    }

    Button btnConfirm;
    Button btnCancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_select_datepicker, null);

        btnConfirm = dialog.findViewById(R.id.datepicker_confirm_btm);
        btnCancel = dialog.findViewById(R.id.datepicker_cancel_btn);

        final NumberPicker yearSelectPicker = (NumberPicker) dialog.findViewById(R.id.select_picker_start_year);
        final NumberPicker monthSelectPicker = (NumberPicker) dialog.findViewById(R.id.select_picker_start_month);
        final NumberPicker daySelectPicker = (NumberPicker) dialog.findViewById(R.id.select_picker_start_day);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatePickerDialog.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                selectYearValue = yearSelectPicker.getValue();
                selectMonthValue = monthSelectPicker.getValue();
                selectDayValue = daySelectPicker.getValue();
                Log.v("datepicker", "선택 년도 : "+selectYearValue + ", 선택 월 : "  +selectMonthValue + ", 선택 일 : "+ selectDayValue);

                onConfirmDateListener.onConfirmDateListener(selectYearValue, selectMonthValue, selectDayValue);
                DatePickerDialog.this.getDialog().cancel();
            }
        });

        monthSelectPicker.setMinValue(1);
        monthSelectPicker.setMaxValue(12);
        monthSelectPicker.setValue(cal.get(Calendar.MONTH) + 1);

        int firstYear = cal.get(Calendar.YEAR);
        yearSelectPicker.setMinValue(MIN_YEAR);
        yearSelectPicker.setMaxValue(MAX_YEAR);
        yearSelectPicker.setValue(firstYear);

        String[] firstStringDate = new String[31];
        for (int i = 0; i < 31; i++) {
            firstStringDate[i] = Integer.toString(i + 1);
        }
        daySelectPicker.setDisplayedValues(firstStringDate);
        daySelectPicker.setMaxValue(30);
        daySelectPicker.setMinValue(0);
        daySelectPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        daySelectPicker.setWrapSelectorWheel(true);
        daySelectPicker.setValue(cDay - 1);


        builder.setView(dialog);


        return builder.create();
    }


    interface OnConfirmDateListener{
        public void onConfirmDateListener(int selectedYear, int selectedMonth, int selectedDay);
    }
}