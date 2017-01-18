package com.example.bluetoothapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Renato Paulić
 */

public class ConnectThread extends Thread {

    public static final UUID MY_UUID = UUID.fromString("8bede167-1f15-47a6-8f1f-69522524b4c8");

    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;

    private String filename;


    public ConnectThread(BluetoothDevice device , String filename) {

        this.filename = filename;

        BluetoothSocket tmp = null;
        mDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            tmp = mDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) { }
        mSocket = tmp;

    }


    public void run() {

        try {
            // Connect the device through the socket
            mSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect
             try {
               mSocket.close();
           } catch (IOException closeException) { }
           return;
        }


        ConnectedThread connectedThread = new ConnectedThread(mSocket , filename);
        connectedThread.start();
    }


    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) { }
    }

}
