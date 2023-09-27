package com.example.authnexus.config.kafka;

import com.example.authnexus.payload.MemberUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, MemberUpdateRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            String jsonInString = mapper.writeValueAsString(request);
            kafkaTemplate.send(topic, jsonInString);
            log.info("Kafka Producer sent data from the KafkaStudy service: {}", request);

        } catch (JsonProcessingException exception) {
            log.error("Error serializing the message: {}", exception.getMessage(), exception);
        }
    }

}
