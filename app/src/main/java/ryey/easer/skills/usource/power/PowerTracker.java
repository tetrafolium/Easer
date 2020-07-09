/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.skills.usource.power;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import ryey.easer.skills.condition.SkeletonTracker;

public class PowerTracker extends SkeletonTracker<PowerUSourceData> {

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
            case Intent.ACTION_POWER_CONNECTED:
                determineAndNotify(true);
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                determineAndNotify(false);
                break;
            }
        }
    };
    private IntentFilter filter;

    {
        filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        filter.addAction(Intent.ACTION_BATTERY_LOW);
//        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }

    PowerTracker(Context context, PowerUSourceData data,
                 @NonNull PendingIntent event_positive,
                 @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);
        Logger.d("PowerTracker constructed");
    }

    @Override
    public void start() {
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void stop() {
        context.unregisterReceiver(receiver);
    }

    @Override
    public Boolean state() {
        Logger.d("PowerTracker.state()");
        Intent batteryStickyIntent = Utils.getBatteryStickyIntent(context);
        int status = batteryStickyIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                             status == BatteryManager.BATTERY_STATUS_FULL;
        return Utils.determine(isCharging, data, batteryStickyIntent);
    }

    private void determineAndNotify(boolean isCharging) {
        newSatisfiedState(Utils.determine(isCharging, data, context));
    }
}
