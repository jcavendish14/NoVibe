package com.example.jmc242.tigerwolf;

import android.location.Location;

/**
 * Created by jonmonreal on 2/7/15.
 */
public class LocationStoreCWRU extends LocationStore {
    public LocationStoreCWRU() {
        Location t1 = new Location("Test");
        Location t2 = new Location("Test");
        t1.setLongitude(-81.607898);
        t1.setLatitude(41.502657);
        t2.setLongitude(-81.607481);
        t2.setLatitude(41.504162);
        this.addLocation(t1);
        this.addLocation(t2);
    }
}
