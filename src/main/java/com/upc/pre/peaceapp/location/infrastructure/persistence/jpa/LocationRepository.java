package com.upc.pre.peaceapp.location.infrastructure.persistence.jpa;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.domain.model.commands.DeleteAllLocationsByIdReportCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("""
           SELECT l.latitude, l.longitude, COUNT(l.idReport) AS reportCount
           FROM Location l
           GROUP BY l.latitude, l.longitude
           ORDER BY reportCount DESC
           """)
    List<Object[]> findDangerousLocations();
    void deleteAllByIdReport(Long idReport);
}
