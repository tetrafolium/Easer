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

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class VolumeOperationSkillViewFragment extends SkillViewFragment<VolumeOperationData> {

    CheckBox checkBox_ring, checkBox_media, checkBox_alarm, checkBox_notification, checkBox_bt;
    SeekBar seekBar_ring, seekBar_media, seekBar_alarm, seekBar_notification, seekBar_bt;
    EditText et_bt_delay;

    @NonNull
    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.plugin_operation__volume, container, false);

        //noinspection ConstantConditions
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            Logger.e("Couldn't get AudioManager");
            throw new IllegalAccessError();
        }

        seekBar_ring = view.findViewById(R.id.seekBar_ring);
        seekBar_ring.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        checkBox_ring = view.findViewById(R.id.checkBox_ring);
        checkBox_ring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                seekBar_ring.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        seekBar_media = view.findViewById(R.id.seekBar_media);
        seekBar_media.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        checkBox_media = view.findViewById(R.id.checkBox_media);
        checkBox_media.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                seekBar_media.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        seekBar_alarm = view.findViewById(R.id.seekBar_alarm);
        seekBar_alarm.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        checkBox_alarm = view.findViewById(R.id.checkBox_alarm);
        checkBox_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                seekBar_alarm.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        seekBar_notification = view.findViewById(R.id.seekBar_notification);
        seekBar_notification.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        checkBox_notification = view.findViewById(R.id.checkBox_notification);
        checkBox_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                seekBar_notification.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        seekBar_bt = view.findViewById(R.id.seekBar_bt);
        seekBar_bt.setMax(audioManager.getStreamMaxVolume(VolumeOperationSkill.STREAM_BLUETOOTH));
        et_bt_delay = view.findViewById(R.id.editText_bt_delay);
        checkBox_bt = view.findViewById(R.id.checkBox_bt);
        checkBox_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                int visibility = b ? View.VISIBLE : View.GONE;
                seekBar_bt.setVisibility(visibility);
                view.findViewById(R.id.textView_bt_delay).setVisibility(visibility);
                et_bt_delay.setVisibility(visibility);
            }
        });

        return view;
    }

    @Override
    protected void _fill(final @ValidData @NonNull VolumeOperationData data) {
        setVolumeVisual(checkBox_ring, seekBar_ring, data.vol_ring);
        setVolumeVisual(checkBox_media, seekBar_media, data.vol_media);
        setVolumeVisual(checkBox_alarm, seekBar_alarm, data.vol_alarm);
        setVolumeVisual(checkBox_notification, seekBar_notification, data.vol_notification);
        setVolumeVisual(checkBox_bt, seekBar_bt, data.vol_bt);
        if (data.vol_bt != null) {
            String bt_delay_str = data.bt_delay == null ? "0" : data.bt_delay.toString();
            et_bt_delay.setText(bt_delay_str);
        }
    }

    @ValidData
    @NonNull
    @Override
    public VolumeOperationData getData() throws InvalidDataInputException {
        Integer vol_ring = getVolume(seekBar_ring);
        Integer vol_media = getVolume(seekBar_media);
        Integer vol_alarm = getVolume(seekBar_alarm);
        Integer vol_notification = getVolume(seekBar_notification);
        Integer vol_bt = getVolume(seekBar_bt);
        Integer bt_delay = null;
        if (vol_bt != null) {
            try {
                bt_delay = Integer.valueOf(et_bt_delay.getText().toString());
            } catch (NumberFormatException e) {
                throw new InvalidDataInputException("bluetooth_delay is invalid");
            }
        }
        return new VolumeOperationData(vol_ring, vol_media, vol_alarm, vol_notification, vol_bt, bt_delay);
    }

    private static void setVolumeVisual(final CheckBox checkBox, final SeekBar seekBar, final Integer value) {
        if (value == null) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
            seekBar.setProgress(value);
        }
    }

    private static Integer getVolume(final SeekBar seekBar) {
        if (seekBar.getVisibility() == View.VISIBLE)
            return seekBar.getProgress();
        else
            return null;
    }
}
