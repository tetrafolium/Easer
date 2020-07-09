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

package ryey.easer.skills.operation.network_transmission;

import androidx.annotation.NonNull;
import ryey.easer.commons.local_skill.IllegalStorageDataException;
import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.commons.local_skill.operationskill.OperationDataFactory;
import ryey.easer.plugin.PluginDataFormat;

class NetworkTransmissionOperationDataFactory
    implements OperationDataFactory<NetworkTransmissionOperationData> {
  @NonNull
  @Override
  public Class<NetworkTransmissionOperationData> dataClass() {
    return NetworkTransmissionOperationData.class;
  }

  @ValidData
  @NonNull
  @Override
  public NetworkTransmissionOperationData dummyData() {
    NetworkTransmissionOperationData.Protocol protocol =
        NetworkTransmissionOperationData.Protocol.tcp;
    int remote_port = 146;
    String remote_address = "192.168.0.143";
    String data = "aaaData";
    return new NetworkTransmissionOperationData(protocol, remote_address,
                                                remote_port, data);
  }

  @ValidData
  @NonNull
  @Override
  public NetworkTransmissionOperationData
  parse(final @NonNull String data, final @NonNull PluginDataFormat format,
        final int version) throws IllegalStorageDataException {
    return new NetworkTransmissionOperationData(data, format, version);
  }
}
