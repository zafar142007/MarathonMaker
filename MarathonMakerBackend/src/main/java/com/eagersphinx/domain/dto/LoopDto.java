package com.eagersphinx.domain.dto;

import lombok.Getter;

@Getter
public class LoopDto {

    private Integer bib;
    private double lat, longitude;
    private String checkpoint;

    private String event;

    private int marathonId;

    @Override
    public String toString() {
        return "LoopDto{" +
                "bib=" + bib +
                ", lat=" + lat +
                ", longitude=" + longitude +
                ", checkpoint='" + checkpoint + '\'' +
                ", event=" + event +
                ", marathonId=" + marathonId +
                '}';
    }
}
