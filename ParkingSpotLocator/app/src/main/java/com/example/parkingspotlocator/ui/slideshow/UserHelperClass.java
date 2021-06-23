package com.example.parkingspotlocator.ui.slideshow;

public class UserHelperClass {
    String id,pincode,address,status;
    Double latitude,longitude;
    public UserHelperClass() {
    }


    public UserHelperClass(String id, String pincode, String address, Double latitude, Double longitude,String status) {
        this.id = id;
        this.pincode = pincode;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLonngitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

