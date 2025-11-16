package com.upc.pre.peaceapp.location.messaging.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportApprovedEvent implements Serializable {

    private Long reportId;
    private String latitude;
    private String longitude;
    private String message;
    private LocalDateTime timestamp;

    public ReportApprovedEvent() {}
}
