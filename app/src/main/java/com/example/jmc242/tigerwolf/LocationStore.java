package com.example.jmc242.tigerwolf;

import android.location.Location;
import java.lang.Iterable;
import java.util.Iterator;

/**
 * Created by jonmonreal on 2/7/15.
 */
public class LocationStore implements Iterable<Location> {

    private Node list;

    public void addLocation(Location loc) {
        list = new Node(loc, list);
    }

    @Override
    public Iterator<Location> iterator() {
        return new LocationIterator();
    }

    public void remove(Location loc) {
        if (list.getLoc().getLongitude() == loc.getLongitude() && list.getLoc().getLatitude() ==
                loc.getLatitude()) {
            list = list.getNext();
        }
        Node ptr = list.getNext();
        Node previous = list;
        while (ptr != null) {
            if (ptr.getLoc().getLongitude() == loc.getLongitude() && ptr.getLoc().getLatitude() ==
                    loc.getLatitude()) {
                previous.setNext(ptr.getNext());
                return;
            }
            previous = ptr;
            ptr = ptr.getNext();
        }
    }

    private class LocationIterator implements Iterator<Location> {
        private Node current = list;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Location next() {
            Location next = current.getLoc();
            current = current.getNext();
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
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

        private void setNext(Node next) {
            this.next = next;
        }
    }

    public static void main(String[] args) {
    }
}
