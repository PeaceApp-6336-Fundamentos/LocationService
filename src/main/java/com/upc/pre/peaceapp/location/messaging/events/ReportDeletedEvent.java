package com.upc.pre.peaceapp.location.messaging.events;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published by the Report Service when a report is deleted.
 * Consumed by the Location Service to delete all locations associated with that report.
 */
@Getter
@Setter
public class ReportDeletedEvent implements Serializable {

    private Long reportId;
    private Long userId;
    private String message;
    private LocalDateTime timestamp;

    // Empty constructor required for deserialization
    public ReportDeletedEvent() {}

    public ReportDeletedEvent(Long reportId, Long userId, String message, LocalDateTime timestamp) {
        this.reportId = reportId;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }
}
