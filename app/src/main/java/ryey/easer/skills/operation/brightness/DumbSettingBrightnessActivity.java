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

package ryey.easer.skills.operation.brightness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

/**
 * Dumb activity to correctly set screen brightness
 * Adapted from
 * https://stackoverflow.com/questions/6708692/changing-the-screen-brightness-system-setting-android
 */
public class DumbSettingBrightnessActivity extends Activity {

  private static final String EXTRA_BRIGHTNESS = "brightness";

  static void applyBrightness(final Context context, final float brightness) {
    Intent intent = new Intent(context, DumbSettingBrightnessActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(EXTRA_BRIGHTNESS, brightness);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Handler handler = new StopHandler(this);
    Intent intent = this.getIntent();
    float brightness = intent.getFloatExtra(EXTRA_BRIGHTNESS, 0);
    WindowManager.LayoutParams params = getWindow().getAttributes();
    params.screenBrightness = brightness;
    getWindow().setAttributes(params);

    Message message = handler.obtainMessage(StopHandler.DELAYED_MESSAGE);
    handler.sendMessageDelayed(message, 1000);
  }

  private static class StopHandler extends Handler {
    static final int DELAYED_MESSAGE = 1;

    Activity activity;
    StopHandler(final Activity activity) { this.activity = activity; }
    @Override
    public void handleMessage(final Message msg) {
      if (msg.what == DELAYED_MESSAGE) {
        activity.finish();
      }
      super.handleMessage(msg);
    }
  }
}
