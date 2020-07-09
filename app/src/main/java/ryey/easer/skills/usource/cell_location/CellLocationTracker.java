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

package ryey.easer.skills.usource.cell_location;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import androidx.annotation.NonNull;
import ryey.easer.skills.condition.SkeletonTracker;

public class CellLocationTracker
    extends SkeletonTracker<CellLocationUSourceData> {

  private static TelephonyManager telephonyManager = null;

  private CellLocationListener cellLocationListener =
      new CellLocationListener();

  CellLocationTracker(final Context context, final CellLocationUSourceData data,
                      final @NonNull PendingIntent event_positive,
                      final @NonNull PendingIntent event_negative) {
    super(context, data, event_positive, event_negative);

    if (telephonyManager == null) {
      telephonyManager =
          (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    }
  }

  @Override
  public void start() {
    telephonyManager.listen(cellLocationListener,
                            PhoneStateListener.LISTEN_CELL_LOCATION);
  }

  @Override
  public void stop() {
    telephonyManager.listen(cellLocationListener,
                            PhoneStateListener.LISTEN_NONE);
  }

  @SuppressLint("MissingPermission")
  @Override
  public Boolean state() {
    return match(telephonyManager.getCellLocation());
  }

  private Boolean match(final CellLocation location) {
    CellLocationSingleData curr =
        CellLocationSingleData.fromCellLocation(location);
    if (curr == null)
      return null;
    return data.data.contains(curr);
  }

  class CellLocationListener extends PhoneStateListener {
    @Override
    synchronized public void
    onCellLocationChanged(final CellLocation location) {
      super.onCellLocationChanged(location);
      newSatisfiedState(match(location));
    }
  }
}
