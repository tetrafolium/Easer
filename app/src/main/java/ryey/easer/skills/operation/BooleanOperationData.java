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

package ryey.easer.skills.operation;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Set;

import ryey.easer.commons.C;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.plugin.PluginDataFormat;
import ryey.easer.skills.reusable.BooleanData;

public abstract class BooleanOperationData extends BooleanData implements OperationData {

    protected BooleanOperationData() {
        super(); }

    protected BooleanOperationData(final @NonNull Boolean state) {
        super(state);
    }

    public BooleanOperationData(final @NonNull String data, final @NonNull PluginDataFormat format, final int version) throws IllegalStorageDataException {
        parse(data, format, version);
    }

    public void parse(final @NonNull String data, final @NonNull PluginDataFormat format, final int version) throws IllegalStorageDataException {
        switch (format) {
            default:
                switch (data) {
                    case C.ON:
                        set(true);
                        break;
                    case C.OFF:
                        set(false);
                        break;
                    default:
                        throw new IllegalStorageDataException("Unknown data");
                }
        }
    }

    @NonNull
    @Override
    public String serialize(final @NonNull PluginDataFormat format) {
        String res;
        switch (format) {
            default:
                Boolean state = get();
                if (state)
                    res = C.ON;
                else
                    res = C.OFF;
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final @NonNull Parcel dest, final int flags) {
        dest.writeByte((byte) (state ? 1 : 0));
    }

    protected BooleanOperationData(final @NonNull Parcel in) {
        state = in.readByte() != 0;
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
