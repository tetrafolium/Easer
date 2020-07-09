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

package ryey.easer.core.ui.data.script;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import ryey.easer.R;
import ryey.easer.commons.C;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.dynamics.DynamicsLink;
import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.commons.ui.DataSelectSpinnerWrapper;
import ryey.easer.core.data.ConditionStructure;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.ProfileStructure;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.ConditionDataStorage;
import ryey.easer.core.data.storage.EventDataStorage;
import ryey.easer.core.data.storage.ProfileDataStorage;
import ryey.easer.core.data.storage.ScriptDataStorage;
import ryey.easer.core.ui.data.AbstractEditDataActivity;
import ryey.easer.core.ui.data.event.EditEventDataFragment;

/*
 * TODO: change the layout
 */
public class EditScriptActivity
	extends AbstractEditDataActivity<ScriptStructure, ScriptDataStorage> {

private static final int REQ_CODE = 1;

static { TAG_DATA_TYPE = "script"; }

EditEventDataFragment editEventDataFragment;

EditText mEditText_name = null;
DataSelectSpinnerWrapper sw_parent;
DataSelectSpinnerWrapper sw_profile;
boolean isActive = true;
RadioGroup rg_mode;
RadioButton rb_inline, rb_event, rb_condition;
CompoundButton mSwitch_reverse;

ConstraintLayout layout_use_event;
DataSelectSpinnerWrapper sw_event;
CompoundButton mSwitch_repeatable;
CompoundButton mSwitch_persistent;

ConstraintLayout layout_use_condition;
DataSelectSpinnerWrapper sw_condition;

ImageButton dynamics;
DynamicsLink dynamicsLink;

@Override
public boolean onCreateOptionsMenu(final Menu menu) {
	getMenuInflater().inflate(R.menu.edit_event, menu);
	menu.findItem(R.id.action_toggle_active).setChecked(isActive);
	return true;
}

@Override
public boolean onOptionsItemSelected(final MenuItem item) {
	switch (item.getItemId()) {
	case R.id.action_toggle_active:
		isActive = !item.isChecked();
		item.setChecked(isActive);
		return true;
	}
	return super.onOptionsItemSelected(item);
}

@Override
protected ScriptDataStorage retDataStorage() {
	return new ScriptDataStorage(this);
}

@Override
protected String title() {
	return getString(R.string.title_script);
}

@Override
protected int contentViewRes() {
	return R.layout.activity_edit_script;
}

protected void init() {
	mEditText_name = findViewById(R.id.editText_script_title);

	sw_parent = new DataSelectSpinnerWrapper(
		this, (Spinner)findViewById(R.id.spinner_parent));
	sw_parent.beginInit()
	.setAllowEmpty(true)
	.fillData(new ScriptDataStorage(this).list())
	.finalizeInit();

	sw_profile = new DataSelectSpinnerWrapper(
		this, (Spinner)findViewById(R.id.spinner_profile));
	sw_profile.beginInit()
	.setAllowEmpty(true)
	.fillData(new ProfileDataStorage(this).list())
	.finalizeInit();

	mSwitch_reverse = findViewById(R.id.switch_reverse);

	editEventDataFragment =
		(EditEventDataFragment)getSupportFragmentManager().findFragmentById(
			R.id.fragment_inline_event);

	layout_use_event = findViewById(R.id.layout_use_event);
	sw_event = new DataSelectSpinnerWrapper(
		this, (Spinner)findViewById(R.id.spinner_event));
	sw_event.beginInit()
	.setAllowEmpty(false)
	.fillData(new EventDataStorage(this).list())
	.finalizeInit();
	mSwitch_repeatable = findViewById(R.id.switch_repeatable);
	mSwitch_persistent = findViewById(R.id.switch_persistent);

	layout_use_condition = findViewById(R.id.layout_use_condition);
	sw_condition = new DataSelectSpinnerWrapper(
		this, (Spinner)findViewById(R.id.spinner_condition));
	sw_condition.beginInit()
	.setAllowEmpty(false)
	.fillData(new ConditionDataStorage(this).list())
	.finalizeInit();

	rg_mode = findViewById(R.id.rg_trigger);
	rg_mode.setOnCheckedChangeListener(
		new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(final RadioGroup radioGroup,
			                             final int id) {
			        int v_inline = View.GONE, v_event = View.GONE,
			        v_condition = View.GONE;
			        if (id == R.id.radioButton_inline_event) {
			                v_inline = View.VISIBLE;
				} else if (id == R.id.radioButton_event) {
			                v_event = View.VISIBLE;
				} else if (id == R.id.radioButton_condition) {
			                v_condition = View.VISIBLE;
				} else {
			                throw new IllegalAccessError();
				}
			        if (v_inline == View.GONE)
					getSupportFragmentManager()
					.beginTransaction()
					.hide(editEventDataFragment)
					.commit();
			        else
					getSupportFragmentManager()
					.beginTransaction()
					.show(editEventDataFragment)
					.commit();
			        layout_use_event.setVisibility(v_event);
			        layout_use_condition.setVisibility(v_condition);
			}
		});
	rg_mode.check(R.id.radioButton_condition);
	rg_mode.check(R.id.radioButton_event);

	rb_inline = findViewById(R.id.radioButton_inline_event);
	rb_event = findViewById(R.id.radioButton_event);
	rb_condition = findViewById(R.id.radioButton_condition);

	dynamics = findViewById(R.id.btn_dynamics);
	dynamics.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
			        Intent intent =
					new Intent(EditScriptActivity.this, ListDynamicsActivity.class);
			        intent.putExtra(ListDynamicsActivity.EXTRA_DYNAMICS_LINK, dynamicsLink);
			        ArrayList<String> placeholders = new ArrayList<>();
			        String profileName = sw_profile.getSelection();
			        if (profileName == null) {
			                showDynamicsNotReady();
			                return;
				}
			        ProfileStructure profile =
					new ProfileDataStorage(EditScriptActivity.this).get(profileName);
			        placeholders.addAll(profile.placeholders());
			        intent.putStringArrayListExtra(ListDynamicsActivity.EXTRA_PLACEHOLDERS,
			                                       placeholders);
			        if (rg_mode.getCheckedRadioButtonId() ==
			            R.id.radioButton_inline_event) {
			                EventData eventData;
			                try {
			                        eventData = editEventDataFragment.saveToData();
					} catch (InvalidDataInputException e) {
			                        showDynamicsNotReady();
			                        return;
					}
			                intent.putExtra(ListDynamicsActivity.EXTRA_PLUGIN_TYPE,
			                                ListDynamicsActivity.PLUGIN_TYPE_EVENT);
			                intent.putExtra(ListDynamicsActivity.EXTRA_PLUGIN_DATA, eventData);
				} else if (rg_mode.getCheckedRadioButtonId() ==
			                   R.id.radioButton_event) {
			                EventDataStorage eventDataStorage =
						new EventDataStorage(EditScriptActivity.this);
			                String event_name = sw_event.getSelection();
			                if (event_name == null) {
			                        showDynamicsNotReady();
			                        return;
					}
			                EventStructure eventStructure = eventDataStorage.get(event_name);
			                intent.putExtra(ListDynamicsActivity.EXTRA_PLUGIN_DATA,
			                                eventStructure.getEventData());
			                intent.putExtra(ListDynamicsActivity.EXTRA_PLUGIN_TYPE,
			                                ListDynamicsActivity.PLUGIN_TYPE_EVENT);
				} else if (rg_mode.getCheckedRadioButtonId() ==
			                   R.id.radioButton_condition) {
			                intent.putExtra(ListDynamicsActivity.EXTRA_PLUGIN_TYPE,
			                                ListDynamicsActivity.PLUGIN_TYPE_CONDITION);
				}
			        startActivityForResult(intent, REQ_CODE);
			}
		});
}

