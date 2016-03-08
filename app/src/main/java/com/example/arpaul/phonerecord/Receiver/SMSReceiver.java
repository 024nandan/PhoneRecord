package com.example.arpaul.phonerecord.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.SmsMessage;

import com.example.arpaul.phonerecord.DataAccess.PhoneRecordCPConstants;
import com.example.arpaul.phonerecord.PhoneRecordActivity;
import com.example.arpaul.phonerecord.R;
import com.example.arpaul.phonerecord.Utilities.CalendarUtils;


/**
 * Created by ARPaul on 04-03-2016.
 */
public class SMSReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
        String sender = shortMessage.getOriginatingAddress();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PhoneRecordCPConstants.COLUMN_CONTACT_NO, sender);
        contentValues.put(PhoneRecordCPConstants.COLUMN_CONTACT_TYPE, PhoneRecordCPConstants.get_Contact_Type_SMS());
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
        builder.setTicker("New SMS received.");
        builder.setContentTitle("New SMS received.");
        builder.setContentText("You have a received a new SMS.");
        builder.setSmallIcon(R.drawable.sms);
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
