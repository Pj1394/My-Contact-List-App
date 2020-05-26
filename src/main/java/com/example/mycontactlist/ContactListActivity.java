package com.example.mycontactlist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    ArrayList<Contact> contacts;
    boolean isDeleting = false;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initListButton();
        initMapButton();
        initSettingsButton();
        initItemClick();
        initAddContactButton();
        initDeleteButton();

        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                double levelScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int batteryPercent = (int) Math.floor(batteryLevel / levelScale * 100);
                TextView textBatteryState = (TextView)findViewById(R.id.textBatteryLevel);
                textBatteryState.setText(batteryPercent + "%");
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    @Override
    public void onResume() {

        super.onResume();

        String sortBy = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "contactname");

        String sortOrder = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortorder","ASC");


        ContactDataSource ds = new ContactDataSource(this);

        try {
            ds.open();
            contacts = ds.getContacts(sortBy,sortOrder);
            ds.close();

            if(contacts.size() > 0){
                ListView listview = (ListView) findViewById(R.id.lvContacts);
                adapter = new ContactAdapter(this,contacts);
                listview.setAdapter(adapter);
            }else{
                Intent intent = new Intent(ContactListActivity.this,ContactActivity.class);
                startActivity(intent);
            }

        }catch (Exception e){
            Toast.makeText(this,"Error retrieving contacts",Toast.LENGTH_LONG).show();

        }
    }
    private void initListButton() {

        ImageButton imageButtonList = (ImageButton) findViewById(R.id.buttonList);
        imageButtonList.setEnabled(false);
    }

    private void initMapButton() {

        ImageButton mapButton = (ImageButton) findViewById(R.id.buttonMap);
        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, ContactMapActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void initSettingsButton() {

        ImageButton settingsButton = (ImageButton) findViewById(R.id.buttonSettings);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, ContactSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }
    private void initItemClick() {
        ListView listview = (ListView)findViewById(R.id.lvContacts);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Contact selectedContact = contacts.get(position);
                if(isDeleting){
                    adapter.showDelete(position,itemClicked,ContactListActivity.this,selectedContact);
                }else {

                    Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
                    intent.putExtra("contactid", selectedContact.getContactID());
                    startActivity(intent);
                }

            }
        });
    }
    private void initAddContactButton(){
        Button newContact = (Button)findViewById(R.id.buttonAdd);
        newContact.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this,ContactActivity.class);
                startActivity(intent);

            }
        });
    }
    private void initDeleteButton(){
        final Button deleteButton = (Button)findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(isDeleting){
                    deleteButton.setText("Delete");
                    isDeleting = false;
                    adapter.notifyDataSetChanged();

                }else{
                    deleteButton.setText("Done Deleting");
                    isDeleting = true;
                }
            }
        });
    }


}
