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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String popUpContents[];
    PopupWindow popupWindowProtocols;
    Button buttonShowDropDown;
    Button buttonDate;
    Button buttonTime;

    private Calendar calendar;
    private int year, month, day;
    private int hour, minute;


    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize pop up window items list

        // add items on the array dynamically
        // format is ProtocolName::ID
        List<String> protocolList = new ArrayList<String>();
        protocolList.add("Cardiac v1.0::1");
        protocolList.add("Cardiac v1.2::2");
        protocolList.add("Cardiac v1.4::4");

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
                }
            }
        };

        // our button
        buttonShowDropDown = (Button) findViewById(R.id.buttonShowDropDown);
        buttonShowDropDown.setOnClickListener(handler);

        //Date Picker
        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(handler);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //Time Picker
        buttonTime = (Button) findViewById(R.id.buttonTime);
        buttonTime.setOnClickListener(handler);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


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
                String text = itemArr[0];
                String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView(MainActivity.this);

                listItem.setText(text);
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
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return new DatePickerDialog(this, mDateListener, year, month, day);
        }
        if (id == 1){
            return new TimePickerDialog(this, mTimeSetListener, hour, minute, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    updateDate(arg1, arg2+1, arg3);
            }
        };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    updateTime(hourOfDay, minute);
                }
            };

    private void updateDate(int year, int month, int day) {
        buttonDate.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }
    private void updateTime(int hour, int minute) {
        buttonTime.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
    }
}
