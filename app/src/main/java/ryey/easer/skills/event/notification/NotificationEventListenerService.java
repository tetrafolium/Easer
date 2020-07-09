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

package ryey.easer.skills.event.notification;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NotificationEventListenerService
	extends NotificationListenerService {
private static final String ACTION_LISTEN =
	"ryey.easer.skills.event.notification.action.LISTEN";
private static final String ACTION_CANCEL =
	"ryey.easer.skills.event.notification.action.CANCEL";
private static final String EXTRA_DATA =
	"ryey.easer.skills.event.notification.extra.DATA";
private static final String EXTRA_URI =
	"ryey.easer.skills.event.notification.extra.URI";

List<CompoundData> dataList = new ArrayList<>();

BroadcastReceiver mReceiver = new BroadcastReceiver() {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		final String action = intent.getAction();
		if (action == null)
			return;
		if (!(action.equals(ACTION_LISTEN) || action.equals(ACTION_CANCEL)))
			return;
		Logger.d("broadcast received");
		NotificationEventData eventData = intent.getParcelableExtra(EXTRA_DATA);
		Uri uri = intent.getParcelableExtra(EXTRA_URI);
		if (action.equals(ACTION_LISTEN)) {
			addListenToNotification(eventData, uri);
		} else if (action.equals(ACTION_CANCEL)) {
			delListenToNotification(eventData, uri);
		}
	}
};

static void listen(final Context context,
                   final NotificationEventData eventData, final Uri uri) {
	Intent intent = new Intent(ACTION_LISTEN);
	intent.putExtra(EXTRA_DATA, eventData);
	intent.putExtra(EXTRA_URI, uri);
	Logger.d("informing 'listen'");
	LocalBroadcastManager.getInstance(context.getApplicationContext())
	.sendBroadcast(intent);
}

static void cancel(final Context context,
                   final NotificationEventData eventData, final Uri uri) {
	Intent intent = new Intent(ACTION_CANCEL);
	intent.putExtra(EXTRA_DATA, eventData);
	intent.putExtra(EXTRA_URI, uri);
	Logger.d("informing 'listen'");
	LocalBroadcastManager.getInstance(context.getApplicationContext())
	.sendBroadcast(intent);
}

private static boolean match(final StatusBarNotification sbn,
                             final String t_app, final String t_title,
                             final String t_content) {
	Logger.d("app: <%s> <%s>", t_app, sbn.getPackageName());
	if ((t_app != null) && (!t_app.equals(sbn.getPackageName())))
		return false;
	Bundle extras = sbn.getNotification().extras;
	String title = extras.getString(Notification.EXTRA_TITLE);
	String contentText = extras.getString(Notification.EXTRA_TEXT);
	if ((t_title != null) && (title == null || !title.contains(t_title)))
		return false;
	if ((t_content != null) && (contentText == null || !contentText.contains(t_content)))
		return false;
	return true;
}

@Override
public void onNotificationPosted(final StatusBarNotification sbn) {
	for (CompoundData compoundData : dataList) {
		NotificationEventData eventData = compoundData.notificationEventData;
		Intent intent =
			match(sbn, eventData.app, eventData.title, eventData.content)
	      ? NotificationSlot.NotifyIntentPrototype.obtainPositiveIntent(
				compoundData.uri)
	      : NotificationSlot.NotifyIntentPrototype.obtainNegativeIntent(
				compoundData.uri);
		intent.putExtra(NotificationEventData.AppDynamics.id,
		                sbn.getPackageName());
		Bundle extras = sbn.getNotification().extras;
		String title = extras.getString(Notification.EXTRA_TITLE);
		String contentText = extras.getString(Notification.EXTRA_TEXT);
		intent.putExtra(NotificationEventData.TitleDynamics.id, title);
		intent.putExtra(NotificationEventData.ContentDynamics.id, contentText);
		sendBroadcast(intent);
	}
	super.onNotificationPosted(sbn);
}

@Override
public void onNotificationRemoved(final StatusBarNotification sbn) {
	super.onNotificationRemoved(sbn);
}

@Override
public void onCreate() {
	Logger.i("NotificationEventListenerService onCreate()");
	super.onCreate();
	IntentFilter filter = new IntentFilter();
	filter.addAction(ACTION_LISTEN);
	filter.addAction(ACTION_CANCEL);
	LocalBroadcastManager.getInstance(getApplicationContext())
	.registerReceiver(mReceiver, filter);
}

@Override
public void onDestroy() {
	Logger.i("NotificationEventListenerService onDestroy()");
	super.onDestroy();
	LocalBroadcastManager.getInstance(getApplicationContext())
	.unregisterReceiver(mReceiver);
	if (dataList.size() > 0) {
		Logger.w("Listener to notifications not cleaned up completely: %d left",
		         dataList.size());
	}
}

private void
addListenToNotification(final NotificationEventData notificationEventData,
                        final Uri uri) {
	dataList.add(new CompoundData(notificationEventData, uri));
}

private void
delListenToNotification(final NotificationEventData notificationEventData,
                        final Uri uri) {
	CompoundData compoundData = new CompoundData(notificationEventData, uri);
	dataList.remove(compoundData);
}

private static class CompoundData {
final NotificationEventData notificationEventData;
final Uri uri;
private CompoundData(final NotificationEventData notificationEventData,
                     final Uri uri) {
	this.notificationEventData = notificationEventData;
	this.uri = uri;
}

@Override
public boolean equals(final Object obj) {
	if (obj == null)
		return false;
	if (!(obj instanceof CompoundData))
		return false;
	return uri.equals(((CompoundData)obj).uri);
}
}
}
