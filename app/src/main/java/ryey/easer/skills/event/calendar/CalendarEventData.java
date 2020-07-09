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

package ryey.easer.skills.event.calendar;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.event.AbstractEventData;

public class CalendarEventData extends AbstractEventData {

    private static final String T_calendar_id = "calendar_id";
    private static final String T_condition = "condition";

    CalendarData data;

    CalendarEventData(CalendarData data) {
        this.data = data;
    }

    CalendarEventData(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (data == null)
            return false;
        if (data.calendar_id == -1)
            return false;
        if (data.conditions.size() == 0)
            return false;
        return true;
    }

    public void parse(@NonNull String data, @NonNull PluginDataFormat format, int version) throws IllegalStorageDataException {
        switch (format) {
        default:
            try {
                JSONObject jsonObject = new JSONObject(data);
                this.data = new CalendarData();
                this.data.calendar_id = jsonObject.optLong(T_calendar_id);
                JSONArray jsonArray_conditions = jsonObject.optJSONArray(T_condition);
                for (int i = 0; i < jsonArray_conditions.length(); i++) {
                    String condition = jsonArray_conditions.getString(i);
                    for (int j = 0; j < CalendarData.condition_name.length; j++) {
                        this.data.conditions.add(condition);
                    }
                }
            } catch (JSONException e) {
                Logger.e(e, "Error parsing %s data to SUFFIX", getClass().getSimpleName());
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull PluginDataFormat format) {
        String res;
        switch (format) {
        default:
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(T_calendar_id, data.calendar_id);
                JSONArray jsonArray_conditions = new JSONArray();
                for (String k : data.conditions) {
                    jsonArray_conditions.put(k);
                }
                jsonObject.put(T_condition, jsonArray_conditions);
            } catch (JSONException e) {
                Logger.e(e, "Error putting %s data", getClass().getSimpleName());
                e.printStackTrace();
            }
            res = jsonObject.toString();
        }
        return res;
    }

    @Nullable
    @Override
    public Dynamics[] dynamics() {
        return null;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof CalendarEventData))
            return false;
        if (data.calendar_id != ((CalendarEventData) obj).data.calendar_id)
            return false;
        if (!data.conditions.equals(((CalendarEventData) obj).data.conditions))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(data.calendar_id);
        dest.writeList(new ArrayList<>(data.conditions));
    }

    public static final Parcelable.Creator<CalendarEventData> CREATOR
    = new Parcelable.Creator<CalendarEventData>() {
        public CalendarEventData createFromParcel(Parcel in) {
            return new CalendarEventData(in);
        }

        public CalendarEventData[] newArray(int size) {
            return new CalendarEventData[size];
        }
    };

    private CalendarEventData(Parcel in) {
        data = new CalendarData();
        data.calendar_id = in.readLong();
        ArrayList<String> list = new ArrayList<>();
        in.readList(list, null);
        data.conditions = new ArraySet<>(list);
    }
}
