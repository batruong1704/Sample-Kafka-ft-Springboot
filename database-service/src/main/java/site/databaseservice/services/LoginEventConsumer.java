package site.databaseservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import site.databaseservice.models.EventModel;
import site.databaseservice.repository.EventRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginEventConsumer {
    private final EventRepo eventRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "db-event", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String input) {
        try {
            input = input.replace("\\\"", "\"");
            input = input.replaceAll("^\"|\"$", "");

            log.info("Processed JSON string: {}", input);

            EventModel inputModel = objectMapper.readValue(input, EventModel.class);

            EventModel eventModel = EventModel.builder()
                    .idUser(inputModel.getIdUser())
                    .content(inputModel.getContent())
                    .timeCreate(inputModel.getTimeCreate())
                    .build();

            eventRepository.save(eventModel);
            log.info("Successfully saved to database");
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
            log.error("Full stack trace: ", e);
            // Not acknowledging the message will cause it to be reprocessed
            throw new RuntimeException("Error processing message: " + e.getMessage(), e);
        }
    }
}