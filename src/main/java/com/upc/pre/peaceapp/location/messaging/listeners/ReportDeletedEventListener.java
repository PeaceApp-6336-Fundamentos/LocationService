package com.upc.pre.peaceapp.location.messaging.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.pre.peaceapp.location.domain.model.commands.DeleteAllLocationsByIdReportCommand;
import com.upc.pre.peaceapp.location.domain.services.LocationCommandService;
import com.upc.pre.peaceapp.location.messaging.events.ReportDeletedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReportDeletedEventListener {

    private final LocationCommandService locationCommandService;

    public ReportDeletedEventListener(LocationCommandService locationCommandService) {
        this.locationCommandService = locationCommandService;
    }
    @RabbitListener(
            queues = "${app.broker.queue.report.deleted}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handleReportDeleted(ReportDeletedEvent event) {
        if (event.getReportId() != null) {
            locationCommandService.handle(new DeleteAllLocationsByIdReportCommand(event.getReportId()));
            System.out.println("📍 Deleted all locations for report ID: " + event.getReportId());
        } else {
            System.out.println("⚠️ Skipped location deletion: reportId missing in event.");
        }
    }

}
