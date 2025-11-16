package com.upc.pre.peaceapp.location.messaging.listeners;

import com.upc.pre.peaceapp.location.application.internal.outboundservices.ExternalReportService;
import com.upc.pre.peaceapp.location.domain.model.commands.CreateLocationCommand;
import com.upc.pre.peaceapp.location.domain.services.LocationCommandService;
import com.upc.pre.peaceapp.location.messaging.events.ReportApprovedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReportApprovedEventListener {

    private final ExternalReportService externalReportService;
    private final LocationCommandService locationCommandService;

    public ReportApprovedEventListener(
            ExternalReportService externalReportService,
            LocationCommandService locationCommandService
    ) {
        this.externalReportService = externalReportService;
        this.locationCommandService = locationCommandService;
    }

    @RabbitListener(
            queues = "${app.broker.queue.report.approved}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handleReportApproved(ReportApprovedEvent event) {

        Long reportId = event.getReportId();

        if (reportId == null) {
            System.out.println("⚠️ Missing reportId in event.");
            return;
        }

        // 1️⃣ Consultar el ReportService (OUTBOUND)
        var report = externalReportService.fetchById(reportId);

        if (report == null) {
            System.out.println("⚠️ Report not found in ReportService");
            return;
        }

        // VALIDAR LAT / LNG
        if (report.getLatitude() == null || report.getLongitude() == null) {
            System.out.println("⚠️ Approved report has no coordinates. Cannot create location.");
            return;
        }

        // 2️⃣ Crear Location
        locationCommandService.handle(
                new CreateLocationCommand(
                        report.getLatitude(),
                        report.getLongitude(),
                        reportId
                )
        );

        System.out.println("📍 Location created for approved report ID " + reportId);
    }
}
