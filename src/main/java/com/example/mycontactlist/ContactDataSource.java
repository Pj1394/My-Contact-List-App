package com.example.mycontactlist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ContactDataSource {

    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public ContactDataSource(Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertContact(Contact c) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("Contactname", c.getContactName());
            initialValues.put("StreetAddress", c.getStreetAddress());
            initialValues.put("City", c.getCity());
            initialValues.put("State", c.getState());
            initialValues.put("ZipCode", c.getZipCode());
            initialValues.put("PhoneNumber", c.getPhoneNumber());
            initialValues.put("CellNumber", c.getCellNumber());
            initialValues.put("Email", c.getEMail());
            initialValues.put("Birthday", String.valueOf(c.getBirthday().getTimeInMillis()));


            if (c.getPicture() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                c.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] photo = baos.toByteArray();
                initialValues.put("contactPhoto", photo);
            }

            didSucceed = database.insert("contact", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateContact(Contact c) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) c.getContactID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("ContactName", c.getContactName());
            updateValues.put("StreetAddress", c.getStreetAddress());
            updateValues.put("City", c.getCity());
            updateValues.put("State", c.getState());
            updateValues.put("ZipCode", c.getZipCode());
            updateValues.put("PhoneNumber", c.getPhoneNumber());
            updateValues.put("CellNumber", c.getCellNumber());
            updateValues.put("Email", c.getEMail());
            updateValues.put("Birthday",
                    String.valueOf(c.getBirthday().getTimeInMillis()));


            if (c.getPicture() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                c.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] photo = baos.toByteArray();
                updateValues.put("contactphoto", photo);
            }

            didSucceed = database.update("contact", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }
    public int getLastContactId() {
        int lastId = -1;
        try {
            String query = "Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public boolean updateAddress(ContactAddress contactAddress) {

        boolean didSucceed = false;

        Long rowId = (long) contactAddress.getContactID();
        ContentValues updateValues = new ContentValues();

        try {

            updateValues.put("streetaddress", contactAddress.getStreetAddress());
            updateValues.put("city", contactAddress.getCity());
            updateValues.put("state", contactAddress.getState());
            updateValues.put("zipcode", contactAddress.getZipCode());

            didSucceed = database.update("contact", updateValues, "_id=" + rowId, null) > 0;


        } catch (Exception e3) {
            e3.printStackTrace();
        }

        return didSucceed;
    }

    public ArrayList<String> getName() {
        ArrayList<String> contactName = new ArrayList<String>();
        try {
            //SQL query is written to return the contactname field for all records in the contact table and
            String query = "Select contactNames from contact";
            //then executed
            Cursor cursor = database.rawQuery(query, null);

            //loop is set up to go through all the records in the cursor.
            //loop is initialized by moving to the first record in the cursor. Next,
            // the while loop is set up to test if the end of the cursor’s record set has been reached. Within the loop,
            // the contact name is added to the ArrayList, and the cursor is advanced to the next record.

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                contactName.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        //calling Activity can test for an empty list to determine if the retrieve was successful.
        catch (Exception ex) {
            contactName = new ArrayList<String>();
        }
        return contactName;
    }


        public ArrayList<Contact> getContacts(String sortField, String sortOrder) {
            ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {
            String query = "SELECT * FROM contact ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Contact newContact;
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                newContact = new Contact();
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.setEMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                contacts.add(newContact);
                cursor.moveToNext();

            }
            cursor.close();
        }
        catch (Exception e) {
            contacts = new ArrayList<Contact>();
        }
        return contacts;
    }


    public Contact getSpecificContact(int contactId) {
        Contact contact = new Contact();
        String query = "SELECT  * FROM contact WHERE _id =" + contactId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            contact.setContactID(cursor.getInt(0));
            contact.setContactName(cursor.getString(1));
            contact.setStreetAddress(cursor.getString(2));
            contact.setCity(cursor.getString(3));
            contact.setState(cursor.getString(4));
            contact.setZipCode(cursor.getString(5));
            contact.setPhoneNumber(cursor.getString(6));
            contact.setCellNumber(cursor.getString(7));
            contact.setEMail(cursor.getString(8));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));

            if(cursor.getInt(10) > 0){
                contact.setAsBestFriend(1);
            }

            contact.setBirthday(calendar);
            byte[] photo = cursor.getBlob(10);
            if (photo != null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap thePicture= BitmapFactory.decodeStream(imageStream);
                contact.setPicture(thePicture);
            }

            cursor.close();
        }
        return contact;
    }
        public boolean deleteContact(int contactId) {
            boolean didDelete = false;
            try {
                didDelete = database.delete("contact", "_id=" + contactId, null) > 0;
            }
            catch (Exception e) {
                //Do nothing -return value already set to false
            }
            return didDelete;
        }
    }


