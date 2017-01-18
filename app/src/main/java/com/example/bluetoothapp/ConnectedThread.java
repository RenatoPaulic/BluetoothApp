package com.example.bluetoothapp;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Renato Paulić
 */

public class ConnectedThread extends Thread {

    private final BluetoothSocket mSocket;
    private final InputStream mInputStream;
    private final OutputStream mOutputStream;

    protected static int fileSizeBytesRead;
    protected static int received;
    private String filename = null;
    private Activity activity = null;

    public final static int MAX_FILE_SIZE_BYTES = 10;

    public ConnectedThread(BluetoothSocket socket) {

        received = 0;

        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams
        try {
            tmpIn = mSocket.getInputStream();
            tmpOut = mSocket.getOutputStream();
        } catch (IOException e) { }

        mInputStream = tmpIn;
        mOutputStream = tmpOut;
    }

    public ConnectedThread(BluetoothSocket socket, String filename){

        this(socket);
        this.filename = filename;

    }

    public ConnectedThread(BluetoothSocket socket, Activity activity){

        this(socket);
        this.activity = activity;

    }

    public void run() {

        // If filename != null then device witch ask for service called this class, and then it should write in stream
        // If filename == null then device witch was asked for service called this class, and it should read from stream
        if(filename != null) {
            write();
        }
          else{

           read();
        }
    }



    public void write(){


        File file = new File(filename);

        byte[] buffer = new byte[(int)file.length()];

        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

        }catch (Exception e){
            e.printStackTrace();
        }

        BufferedInputStream bis = new BufferedInputStream(fis);


        try {
            bis.read(buffer, 0, buffer.length);

        }catch (Exception e){
            e.printStackTrace();
        }

        // Byte array that contains file size in bytes
        byte[] fileSizeBytes = Integer.valueOf((int)file.length()).toString().getBytes();
        // Empty byte array
        byte[] nullArrayBytes = new byte[MAX_FILE_SIZE_BYTES-fileSizeBytes.length];

        try {
            // Writing size of file that is sending (in bytes)
            mOutputStream.write(fileSizeBytes,0,fileSizeBytes.length);
            // Writing zeros to fill space of MAX_FILE_SIZE_BYTES bytes that are reserved for information about file size
            mOutputStream.write(nullArrayBytes,0,nullArrayBytes.length);
            // Writing image file bytes
            mOutputStream.write(buffer, 0, buffer.length);

            mOutputStream.flush();

        }catch (Exception e){
           e.printStackTrace();
        }


    }




    public void read(){

        int bytesRead;
        int current;

        byte[] buffer2 = new byte[MAX_FILE_SIZE_BYTES];


        try{
            // Reading first MAX_FILE_SIZE_BYTES bytes that contains information about receiving file size
            bytesRead = mInputStream.read(buffer2,0,MAX_FILE_SIZE_BYTES);

            fileSizeBytesRead = bytesRead;

        }catch (Exception e){}

        // Getting size of received file from read bytes
        Integer integer = Integer.parseInt(new String(buffer2).trim().replaceAll("\\?", " "));
        int fileSize = integer.intValue();



        byte[] buffer = new byte[fileSize + MAX_FILE_SIZE_BYTES - fileSizeBytesRead];


        // Reading receiving file
        try {
            bytesRead = mInputStream.read(buffer, 0, buffer.length);

            current = bytesRead;

            do {

                bytesRead = mInputStream.read(buffer, current, buffer.length - current);

                if (bytesRead >= 0)
                    current += bytesRead;

                if( current < fileSize) {
                    received = current;
                }else {
                    received = fileSize;
                }



            }while (current < fileSize + MAX_FILE_SIZE_BYTES - fileSizeBytesRead);

        } catch (IOException e) {
            e.printStackTrace();

        }


        try {
            mInputStream.close();
        }catch (Exception r ){}




        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Received: " + received , Toast.LENGTH_LONG).show();
            }
        });

    }

}
