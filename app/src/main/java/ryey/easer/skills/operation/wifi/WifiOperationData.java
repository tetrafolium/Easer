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

package ryey.easer.skills.operation.wifi;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.operation.BooleanOperationData;

public class WifiOperationData extends BooleanOperationData {

    WifiOperationData(final Boolean state) {
        super(state);
    }

    WifiOperationData(final @NonNull String data, final @NonNull PluginDataFormat format, final int version) throws IllegalStorageDataException {
        super(data, format, version);
    }

    public static final Parcelable.Creator<WifiOperationData> CREATOR
            = new Parcelable.Creator<WifiOperationData>() {
        public WifiOperationData createFromParcel(final Parcel in) {
            return new WifiOperationData(in);
        }

        public WifiOperationData[] newArray(final int size) {
            return new WifiOperationData[size];
        }
    };

    private WifiOperationData(final Parcel in) {
        super(in);
    }

}
