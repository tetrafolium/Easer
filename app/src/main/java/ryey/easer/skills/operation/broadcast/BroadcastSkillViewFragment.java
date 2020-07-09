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

package ryey.easer.skills.operation.broadcast;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;
import ryey.easer.skills.operation.EditExtraFragment;

public class BroadcastSkillViewFragment
	extends SkillViewFragment<BroadcastOperationData> {
private EditText m_text_action;
private EditText m_text_category;
private EditText m_text_type;
private EditText m_text_data;
private EditExtraFragment editExtraFragment;

@NonNull
@Override
public View onCreateView(final @NonNull LayoutInflater inflater,
                         final @Nullable ViewGroup container,
                         final @Nullable Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.plugin_operation__broadcast,
	                             container, false);
	m_text_action = view.findViewById(R.id.text_action);
	m_text_category = view.findViewById(R.id.text_category);
	m_text_type = view.findViewById(R.id.text_type);
	m_text_data = view.findViewById(R.id.text_data);
	editExtraFragment =
		(EditExtraFragment)getChildFragmentManager().findFragmentById(
			R.id.fragment_edit_extra);

	return view;
}

@Override
protected void _fill(final @ValidData @NonNull BroadcastOperationData data) {
	IntentData rdata = data.data;
	m_text_action.setText(rdata.action);
	m_text_category.setText(
		Utils.StringCollectionToString(rdata.category, false));
	m_text_type.setText(rdata.type);
	if (rdata.data != null)
		m_text_data.setText(rdata.data.toString());
	editExtraFragment.fillExtras(rdata.extras);
}

@ValidData
@NonNull
@Override
public BroadcastOperationData getData() throws InvalidDataInputException {
	IntentData data = new IntentData();
	data.action = m_text_action.getText().toString();
	data.category =
		Utils.stringToStringList(m_text_category.getText().toString());
	data.type = m_text_type.getText().toString();
	data.data = Uri.parse(m_text_data.getText().toString());
	data.extras = editExtraFragment.getExtras();
	BroadcastOperationData broadcastOperationData =
		new BroadcastOperationData(data);
	return broadcastOperationData;
}
}
