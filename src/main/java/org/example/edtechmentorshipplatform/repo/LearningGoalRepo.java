package org.example.edtechmentorshipplatform.repo;

import org.example.edtechmentorshipplatform.entity.LearningGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningGoalRepo extends JpaRepository<LearningGoal, Long> {
}
