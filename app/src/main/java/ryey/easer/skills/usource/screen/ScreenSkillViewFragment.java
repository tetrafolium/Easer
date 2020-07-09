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

package ryey.easer.skills.usource.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ryey.easer.R;
import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillViewFragment;

public class ScreenSkillViewFragment
    extends SkillViewFragment<ScreenUSourceData> {

  RadioButton rb_screen_on, rb_screen_off, rb_screen_unlocked;

  @NonNull
  @Override
  public View onCreateView(final @NonNull LayoutInflater inflater,
                           final @Nullable ViewGroup container,
                           final @Nullable Bundle savedInstanceState) {
    View view =
        inflater.inflate(R.layout.plugin_usource__screen, container, false);
    rb_screen_on = view.findViewById(R.id.radioButton_screen_on);
    rb_screen_off = view.findViewById(R.id.radioButton_screen_off);
    rb_screen_unlocked = view.findViewById(R.id.radioButton_screen_unlocked);
    return view;
  }

  @Override
  protected void _fill(final @ValidData @NonNull ScreenUSourceData data) {
    if (data.screenEvent == ScreenUSourceData.ScreenEvent.on)
      rb_screen_on.setChecked(true);
    else if (data.screenEvent == ScreenUSourceData.ScreenEvent.off)
      rb_screen_off.setChecked(true);
    else if (data.screenEvent == ScreenUSourceData.ScreenEvent.unlocked)
      rb_screen_unlocked.setChecked(true);
  }

  @ValidData
  @NonNull
  @Override
  public ScreenUSourceData getData() throws InvalidDataInputException {
    if (rb_screen_on.isChecked())
      return new ScreenUSourceData(ScreenUSourceData.ScreenEvent.on);
    else if (rb_screen_off.isChecked())
      return new ScreenUSourceData(ScreenUSourceData.ScreenEvent.off);
    else
      return new ScreenUSourceData(ScreenUSourceData.ScreenEvent.unlocked);
  }
}
