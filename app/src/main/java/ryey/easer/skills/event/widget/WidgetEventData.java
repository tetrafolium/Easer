/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.event.widget;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.event.AbstractEventData;

public class WidgetEventData extends AbstractEventData {

private final String K_WIDGET_TAG =
	"ryey.easer.skills.event.widget.KEY.WIDGET_TAG";

final String widgetTag;

WidgetEventData(final String widgetTag) {
	this.widgetTag = widgetTag;
}

WidgetEventData(final @NonNull String data,
                final @NonNull PluginDataFormat format, final int version)
throws IllegalStorageDataException {
	switch (format) {
	case JSON:
	default:
		try {
			JSONObject jsonObject = new JSONObject(data);
			widgetTag = jsonObject.getString(K_WIDGET_TAG);
		} catch (JSONException e) {
			throw new IllegalStorageDataException(e);
		}
	}
}

@NonNull
@Override
public String serialize(final @NonNull PluginDataFormat format) {
	String res = "";
	switch (format) {
	case JSON:
	default:
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(K_WIDGET_TAG, widgetTag);
			res = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
public boolean isValid() {
	return true;
}

@SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
@Override
public boolean equals(final Object obj) {
	if (!(obj instanceof WidgetEventData))
		return false;
	if (!widgetTag.equals(((WidgetEventData)obj).widgetTag))
		return false;
	return true;
}

@Override
public int describeContents() {
	return 0;
}

@Override
public void writeToParcel(final Parcel dest, final int flags) {
	dest.writeString(widgetTag);
}

public static final Creator<WidgetEventData> CREATOR =
	new Creator<WidgetEventData>() {
	public WidgetEventData createFromParcel(final Parcel in) {
		return new WidgetEventData(in);
	}

	public WidgetEventData[] newArray(final int size) {
		return new WidgetEventData[size];
	}
};

private WidgetEventData(final Parcel in) {
	widgetTag = in.readString();
}
}
