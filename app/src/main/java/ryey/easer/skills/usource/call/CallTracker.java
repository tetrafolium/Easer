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

package ryey.easer.skills.usource.call;

import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import ryey.easer.skills.condition.SkeletonTracker;

public class CallTracker extends SkeletonTracker<CallUSourceData> implements CallReceiver.CallEventHandler {

    private CallReceiver callReceiver = new CallReceiver(this);

    CallTracker(final Context context, final CallUSourceData data,
                   final @NonNull PendingIntent event_positive,
                   final @NonNull PendingIntent event_negative) {
        super(context, data, event_positive, event_negative);
    }

    @Override
    public void start() {
        context.registerReceiver(callReceiver, CallReceiver.Companion.getCallFilter());
    }

    @Override
    public void stop() {
        context.unregisterReceiver(callReceiver);
    }

    @Override
    public void onIdle(final @NotNull String number) {
        newSatisfiedState(CallReceiver.Companion.handleIdle(data, number));
    }

    @Override
    public void onRinging(final @NotNull String number) {
        newSatisfiedState(CallReceiver.Companion.handleRinging(data, number));
    }

    @Override
    public void onOffHook(final @NotNull String number) {
        newSatisfiedState(CallReceiver.Companion.handleOffHook(data, number));
    }
}
