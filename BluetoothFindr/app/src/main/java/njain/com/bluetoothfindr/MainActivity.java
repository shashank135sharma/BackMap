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

import java.util.Collection;


// This app discovers the bluetooth devices acting as iBeacons around it

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    private BeaconManager baconManager; // to manage bacon

//    TextView beaconDistances

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBTPermissions();

        baconManager = BeaconManager.getInstanceForApplication(this);
        // add iBeacons to the list since they are proprietary beacons
        baconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        // iBeacon = m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24

        baconManager.bind(this);

    }

    @Override
    public void onBeaconServiceConnect() {
        baconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {
                    Log.d("DO NOT READ","I spy with my little eye " + beacons.iterator().next().getDistance()
                            + " metres away");
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
