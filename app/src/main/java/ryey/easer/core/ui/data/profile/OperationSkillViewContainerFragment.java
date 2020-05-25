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

package ryey.easer.core.ui.data.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.core.ui.data.SkillViewContainerFragment;
import ryey.easer.skills.LocalSkillRegistry;

public class OperationSkillViewContainerFragment<T extends OperationData> extends SkillViewContainerFragment<T> {

    private static final String EXTRA_PLUGIN = "plugin";

    static <T extends OperationData> OperationSkillViewContainerFragment<T> createInstance(final OperationSkill<T> plugin) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PLUGIN, plugin.id());
        OperationSkillViewContainerFragment<T> fragment = new OperationSkillViewContainerFragment<>();
        fragment.setArguments(bundle);
        return fragment;
    }

    private CheckBox mCheckBox;

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String plugin_id = getArguments().getString(EXTRA_PLUGIN);
        @SuppressWarnings("unchecked") OperationSkill<T> plugin = LocalSkillRegistry.getInstance().operation().findSkill(plugin_id);
        pluginViewFragment = plugin.view();
    }

    @NonNull
    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skillview_container_operation_profile, container, false);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content_pluginview, pluginViewFragment)
                .commit();
        getChildFragmentManager().executePendingTransactions();
        mCheckBox = view.findViewById(R.id.checkbox_pluginview_enabled);
        pluginViewFragment.setEnabled(false);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                pluginViewFragment.setEnabled(b);
            }
        });
        String desc = pluginViewFragment.desc(getResources());
        ((TextView) view.findViewById(R.id.text_pluginview_desc)).setText(desc);

        return view;
    }

    @Override
    protected void _fill(final @NonNull T data) {
        mCheckBox.setChecked(true);
        super._fill(data);
    }

    @NonNull
    @Override
    public T getData() throws InvalidDataInputException {
        if (mCheckBox.isChecked()) {
            return super.getData();
        } else {
            throw new IllegalStateException("The view should be checked as \"enabled\" before getting its data");
        }
    }

    boolean isEnabled() {
        return mCheckBox.isChecked();
    }
}
