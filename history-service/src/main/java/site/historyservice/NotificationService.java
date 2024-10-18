package site.historyservice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@KafkaListener(topics = "notification-event", groupId = "history-service")
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger("NOTIFICATIONS");

    @KafkaHandler
    public void consume(String input) {
        try {
            log.info(input);
            logger.info(String.format("Received notification: %s", input));
        } catch (Exception e) {
            log.error("Error processing notification: {}", input);
            logger.error(String.format("Error processing notification: %s", input), e);
            throw new RuntimeException(e);
        }

    }
}
