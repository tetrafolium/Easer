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

package ryey.easer.skills.operation.ringer_mode;

import android.media.AudioManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Set;
import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;

public class RingerModeOperationData implements OperationData {

RingerMode ringerMode;

RingerModeOperationData(final RingerMode ringerMode) {
	this.ringerMode = ringerMode;
}

RingerModeOperationData(final @NonNull String data,
                        final @NonNull PluginDataFormat format,
                        final int version)
throws IllegalStorageDataException {
	parse(data, format, version);
}

public void parse(final @NonNull String data,
                  final @NonNull PluginDataFormat format, final int version)
throws IllegalStorageDataException {
	switch (format) {
	default:
		if (version < C.VERSION_GRANULAR_RINGER_MODE) {
			// backward compatible
			// was IntegerOperationData, [0, 2]
			Integer level = Integer.valueOf(data);
			switch (level) {
			case AudioManager.RINGER_MODE_SILENT:
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
					ringerMode = RingerMode.silent;
				else
					ringerMode = RingerMode.dnd_none;
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				ringerMode = RingerMode.vibrate;
				break;
			case AudioManager.RINGER_MODE_NORMAL:
				ringerMode = RingerMode.normal;
				break;
			default:
				throw new IllegalStateException(
					      "Compatibility for RingerMode shouldn't run out of cases");
			}
		} else {
			ringerMode = RingerMode.valueOf(data);
		}
	}
}

@NonNull
@Override
public String serialize(final @NonNull PluginDataFormat format) {
	String res = "";
	switch (format) {
	default:
		res = RingerMode.compatible(ringerMode).name();
	}
	return res;
}

@SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
@Override
public boolean equals(final Object obj) {
	if (obj == this)
		return true;
	if (!(obj instanceof RingerModeOperationData))
		return false;
	return Utils.nullableEqual(ringerMode,
	                           ((RingerModeOperationData)obj).ringerMode);
}

@SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
@Override
public boolean isValid() {
	return ringerMode != null;
}

public static final Parcelable.Creator<RingerModeOperationData> CREATOR =
	new Parcelable.Creator<RingerModeOperationData>() {
	public RingerModeOperationData createFromParcel(final Parcel in) {
		return new RingerModeOperationData(in);
	}

	public RingerModeOperationData[] newArray(final int size) {
		return new RingerModeOperationData[size];
	}
};

private RingerModeOperationData(final Parcel in) {
	ringerMode = (RingerMode)in.readSerializable();
}

@Override
public int describeContents() {
	return 0;
}

@Override
public void writeToParcel(final Parcel parcel, final int i) {
	parcel.writeSerializable(ringerMode);
}

@Nullable
@Override
public Set<String> placeholders() {
	return null;
}

@NonNull
@Override
public OperationData
applyDynamics(final SolidDynamicsAssignment dynamicsAssignment) {
	return this;
}
}
