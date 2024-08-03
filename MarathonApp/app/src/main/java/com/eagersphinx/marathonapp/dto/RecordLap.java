package com.eagersphinx.marathonapp.dto;


public class RecordLap {
    private Integer bib;
    private Double lat;
    private Double longitude;
    private String checkpoint;
    private String event;
    private Integer marathonId;

    public RecordLap(Integer bib, Double lat, Double longitude, String checkpoint, Integer marathonId, String item) {
        this.bib = bib;
        this.lat = lat;
        this.longitude = longitude;
        this.checkpoint = checkpoint;
        this.marathonId = marathonId;
        event = item;
    }
    public RecordLap(Integer bib, Double lat, Double longitude, String checkpoint, Integer m) {
        this.bib = bib;
        this.lat = lat;
        this.longitude = longitude;
        this.checkpoint = checkpoint;
        this.marathonId = m;
    }


    public int getBib() {
        return bib;
    }

    public void setBib(int bib) {
        this.bib = bib;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }

    public void setBib(Integer bib) {
        this.bib = bib;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Integer getMarathonId() {
        return marathonId;
    }

    public void setMarathonId(Integer marathonId) {
        this.marathonId = marathonId;
    }
}
