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

package ryey.easer.skills.condition.ringer_mode;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ryey.easer.R;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.conditionskill.ConditionDataFactory;
import ryey.easer.commons.local_skill.conditionskill.ConditionSkill;
import ryey.easer.commons.local_skill.conditionskill.Tracker;
import ryey.easer.skills.SkillViewFragment;

public class RingerModeConditionSkill
    implements ConditionSkill<RingerModeConditionData> {

  @NonNull
  @Override
  public String id() {
    return "ringer_mode";
  }

  @Override
  public int name() {
    return R.string.condition_ringer_mode;
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
                                 final int requestCode) {}

  @NonNull
  @Override
  public ConditionDataFactory<RingerModeConditionData> dataFactory() {
    return new RingerModeConditionDataFactory();
  }

  @NonNull
  @Override
  public SourceCategory category() {
    return SourceCategory.device;
  }

  @NonNull
  @Override
  public SkillViewFragment<RingerModeConditionData> view() {
    return new RingerModeSkillViewFragment();
  }

  @NonNull
  @Override
  public Tracker<RingerModeConditionData>
  tracker(final @NonNull Context context,
          final @ValidData @NonNull RingerModeConditionData data,
          final @NonNull PendingIntent event_positive,
          final @NonNull PendingIntent event_negative) {
    return new RingerModeTracker(context, data, event_positive, event_negative);
  }
}
