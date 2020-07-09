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

package ryey.easer.skills.operation.cellular;

import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.SkillUtils;
import ryey.easer.skills.operation.OperationLoader;

public class CellularLoader extends OperationLoader<CellularOperationData> {
    public CellularLoader(final Context context) {
        super(context);
    }

    @Override
    public boolean load(final @ValidData @NonNull CellularOperationData data) {
        Boolean state = data.get();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (state == (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)) {
            return true;
        } else {
            if (SkillUtils.useRootFeature(context)) {
                try {
                    String command = "svc data " + (state ? "enable" : "disable");
                    SkillUtils.executeCommandAsRoot(context, command);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                try {
                    Class telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
                    Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
                    getITelephonyMethod.setAccessible(true);
                    Object ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
                    Class ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());
                    Method dataConnSwitchMethod;
                    if (state) {
                        dataConnSwitchMethod = ITelephonyClass.getDeclaredMethod("enableDataConnectivity");
                    } else {
                        dataConnSwitchMethod = ITelephonyClass.getDeclaredMethod("disableDataConnectivity");
                    }
                    dataConnSwitchMethod.setAccessible(true);
                    dataConnSwitchMethod.invoke(ITelephonyStub);
                    return true;
                } catch (ClassNotFoundException e) {
                    Logger.e(e, null);
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    Logger.e(e, null);
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    Logger.e(e, null);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Logger.e(e, null);
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
