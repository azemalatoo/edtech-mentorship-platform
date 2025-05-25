package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileSummaryDto;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import alatoo.edu.edtechmentorshipplatform.enums.ProfileStatus;
import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.repo.MentorProfileRepo;
import alatoo.edu.edtechmentorshipplatform.services.MentorSearchService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorSearchServiceImpl implements MentorSearchService {
    private final MentorProfileRepo repo;

    @Override
    public Page<MentorProfileSummaryDto> search(Long categoryId, Lang lang, Integer minExperience, Pageable pageable) {
        Specification<MentorProfile> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("profileStatus"), ProfileStatus.APPROVED));
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("expertiseCategory").get("id"), categoryId));
            }
            if (lang != null) {
                predicates.add(cb.isMember(lang, root.get("languages")));
            }
            if (minExperience != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("yearsExperience"), minExperience));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return repo.findAll(spec, pageable)
                .map(m -> MentorProfileSummaryDto.builder()
                        .id(m.getId())
                        .fullName(m.getUser().getFullName())
                        .headline(m.getHeadline())
                        .yearsExperience(m.getYearsExperience())
                        .averageRating(m.getAverageRating())
                        .build());
    }
}
