package com.eagersphinx.domain.dto;

import java.sql.Date;
import java.sql.Timestamp;

public interface MarathonResponse {

    Integer getBib();

    Double getLat() ;

    Integer getLoop();


     Double getLongitude();


     Timestamp getCreated_at();


     String getCheckpoint();


     int getId() ;


     String getAddress() ;

    Integer getEvent_id() ;


    int getUser_id() ;


     String getUname() ;


     String getEname() ;

     Boolean getAwarded();

}
