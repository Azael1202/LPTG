package com.example.azael.ltpg;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class App extends AppCompatActivity {

    private static final String TAG = "PIC";
    TextView prueba;
    ImageButton sol_ma, re_me, re_ma, mi_me, mi_ma, la_me, la_ma, do_ma, do_7;
    MediaPlayer solma, reme, rema, mime, mima, lame, lama, doma, do7;


    Handler bluetoothIn;
    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;


    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        //on = (Button) findViewById(R.id.button);
        sol_ma= (ImageButton) findViewById(R.id.solma);
        re_me= (ImageButton) findViewById(R.id.re_menor);
        re_ma= (ImageButton) findViewById(R.id.re_mayor);
        mi_me= (ImageButton) findViewById(R.id.mi_menor);
        mi_ma= (ImageButton) findViewById(R.id.mi_mayor);
        la_me= (ImageButton) findViewById(R.id.la_menor);
        la_ma= (ImageButton) findViewById(R.id.la_mayor);
        do_ma= (ImageButton) findViewById(R.id.do_mayor);
        do_7= (ImageButton) findViewById(R.id.do_7);

        solma = MediaPlayer.create(this, R.raw.solma);
        reme = MediaPlayer.create(this, R.raw.reme);
        rema = MediaPlayer.create(this, R.raw.rema);
        mime = MediaPlayer.create(this, R.raw.mime);
        mima = MediaPlayer.create(this, R.raw.mima);
        lame = MediaPlayer.create(this, R.raw.lame);
        lama = MediaPlayer.create(this, R.raw.lama);
        do7 = MediaPlayer.create(this, R.raw.do7);
        doma = MediaPlayer.create(this, R.raw.doma);


        if (getIntent().hasExtra("address")) {
            address = getIntent().getExtras().getString("address");
        }
        if (address.equals("")) {
            goToDeviceList();
        }

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                          // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                              //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(1, endOfLineIndex);    // extract string
                        prueba.setText(dataInPrint);

                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        sol_ma.setOnClickListener(nota);
        re_me.setOnClickListener(nota);
        re_ma.setOnClickListener(nota);
        mi_me.setOnClickListener(nota);
        mi_ma.setOnClickListener(nota);
        la_me.setOnClickListener(nota);
        la_ma.setOnClickListener(nota);
        do_ma.setOnClickListener(nota);
        do_7.setOnClickListener(nota);


        /**sol_ma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //do_Mayor.start();
                mConnectedThread.write("a");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();

            }
        });**/

    }

    View.OnClickListener nota = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == sol_ma) {
                mConnectedThread.write("a");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Nota Sol_Mayor", Toast.LENGTH_SHORT).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                solma.start();


            }
            else if (v == re_me)
            {
                mConnectedThread.write("b");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Nota Re Menor", Toast.LENGTH_SHORT).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                reme.start();

            }
            else if (v == re_ma)
            {
                mConnectedThread.write("c");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Nota Re Mayor", Toast.LENGTH_SHORT).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                rema.start();

            }
            else if (v == mi_me)
            {
                mConnectedThread.write("d");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Nota Mi Menor", Toast.LENGTH_SHORT).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                mime.start();

            }
            else if (v == mi_ma)
            {
                mConnectedThread.write("e");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Nota Mi Mayor", Toast.LENGTH_SHORT).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                mima.start();
            }
            else if (v == la_me)
            {
                mConnectedThread.write("f");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Nota La Menor", Toast.LENGTH_SHORT).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                lame.start();
            }
            else if (v == la_ma)
            {
                mConnectedThread.write("g");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Nota La Mayor", Toast.LENGTH_SHORT).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                lama.start();
            }
            else if (v == do_ma)
            {
                mConnectedThread.write("h");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Nota Do Mayor", Toast.LENGTH_SHORT).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                doma.start();

            }
            else if (v == do_7)
            {
                mConnectedThread.write("i");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Nota Do 7", Toast.LENGTH_SHORT).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                do7.start();
            }
        }
    };



    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    public void goToDeviceList() {
        Intent intent = new Intent(this, Inicio.class);
        startActivity(intent);
        finish();
    }

    public void messageToast(String message) {
        Toast.makeText(App.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
                Log.d(TAG, "-------->" + input);
            } catch (IOException e) {
                //if you cannot write, close the application
                messageToast("Conexión perdida");
                goToDeviceList();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        View decoreWindow = getWindow().getDecorView();
        if(hasFocus){
            decoreWindow.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}

