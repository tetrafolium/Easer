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

package ryey.easer.skills.usource.day_of_week;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.event.AbstractSlot;

public class DayOfWeekEventSkill implements USourceSkill<DayOfWeekUSourceData> {

@NonNull
@Override
public String id() {
	return "day_of_week";
}

@Override
public int name() {
	return R.string.usource_day_of_week;
}

@Override
public boolean isCompatible(@NonNull final Context context) {
	return true;
}

@Nullable
@Override
public Boolean checkPermissions(final @NonNull Context context) {
	return null;
}

@Override
public void requestPermissions(final @NonNull Activity activity,
                               final int requestCode) {
}

@NonNull
@Override
public USourceDataFactory<DayOfWeekUSourceData> dataFactory() {
	return new DayOfWeekUSourceDataFactory();
}

@NonNull
@Override
public SourceCategory category() {
	return SourceCategory.date_time;
}

@NonNull
@Override
public SkillView<DayOfWeekUSourceData> view() {
	return new DayOfWeekSkillViewFragment();
}

@Override
public AbstractSlot<DayOfWeekUSourceData>
slot(final @NonNull Context context,
     final @ValidData @NonNull DayOfWeekUSourceData data) {
	return new DayOfWeekSlot(context, data);
}

@Override
public AbstractSlot<DayOfWeekUSourceData>
slot(final @NonNull Context context, final @NonNull DayOfWeekUSourceData data,
     final boolean retriggerable, final boolean persistent) {
	return new DayOfWeekSlot(context, data, retriggerable, persistent);
}

@Override
public Tracker<DayOfWeekUSourceData>
tracker(final @NonNull Context context,
        final @NonNull DayOfWeekUSourceData data,
        final @NonNull PendingIntent event_positive,
        final @NonNull PendingIntent event_negative) {
	return new DayOfWeekTracker(context, data, event_positive, event_negative);
}
}
