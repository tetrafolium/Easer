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

package ryey.easer.skills.usource.cell_location;

import android.Manifest;
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
import ryey.easer.commons.local_skill.eventskill.Slot;
import ryey.easer.commons.local_skill.usource.USourceDataFactory;
import ryey.easer.commons.local_skill.usource.USourceSkill;
import ryey.easer.skills.SkillUtils;

public class CellLocationUSourceSkill implements USourceSkill<CellLocationUSourceData> {

    @NonNull
    @Override
    public String id() {
        return "cell location";
    }

    @Override
    public int name() {
        return R.string.usource_cell_location;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @Nullable
    @Override
    public Boolean checkPermissions(final @NonNull Context context) {
        return SkillUtils.checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    public void requestPermissions(final @NonNull Activity activity, final int requestCode) {
        SkillUtils.requestPermission(activity, requestCode, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @NonNull
    @Override
    public USourceDataFactory<CellLocationUSourceData> dataFactory() {
        return new CellLocationUSourceDataFactory();
    }

    @NonNull
    @Override
    public SourceCategory category() {
        return SourceCategory.personal;
    }

    @NonNull
    @Override
    public SkillView<CellLocationUSourceData> view() {
        return new CellLocationSkillViewFragment();
    }

    @Override
    public Slot<CellLocationUSourceData> slot(final @NonNull Context context, final @NonNull CellLocationUSourceData data) {
        return new CellLocationSlot(context, data);
    }

    @Override
    public Slot<CellLocationUSourceData> slot(final @NonNull Context context, final @NonNull CellLocationUSourceData data, final boolean retriggerable, final boolean persistent) {
        return new CellLocationSlot(context, data, retriggerable, persistent);
    }

    @NonNull
    @Override
    public Tracker<CellLocationUSourceData> tracker(final @NonNull Context context,
                                                    final @ValidData @NonNull CellLocationUSourceData data,
                                                    final @NonNull PendingIntent event_positive,
                                                    final @NonNull PendingIntent event_negative) {
        return new CellLocationTracker(context, data, event_positive, event_negative);
    }

}
