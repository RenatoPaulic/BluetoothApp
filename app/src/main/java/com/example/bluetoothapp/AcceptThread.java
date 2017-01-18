package com.example.bluetoothapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Renato Paulić
 */

public class AcceptThread extends Thread {

    public static final String NAME = "BluetoothConnection" ;
    public static final UUID MY_UUID = UUID.fromString("8bede167-1f15-47a6-8f1f-69522524b4c8");

    private final BluetoothServerSocket mServerSocket;
    private BluetoothAdapter bluetoothAdapter;

    private Activity activity;

    public AcceptThread(Activity activity) {

        this.activity = activity;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        BluetoothServerSocket tmp = null;
        try {
            tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (Exception e) { }
        mServerSocket = tmp;
    }

    public void run() {



        BluetoothSocket socket = null;

        // Listening for connection
        while (true) {
            try {
                socket = mServerSocket.accept();
            } catch (Exception e) {
                break;
            }
            // If a connection was accepted, starting connection
            if (socket != null) {

                ConnectedThread connectedThread = new ConnectedThread(socket, activity);
                connectedThread.start();


            }
        }


    }


    public void cancel() {
        try {
            mServerSocket.close();
        } catch (IOException e) { }
    }

}
