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

package ryey.easer.skills.operation.alarm;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;

public class AlarmOperationData implements OperationData {
    private static final String K_TIME = "time";
    private static final String K_MESSAGE = "message";
    private static final String K_ABSOLUTE_BOOL = "absolute?";

    private static final int[] TIME_FIELDS = new int[] {Calendar.HOUR_OF_DAY, Calendar.MINUTE};

    private static final SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm", Locale.US);

    static String TimeToText(final Calendar calendar) {
        return sdf_time.format(calendar.getTime());
    }

    static Calendar TextToTime(final String text) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf_time.parse(text));
        return calendar;
    }

    Calendar time;
    String message;
    boolean absolute = true;

    AlarmOperationData(final Calendar time, final String message, final boolean absolute) {
        this.time = time;
        this.message = message;
        this.absolute = absolute;
    }

    AlarmOperationData(final @NonNull String data, final @NonNull PluginDataFormat format, final int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(final @NonNull String data, final @NonNull PluginDataFormat format, final int version) throws IllegalStorageDataException {
        switch (format) {
        default:
            try {
                JSONObject jsonObject = new JSONObject(data);
                try {
                    time = TextToTime(jsonObject.getString(K_TIME));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new IllegalStorageDataException(e);
                }
                message = jsonObject.optString(K_MESSAGE, null);
                absolute = jsonObject.optBoolean(K_ABSOLUTE_BOOL, true);
            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalStorageDataException(e);
            }
        }
    }

    @NonNull
    @Override
    public String serialize(final @NonNull PluginDataFormat format) {
        String res = "";
        switch (format) {
        default:
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(K_TIME, TimeToText(time));
                jsonObject.put(K_MESSAGE, message);
                jsonObject.put(K_ABSOLUTE_BOOL, absolute);
                res = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean isValid() {
        if (time == null)
            return false;
        return true;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof AlarmOperationData))
            return false;
        if (!Utils.nullableEqual(message, ((AlarmOperationData) obj).message))
            return false;
        for (int field : TIME_FIELDS)
            if (time.get(field) != ((AlarmOperationData) obj).time.get(field))
                return false;
        if (absolute != ((AlarmOperationData) obj).absolute)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        for (int field : TIME_FIELDS) {
            dest.writeInt(time.get(field));
        }
        dest.writeString(message);
        dest.writeByte((byte) (absolute ? 1 : 0));
    }

    public static final Creator<AlarmOperationData> CREATOR
    = new Creator<AlarmOperationData>() {
        public AlarmOperationData createFromParcel(final Parcel in) {
            return new AlarmOperationData(in);
        }

        public AlarmOperationData[] newArray(final int size) {
            return new AlarmOperationData[size];
        }
    };

    private AlarmOperationData(final Parcel in) {
        time = Calendar.getInstance();
        for (int field : TIME_FIELDS) {
            time.set(field, in.readInt());
        }
        message = in.readString();
        absolute = in.readByte() != 0;
    }

    @Nullable
    @Override
    public Set<String> placeholders() {
        return Utils.extractPlaceholder(message);
    }

    @NonNull
    @Override
    public OperationData applyDynamics(final SolidDynamicsAssignment dynamicsAssignment) {
        String new_message = Utils.applyDynamics(message, dynamicsAssignment);
        return new AlarmOperationData(time, new_message, absolute);
    }
}
