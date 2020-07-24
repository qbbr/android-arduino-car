package io.qbbr.arduinocar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class G {
    public static final String LOG_TAG = "arduinocar";
    public static final String MY_UUID = "00001101-0000-1000-8000-00805f9b34fb";
    public static BluetoothAdapter bluetoothAdapter;
    public static BluetoothDevice bluetoothDevice;
    public static ConnectThread connectThread;

    public static boolean connect(Device device) {
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(device.getAddress());
        connectThread = new ConnectThread(bluetoothDevice);

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        return connectThread.isConnected();
    }
}
