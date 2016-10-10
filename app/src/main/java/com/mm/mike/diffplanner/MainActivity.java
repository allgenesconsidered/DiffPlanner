package com.mm.mike.diffplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String popUpContents[];
    PopupWindow popupWindowProtocols;
    EditText mEditTextTitle;
    Button buttonShowDropDown;
    Button buttonDate;
    Button buttonTime;
    Button buttonCreatePlanner;

    private Calendar calendar;
    int mYear, mMonth, mDay;
    int mHour, mMinute;
    String mProtocol;
    String mTitle;



    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize pop up window items list

        // add items on the array dynamically
        // format is ProtocolName::ID
        List<String> protocolList = new ArrayList<String>();
        protocolList.add("Cardiac::v1.0::1");
        protocolList.add("Cardiac::v1.2::2");
        protocolList.add("Cardiac::v1.4::4");

        // convert to simple array
        popUpContents = new String[protocolList.size()];
        protocolList.toArray(popUpContents);

        // initialize pop up window
        popupWindowProtocols = popupWindowProtocols();


        // button on click listener
        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonShowDropDown:
                        // show the list view as dropdown
                        popupWindowProtocols.showAsDropDown(v, -5, 0);
                        break;
                    case R.id.buttonDate:
                        showDialog(0);
                        break;
                    case R.id.buttonTime:
                        showDialog(1);
                        break;
                    case R.id.buttonCreatePlanner:
                        int duration = Toast.LENGTH_LONG;
                        String text =  new StringBuilder().append(mTitle).append(" - ")
                                .append(mMonth).append("/").append(mDay).append("/").append(mYear)
                                .append(" ").append(mHour).append(":").append(formatMinute(mMinute)).append(" ").append(mProtocol).toString();
                        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                        toast.show();
                        break;
                }
            }
        };

        mEditTextTitle = (EditText) findViewById(R.id.editTextName);
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // our button
        buttonShowDropDown = (Button) findViewById(R.id.buttonShowDropDown);
        buttonShowDropDown.setOnClickListener(handler);

        //Date Picker
        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(handler);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        //Time Picker
        buttonTime = (Button) findViewById(R.id.buttonTime);
        buttonTime.setOnClickListener(handler);

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        mProtocol = "None";

        // Execute
        buttonCreatePlanner = (Button) findViewById(R.id.buttonCreatePlanner);
        buttonCreatePlanner.setOnClickListener(handler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public PopupWindow popupWindowProtocols() {
        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);
        // the drop down list is a list view
        ListView listViewProtocols = new ListView(this);
        // set our adapter and pass our pop up window contents
        listViewProtocols.setAdapter(protocolsAdapter(popUpContents));
        // set the item click listener
        listViewProtocols.setOnItemClickListener(new DropdownOnItemClickListener());

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(450);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewProtocols);

        return popupWindow;
    }

    /*
     * adapter where the list values will be set
     */
    private ArrayAdapter<String> protocolsAdapter(String protocolArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, protocolArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list
                String item = getItem(position);
                String[] itemArr = item.split("::");
                String text0 = itemArr[0];
                String text1 = itemArr[1];
                String id = itemArr[2];

                // visual settings for the list item
                TextView listItem = new TextView(MainActivity.this);

                listItem.setText(new StringBuilder().append(text0).append(" ").append(text1));
                listItem.setTag(id);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };
        return adapter;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        if (id == 0) { // Date picker
            return new DatePickerDialog(this, mDateListener, mYear, mMonth, mDay);
        }
        if (id == 1){ // Time picker
            return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    updateDate(year, month+1, day);
            }
        };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    updateTime(hourOfDay, minute);
                }
            };

    private void updateDate(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
        buttonDate.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }
    private void updateTime(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
        buttonTime.setText(new StringBuilder().append(hour).append(":")
                .append(formatMinute(minute)));
    }

    private String formatMinute(int minute){
        String minuteString;
        if(minute < 10){
            minuteString = new StringBuilder().append("0").append(minute).toString();
        }
        else minuteString = Integer.toString(minute);
        return minuteString;
    }
}
