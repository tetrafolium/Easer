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

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import ryey.easer.commons.local_skill.ValidData;
import ryey.easer.skills.operation.OperationLoader;

public class NetworkTransmissionLoader extends OperationLoader<NetworkTransmissionOperationData> {
    public NetworkTransmissionLoader(final Context context) {
        super(context);
    }

    @Override
    public boolean load(final @ValidData @NonNull NetworkTransmissionOperationData data) {
        //TODO: Async and correctly report
        NetworkTask task = new NetworkTask();
        task.execute(data);
        return true;
    }

    private static class NetworkTask extends AsyncTask<NetworkTransmissionOperationData, Void, Boolean> {

        @Override
        protected Boolean doInBackground(final NetworkTransmissionOperationData... networkTransmissionOperationData) {
            NetworkTransmissionOperationData data = networkTransmissionOperationData[0];
            try {
                InetAddress remote_address = InetAddress.getByName(data.remote_address);
                switch (data.protocol) {
                case tcp:
                    try {
                        Socket socket = new Socket(remote_address, data.remote_port);
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            try {
                                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                                try {
                                    dataOutputStream.writeBytes(data.data);
                                } finally {
                                    dataOutputStream.flush();
                                    dataOutputStream.close();
                                }
                            } finally {
                                outputStream.flush();
                                outputStream.close();
                            }
                        } finally {
                            socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                    break;
                case udp:
                    DatagramPacket datagramPacket = new DatagramPacket(data.data.getBytes(), data.data.length(), remote_address, data.remote_port);
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        socket.send(datagramPacket);
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                    break;
                default:
                    throw new IllegalAccessError("data should be valid when calling this method");
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
