package com.aisawan.dto;

import lombok.Data;

@Data
public class RequestSatelliteDTO {
    private Integer id;
    private String manufacturer;
    private String model;
    private String prodDate;
    private String prodCountry;
}
