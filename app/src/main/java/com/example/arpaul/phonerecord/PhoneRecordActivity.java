package com.example.arpaul.phonerecord;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.arpaul.phonerecord.Adapter.CallListAdapter;
import com.example.arpaul.phonerecord.DataAccess.PhoneRecordCPConstants;
import com.example.arpaul.phonerecord.DataObject.CallDetailsDO;

import java.util.ArrayList;

public class PhoneRecordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private TextView tvNoCAlls;
    private RecyclerView rvCallDetails;
    private CallListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*************Activity created*******************/
        /*************Activity view set*******************/
        setContentView(R.layout.activity_phone_record);

        /*************Activity controls fetched*******************/
        initializeControls();

        /*************Content Provider initialised using loader callbacks*******************/
        getSupportLoaderManager().initLoader(1, null, PhoneRecordActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*************Content Provider Loader restarted*******************/
        getSupportLoaderManager().restartLoader(1,null,PhoneRecordActivity.this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        /*************Content provider uri fetched*******************/
        Uri CONTENT_URI = PhoneRecordCPConstants.CONTENT_URI;

        /*************Content provider get method called to fetch all calls or messages received*******************/
        return new CursorLoader(this, CONTENT_URI, new String[]{PhoneRecordCPConstants.COLUMN_CONTACT_NO, PhoneRecordCPConstants.COLUMN_CONTACT_TYPE,
                PhoneRecordCPConstants.COLUMN_CALL_DATETIME}, null, null, PhoneRecordCPConstants.COLUMN_CALL_DATETIME + " DESC");
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if(data instanceof Cursor) {
            /*************Once the Loader callback finished it return the cursor*******************/
            Cursor cursor = (Cursor) data;

            ArrayList<CallDetailsDO> arrCallDetails = new ArrayList<>();

            if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                /*************Fetching all the data from cursor*******************/
                do {
                    CallDetailsDO objCallDetailsDO = new CallDetailsDO();
                    objCallDetailsDO.contactNumber = cursor.getString(cursor.getColumnIndex(PhoneRecordCPConstants.COLUMN_CONTACT_NO));
                    objCallDetailsDO.contactType = cursor.getInt(cursor.getColumnIndex(PhoneRecordCPConstants.COLUMN_CONTACT_TYPE));
                    objCallDetailsDO.callTime = cursor.getString(cursor.getColumnIndex(PhoneRecordCPConstants.COLUMN_CALL_DATETIME));
                    arrCallDetails.add(objCallDetailsDO);
                } while(cursor.moveToNext());
            }
            /*************Setting up recycler view once the Loader calback finishes*******************/
            showList(arrCallDetails);

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    private void showList(final ArrayList<CallDetailsDO> arrCallDetails) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(arrCallDetails != null && arrCallDetails.size() > 0) {
                    /*************Setting up recycler view once the Loader calback finishes*******************/
                    adapter.refresh(arrCallDetails);
                    tvNoCAlls.setVisibility(View.GONE);
                    rvCallDetails.setVisibility(View.VISIBLE);
                } else {
                    tvNoCAlls.setVisibility(View.VISIBLE);
                    rvCallDetails.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initializeControls(){
        /*************Initialising Screen controls*******************/
        tvNoCAlls = (TextView) findViewById(R.id.tvNoCAlls);
        rvCallDetails = (RecyclerView) findViewById(R.id.rvCallDetails);

        /*************Initialising Adapter*******************/
        adapter = new CallListAdapter(PhoneRecordActivity.this, new ArrayList<CallDetailsDO>());
        rvCallDetails.setAdapter(adapter);
    }
}
