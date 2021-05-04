package com.lck.capston_design;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.util.Calendar;

public class AddCalendarDialog extends Dialog {
    private Context context;
    CustomDialogClickListener customDialogClickListener;
    EditText text;
    TextView date,date2;
    Button add, cancel, set, visible;
    DatePicker datePicker;
    TimePicker timePicker;

    String year,month,day,setdate,todaydate;
    int y,m,d;

    public AddCalendarDialog(@NonNull Context context,CustomDialogClickListener customDialogClickListener) {
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addcal);

        text = findViewById(R.id.addcal_et_text);
        date = findViewById(R.id.addcal_et_date);
        add = findViewById(R.id.addcal_add_btn);
        cancel = findViewById(R.id.addcal_can_btn);
        visible = findViewById(R.id.dialog_date_btn);
        datePicker = findViewById(R.id.dialog_datepicker);
        timePicker = findViewById(R.id.dialog_timepicker);
        set = findViewById(R.id.dialog_set_button);
        date2 = findViewById(R.id.dialog_date);

        Calendar calendar = Calendar.getInstance();

        todaydate = calendar.get(Calendar.YEAR)+"년"+(calendar.get(Calendar.MONTH)+1)+"월"+
                calendar.get(Calendar.DAY_OF_MONTH)+"일"+calendar.get(Calendar.HOUR_OF_DAY)+"시"
                +calendar.get(Calendar.MINUTE)+"분";
        date.setText(todaydate);

        visible.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.VISIBLE);
                set.setVisibility(View.VISIBLE);
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtyear = datePicker.getYear()+"";
                String txtmon = (datePicker.getMonth()+1)+"";
                String txtday = datePicker.getDayOfMonth()+"";
                String txthour = timePicker.getHour()+"";
                String txtmin = timePicker.getMinute()+"";

                if (timePicker.getMinute()<10)
                    txtmin=0+txtmin;
                if (timePicker.getHour()<10)
                    txthour=0+txthour;
                setdate=txtyear+"-"+txtmon+"-"+txtday+"T"+txthour+":"+txtmin+":00.000Z";
                date2.setText(setdate);
                date.setText(txtyear+"년"+txtmon+"월"+txtday+"일"+txthour+"시"+txtmin+"분");

                set.setVisibility(View.GONE);
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.GONE);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }
}
