package com.example.jmc242.tigerwolf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.content.Context;
import android.view.View;
import android.os.Vibrator;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends FragmentActivity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    private boolean reqLocUpd = true;
    private GoogleApiClient mGoogleApiClient;
    private Location curLoc;
    private String lastUpdTime;
    private LocationRequest mLocationRequest=new LocationRequest();
    private LocationStore locDatabase = new LocationStoreCWRU();
    private AudioManager mAudioManager;
    private Location lastCancel;

    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        updateValuesFromBundle(savedInstanceState);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient.isConnected() && !reqLocUpd) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        curLoc = location;
        lastUpdTime = DateFormat.getTimeInstance().format(new Date());
        if (lastCancel != null && lastCancel.distanceTo(curLoc) > 30) {
            lastCancel = null;
        }
        checkLoc();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if(reqLocUpd) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, reqLocUpd);
        savedInstanceState.putParcelable(LOCATION_KEY, curLoc);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, lastUpdTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            if(savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                reqLocUpd = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
            }
            if(savedInstanceState.keySet().contains(LOCATION_KEY)) {
                curLoc = savedInstanceState.getParcelable(LOCATION_KEY);
            }
            if(savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                lastUpdTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            checkLoc();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void checkLoc() {
        if (lastCancel != null || mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE
                || mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
            return;
        }
        for (Location e : locDatabase) {
            if (e.distanceTo(curLoc) < 30 && mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE && mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
                Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Do you wish to put phone on vibrate?");
                    builder1.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            dialog.cancel();
                        }
                    });
                    builder1.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            lastCancel = curLoc;
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert1 = builder1.create();
                    alert1.show();
            }
        }
    }

    /* private void updateUI() {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "Longitude:" + curLoc.getLongitude() + "Latitude" +
                curLoc.getLatitude(), Toast.LENGTH_LONG);
        toast.show();
    } */

    public void addLoc(View view) {
        locDatabase.addLocation(curLoc);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
