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

package ryey.easer.skills.usource.date;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ryey.easer.skills.event.SelfNotifiableSlot;

/*
 * TODO: cancel (or set extra) alarm after being satisfied or unsatisfied (for different event types)
 */
public class DateSlot extends SelfNotifiableSlot<DateUSourceData> {
    private static AlarmManager mAlarmManager;

    private Calendar calendar = null;

    public DateSlot(final Context context, final DateUSourceData data) {
        this(context, data, RETRIGGERABLE_DEFAULT, PERSISTENT_DEFAULT);
    }

    DateSlot(final Context context, final DateUSourceData data, final boolean retriggerable, final boolean persistent) {
        super(context, data, retriggerable, persistent);
        setDate(data.date);

        if (mAlarmManager == null)
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private void setDate(final Calendar date) {
        if (date == null)
            return;
        if (calendar == null) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);
        }
        calendar.setTime(date.getTime());
    }

    @Override
    public void listen() {
        super.listen();
        if (calendar != null) {
            mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                              AlarmManager.INTERVAL_DAY, notifySelfIntent_positive);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (calendar != null) {
            mAlarmManager.cancel(notifySelfIntent_positive);
            mAlarmManager.cancel(notifySelfIntent_negative);
        }
    }

    @Override
    protected void onPositiveNotified(final Intent intent) {
        changeSatisfiedState(true);
    }
}
