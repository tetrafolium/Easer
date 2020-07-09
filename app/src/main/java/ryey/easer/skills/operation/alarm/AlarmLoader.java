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

package ryey.easer.skills.operation.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.AlarmClock;
import androidx.annotation.NonNull;
import java.util.Calendar;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class AlarmLoader extends OperationLoader<AlarmOperationData> {
public AlarmLoader(final Context context) {
	super(context);
}

@Override
public boolean load(final @ValidData @NonNull AlarmOperationData data) {
	Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
	int hour = data.time.get(Calendar.HOUR_OF_DAY);
	int minute = data.time.get(Calendar.MINUTE);
	if (!data.absolute) {
		Calendar calendar = Calendar.getInstance();
		minute += calendar.get(Calendar.MINUTE);
		if (minute >= 60) {
			hour += 1;
			minute -= 60;
		}
		hour += calendar.get(Calendar.HOUR_OF_DAY);
		if (hour >= 24) {
			hour -= 24;
		}
	}
	intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
	intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
	intent.putExtra(AlarmClock.EXTRA_MESSAGE, data.message);
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		intent.putExtra(AlarmClock.EXTRA_VIBRATE, true);
	}
	intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	if (intent.resolveActivity(context.getPackageManager()) != null) {
		context.startActivity(intent);
		return true;
	} else
		return false;
}
}
