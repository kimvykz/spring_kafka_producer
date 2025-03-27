package com.aisawan.service;

import com.aisawan.dto.RequestSatelliteDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SatelliteService {

    @Autowired
    KafkaTemplate<String, RequestSatelliteDTO> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ResponseEntity<?> publishSatellite(RequestSatelliteDTO requestSatelliteDTO) {

        try {
            log.info("--->>>Sending to kafka--->>> " + requestSatelliteDTO);
            kafkaTemplate.send(topicName, String.valueOf(requestSatelliteDTO.getId()), requestSatelliteDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    e.getMessage()
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                "Published Successfully"
        );
    }
}
