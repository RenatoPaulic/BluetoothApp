package com.example.bluetoothapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Renato Paulić
 */

public class TransmittingActivity extends AppCompatActivity {

    private static final UUID SERVICE_UUID = UUID.fromString("18902a9a-1f4a-44fe-936f-14c8eea41800");
    private static final UUID CHARACTERISTIC_UUID = UUID.fromString("18902a9a-1f4a-44fe-936f-14c8eea41801");

    private BeaconTransmitter beaconTransmitter = null;
    private BluetoothGattServer gattServer = null;

    //List of services that device can offer
    private ArrayList<String> availableServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitting);

        availableServices = new ArrayList<>();

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        // Starting gatt server, classic bluetooth server and beacon transmitter
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    AcceptThread acceptThread = new AcceptThread(TransmittingActivity.this);
                    acceptThread.start();

                    initServer();
                    startTransmitting();


                } else {

                    Toast.makeText(TransmittingActivity.this, "Cant transmit beacon", Toast.LENGTH_LONG).show();

                }

            }
        });

        // Showing current received bytes in text view
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView textView = (TextView) findViewById(R.id.textView);

                textView.setText("Received: " + ConnectedThread.received + " bytes");

            }
        });

        // Adding entered service in available service list and showing it in text view
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = (EditText) findViewById(R.id.editText);

                String[] serviceSet = editText.getText().toString().split(",");

                availableServices.clear();

                for (int i = 0 ; i < serviceSet.length ; i ++){

                    availableServices.add(serviceSet[i]);

                }

                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(availableServices.toString());

            }
        });


    }

    /**
     * Method witch initiates beacon transmitter
     */
    public void startTransmitting(){

        // Creating uuid witch will be transmitted
        // Last 12 characters represent device mac address
        String deviceMacAddress = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
        String uuidMacAddress = deviceMacAddress.substring(0,2) + deviceMacAddress.substring(3,5) + deviceMacAddress.substring(6,8) + deviceMacAddress.substring(9,11)
                + deviceMacAddress.substring(12,14) + deviceMacAddress.substring(15,17);
        String uuid = "2f234454-cf6d-4a0f-adf2-" + uuidMacAddress.toLowerCase() ;

        // Building beacon
        Beacon beacon = new Beacon.Builder()
                .setId1(uuid )
                .setId2("1")                  // max 65535
                .setId3("65535")              // max 65535
                .setManufacturer(0x0118)
                .setTxPower(-70)
                .setDataFields(Arrays.asList(new Long[]{0l}))     // max 255
                .build();


        // Setting beacon layout, transmitter and starting transmitting
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");

        beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        beaconTransmitter.setAdvertiseTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM);
        beaconTransmitter.startAdvertising(beacon);


        if(beaconTransmitter.isStarted())
        Toast.makeText(this , "Transmitting started" , Toast.LENGTH_LONG).show();


    }

    /**
     * Method witch starts Ble Gatt server and adds default service and characteristic that are available for writing and reading
     */
     private void initServer() {

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        gattServer = bluetoothManager.openGattServer(TransmittingActivity.this, gattServerCallback);

        BluetoothGattService service = new BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);

         BluetoothGattCharacteristic offsetCharacteristic =
                new BluetoothGattCharacteristic(CHARACTERISTIC_UUID,
                        //Read+write permissions
                        BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE,
                        BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PERMISSION_WRITE);


        service.addCharacteristic(offsetCharacteristic);
         gattServer.addService(service);

    }

    /**
     * Callback that is called when certain action are made between two devices that are connected
     */
    private BluetoothGattServerCallback gattServerCallback = new BluetoothGattServerCallback() {

        /**
         *Callback that is called when other devise send request for certian service
         */
        @Override
        public void onCharacteristicWriteRequest(final BluetoothDevice device,
                                                 int requestId,
                                                 BluetoothGattCharacteristic characteristic,
                                                 boolean preparedWrite,
                                                 boolean responseNeeded,
                                                 int offset,
                                                 byte[] value) {

            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);


            final String stringValue = new String(value);

            TransmittingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(TransmittingActivity.this , " Received request: " + stringValue + " from device: " +
                            device.getAddress().toString() , Toast.LENGTH_LONG).show();
                }
            });


            // Checking if device have requested, if it have sending success respond, else sending failure respond
            if(availableServices.contains(stringValue)) {

                gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null);

            }else{

                gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_FAILURE, 0, null);

            }



        }


    };


    public void onDestroy(){

        super.onDestroy();

        if(beaconTransmitter != null && beaconTransmitter.isStarted()){

            beaconTransmitter.stopAdvertising();

        }

        if(gattServer != null ){

            gattServer.close();

        }

    }

}
