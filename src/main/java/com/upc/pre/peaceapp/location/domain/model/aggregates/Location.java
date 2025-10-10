package com.upc.pre.peaceapp.location.domain.model.aggregates;

import com.upc.pre.peaceapp.shared.documentation.models.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location extends AuditableAbstractAggregateRoot {
    @Getter
    @Setter
    @Column(name="latitude", nullable = false, length = 30)
    private String latitude;
    @Getter
    @Setter
    @Column(name="longitude", nullable = false, length = 30)
    private String longitude;
    @Getter
    @Setter
    @Column(name="id_report", nullable = false)
    private Long idReport;

    public Location(String aLatitude, String aLongitude, Long idReport) {
        this.latitude = aLatitude;
        this.longitude = aLongitude;
        this.idReport = idReport;
    }
}
