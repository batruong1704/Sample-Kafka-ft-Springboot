package site.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import site.authservice.models.EventModel;

@EnableJpaRepositories
public interface EventRepo extends JpaRepository<EventModel, Integer> {
}
