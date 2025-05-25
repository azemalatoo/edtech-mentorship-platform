package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
}
