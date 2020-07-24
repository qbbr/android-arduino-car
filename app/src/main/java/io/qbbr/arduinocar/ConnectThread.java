package io.qbbr.arduinocar;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ConnectThread extends Thread {
    public final static int RECEIVE_MESSAGE = 1;

    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private Handler handler;

    public ConnectThread(BluetoothDevice bluetoothDevice) {
        BluetoothSocket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            socket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(G.MY_UUID));
            Log.d(G.LOG_TAG, "Socket created");
        } catch (IOException e) {
            Log.d(G.LOG_TAG, "Could'n create an RFCOMM Socket: " + e.toString());
        }

        this.socket = socket;

        try {
            connect();
        } catch (IOException e) {
            Log.d(G.LOG_TAG, "Socket could'n connect: " + e.toString());
            Log.d(G.LOG_TAG, "Try again...");

            try {
                connect();
            } catch (IOException ex) {
                Log.d(G.LOG_TAG, "Socket could'n connect: " + ex.toString());
            }
        }

        if (socket.isConnected()) {
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                Log.d(G.LOG_TAG, "Socket IO Streams opened");
            } catch (IOException e) {
                Log.d(G.LOG_TAG, "Could'n open Socket IO streams: " + e.toString());
            }
        }

        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    private void connect() throws IOException {
        Log.d(G.LOG_TAG, "Socket connecting...");
        socket.connect();
        Log.d(G.LOG_TAG, "Socket connected");
    }

    public boolean isConnected() {
        if (socket == null) {
            return false;
        }

        return socket.isConnected();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        byte[] buffer = new byte[256];
        int bytes;
        while (true) {
            try {
                bytes = inputStream.read(buffer);
                handler.obtainMessage(RECEIVE_MESSAGE, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }

    public boolean write(char data) {
        Log.d(G.LOG_TAG, "write data: " + data);

        try {
            outputStream.write(data);

            return true;
        } catch (IOException e) {
            if (!this.isAlive()) {
                this.cancel();
            }

            Log.d(G.LOG_TAG, "write error: " + e.getMessage());
        }

        return false;
    }

    public void cancel() {
        try {
            Log.d(G.LOG_TAG, "Socket closing...");
            socket.close();
            Log.d(G.LOG_TAG, "Socket closed");
        } catch (IOException e) {
            Log.d(G.LOG_TAG, "Could'n close Socket: " + e.getMessage());
        }
    }
}
