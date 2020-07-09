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

package ryey.easer.skills.operation.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import androidx.annotation.NonNull;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class WifiLoader extends OperationLoader<WifiOperationData> {
  public WifiLoader(final Context context) { super(context); }

  @Override
  public boolean load(final @ValidData @NonNull WifiOperationData data) {
    Boolean state = data.get();
    WifiManager wifiManager =
        (WifiManager)context.getApplicationContext().getSystemService(
            Context.WIFI_SERVICE);
    return wifiManager.setWifiEnabled(state);
  }
}
