package org.hospitalmanager.model;

public class Location {
    private String address;
    private String floor;
    private String roomNumber;

    public Location(String address, String floor, String roomNumber) {
        this.address = address;
        this.floor = floor;
        this.roomNumber = roomNumber;
    }


    public String getAddress() {
        return address;
    }

    public String getFloor() {
        return floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
