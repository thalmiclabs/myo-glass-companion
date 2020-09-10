// Copyright 2015 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.thalmic.android.myoglass;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.thalmic.myo.Hub;

public class MyoRemoteService extends Service {
    public static final String ACTION_STOP_MYO_GLASS = "ACTION_STOP_MYO_GLASS";

    private MyoRemote mMyoRemote;
    private StopReceiver mStopReceiver = new StopReceiver();

    public MyoRemoteService() {
    }

    public MyoRemote getMyoRemote() {
        return mMyoRemote;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new MBinder();

    public class MBinder extends Binder {
        public MyoRemoteService getService() {
            return MyoRemoteService.this;
        }
    }

    private class StopReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Hub hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            stopSelf();
            return;
        }

        mMyoRemote = new MyoRemote(this);
        hub.addListener(mMyoRemote);

        AppPrefs prefs = new AppPrefs(this);
        String address = prefs.getMyoAddress();
        if (!TextUtils.isEmpty(address)) {
            // Will do nothing if already paired to Myo at given address.
            Hub.getInstance().attachByMacAddress(address);
        }
        registerReceiver(mStopReceiver, new IntentFilter(ACTION_STOP_MYO_GLASS));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Hub.getInstance().removeListener(mMyoRemote);
        mMyoRemote.shutdown();
        unregisterReceiver(mStopReceiver);
        Hub.getInstance().shutdown();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
