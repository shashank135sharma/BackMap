package njain.com.bluetoothfindr;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;


// This app discovers the bluetooth devices acting as iBeacons around it

public class MainActivity extends AppCompatActivity implements BeaconConsumer, SensorEventListener{

    private BeaconManager baconManager; // to manage bacon

    TextView beaconDistancesTV;

    TextView compassTV;

    private String textUpdate;

    private ArrayList<String> uuidArray;

    private SensorManager mSensorManager;
    private Sensor sensor;
    private float degree; // not the deodorant!
    private String direction = "west"; // which way do you want to face boi

    private int fingers = 0;
    private int max = 0;

    private String paperdotio = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // point yourself north
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        uuidArray = new ArrayList<String>();

        beaconDistancesTV = (TextView) findViewById(R.id.beaconDistancesTV);
        compassTV = (TextView) findViewById(R.id.compassTV);

        // get forking permissions (what a crisis)
        checkBTPermissions();

        baconManager = BeaconManager.getInstanceForApplication(this);
        // add iBeacons to the list since they are proprietary beacons
        baconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        // iBeacon = m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24

        baconManager.bind(this);

        beaconDistancesTV.setText(textUpdate);

//             point north
//        while(Math.abs(degree - 0) < 7) {
//            if (degree > 0) {
//                sendMessage("left");
//            }
//            else {
//                sendMessage("right");
//            }
//        }



    }

    @Override
    public void onResume() {
        super.onResume();

        if (sensor != null) {
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            Toast.makeText(MainActivity.this, "come on dude", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        baconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {

//                    int counter = 0;
                    textUpdate = "";
//                    SortedSet<Beacon> sortedBeacons = (SortedSet) beacons;
//
//                    Collections.sort(beacons, new Comparator<Object>() {
//                        @Override
//                        public int compare(Beacon o1, Beacon o2) {
//
////                            o1 = (Beacon) o1;
//
//                            return new Integer(o1.getId1().toString().compareTo(o2.getId1().toString()));
//                        }
//                    });

                    for (Beacon beacon : beacons) {

//                        int numOfBeacons = beacons.size();

                        // only update the ui if there is a new uuid (change distance ahhh)
//                        String uuid = beacon.getId1().toString();


                        textUpdate += "\nUU: " + beacon.getId1().toString()
                                + "\nMajor: " + beacon.getId2().toString()
                                + "\nDistance: " + beacon.getDistance() + " meters away\n"
                                + "______________________________________\n";


//                        if (!uuidArray.contains(uuid)) {
//
//                            uuidArray.add(uuid);
//
//                            textUpdate += "\nUU: " + beacon.getId1().toString()
//                                    + "\nDistance: " + beacon.getDistance() + " meters away\n"
//                                    + "______________________________________\n";
//
////                            Log.i("BACON", textUpdate);
//
//                        }
//                        else {
//                            String currentText = beaconDistancesTV.getText().toString();
//
//                            ArrayList<Integer> startIndices = new ArrayList<Integer>();
//                            ArrayList<Integer> endIndices = new ArrayList<Integer>();
//
//                            int counter = -1;
//                            for (char c : currentText.toCharArray()) {
//                                counter++;
//
//                                if (c == 'D')
//                                    startIndices.add(counter);
//
//                                if (c == 'y')
//                                    endIndices.add(counter + 1);
//
//                            }
////
////                            int startIndex = currentText.indexOf('D');
////                            int endIndex = currentText.indexOf('y') + 1;
//
//                            String adder = "";
//
//                            for (int i = 0; i < startIndices.size(); i++) {
//
//
//                                int startIndex = startIndices.get(i);
//                                int endIndex = endIndices.get(i);
//
//                                String replaceMe = currentText.substring(startIndex, endIndex);
//                                String newStr = "Distance: " + beacon.getDistance() + " meters away";
//
//                                adder = currentText.replace(replaceMe, newStr);
//
//                            }
//
//                            textUpdate = adder;
//
//
//                            //currentText.replace("Distance: ");
//                        }


                    }

                    // only update ui if there is a new uuid
//                    String currentText = beaconDistancesTV.getText().toString();
//                    if (!textUpdate.equals(currentText)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final String temp = textUpdate;
                            beaconDistancesTV.setText(temp);
                        }
                    });
//                    }

//                    Log.d("DO NOT READ","I spy with my little eye " + beacons.iterator().next().getDistance()
//                            + " metres away");
                }

            }
        });

        try {
            baconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    // send a right or left to the who even knows why weraweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
    private void sendMessage(final String direction) {

        Log.d("Direction", direction);

        paperdotio = direction;

//        direction = final direc

        // put some delay
        SystemClock.sleep(1500);
        HttpTask shutuprishi = new HttpTask();
        shutuprishi.execute();

    }

    class HttpTask extends AsyncTask<Void, Void, Void> {

        public HttpTask() {
            super();
        }

        @Override
        protected Void doInBackground(Void... params) {


            // send request
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                final String no = paperdotio;
                url = new URL("http://97d0578c.ngrok.io/" + no);
                Log.d("tanmay", url.toString());

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();
//
                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    System.out.print(current);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    // check permissions
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d("bruh", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {

        Log.d("Clean batch", Integer.toString(e.getPointerCount()));

        if (e.getPointerCount() > max){
            max = e.getPointerCount();
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                Log.d("batch", Integer.toString(max));

                // max here
                if (max == 1) {
                    direction = "west";
                }
                else if (max == 2) {
                    direction = "south";

                }
                else if (max == 3) {
                    direction = "east";

                }


//        Log.d("Direction", direction);
                // align direction
                if (direction.equals("north")) {
                    // point north
                    if (degree > 10 && degree < 180) {
                        sendMessage("left");
                    }
                    else if (degree < 350 && degree > 180) {
                        sendMessage("right");
                    }
                }
                else if (direction.equals("east")) {
                    // point south
                    if (degree > 95 && degree < 270) {
                        sendMessage("left");
                    }
                    else if (degree < 85 && degree > 0) {
                        sendMessage("right");
                    }
                }
                else if (direction.equals("south")) {
                    // point east meets west
                    if (degree > 185 && degree < 359) {
                        sendMessage("left");
                    }
                    else if (degree < 175 && degree > 0) {
                        sendMessage("right");
                    }
                }
                else if (direction.equals("west")) {
                    // go west yung man
                    if (degree > 275 && degree < 360) {
                        sendMessage("left");
                    }
                    else if (degree < 265 && degree > 90) {
                        sendMessage("right");
                    }
                }

                max = 0;
                break;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        baconManager.unbind(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // do something
        degree = event.values[0];
        compassTV.setText(Float.toString(degree));
//
////        Log.d("Direction", direction);
//        // align direction
//        if (direction.equals("north")) {
//            // point north
//            if (degree > 10 && degree < 180) {
//                sendMessage("left");
//            }
//            else if (degree < 350 && degree > 180) {
//                sendMessage("right");
//            }
//        }
//        else if (direction.equals("east")) {
//            // point south
//            if (degree > 95 && degree < 270) {
//                sendMessage("left");
//            }
//            else if (degree < 85 && degree > 0) {
//                sendMessage("right");
//            }
//        }
//        else if (direction.equals("south")) {
//            // point east meets west
//            if (degree > 185 && degree < 359) {
//                sendMessage("left");
//            }
//            else if (degree < 175 && degree > 0) {
//                sendMessage("right");
//            }
//        }
//        else if (direction.equals("west")) {
//            // go west yung man
//            if (degree > 275 && degree < 360) {
//                sendMessage("left");
//            }
//            else if (degree < 265 && degree > 90) {
//                sendMessage("right");
//            }
//        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }
} // end of MainActivity
