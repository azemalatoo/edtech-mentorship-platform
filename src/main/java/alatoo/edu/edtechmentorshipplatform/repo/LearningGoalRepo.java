package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.LearningGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LearningGoalRepo extends JpaRepository<LearningGoal, Long> {
    List<LearningGoal> findByMenteeId(UUID menteeId);
}
