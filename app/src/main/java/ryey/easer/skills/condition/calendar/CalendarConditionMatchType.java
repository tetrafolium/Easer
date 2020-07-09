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

package ryey.easer.skills.condition.calendar;

import java.util.HashMap;

public enum CalendarConditionMatchType {
    ANY(0),
    EVENT_TITLE(1);

    private int id;

    private static HashMap<Integer, CalendarConditionMatchType> enumById = new HashMap<>();
    static {
        for (CalendarConditionMatchType m: CalendarConditionMatchType.values()) {
            enumById.put(m.getId(), m);
        }
    }

    CalendarConditionMatchType(int id) {
        this.id = id;
    }

    public static CalendarConditionMatchType getById(int id) {
        return enumById.get(id);
    }
    public int getId() {
        return id;
    }
}
