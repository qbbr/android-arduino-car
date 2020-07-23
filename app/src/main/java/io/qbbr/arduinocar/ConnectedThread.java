package io.qbbr.arduinocar;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    public final static int RECIEVE_MESSAGE = 1;
    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Handler handler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        this.socket = socket;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        this.handler = handler;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        super.run();

        byte[] buffer = new byte[256];
        int bytes;
        while (true) {
            try {
                bytes = inputStream.read(buffer);
                handler.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }

    public void write(char data) {
        Log.d(MainActivity.LOG_TAG, "write data: " + data);

        try {
            outputStream.write(data);
        } catch (IOException e) {
            Log.d(MainActivity.LOG_TAG, "write error: " + e.getMessage());
        }
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
