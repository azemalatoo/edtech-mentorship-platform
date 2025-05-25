package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MentorSearchService {
    Page<MentorProfileSummaryDto> search(
        Long categoryId,
        alatoo.edu.edtechmentorshipplatform.enums.Lang lang,
        Integer minExperience,
        Pageable pageable
    );
}
