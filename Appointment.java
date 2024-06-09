package com.example.project;

public class Appointment {
    private int id;
    private String patientName;
    private String doctorName;
    private String date;
    private String time;

    public Appointment(int id, String patientName, String doctorName, String date, String time) {
        this.id = id;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
