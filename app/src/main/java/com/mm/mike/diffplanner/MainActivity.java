package com.mm.mike.diffplanner;

import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    String TAG = "MainActivity.java";

    String popUpContents[];
    PopupWindow popupWindowProtocols;
    Button buttonShowDropDown;

    @Override
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
                }
            }
        };

        // our button
        buttonShowDropDown = (Button) findViewById(R.id.buttonShowDropDown);
        buttonShowDropDown.setOnClickListener(handler);

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
}
