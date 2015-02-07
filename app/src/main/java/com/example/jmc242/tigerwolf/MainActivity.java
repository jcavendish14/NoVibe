package com.example.jmc242.tigerwolf;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.widget.Toast;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private boolean reqLocUpd = true;
    private LocationRequest locReq;
    private GoogleApiClient mGoogleApiClient;
    private Location curLoc;
    private String lastUpdTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();
        createLocationRequest();
        startLocationUpdates();
        onLocationChanged(curLoc);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
    }

    protected void createLocationRequest() {
        LocationRequest locReq = new LocationRequest();
        locReq.setInterval(5000);
        locReq.setFastestInterval(2000);
        locReq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locReq, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        curLoc = location;
        lastUpdTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "Longitude:" + curLoc.getLongitude() + "Latitude" +
                curLoc.getLatitude(), Toast.LENGTH_LONG);
        toast.show();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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
