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

package ryey.easer.skills.event.timer;

import androidx.annotation.NonNull;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.eventskill.EventDataFactory;
import ryey.easer.plugin.PluginDataFormat;

class TimerEventDataFactory implements EventDataFactory<TimerEventData> {
  @NonNull
  @Override
  public Class<TimerEventData> dataClass() {
    return TimerEventData.class;
  }

  @ValidData
  @NonNull
  @Override
  public TimerEventData dummyData() {
    return new TimerEventData(false, 102, true, true);
  }

  @ValidData
  @NonNull
  @Override
  public TimerEventData parse(final @NonNull String data,
                              final @NonNull PluginDataFormat format,
                              final int version)
      throws IllegalStorageDataException {
    return new TimerEventData(data, format, version);
  }
}
