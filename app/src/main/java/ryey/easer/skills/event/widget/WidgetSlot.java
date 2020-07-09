/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.event.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ryey.easer.skills.event.AbstractSlot;

public class WidgetSlot extends AbstractSlot<WidgetEventData> {

    private static IntentFilter intentFilter = new IntentFilter(UserActionWidget.Companion.getACTION_WIDGET_CLICKED());

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            assert intent != null;
            if (UserActionWidget.Companion.getACTION_WIDGET_CLICKED().equals(intent.getAction())) {
//                intent.setExtrasClassLoader(String.class.getClassLoader());
                String tag = intent.getStringExtra(UserActionWidget.Companion.getEXTRA_WIDGET_TAG());
                if (eventData.widgetTag.equals(tag)) {
                    changeSatisfiedState(true);
                }
            }
        }
    };

    WidgetSlot(final Context context, final WidgetEventData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    WidgetSlot(final Context context, final WidgetEventData data, final boolean retriggerable, final boolean persistent) {
        super(context, data, retriggerable, persistent);
    }

    @Override
    public void listen() {
        LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void cancel() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiver);
    }

}
