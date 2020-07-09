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

package ryey.easer.skills.usource.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.Dynamics;
import ryey.easer.commons.local_skill.usource.USourceData;
import ryey.easer.plugin.PluginDataFormat;

public class WifiUSourceData implements USourceData {
private static final String K_ESSID = "essid";
private static final String K_BSSID = "bssid";

boolean mode_essid = true;
Set<String> ssids = new ArraySet<>();

WifiUSourceData(final String ssids, final boolean mode_essid) {
	this.mode_essid = mode_essid;
	setFromMultiple(ssids.split("\n"));
}

WifiUSourceData(final @NonNull String data,
                final @NonNull PluginDataFormat format, final int version)
throws IllegalStorageDataException {
	switch (format) {
	default:
		try {
			if (version < C.VERSION_WIFI_ADD_BSSID) {
				JSONArray jsonArray = new JSONArray(data);
				readFromJsonArray(jsonArray);
			} else {
				JSONObject jsonObject = new JSONObject(data);
				if (jsonObject.has(K_ESSID)) {
					mode_essid = true;
					readFromJsonArray(jsonObject.getJSONArray(K_ESSID));
				} else {
					mode_essid = false;
					readFromJsonArray(jsonObject.getJSONArray(K_BSSID));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IllegalStorageDataException(e);
		}
	}
}

private void setFromMultiple(final String[] ssids) {
	this.ssids.clear();
	for (String ssid : ssids) {
		if (!Utils.isBlank(ssid))
			this.ssids.add(ssid.trim());
	}
}

@SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
@Override
public boolean isValid() {
	if (ssids.size() == 0)
		return false;
	return true;
}

@Nullable
@Override
public Dynamics[] dynamics() {
	return null;
}

@SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
@Override
public boolean equals(final Object obj) {
	if (obj == null || !(obj instanceof WifiUSourceData))
		return false;
	if (mode_essid != ((WifiUSourceData)obj).mode_essid)
		return false;
	if (!ssids.equals(((WifiUSourceData)obj).ssids))
		return false;
	return true;
}

private void readFromJsonArray(final JSONArray jsonArray)
throws JSONException {
	for (int i = 0; i < jsonArray.length(); i++) {
		ssids.add(jsonArray.getString(i));
	}
}

@NonNull
@Override
public String serialize(final @NonNull PluginDataFormat format) {
	String res;
	switch (format) {
	default:
		try {
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			for (String ssid : ssids) {
				jsonArray.put(ssid);
			}
			if (mode_essid)
				jsonObject.put(K_ESSID, jsonArray);
			else
				jsonObject.put(K_BSSID, jsonArray);
			res = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}
	return res;
}

public boolean match(final @NonNull Object obj) {
	if (obj instanceof String) {
		return ssids.contains(((String)obj).trim());
	}
	return equals(obj);
}

@Override
public int describeContents() {
	return 0;
}

@Override
public void writeToParcel(final Parcel dest, final int flags) {
	dest.writeByte((byte)(mode_essid ? 1 : 0));
	dest.writeStringList(new ArrayList<>(ssids));
}

public static final Parcelable.Creator<WifiUSourceData> CREATOR =
	new Parcelable.Creator<WifiUSourceData>() {
	public WifiUSourceData createFromParcel(final Parcel in) {
		return new WifiUSourceData(in);
	}

	public WifiUSourceData[] newArray(final int size) {
		return new WifiUSourceData[size];
	}
};

private WifiUSourceData(final Parcel in) {
	mode_essid = in.readByte() > 0;
	List<String> list = new ArrayList<>();
	in.readStringList(list);
	ssids.addAll(list);
}
}
