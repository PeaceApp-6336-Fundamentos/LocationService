package com.upc.pre.peaceapp.location.infrastructure.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String type;
    private Long userId;
    private String imageUrl;
    private String latitude;
    private String longitude;
}
