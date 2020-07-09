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

package ryey.easer.core.data.storage.backend.json.script;

import android.content.Context;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.core.data.ScriptStructure;
import ryey.easer.core.data.storage.backend.FileDataStorageBackendHelper;
import ryey.easer.core.data.storage.backend.IOUtils;
import ryey.easer.core.data.storage.backend.ScriptDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.NC;

public class JsonScriptDataStorageBackend
    implements ScriptDataStorageBackendInterface {

  private final Context context;
  private static File dir;

  public JsonScriptDataStorageBackend(final Context context) {
    this.context = context;
    dir = IOUtils.mustGetSubDir(context.getFilesDir(), "script");
  }

  @Override
  public boolean has(final String name) {
    return IOUtils.fileExists(dir, name + NC.SUFFIX);
  }

  @Override
  public List<String> list() {
    ArrayList<String> list = new ArrayList<>();
    for (ScriptStructure event : all()) {
      list.add(event.getName());
    }
    return list;
  }

  @Override
  public ScriptStructure get(final String name)
      throws FileNotFoundException, IllegalStorageDataException {
    File file = new File(dir, name + NC.SUFFIX);
    return get(file);
  }

  private ScriptStructure get(final File file)
      throws FileNotFoundException, IllegalStorageDataException {
    ScriptParser parser = new ScriptParser(context);
    return FileDataStorageBackendHelper.get(parser, file);
  }

  @Override
  public void write(final ScriptStructure event) throws IOException {
    File file = new File(dir, event.getName() + NC.SUFFIX);
    ScriptSerializer serializer = new ScriptSerializer();
    FileDataStorageBackendHelper.write(serializer, file, event);
  }

  @Override
  public void delete(final String name) {
    File file = new File(dir, name + NC.SUFFIX);
    if (!file.delete())
      throw new IllegalStateException("Unable to delete file " + file);
  }

  @Override
  public void update(final ScriptStructure event) throws IOException {
    delete(event.getName());
    write(event);
  }

  @Override
  public List<ScriptStructure> all() {
    List<ScriptStructure> list = new ArrayList<>();
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
      ScriptStructure event;
      try {
        event = get(file);
        list.add(event);
      } catch (IllegalStorageDataException e) {
        e.printStackTrace();
      } catch (FileNotFoundException e) {
        throw new IllegalStateException(e.getCause());
      }
    }
    return list;
  }
}
