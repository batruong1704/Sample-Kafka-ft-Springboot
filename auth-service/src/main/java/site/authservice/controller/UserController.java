package site.authservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import site.authservice.models.EventModel;
import site.authservice.models.UserDTO;
import site.authservice.models.UserModel;
import site.authservice.repository.UserRepo;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final UserRepo userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    private EventModel login(@RequestBody UserDTO userDto) throws JsonProcessingException {
        log.info("API login called");
        UserModel usermodel = userRepository.findByUsername(userDto.getUsername());
        log.info("Data:\n\tUsername: {}\n\tPassword: {}", userDto.getUsername(), userDto.getPassword());
        if(usermodel.getPassword().equals(userDto.getPassword())) {
            EventModel eventModel = EventModel.builder()
                    .idUser(usermodel.getId())
                    .content(String.format("User %s login success in %s", usermodel.getUsername(), new Date(System.currentTimeMillis())))
                    .timeCreate(LocalDateTime.now())
                    .build();

            this.kafkaTemplate.send(new ProducerRecord<>("notification-event", "200 Ok"));
            this.kafkaTemplate.send(new ProducerRecord<>("db-event", objectMapper.writeValueAsString(eventModel)));
            this.kafkaTemplate.send(new ProducerRecord<>("logs", "In log: login success"));
            return eventModel;

        }
        log.info("User login failed");
        this.kafkaTemplate.send(new ProducerRecord<>("notification-event", String.format("User %s login failed", userDto.getUsername())));
        this.kafkaTemplate.send(new ProducerRecord<>("logs", String.format("User %s login failed", userDto.getUsername())));
        return null;
    }

}