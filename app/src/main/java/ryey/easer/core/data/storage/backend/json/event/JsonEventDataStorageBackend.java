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

package ryey.easer.core.data.storage.backend.json.event;

import android.content.Context;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.storage.backend.EventDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.FileDataStorageBackendHelper;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.json.NC;

public class JsonEventDataStorageBackend
    implements EventDataStorageBackendInterface {

  private final Context context;
  private static File dir;

  public JsonEventDataStorageBackend(final Context context) {
    this.context = context;
    dir = IOUtils.mustGetSubDir(context.getFilesDir(), "event");
  }

  @Override
  public boolean has(final String name) {
    return IOUtils.fileExists(dir, name + NC.SUFFIX);
  }

  @Override
  public List<String> list() {
    ArrayList<String> list = new ArrayList<>();
    for (EventStructure scenario : all()) {
      list.add(scenario.getName());
    }
    return list;
  }

  @Override
  public EventStructure get(final String name)
      throws FileNotFoundException, IllegalStorageDataException {
    File file = new File(dir, name + NC.SUFFIX);
    return get(file);
  }

  private EventStructure get(final File file)
      throws FileNotFoundException, IllegalStorageDataException {
    EventParser parser = new EventParser();
    return FileDataStorageBackendHelper.get(parser, file);
  }

  @Override
  public void write(final EventStructure profile) throws IOException {
    File file = new File(dir, profile.getName() + NC.SUFFIX);
    EventSerializer serializer = new EventSerializer();
    FileDataStorageBackendHelper.write(serializer, file, profile);
  }

  @Override
  public void delete(final String name) {
    File file = new File(dir, name + NC.SUFFIX);
    if (!file.delete())
      throw new IllegalStateException("Unable to delete " + file);
  }

  @Override
  public List<EventStructure> all() {
    List<EventStructure> list = new ArrayList<>();
    File[] files = dir.listFiles(new FileFilter() {
      @Override
      public boolean accept(final File pathname) {
        if (pathname.isFile()) {
          if (pathname.getName().endsWith(NC.SUFFIX)) {
            return true;
          }
        }
        return false;
      }
    });
    for (File file : files) {
      try {
        list.add(get(file));
      } catch (IllegalStorageDataException e) {
        e.printStackTrace();
      } catch (FileNotFoundException e) {
        throw new IllegalStateException(e.getCause());
      }
    }
    return list;
  }
}
