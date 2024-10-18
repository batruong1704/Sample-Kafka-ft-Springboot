package site.databaseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import site.databaseservice.models.EventModel;
import site.databaseservice.models.UserModel;

@EnableJpaRepositories
public interface EventRepo extends JpaRepository<EventModel, Integer> {
}
