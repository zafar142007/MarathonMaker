package com.eagersphinx.marathonapp.dto;

public class LapResponse extends Response {
    private long timeOfLap;
    private int finishedLap;

    public LapResponse() {
    }

    public LapResponse(long timeOfLap, int finishedLap) {
        this.timeOfLap = timeOfLap;
        this.finishedLap = finishedLap;
    }

    public long getTimeOfLap() {
        return timeOfLap;
    }

    public void setTimeOfLap(long timeOfLap) {
        this.timeOfLap = timeOfLap;
    }

    public int getFinishedLap() {
        return finishedLap;
    }

    public void setFinishedLap(int finishedLap) {
        this.finishedLap = finishedLap;
    }

    @Override
    public String toString() {
        return "LapResponse{" +
                "timeOfLap=" + timeOfLap +
                ", finishedLap=" + finishedLap +
                '}';
    }
}