private void showDynamicsNotReady() {
	Toast
	.makeText(EditScriptActivity.this, R.string.prompt_dynamics_not_ready,
	          Toast.LENGTH_LONG)
	.show();
}

@Override
protected void loadFromData(final ScriptStructure script) {
	rb_inline.setVisibility(View.GONE);

	oldName = script.getName();
	mEditText_name.setText(oldName);
	String profile = script.getProfileName();
	sw_profile.setSelection(profile);
	String parent = script.getParentName();
	sw_parent.setSelection(parent);

	mSwitch_reverse.setChecked(script.isReverse());

	if (script.isEvent()) {
		EventStructure scenario = script.getEvent();
		if (scenario.isTmpEvent()) {
			rb_inline.setVisibility(View.VISIBLE);
			rb_event.setText(R.string.text_predefined_event);
			rb_condition.setText(R.string.text_use_condition);

			rg_mode.check(R.id.radioButton_inline_event);
			EventData eventData = scenario.getEventData();
			editEventDataFragment.loadFromData(eventData);
		} else {
			rg_mode.check(R.id.radioButton_event);
			sw_event.setSelection(scenario.getName());
			mSwitch_repeatable.setChecked(script.isRepeatable());
			mSwitch_persistent.setChecked(script.isPersistent());
		}
	} else {
		rg_mode.check(R.id.radioButton_condition);
		ConditionStructure condition = script.getCondition();
		sw_condition.setSelection(condition.getName());
	}

	isActive = script.isActive();

	dynamicsLink = script.getDynamicsLink();
}

@Override
protected ScriptStructure saveToData() throws InvalidDataInputException {
	ScriptStructure script = new ScriptStructure(C.VERSION_CREATED_IN_RUNTIME);
	script.setName(mEditText_name.getText().toString());
	String profile = sw_profile.getSelection();
	script.setProfileName(profile);
	script.setActive(isActive);
	script.setParentName(sw_parent.getSelection());

	script.setReverse(mSwitch_reverse.isChecked());

	if (rg_mode.getCheckedRadioButtonId() == R.id.radioButton_inline_event) {
		EventDataStorage eventDataStorage = new EventDataStorage(this);
		script.setEventData(editEventDataFragment.saveToData());
	} else if (rg_mode.getCheckedRadioButtonId() == R.id.radioButton_event) {
		EventDataStorage eventDataStorage = new EventDataStorage(this);
		String scenario_name = sw_event.getSelection();
		script.setEvent(eventDataStorage.get(scenario_name));
		script.setRepeatable(mSwitch_repeatable.isChecked());
		script.setPersistent(mSwitch_persistent.isChecked());
	} else if (rg_mode.getCheckedRadioButtonId() ==
	           R.id.radioButton_condition) {
		ConditionDataStorage conditionDataStorage =
			new ConditionDataStorage(this);
		String condition_name = sw_condition.getSelection();
		script.setCondition(conditionDataStorage.get(condition_name));
	}
	script.setDynamicsLink(dynamicsLink);
	return script;
}

@Override
protected void onActivityResult(final int requestCode, final int resultCode,
                                final Intent data) {
	if (requestCode == REQ_CODE) {
		if (resultCode == Activity.RESULT_OK) {
			this.dynamicsLink =
				data.getParcelableExtra(ListDynamicsActivity.EXTRA_DYNAMICS_LINK);
		}
	} else
		super.onActivityResult(requestCode, resultCode, data);
}
}
