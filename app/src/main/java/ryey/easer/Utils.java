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

package ryey.easer;

import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ryey.easer.commons.local_skill.dynamics.SolidDynamicsAssignment;

public class Utils {

    private Utils() { }

    public static void panic(final String message, final Object... objs) {
        Logger.e(message, objs);
        throw new IllegalStateException(String.format(message, objs));
    }

    public static boolean isBlank(final @Nullable String str) {
        if (str == null)
            return true;
        if (str.isEmpty())
            return true;
        if (str.trim().isEmpty())
            return true;
        return false;
    }

    public static boolean nullableEqual(final @Nullable Object obj1, final @Nullable Object obj2) {
        if (obj1 == null && obj2 == null)
            return true;
        if (obj1 == null || obj2 == null)
            return false;
        return obj1.equals(obj2);
    }

    public static Set<Integer> str2set(final String text) throws ParseException {
        Set<Integer> days = new HashSet<>();
        for (String str : text.split("\n")) {
            if (isBlank(str))
                continue;
            days.add(Integer.parseInt(str));
        }
        return days;
    }

    public static String set2str(final Set<Integer> days) {
        StringBuilder str = new StringBuilder();
        for (int day : days) {
            str.append(String.format(Locale.US, "%d\n", day));
        }
        return str.toString();
    }

    public static <T> List<String> set2strlist(final Set<T> set) {
        List<String> list = new ArrayList<>(set.size());
        for (T num : set) {
            list.add(num.toString());
        }
        return list;
    }

    @NonNull
    public static String StringCollectionToString(final @Nullable Collection<String> collection, final boolean trailing) {
        if (collection == null)
            return "";
        StringBuilder text = new StringBuilder();
        boolean is_first = true;
        for (String line : collection) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                if (!is_first)
                    text.append("\n");
                text.append(trimmed);
                is_first = false;
            }
        }
        if (!is_first && trailing)
            text.append("\n");
        return text.toString();
    }

    public static List<String> stringToStringList(final String text) {
        List<String> list = new ArrayList<>();
        for (String str : text.split("\n")) {
            String trimmed = str.trim();
            if (!trimmed.isEmpty())
                list.add(trimmed);
        }
        return list;
    }

    public static int checkedIndexFirst(final CompoundButton[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isChecked())
                return i;
        }
        throw new IllegalStateException("At least one button should be checked");
    }

    public static final DateFormat df_24hour = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    public static final DateFormat df_12hour = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a", Locale.US);

    private static final String regex_placeholder = "<<[^ ]+>>";
    private static final Pattern pattern_placeholder = Pattern.compile(regex_placeholder);

    @NonNull
    public static Set<String> extractPlaceholder(final @NonNull String str) {
        Set<String> set = new ArraySet<>();
        Matcher matcher = pattern_placeholder.matcher(str);
        while (matcher.find()) {
            String placeholder = matcher.group();
            set.add(placeholder);
        }
        return set;
    }

    @NonNull
    public static String applyDynamics(final @NonNull String str, final @NonNull SolidDynamicsAssignment dynamicsAssignment) {
        Set<String> placeholders = extractPlaceholder(str);
        for (String placeholder : placeholders) {
            String property = dynamicsAssignment.getAssignment(placeholder);
            str = str.replaceAll(placeholder, property);
        }
        return str;
    }
}
