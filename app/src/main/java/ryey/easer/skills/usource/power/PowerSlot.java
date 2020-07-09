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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import ryey.easer.skills.event.AbstractSlot;

public class PowerSlot extends AbstractSlot<PowerUSourceData> {

  private final BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(final Context context, final Intent intent) {
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

  public PowerSlot(final Context context, final PowerUSourceData data) {
    this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
  }

  PowerSlot(final Context context, final PowerUSourceData data,
            final boolean retriggerable, final boolean persistent) {
    super(context, data, retriggerable, persistent);
  }

  @Override
  public void listen() {
    context.registerReceiver(receiver, filter);
  }

  @Override
  public void cancel() {
    context.unregisterReceiver(receiver);
  }

  private void determineAndNotify(final boolean isCharging) {
    changeSatisfiedState(Utils.determine(isCharging, eventData, context));
  }
}
