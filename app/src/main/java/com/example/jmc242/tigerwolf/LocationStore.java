package com.example.jmc242.tigerwolf;

import android.location.Location;

/**
 * Created by jonmonreal on 2/7/15.
 */
public class LocationStore {
    private Node list;

    public void addLocation(Location loc) {
        list = new Node(loc, list);
    }

    private class Node {
        private Location loc;
        private Node next;

        private Node(Location loc, Node next) {
            this.loc = loc;
            this.next = next;
        }

        private Node(String provider, Node next) {
            this.loc = new Location(provider);
            this.next = next;
        }

        private Location getLoc() {
            return loc;
        }

        private Node getNext() {
            return next;
        }
    }
}
