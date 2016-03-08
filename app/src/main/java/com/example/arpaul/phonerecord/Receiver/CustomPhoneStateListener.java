package com.example.arpaul.phonerecord.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.arpaul.phonerecord.DataAccess.PhoneRecordCPConstants;
import com.example.arpaul.phonerecord.PhoneRecordActivity;
import com.example.arpaul.phonerecord.R;
import com.example.arpaul.phonerecord.Utilities.CalendarUtils;

/**
 * Created by ARPaul on 04-03-2016.
 */
public class CustomPhoneStateListener extends PhoneStateListener {

    Context context; //Context to make Toast if required
    private static boolean firstConnect = false;
    String str = "";
    public CustomPhoneStateListener(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                //when Idle i.e no call
                str = "1";
                //Toast.makeText(context, "Phone state Idle", Toast.LENGTH_LONG).show();
                firstConnect = false;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //when Off hook i.e in call
                str ="2";
                //Make intent and start your service here
                //Toast.makeText(context, "Phone state Off hook", Toast.LENGTH_LONG).show();
                firstConnect = false;
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                //when Ringing
                //Toast.makeText(context, "Phone state Ringing", Toast.LENGTH_LONG).show();
                //Saving into database with current date time.
                if(!firstConnect){
                    saveCallList(incomingNumber);
                    firstConnect = true;
                }
                break;
            default:
                break;
        }
    }

    private void saveCallList(String incomingNumber){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhoneRecordCPConstants.COLUMN_CONTACT_NO, incomingNumber);
        contentValues.put(PhoneRecordCPConstants.COLUMN_CONTACT_TYPE, PhoneRecordCPConstants.get_Contact_Type_Call());
        contentValues.put(PhoneRecordCPConstants.COLUMN_CALL_DATETIME, CalendarUtils.getCurrentDateTime());
        //Keeping ischecked 0 since its not checked yet.
        contentValues.put(PhoneRecordCPConstants.COLUMN_CHECHED, PhoneRecordCPConstants.get_Not_Checked());

        context.getContentResolver().insert(PhoneRecordCPConstants.CONTENT_URI, contentValues);

        notifyUser();
    }

    public void notifyUser(){

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, PhoneRecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //use the flag FLAG_UPDATE_CURRENT to override any notification already there
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setAutoCancel(false);
        builder.setTicker("New call received.");
        builder.setContentTitle("New call received.");
        builder.setContentText("You have a received a new call.");
        builder.setSmallIcon(R.drawable.call);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher);
        builder.setLargeIcon(icon);
        builder.setContentIntent(contentIntent);
        builder.setOngoing(true);
        //builder.setNumber(100);
        builder.build();

        Notification notification = builder.getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;
        notificationManager.notify(11, notification);

    }
}
