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

package ryey.easer.skills.operation.airplane_mode;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.commons.local_skill.operationskill.PrivilegeUsage;
import ryey.easer.plugin.operation.Category;
import ryey.easer.skills.operation.OperationLoader;

public class AirplaneModeOperationSkill
	implements OperationSkill<AirplaneModeOperationData> {

@NonNull
@Override
public String id() {
	return "airplane_mode";
}

@Override
public int name() {
	return R.string.operation_airplane_mode;
}

@Override
public boolean isCompatible(@NonNull final Context context) {
	return true;
}

@NonNull
@Override
public PrivilegeUsage privilege() {
	return PrivilegeUsage.prefer_root;
}

@Override
public int maxExistence() {
	return 1;
}

@NonNull
@Override
public Category category() {
	return Category.system_config;
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
public OperationDataFactory<AirplaneModeOperationData> dataFactory() {
	return new AirplaneModeOperationDataFactory();
}

@NonNull
@Override
public SkillView<AirplaneModeOperationData> view() {
	return new AirplaneModeSkillViewFragment();
}

@NonNull
@Override
public OperationLoader<AirplaneModeOperationData>
loader(final @NonNull Context context) {
	return new AirplaneModeLoader(context);
}
}
