package martproject.MartProject.repository;

import martproject.MartProject.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
