package ru.startandroid.develop.p1011contentprovider;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity
{

    final String LOG_TAG = "myLogs";

    final Uri CONTACT_URI = Uri
    .parse("content://ru.startandroid.providers.AdressBook/contacts");

    final String CONTACT_NAME = "name";
    final String CONTACT_EMAIL = "email";
    ListView lvContact;
    long idTable;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
                                                   null, null);
        startManagingCursor(cursor);

        String from[] = { "name", "email" };
        int to[] = { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                                                              android.R.layout.simple_list_item_2, cursor, from, to);

        lvContact = (ListView) findViewById(R.id.lvContact);
        lvContact.setAdapter(adapter);
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long tableid)
                {
                    idTable = tableid;
                }
            });
    }

    public void onClickInsert(View v)
    {
        ContentValues cv = new ContentValues();
        int p = lvContact.getCount();      
        cv.put(CONTACT_NAME, "name " + p);
        cv.put(CONTACT_EMAIL, "email " + p);
        Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
    }

    public void onClickUpdate(View v)
    {
        ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, "name 5");
        cv.put(CONTACT_EMAIL, "email 5");
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
        int cnt = getContentResolver().update(uri, cv, null, null);
        Log.d(LOG_TAG, "update, count = " + cnt);
    }

    public void onClickDelete(View v)
    {
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, idTable);
        int cnt = getContentResolver().delete(uri, null, null);
        Log.d(LOG_TAG, "delete, count = " + cnt);
    }

    public void onClickError(View v)
    {
        Uri uri = Uri.parse("content://ru.startandroid.providers.AdressBook/phones");
        try
        {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        }
        catch (Exception ex)
        {
            Log.d(LOG_TAG, "Error: " + ex.getClass() + ", " + ex.getMessage());
        }

    }
}
