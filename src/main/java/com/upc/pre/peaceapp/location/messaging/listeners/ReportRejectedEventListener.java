package com.upc.pre.peaceapp.location.messaging.listeners;

import com.upc.pre.peaceapp.location.domain.model.commands.DeleteAllLocationsByIdReportCommand;
import com.upc.pre.peaceapp.location.domain.services.LocationCommandService;
import com.upc.pre.peaceapp.location.messaging.events.ReportRejectedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReportRejectedEventListener {

    private final LocationCommandService locationCommandService;

    public ReportRejectedEventListener(LocationCommandService locationCommandService) {
        this.locationCommandService = locationCommandService;
    }

    @RabbitListener(
            queues = "${app.broker.queue.report.rejected}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handleReportRejected(ReportRejectedEvent event) {

        if (event.getReportId() != null) {
            // Eliminar locations asociados al reporte rechazado
            locationCommandService.handle(new DeleteAllLocationsByIdReportCommand(event.getReportId()));

            System.out.println("❌ REJECTED → Deleted all locations for report ID: " + event.getReportId());
        } else {
            System.out.println("⚠️ Skipped location deletion (REJECTED): reportId missing in event.");
        }
    }
}
