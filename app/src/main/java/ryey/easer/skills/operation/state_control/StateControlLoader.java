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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.annotation.NonNull;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.core.EHService;
import ryey.easer.skills.operation.OperationLoader;

public class StateControlLoader
    extends OperationLoader<StateControlOperationData> {

  private StateControlOperationData data;

  private ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(final ComponentName componentName,
                                   final IBinder iBinder) {
      EHService.EHBinder binder = (EHService.EHBinder)iBinder;
      binder.setLotusStatus(data.scriptName, data.newStatus);
      context.unbindService(this);
    }

    @Override
    public void onServiceDisconnected(final ComponentName componentName) {}
  };

  StateControlLoader(final Context context) { super(context); }

  @Override
  public boolean load(final
                      @ValidData @NonNull StateControlOperationData data) {
    this.data = data;
    Intent intent = new Intent(context, EHService.class);
    context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    return true;
  }
}
