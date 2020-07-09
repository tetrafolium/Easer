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

package ryey.easer.skills.event.condition_event;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import ryey.easer.R;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.event.AbstractEventData;

public class ConditionEventEventData extends AbstractEventData {

    enum ConditionEvent {
        enter,
        leave,
    }

    private static final String K_CONDITION_NAME = "condition_name";
    private static final String K_CONDITION_EVENT = "condition_event";

    public final String conditionName;
    final ConditionEvent conditionEvent;

    ConditionEventEventData(final @NonNull String data, final @NonNull PluginDataFormat format, final int version) throws IllegalStorageDataException {
        switch (format) {
        default:
            try {
                JSONObject jsonObject = new JSONObject(data);
                conditionName = jsonObject.getString(K_CONDITION_NAME);
                conditionEvent = ConditionEvent.valueOf(jsonObject.getString(K_CONDITION_EVENT));
            } catch (JSONException e) {
                throw new IllegalStorageDataException(e);
            }
        }
    }

    ConditionEventEventData(final String conditionName, final ConditionEvent conditionEvent) {
        this.conditionName = conditionName;
        this.conditionEvent = conditionEvent;
    }

    public ConditionEventEventData(final ConditionEventEventData ref, final String newConditionName) {
        conditionName = newConditionName;
        conditionEvent = ref.conditionEvent;
    }

    @NonNull
    @Override
    public String serialize(final @NonNull PluginDataFormat format) {
        String res;
        switch (format) {
        default:
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(K_CONDITION_NAME, conditionName);
                jsonObject.put(K_CONDITION_EVENT, conditionEvent.name());
                res = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        return true;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return new Dynamics[] {new ConditionNameDynamics()};
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof ConditionEventEventData))
            return false;
        return conditionEvent.equals(((ConditionEventEventData) obj).conditionEvent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(conditionName);
        dest.writeInt(conditionEvent.ordinal());
    }

    public static final Creator<ConditionEventEventData> CREATOR
    = new Creator<ConditionEventEventData>() {
        public ConditionEventEventData createFromParcel(final Parcel in) {
            return new ConditionEventEventData(in);
        }

        public ConditionEventEventData[] newArray(final int size) {
            return new ConditionEventEventData[size];
        }
    };

    private ConditionEventEventData(final Parcel in) {
        conditionName = in.readString();
        conditionEvent = ConditionEvent.values()[in.readInt()];
    }

    public static class ConditionNameDynamics implements Dynamics {

        public static final String id = "ryey.easer.skills.event.condition_event.condition_name";

        @Override
        public String id() {
            return id;
        }

        @Override
        public int nameRes() {
            return R.string.event_condition_event_dynamics_name;
        }
    }
}
