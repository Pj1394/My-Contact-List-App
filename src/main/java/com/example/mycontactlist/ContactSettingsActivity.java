package com.example.mycontactlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;



public class ContactSettingsActivity extends AppCompatActivity {

    private ScrollView scrollViewObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        scrollViewObject = (ScrollView) findViewById(R.id.scrollView);

        initListButton();
        initMapButton();
        initSettingsButton();
        initSettings();
        initSortByClick();
        initSortOrderClick();
        initColorChooser();
    }
        private void initSortByClick() {
            RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radioGroupSortBy);
            rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {


                    RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
                    RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
                    if (rbName.isChecked()) {
                        getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortField", "contactName").commit();
                    } else if (rbCity.isChecked()) {
                        getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortField", "City").commit();
                    } else {
                        getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortField", "Birthday").commit();
                    }
                }
            });

    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = (RadioGroup) findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortOrder", "ASC").commit();


                }
                else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortOrder", "DESC").commit();
                }
            }
        });
    }

        private void initColorChooser(){

            RadioGroup rgColorChooser = (RadioGroup)findViewById(R.id.radioGroupColorChooser);
            rgColorChooser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    RadioButton rbNormal = (RadioButton)findViewById(R.id.radioNormal);
                    RadioButton rbYellow = (RadioButton)findViewById(R.id.radioYellow);
                    RadioButton rbBlue = (RadioButton)findViewById(R.id.radioBlue);


                    if(rbNormal.isChecked()){
                        getSharedPreferences("MyContactListPreferences",
                                Context.MODE_PRIVATE).edit()
                                .putString("colorchoice","standard").commit();
                        scrollViewObject.setBackgroundResource(R.color.standard_background);
                    }else if(rbYellow.isChecked()){
                        getSharedPreferences("MyContactListPreferences",
                                Context.MODE_PRIVATE).edit()
                                .putString("colorchoice","yellow").commit();
                        scrollViewObject.setBackgroundResource(R.color.settings_background_1);

                    }
                    else if(rbBlue.isChecked()){
                        getSharedPreferences("MyContactListPreferences",
                                Context.MODE_PRIVATE).edit()
                                .putString("colorchoice","blue").commit();
                        scrollViewObject.setBackgroundResource(R.color.settings_background_3);

                    }
                    else {
                        getSharedPreferences("MyContactListPreferences",
                                Context.MODE_PRIVATE).edit()
                                .putString("colorchoice","green").commit();
                        scrollViewObject.setBackgroundResource(R.color.settings_background_2);
                    }


                }
            });
        }

        private void initSettings() {
            String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortField","contactName");
            String sortOrder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortOrder","ASC");

            RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
            RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
            RadioButton rbBirthDay = (RadioButton) findViewById(R.id.radioBirthday);

            if (sortBy.equalsIgnoreCase("contactname")) {
                rbName.setChecked(true);
            }
            else if (sortBy.equalsIgnoreCase("city")) {
                rbCity.setChecked(true);
            }
            else {
                rbBirthDay.setChecked(true);
            }

            RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
            RadioButton rbDescending = (RadioButton) findViewById(R.id.radioDescending);
            if (sortOrder.equalsIgnoreCase("ASC")) {
                rbAscending.setChecked(true);
            }
            else {
                rbDescending.setChecked(true);
            }
        }


        //Navigation Bar
    //ListButton Code
    private void initListButton() {
        //A variable to hold an ImageButton is declared, and findViewById gets the widget named imageButtonList

        ImageButton ibList = (ImageButton) findViewById(R.id.buttonList);
        //A listener is added to the ImageButton
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent Variable is declared and new intent is created and assigned
                Intent intent = new Intent(ContactSettingsActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }
    private void initMapButton(){
        ImageButton ibList = (ImageButton) findViewById(R.id.buttonMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactSettingsActivity.this, ContactMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }
    private void initSettingsButton() {
        ImageButton ibSettings = (ImageButton) findViewById(R.id.buttonSettings);
        ibSettings.setEnabled(false);
            }



    }







