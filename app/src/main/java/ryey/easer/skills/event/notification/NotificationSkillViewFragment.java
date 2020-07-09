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

package ryey.easer.skills.event.notification;

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

public class NotificationSkillViewFragment
	extends SkillViewFragment<NotificationEventData> {
private EditText editText_app;
private EditText editText_title;
private EditText editText_content;

@NonNull
@Override
public View onCreateView(final @NonNull LayoutInflater inflater,
                         final @Nullable ViewGroup container,
                         final @Nullable Bundle savedInstanceState) {
	View view =
		inflater.inflate(R.layout.plugin_event__notification, container, false);

	editText_app = view.findViewById(R.id.editText_app);
	editText_title = view.findViewById(R.id.editText_title);
	editText_content = view.findViewById(R.id.editText_content);

	return view;
}

@Override
protected void _fill(final @ValidData @NonNull NotificationEventData data) {
	editText_app.setText(data.app);
	editText_title.setText(data.title);
	editText_content.setText(data.content);
}

@ValidData
@NonNull
@Override
public NotificationEventData getData() throws InvalidDataInputException {
	String app = textOf(editText_app);
	String title = textOf(editText_title);
	String content = textOf(editText_content);
	return new NotificationEventData(app, title, content);
}

@Nullable
private static String textOf(final EditText editText) {
	String text = editText.getText().toString();
	if (text.isEmpty())
		return null;
	else
		return text;
}
}
