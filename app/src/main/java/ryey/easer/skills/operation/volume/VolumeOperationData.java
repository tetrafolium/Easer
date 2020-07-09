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

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;

public class VolumeOperationData implements OperationData {
    private static final String K_VOL_RING = "ring";
    private static final String K_VOL_MEDIA = "media";
    private static final String K_VOL_ALARM = "alarm";
    private static final String K_VOL_NOTIFICATION = "notification";
    private static final String K_VOL_BT = "bluetooth";
    private static final String K_BT_DELAY = "bluetooth_delay";

    Integer vol_ring;
    Integer vol_media;
    Integer vol_alarm;
    Integer vol_notification;
    Integer vol_bt;
    Integer bt_delay;

    VolumeOperationData(final Integer vol_ring, final Integer vol_media, final Integer vol_alarm, final Integer vol_notification, final Integer vol_bt, final Integer bt_delay) {
        this.vol_ring = vol_ring;
        this.vol_media = vol_media;
        this.vol_alarm = vol_alarm;
        this.vol_notification = vol_notification;
        this.vol_bt = vol_bt;
        this.bt_delay = bt_delay;
    }

    VolumeOperationData(final @NonNull String data, final @NonNull PluginDataFormat format, final int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    private static Integer optInteger(final JSONObject jsonObject, final String key) {
        int value = jsonObject.optInt(key, -1);
        if (value == -1)
            return null;
        return value;
    }

    private static void writeNonNull(final JSONObject jsonObject, final Integer value, final String key) throws JSONException {
        if (value != null)
            jsonObject.put(key, value);
    }

    public void parse(final @NonNull String data, final @NonNull PluginDataFormat format, final int version) throws IllegalStorageDataException {
        switch (format) {
        default:
            try {
                JSONObject jsonObject = new JSONObject(data);
                vol_ring = optInteger(jsonObject, K_VOL_RING);
                vol_media = optInteger(jsonObject, K_VOL_MEDIA);
                vol_alarm = optInteger(jsonObject, K_VOL_ALARM);
                vol_notification = optInteger(jsonObject, K_VOL_NOTIFICATION);
                vol_bt = optInteger(jsonObject, K_VOL_BT);
                bt_delay = optInteger(jsonObject, K_BT_DELAY);
            } catch (JSONException e) {
                throw new IllegalStorageDataException(e);
            }
        }
    }

    @NonNull
    @Override
    public String serialize(final @NonNull PluginDataFormat format) {
        String res;
        switch (format) {
        default:
            JSONObject jsonObject = new JSONObject();
            try {
                writeNonNull(jsonObject, vol_ring, K_VOL_RING);
                writeNonNull(jsonObject, vol_media, K_VOL_MEDIA);
                writeNonNull(jsonObject, vol_alarm, K_VOL_ALARM);
                writeNonNull(jsonObject, vol_notification, K_VOL_NOTIFICATION);
                writeNonNull(jsonObject, vol_bt, K_VOL_BT);
                writeNonNull(jsonObject, bt_delay, K_BT_DELAY);
                res = jsonObject.toString();
            } catch (JSONException e) {
                throw new IllegalStateException(e);
            }
        }
        return res;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !(obj instanceof VolumeOperationData))
            return false;
        if (!Utils.nullableEqual(vol_ring, ((VolumeOperationData) obj).vol_ring))
            return false;
        if (!Utils.nullableEqual(vol_media, ((VolumeOperationData) obj).vol_media))
            return false;
        if (!Utils.nullableEqual(vol_alarm, ((VolumeOperationData) obj).vol_alarm))
            return false;
        if (!Utils.nullableEqual(vol_notification, ((VolumeOperationData) obj).vol_notification))
            return false;
        if (!Utils.nullableEqual(vol_bt, ((VolumeOperationData) obj).vol_bt))
            return false;
        if (!Utils.nullableEqual(bt_delay, ((VolumeOperationData) obj).bt_delay))
            return false;
        return true;
    }

    @Override
    public boolean isValid() {
        return (isNotNegative(vol_ring)
                || isNotNegative(vol_media)
                || isNotNegative(vol_alarm)
                || isNotNegative(vol_notification)
                || isNotNegative(vol_bt)
                || isNotNegative(bt_delay))
               && ((vol_bt == null) == (bt_delay == null));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeValue(vol_ring);
        parcel.writeValue(vol_media);
        parcel.writeValue(vol_alarm);
        parcel.writeValue(vol_notification);
        parcel.writeValue(vol_bt);
        parcel.writeValue(bt_delay);
    }

    public static final Creator<VolumeOperationData> CREATOR
    = new Creator<VolumeOperationData>() {
        public VolumeOperationData createFromParcel(final Parcel in) {
            return new VolumeOperationData(in);
        }

        public VolumeOperationData[] newArray(final int size) {
            return new VolumeOperationData[size];
        }
    };

    private VolumeOperationData(final Parcel in) {
        vol_ring = (Integer) in.readValue(Integer.class.getClassLoader());
        vol_media = (Integer) in.readValue(Integer.class.getClassLoader());
        vol_alarm = (Integer) in.readValue(Integer.class.getClassLoader());
        vol_notification = (Integer) in.readValue(Integer.class.getClassLoader());
        vol_bt = (Integer) in.readValue(Integer.class.getClassLoader());
        bt_delay = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    private static boolean isNotNegative(final @Nullable Integer value) {
        return value == null || value >= 0;
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        return null;
    }

    @NonNull
    @Override
    public OperationData applyDynamics(final SolidDynamicsAssignment dynamicsAssignment) {
        return this;
    }
}
