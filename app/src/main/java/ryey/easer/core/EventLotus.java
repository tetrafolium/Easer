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

package ryey.easer.core;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.orhanobut.logger.Logger;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import ryey.easer.SettingsUtils;
import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.commons.local_skill.eventskill.Slot;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.ScriptTree;
import ryey.easer.skills.LocalSkillRegistry;

/*
 * Note: old document; may be outdated.
 *
 * The root of that tree will be registered listener.
 * When the root is satisfied, Lotus will recursively check all children to find
 * which is satisfied. After finding a satisfied child and the child has a
 * Profile, Lotus will load the Profile.
 *
 * See onSlotSatisfied() for the actual way to check and determine which is
 * satisfied.
 */
class EventLotus extends Lotus {

  private Slot mSlot;

  private final long cooldownInMillisecond;
  private Calendar lastSatisfied;

  private final boolean repeatable;
  private final boolean persistent;

  EventLotus(final @NonNull Context context,
             final @NonNull ScriptTree scriptTree,
             final @NonNull ExecutorService executorService,
             final @NonNull EHService.DelayedConditionHolderBinderJobs jobCH,
             final @NonNull AsyncHelper.DelayedLoadProfileJobs jobLP) {
    super(context, scriptTree, executorService, jobCH, jobLP);

    repeatable = scriptTree.isRepeatable();
    persistent = scriptTree.isPersistent();
    mSlot = nodeToSlot(scriptTree);
    mSlot.register(uri);

    cooldownInMillisecond = SettingsUtils.coolDownInterval(context) * 1000;
  }

  private <T extends EventData> Slot<T> nodeToSlot(final ScriptTree node) {
    EventStructure scenario = node.getEvent();
    Slot<T> slot;
    // noinspection unchecked
    T data = (T)scenario.getEventData();
    // noinspection unchecked
    EventSkill<T> plugin =
        LocalSkillRegistry.getInstance().event().findSkill(data);
    if (scenario.isTmpEvent()) {
      slot = plugin.slot(context, data);
    } else {
      slot = plugin.slot(context, data, repeatable, persistent);
    }
    return slot;
  }

  protected synchronized void onListen() {
    executorService.submit(new Runnable() {
      @Override
      public void run() {
        mSlot.listen();
      }
    });
  }

  protected synchronized void onCancel() {
    executorService.submit(new Runnable() {
      @Override
      public void run() {
        mSlot.cancel();
      }
    });
  }

  private boolean checkAndSetCooldown(final String eventName) {
    if (cooldownInMillisecond > 0) {
      Calendar now = Calendar.getInstance();
      if (lastSatisfied != null) {
        if (now.getTimeInMillis() - lastSatisfied.getTimeInMillis() <
            cooldownInMillisecond) {
          Logger.d("event <%s> is within cooldown time", eventName);
          return false;
        }
      }
      Logger.d("event <%s> is not within cooldown time", eventName);
      lastSatisfied = now;
      return true;
    }
    return true;
  }

  protected synchronized void onSatisfied(final Bundle extras) {
    if (!repeatable && satisfied)
      return;
    if (checkAndSetCooldown(scriptTree.getName())) {
      super.onSatisfied(extras);
    }
  }

  protected synchronized void onUnsatisfied() {
    if (persistent && satisfied)
      return;
    if (checkAndSetCooldown(scriptTree.getName())) {
      super.onUnsatisfied();
    }
  }
}
