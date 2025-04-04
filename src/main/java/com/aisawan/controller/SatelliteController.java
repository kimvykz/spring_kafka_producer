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

    @PostMapping("/publish-with-callback/satellites")
    public ResponseEntity<?> publishSatelliteWithCallback(@RequestBody RequestSatelliteDTO requestSatelliteDTO) {

        return satelliteService.publishSatelliteWithCallback(requestSatelliteDTO);
    }

    @PostMapping("/publish-with-blocking/satellites")
    public ResponseEntity<?> publishSatelliteWithBlocking(@RequestBody RequestSatelliteDTO requestSatelliteDTO) {

        return satelliteService.publishSatelliteWithblocking(requestSatelliteDTO);
    }

    @PostMapping("/publish-with-dlq/satellites")
    public ResponseEntity<?> publishSatelliteWithDlq(@RequestBody RequestSatelliteDTO requestSatelliteDTO) {

        return satelliteService.publishSatelliteWithDlq(requestSatelliteDTO);
    }


}
