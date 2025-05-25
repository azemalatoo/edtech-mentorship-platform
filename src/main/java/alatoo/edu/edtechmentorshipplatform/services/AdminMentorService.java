package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import java.util.List;
import java.util.UUID;

public interface AdminMentorService {
    List<MentorProfile> listPending();
    MentorProfile getById(UUID id);
    MentorProfile approve(UUID id);
    MentorProfile reject(UUID id, String reason);
}
