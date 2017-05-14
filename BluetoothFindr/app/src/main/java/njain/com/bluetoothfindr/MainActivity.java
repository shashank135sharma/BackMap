package njain.com.bluetoothfindr;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;


// This app discovers the bluetooth devices acting as iBeacons around it

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    private BeaconManager baconManager; // to manage bacon

    TextView beaconDistancesTV;

    private String textUpdate;

    private ArrayList<String> uuidArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uuidArray = new ArrayList<String>();

        beaconDistancesTV = (TextView) findViewById(R.id.beaconDistancesTV);

        // get forking permissions (what a crisis)
        checkBTPermissions();

        baconManager = BeaconManager.getInstanceForApplication(this);
        // add iBeacons to the list since they are proprietary beacons
        baconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        // iBeacon = m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24

        baconManager.bind(this);

        beaconDistancesTV.setText(textUpdate);

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
    protected void onDestroy() {
        super.onDestroy();

        baconManager.unbind(this);
    }


} // end of MainActivity
