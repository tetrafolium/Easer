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

package ryey.easer.commons.local_skill.conditionskill;

import android.app.PendingIntent;
import android.content.Context;
import androidx.annotation.NonNull;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;

public interface ConditionSkill<D extends ConditionData>
	extends Skill<D>, SourceCategory.Categorized {

@NonNull ConditionDataFactory<D> dataFactory();

@NonNull
default SourceCategory category() {
	return SourceCategory.unknown;
}

@NonNull
Tracker<D> tracker(@NonNull Context context, @ValidData @NonNull D data,
                   @NonNull PendingIntent event_positive,
                   @NonNull PendingIntent event_negative);
}
