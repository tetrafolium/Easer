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

package ryey.easer.skills.operation.send_sms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class SmsSkillViewFragment extends SkillViewFragment<SmsOperationData> {
    private EditText et_destination;
    private EditText et_content;

    @NonNull
    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_operation__send_sms, container, false);
        et_destination = view.findViewById(R.id.editText_destination);
        et_content = view.findViewById(R.id.editText_content);
        return view;
    }

    @Override
    protected void _fill(final @ValidData @NonNull SmsOperationData data) {
        et_destination.setText(data.destination);
        et_content.setText(data.content);
    }

    @ValidData
    @NonNull
    @Override
    public SmsOperationData getData() throws InvalidDataInputException {
        String destination = et_destination.getText().toString();
        String content = et_content.getText().toString();
        return new SmsOperationData(destination, content);
    }
}
