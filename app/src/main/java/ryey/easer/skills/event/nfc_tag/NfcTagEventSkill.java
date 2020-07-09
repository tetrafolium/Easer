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

package ryey.easer.skills.event.nfc_tag;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ryey.easer.R;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.SourceCategory;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.eventskill.EventDataFactory;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.event.AbstractSlot;

public class NfcTagEventSkill implements EventSkill<NfcTagEventData> {

@NonNull
@Override
public String id() {
	return "nfc_tag";
}

@Override
public int name() {
	return R.string.event_nfc_tag;
}

@Override
public boolean isCompatible(@NonNull final Context context) {
	NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
	return adapter != null;
}

@Nullable
@Override
public Boolean checkPermissions(final @NonNull Context context) {
	return SkillUtils.checkPermission(context, Manifest.permission.NFC);
}

@Override
public void requestPermissions(final @NonNull Activity activity,
                               final int requestCode) {
	SkillUtils.requestPermission(activity, requestCode,
	                             Manifest.permission.NFC);
}

@NonNull
@Override
public EventDataFactory<NfcTagEventData> dataFactory() {
	return new NfcTagEventDataFactory();
}

@NonNull
@Override
public SourceCategory category() {
	return SourceCategory.data_communication;
}

@NonNull
@Override
public SkillView<NfcTagEventData> view() {
	return new NfcTagSkillViewFragment();
}

@Override
public AbstractSlot<NfcTagEventData>
slot(final @NonNull Context context,
     final @ValidData @NonNull NfcTagEventData data) {
	return new NfcTagSlot(context, data);
}

@Override
public AbstractSlot<NfcTagEventData>
slot(final @NonNull Context context, final @NonNull NfcTagEventData data,
     final boolean retriggerable, final boolean persistent) {
	return new NfcTagSlot(context, data, retriggerable, persistent);
}
}
