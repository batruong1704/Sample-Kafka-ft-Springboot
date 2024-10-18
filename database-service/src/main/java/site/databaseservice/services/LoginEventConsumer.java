package site.databaseservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import site.databaseservice.models.EventModel;
import site.databaseservice.repository.EventRepo;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "db-event", groupId = "database-service")
public class LoginEventConsumer {
    private final EventRepo eventRepository;

    @KafkaHandler
    public void consume(EventModel input) {
        try {
            log.info("In Db-Service: \n\tReceived event: %s", input);
            EventModel eventModel = EventModel.builder()
                    .idUser(input.getIdUser())
                    .content(input.getContent())
                    .timeCreate(input.getTimeCreate())
                    .build();

            eventRepository.save(eventModel);
        } catch (Exception e) {
            log.error("\nIn DB-Service: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
