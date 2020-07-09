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

package ryey.easer.skills.operation.volume;

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

public class VolumeOperationSkill implements OperationSkill<VolumeOperationData> {

    static final int STREAM_BLUETOOTH = 6;

    @NonNull
    @Override
    public String id() {
        return "volume";
    }

    @Override
    public int name() {
        return R.string.operation_volume;
    }

    @Override
    public boolean isCompatible(@NonNull final Context context) {
        return true;
    }

    @NonNull
    @Override
    public PrivilegeUsage privilege() {
        return PrivilegeUsage.no_root;
    }

    @Override
    public int maxExistence() {
        return 0;
    }

    @NonNull
    @Override
    public Category category() {
        return Category.system_config;
    }

    @Nullable
    @Override
    public Boolean checkPermissions(final @NonNull Context context) {
        return true;
    }

    @Override
    public void requestPermissions(final @NonNull Activity activity, final int requestCode) {

    }

    @NonNull
    @Override
    public OperationDataFactory<VolumeOperationData> dataFactory() {
        return new VolumeOperationDataFactory();

    }

    @NonNull
    @Override
    public SkillView<VolumeOperationData> view() {
        return new VolumeOperationSkillViewFragment();
    }

    @NonNull
    @Override
    public OperationLoader<VolumeOperationData> loader(final @NonNull Context context) {
        return new VolumeLoader(context);
    }

}
