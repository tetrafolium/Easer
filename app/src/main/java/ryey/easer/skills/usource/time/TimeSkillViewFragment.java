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

package ryey.easer.skills.usource.time;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Calendar;
import ryey.easer.R;
import ryey.easer.SettingsUtils;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class TimeSkillViewFragment extends SkillViewFragment<TimeUSourceData> {
private TimePicker timePicker;

@NonNull
@Override
public View onCreateView(final @NonNull LayoutInflater inflater,
                         final @Nullable ViewGroup container,
                         final @Nullable Bundle savedInstanceState) {
	View view =
		inflater.inflate(R.layout.plugin_condition__time, container, false);

	timePicker = view.findViewById(R.id.timePicker);
	timePicker.setIs24HourView(!SettingsUtils.use12HourClock(getContext()));

	return view;
}

private static void setTimePicker(final TimePicker timePicker,
                                  final Calendar calendar) {
	int hour, minute;
	hour = calendar.get(Calendar.HOUR_OF_DAY);
	minute = calendar.get(Calendar.MINUTE);
	if (Build.VERSION.SDK_INT >= 23) {
		timePicker.setHour(hour);
		timePicker.setMinute(minute);
	} else {
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
	}
}

private static Calendar fromTimePicker(final TimePicker timePicker) {
	Calendar calendar = Calendar.getInstance();
	if (Build.VERSION.SDK_INT >= 23) {
		calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
		calendar.set(Calendar.MINUTE, timePicker.getMinute());
	} else {
		calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
		calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
	}
	return calendar;
}

@Override
protected void _fill(final @ValidData @NonNull TimeUSourceData data) {
	setTimePicker(timePicker, data.time);
}

@ValidData
@NonNull
@Override
public TimeUSourceData getData() throws InvalidDataInputException {
	return new TimeUSourceData(fromTimePicker(timePicker),
	                           TimeUSourceData.Rel.after);
}
}
