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

package ryey.easer.skills.operation.brightness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class BrightnessSkillViewFragment extends SkillViewFragment<BrightnessOperationData> {
    private Switch mIsAuto;
    private SeekBar mBrightnessLevel;

    @NonNull
    @Override
    public ViewGroup onCreateView(final @NonNull LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        LinearLayout view = new LinearLayout(getContext());
        view.setOrientation(LinearLayout.VERTICAL);

        LinearLayout auto_layout = new LinearLayout(getContext());
        auto_layout.setOrientation(LinearLayout.HORIZONTAL);
        TextView tv_auto = new TextView(getContext());
        tv_auto.setText(getResources().getString(R.string.operation_brightness_desc_autobrightness));
        mIsAuto = new Switch(getContext());
        mIsAuto.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBrightnessLevel = new SeekBar(getContext());
        mBrightnessLevel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBrightnessLevel.setMax(255);
        mBrightnessLevel.setEnabled(false);
        mIsAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (isChecked)
                    mBrightnessLevel.setEnabled(false);
                else
                    mBrightnessLevel.setEnabled(true);
            }
        });
        auto_layout.addView(mIsAuto);
        auto_layout.addView(tv_auto);

        view.addView(auto_layout);
        view.addView(mBrightnessLevel);

        return view;
    }

    @Override
    protected void _fill(final @ValidData @NonNull BrightnessOperationData data) {
        BrightnessOperationData idata = data;
        if (idata.isAuto()) {
            mIsAuto.setChecked(true);
        } else {
            mIsAuto.setChecked(false);
            mBrightnessLevel.setProgress(idata.get());
        }
    }

    @ValidData
    @NonNull
    @Override
    public BrightnessOperationData getData() throws InvalidDataInputException {
        if (mIsAuto.isChecked())
            return new BrightnessOperationData(true);
        else {
            return new BrightnessOperationData(mBrightnessLevel.getProgress());
        }
    }
}
