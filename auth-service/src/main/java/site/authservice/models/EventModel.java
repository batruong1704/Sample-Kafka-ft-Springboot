package site.authservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
@Builder
public class EventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_user")
    private int idUser;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "time", nullable = false)
    private LocalDateTime timeCreate;

}
