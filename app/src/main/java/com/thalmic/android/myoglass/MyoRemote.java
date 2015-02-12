package com.thalmic.android.myoglass;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.XDirection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyoRemote extends AbstractDeviceListener {
    private static final String TAG = "MyoRemote";

    private static final int NOTIFICATION_ID = 1;

    private Service mService;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private GlassDevice mGlass = new GlassDevice();

    public MyoRemote(Service service) {
        Log.i(TAG, "Starting Service");
        mService = service;

        showDisconnectedNotification();
    }

    public GlassDevice getGlassDevice() {
        return mGlass;
    }

    public void connectGlassDevice(final String address) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.i(TAG, "Connecting to Glass device.");
                mGlass.connect(address);
                return null;
            }
        }.executeOnExecutor(mExecutor);
    }

    public void closeGlassDevice() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mGlass.close();
                return null;
            }
        }.executeOnExecutor(mExecutor);
    }

    public void shutdown() {
        mExecutor.shutdown();
        mGlass.close();
    }

    @Override
    public void onConnect(Myo myo, long timestamp) {
        showConnectedNotification();
    }

    @Override
    public void onDisconnect(Myo myo, long timestamp) {
        showDisconnectedNotification();
    }

    @Override
    public void onUnlock(Myo myo, long timestamp) {
        showEnabledNotification();
    }

    @Override
    public void onLock(Myo myo, long timestamp) {
        showConnectedNotification();
    }

    @Override
    public void onPose(final Myo myo, long timestamp, Pose pose) {
        if (pose != Pose.DOUBLE_TAP) {
            myo.unlock(Myo.UnlockType.TIMED);
            myo.notifyUserAction();
        }

        // Flip the wave poses if using the left arm.
        if (myo.getArm() == Arm.LEFT) {
            if (pose == Pose.WAVE_IN) {
                pose = Pose.WAVE_OUT;
            } else if (pose == Pose.WAVE_OUT) {
                pose = Pose.WAVE_IN;
            }
        }

        if (pose == Pose.WAVE_IN) {
            mGlass.swipeLeft();
        } else if (pose == Pose.WAVE_OUT) {
            mGlass.swipeRight();
        } else if (pose == Pose.FINGERS_SPREAD) {
            mGlass.swipeDown();
        } else if (pose == Pose.FIST) {
            mGlass.tap();
        }
    }

    private void showDisconnectedNotification() {
        showNotification(R.drawable.ic_stat_myo_red, R.string.notification_disconnected_title,
                R.string.notification_text);
    }

    private void showConnectedNotification() {
        showNotification(R.drawable.ic_stat_myo, R.string.notification_connected_title,
                R.string.notification_text);
    }

    private void showEnabledNotification() {
        showNotification(R.drawable.ic_stat_myo_blue, R.string.notification_unlocked_title,
                R.string.notification_text);
    }

    private void showNotification(int drawable, int title, int text) {
        // We should add a closeIntent
       PendingIntent closeIntent = PendingIntent.getBroadcast(mService, 0,
                new Intent(MyoRemoteService.ACTION_STOP_MYO_GLASS), PendingIntent.FLAG_UPDATE_CURRENT);
       PendingIntent openIntent = PendingIntent.getActivity(mService, 0,
                new Intent(mService, ConfigActivity.class), 0);

        RemoteViews contentView = new RemoteViews(mService.getPackageName(), R.layout.notification_layout);
        contentView.setTextViewText(R.id.notification_title, mService.getString(title));
        contentView.setTextViewText(R.id.notification_text, mService.getString(text));
        contentView.setImageViewResource(R.id.notification_image, drawable);
        contentView.setOnClickPendingIntent(R.id.notification_close_button, closeIntent);

        Notification notification = new Notification.Builder(mService)
                .setOngoing(true)
                .setShowWhen(false)
                .setTicker(mService.getString(R.string.app_name))
                .setSmallIcon(drawable)
                .setContentIntent(openIntent)
                .setContent(contentView)
                .build();
        mService.startForeground(NOTIFICATION_ID, notification);
    }
}