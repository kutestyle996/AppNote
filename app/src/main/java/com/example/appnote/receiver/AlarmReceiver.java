package com.example.appnote.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.example.appnote.R;
import com.example.appnote.data.model.Constants;
import com.example.appnote.data.model.NoteAction;
import com.example.appnote.detail.DetailActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private int mId;

    @Override
    public void onReceive(Context context, Intent intent) {
        mId = intent.getIntExtra(Constants.Extra.EXTRA_NOTIFICATION_ID, -1);
        if(mId != -1){
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setContentTitle(context.getString(R.string.text_alarm_title))
                    .setContentText(context.getString(R.string.text_alarm_content))
                    .setOngoing(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            Intent i = new Intent(context, DetailActivity.class);
            i.putExtra(Constants.Extra.EXTRA_NOTIFICATION_ID, mId);
            i.putExtra(Constants.Extra.EXTRA_ACTION, NoteAction.EDIT_NOTE);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            mId,
                            i,
                            PendingIntent.FLAG_ONE_SHOT
                    );
            // example for blinking LED
            builder.setLights(0xFFb71c1c, 1000, 2000);
            // builder.setSound(uri);
            builder.setContentIntent(pendingIntent);
            manager.notify(mId, builder.build());
        }
    }
}
