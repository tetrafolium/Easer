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

package ryey.easer.skills.operation.state_control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.ui.DataSelectSpinnerWrapper;
import ryey.easer.core.data.storage.ScriptDataStorage;
import ryey.easer.skills.SkillViewFragment;

public class StateControlSkillViewFragment
	extends SkillViewFragment<StateControlOperationData> {

private DataSelectSpinnerWrapper sw_script;

@NonNull
@Override
public View onCreateView(final @NonNull LayoutInflater inflater,
                         final @Nullable ViewGroup container,
                         final @Nullable Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.plugin_operation__event_control,
	                             container, false);
	sw_script = new DataSelectSpinnerWrapper(
		getContext(), (Spinner)view.findViewById(R.id.spinner_event));
	sw_script.beginInit()
	.setAllowEmpty(false)
	.fillData(new ScriptDataStorage(getContext()).list())
	.finalizeInit();
	return view;
}

@Override
protected void _fill(final
                     @ValidData @NonNull StateControlOperationData data) {
	sw_script.setSelection(data.scriptName);
}

@ValidData
@NonNull
@Override
public StateControlOperationData getData() throws InvalidDataInputException {
	String eventName = sw_script.getSelection();
	return new StateControlOperationData(eventName, false);
}
}
