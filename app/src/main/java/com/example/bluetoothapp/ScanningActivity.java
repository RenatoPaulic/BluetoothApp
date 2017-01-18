package com.example.bluetoothapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by Renato Paulić
 */

public class ScanningActivity extends AppCompatActivity implements BeaconConsumer {


    private final static int PERMISSION = 1;
    private final static int FILE_FOUND = 1 ;

    private static final UUID SERVICE_UUID = UUID.fromString("18902a9a-1f4a-44fe-936f-14c8eea41800");
    private static final UUID CHARACTERISTIC_UUID = UUID.fromString("18902a9a-1f4a-44fe-936f-14c8eea41801");

    private String filename = null;

    private BeaconManager beaconManager = null;
    private BluetoothGatt bluetoothGatt = null;

    // List of scanned beacons
    private ArrayList<Beacon> beaconList;
    // List of services that device need and ask for
    private ArrayList<String> requestServices;
    // List of other devices that are asked for service
    private HashSet<BluetoothDevice> requestedServiceDeviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        beaconList = new ArrayList<>();
        requestServices = new ArrayList<>();
        requestedServiceDeviceList = new HashSet<>();

        // If device is running Android 6.0 + this permissions are required and must be enabled for scanning and sending files
        permissionRequest();

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        // Starting beacon scan
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                beaconScanInit();

            }
        });

        // Showing file size of current picked file (that should be send) in text view
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(filename != null ) {

                    File file = new File(filename);

                    TextView textView = (TextView) findViewById(R.id.textView);
                    textView.setText("Sending: " + file.length() + " bytes");

                }
            }
        });

        // Adding entered service in requestService list and showing it in text view
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = (EditText) findViewById(R.id.editText);

                String service = editText.getText().toString();

                requestServices.clear();
                requestServices.add(service);

                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(requestServices.toString());

            }
        });


    }

    /**
     * Method witch initiates beacon scanner
     */
    public void beaconScanInit(){

        beaconManager = BeaconManager.getInstanceForApplication(ScanningActivity.this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(ScanningActivity.this);

    }

    /**
     * Callback method that is periodically called when scan start
     */
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                // Creating new list for all beacons that have been scanned
                beaconList = new ArrayList<>();
                beaconList.addAll(beacons);

                // Sorting beacons by uuid
                Collections.sort(beaconList, new Comparator<Beacon>() {
                    @Override
                    public int compare(Beacon beacon, Beacon t1) {

                        return beacon.getId1().compareTo(t1.getId1());
                    }
                });


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Updating list adapter
                        ListView listView = (ListView) findViewById(R.id.listView);
                        ListAdapter listAdapter = new ListAdapter(ScanningActivity.this, R.layout.list_layout, beaconList);
                        listView.setAdapter(listAdapter);

                    }
                });


            }
        });


        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myBeacon", null, null, null));
        } catch (RemoteException e) {
        }
    }

    /**
     * Inner class witch update GUI with new list of scanned beacons every time it is called
     */
    private class ListAdapter extends ArrayAdapter<Beacon> {

        int resource;
        Context context;


        private ListAdapter(Context context, int resource, List<Beacon> object) {

            super(context, resource, object);

            this.resource = resource;
            this.context = context;

        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(resource, parent, false);

            TextView textView1 = (TextView) view.findViewById(R.id.list_text1);
            TextView textView2 = (TextView) view.findViewById(R.id.list_text2);
            TextView textView3 = (TextView) view.findViewById(R.id.list_text3);
            TextView textView4 = (TextView) view.findViewById(R.id.list_text4);
            TextView textView5 = (TextView) view.findViewById(R.id.list_text5);
            TextView textView6 = (TextView) view.findViewById(R.id.list_text6);
            TextView textView7 = (TextView) view.findViewById(R.id.list_text7);
            TextView textView8 = (TextView) view.findViewById(R.id.list_text8);

            //Setting current beacon parameters in view
            textView1.setText("Distance: " + getItem(position).getDistance());
            textView2.setText("Rssi: " + getItem(position).getRssi());
            textView3.setText("UUID: " + getItem(position).getId1().toString());
            textView4.setText("Major: " + getItem(position).getId2().toString());
            textView5.setText("Minor: " + getItem(position).getId3().toString());
            textView6.setText("Name: " + getItem(position).getBluetoothName());
            textView7.setText("Address: " + getItem(position).getBluetoothAddress());
            textView8.setText("DataFields: " + getItem(position).getDataFields());


            Button button = (Button) view.findViewById(R.id.list_button);

            // Sending request for required service to device witch transmitted current beacon
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Getting mac address of device that transmitted current beacon
                    // Last 12 characters of uuid represent mac address of device
                    String deviceMacAddress = getItem(position).getId1().toString().substring(24,36).toUpperCase();
                    String macAddress = deviceMacAddress.substring(0, 2) + ":" + deviceMacAddress.substring(2, 4) + ":" + deviceMacAddress.substring(4, 6) + ":"
                            + deviceMacAddress.substring(6, 8) + ":" + deviceMacAddress.substring(8, 10) + ":" + deviceMacAddress.substring(10, 12);

                    // Creating BluetoothDevice object of device that transmitted current beacon
                    BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);

                    requestedServiceDeviceList.add(device);

                    // Creating BLE connection with device witch beacon has been clicked
                    bluetoothGatt = device.connectGatt(ScanningActivity.this,false,bleGattCallback);


                }
            });


            return view;

        }
    }

    /**
     * Callback that is called when certain action are made between two devices that are connected
     */
    private final BluetoothGattCallback bleGattCallback = new BluetoothGattCallback() {



        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {


            if (newState == BluetoothProfile.STATE_CONNECTED) {

                // If devices are connected then it can start service discovery
                bluetoothGatt.discoverServices();

            }

        }

        /**
         * This method is called as a callback when service are discovered
         */
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {


            if(status == BluetoothGatt.GATT_SUCCESS){

                List<BluetoothGattService> services = gatt.getServices();

                for (BluetoothGattService service : services){

                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();

                    for (BluetoothGattCharacteristic characteristic : characteristics){

                        // Default characteristic witch serves for asking(writing and reading) for service
                        if(service.getUuid().equals(SERVICE_UUID)&&
                                characteristic.getUuid().equals(CHARACTERISTIC_UUID)){

                            // Asking for service by writing in default characteristic
                            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                            characteristic.setValue(requestServices.get(0).getBytes());
                            gatt.writeCharacteristic(characteristic);

                        }

                    }


                }


            }


        }

        /**
         * This method is called as a callback on write request from device that needs certain service
         */
        @Override
        public void onCharacteristicWrite(final BluetoothGatt gatt , BluetoothGattCharacteristic characteristic , int status) {


            //if asked device have required service GATT_SUCCESS is returned, if not GATT_FAILURE
            if (status == BluetoothGatt.GATT_SUCCESS) {


                for (BluetoothDevice device : requestedServiceDeviceList) {

                    // Finding device witch asked for service
                    if (device.equals(gatt.getDevice())) {

                        ScanningActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(ScanningActivity.this, " Device: " + gatt.getDevice().getName() + " " + gatt.getDevice().getAddress()
                                        + " OK ", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // If asked service is 1 then data transfer is started by method pickingFile()
                        if (requestServices.contains("1")) {


                            ScanningActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ScanningActivity.this, "Select image file from gallery" , Toast.LENGTH_SHORT).show();
                                }
                            });


                            pickingFile();

                            // Waiting until image file is picked(if filename != null) and then connecting to device and start file transport
                            while(filename == null){

                            }

                            ConnectThread connectThread = new ConnectThread(device , filename);
                            connectThread.start();

                            filename = null;

                        }
                    }


                }
            }else {


                ScanningActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ScanningActivity.this, " Device: " + gatt.getDevice().getName() + " " + gatt.getDevice().getAddress()
                                + " FAIL ", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }

    };


    public void permissionRequest(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION);

        }

    }

    /**
     * Callback method witch is called on permissionRequest() method
     * Method check is asked permissions are granted and write Toast massage about result
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Scan can start", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(this, "Cant scan without location permission enabled", Toast.LENGTH_LONG).show();

                }

                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Reading external storage enabled", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(this, "Cant read and send storage files without read external storage permission", Toast.LENGTH_LONG).show();

                }

                return;
            }
        }
    }
    /**
     * Method witch is called when is required to send file
     * Start intent for picking file from mobile eternal storage
     */
    public void pickingFile(){


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");

        startActivityForResult(intent,FILE_FOUND);


    }
    /**
    * Callback method witch is called when file is picked
    */
    @Override
    protected void onActivityResult (final int requestCode , final int resultCode , Intent data) {


        if(requestCode == FILE_FOUND && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            filename = getRealPathFromURI(selectedImageUri);

            File file = new File(filename);

            TextView text = (TextView) findViewById(R.id.textView);
            text.setText("Sending: " + file.length() + " bytes");


        }


    }
    /**
     * Method witch generates real image path from uri
    */
    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    @Override
    public void onDestroy(){

        super.onDestroy();

        if(beaconManager != null && beaconManager.isBound(ScanningActivity.this)){

            beaconManager.unbind(ScanningActivity.this);

        }

        if(bluetoothGatt != null){

            bluetoothGatt.close();

        }

    }

}
