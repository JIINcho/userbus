package com.example.userr_bus;

import com.google.firebase.Timestamp;

public class Reservation implements Comparable<Reservation> {
    private String userId;
    private String route;
    private String place;
    private String time;
    private Timestamp reservationDate;
    private String documentId;

    public Reservation() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    @Override
    public int compareTo(Reservation other){
        return this.reservationDate.compareTo(other.reservationDate);
    }

    public String getDocumentId(){
        return documentId;
    }

    public void setDocumentId(String documentId){ this.documentId = documentId; }


}

