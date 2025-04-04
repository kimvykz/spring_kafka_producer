package com.aisawan.service;

import com.aisawan.dto.RequestSatelliteDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SatelliteService {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    @Value("${kafka.topic-dlq.name}")
    private String topicDlqName;


    private void sendToDLQ(RequestSatelliteDTO dto) {
        try {
            kafkaTemplate.send(topicDlqName, String.valueOf(dto.getId()), dto);
            log.warn("Message sent to DLQ: {}", dto);
        } catch (Exception e) {
            log.error("Error sending to DLQ! Data has been lost! ", e);
        }
    }


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


    public ResponseEntity<?> publishSatelliteWithCallback(RequestSatelliteDTO requestSatelliteDTO) {

        try {
            log.info("--->>>Sending with callback to kafka--->>> {}", requestSatelliteDTO);
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topicName, String.valueOf(requestSatelliteDTO.getId()), requestSatelliteDTO);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Error occurred: {}", ex.getMessage(), ex);
                } else {
                    log.info("Message sent! offset: {}", result.getRecordMetadata().offset());
                }
            });

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

    public ResponseEntity<?> publishSatelliteWithblocking(RequestSatelliteDTO requestSatelliteDTO) {

        try {
            log.info("--->>>Sending with blocking to kafka--->>> {}", requestSatelliteDTO);
            kafkaTemplate.send(topicName, String.valueOf(requestSatelliteDTO.getId()), requestSatelliteDTO).get(3, TimeUnit.SECONDS);

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

    public ResponseEntity<?> publishSatelliteWithDlq(RequestSatelliteDTO requestSatelliteDTO) {
        try {
            log.info("--->>> Sending with DLQ logic to kafka--->>> {}", requestSatelliteDTO);
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topicName, String.valueOf(requestSatelliteDTO.getId()), requestSatelliteDTO);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Error occurred: {}", ex.getMessage());
                    sendToDLQ(requestSatelliteDTO);
                } else {
                    log.info("data has been sent! offset: {}", result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("Critical error: ", e.getMessage());
            sendToDLQ(requestSatelliteDTO);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Kafka send failed, saved to DLQ");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Published Successfully");
    }

}
