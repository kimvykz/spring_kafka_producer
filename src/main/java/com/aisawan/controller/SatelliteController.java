package com.aisawan.controller;

import com.aisawan.dto.RequestSatelliteDTO;
import com.aisawan.service.SatelliteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SatelliteController {
    private final SatelliteService satelliteService;

    @PostMapping("/publish/satellites")
    public ResponseEntity<?> publishSatellite(@RequestBody RequestSatelliteDTO requestSatelliteDTO) {

        return satelliteService.publishSatellite(requestSatelliteDTO);
    }
}
