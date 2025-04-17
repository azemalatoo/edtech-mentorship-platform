package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepo extends JpaRepository<Review, UUID> {
    List<Review> findByRevieweeId(UUID userId);

    List<Review> findByReviewerId(UUID reviewerId);
}
