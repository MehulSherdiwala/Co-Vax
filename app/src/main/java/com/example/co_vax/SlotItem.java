package com.example.co_vax;

public class SlotItem {
    private int location_id;
    private String location_name;
    private String location_address;
    private String vaccine_name;
    private String date;
    private String slot;

    public SlotItem(int location_id, String location_name, String location_address, String vaccine_name, String date, String slot) {
        this.location_id = location_id;
        this.location_name = location_name;
        this.location_address = location_address;
        this.vaccine_name = vaccine_name;
        this.date = date;
        this.slot = slot;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
}
