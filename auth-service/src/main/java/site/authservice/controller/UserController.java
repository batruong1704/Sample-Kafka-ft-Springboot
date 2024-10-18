package site.authservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
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
@Slf4j
public class UserController {

    private final UserRepo userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/login")
    private EventModel login(@RequestBody UserDTO userDto) {
        UserModel usermodel = userRepository.findByUsername(userDto.getUsername());
        log.info("Data:\n\tUsername: " + userDto.getUsername() + "\n\tPassword: " + userDto.getPassword());
        if(usermodel.getPassword().equals(userDto.getPassword())) {
            EventModel eventModel = EventModel.builder()
                    .idUser(usermodel.getId())
                    .content(String.format("User %s login success in %s", usermodel.getUsername(), new Date(System.currentTimeMillis())))
                    .timeCreate(LocalDateTime.now())
                    .build();

            this.kafkaTemplate.send(new ProducerRecord<>("notification-event", eventModel.getContent()));
            this.kafkaTemplate.send(new ProducerRecord<>("db-event", eventModel));
            this.kafkaTemplate.send(new ProducerRecord<>("history-event", eventModel.getContent()));
            return eventModel;

        }
        this.kafkaTemplate.send(new ProducerRecord<>("notification-event", String.format("User %s login failed", userDto.getUsername())));
        return null;
    }

}