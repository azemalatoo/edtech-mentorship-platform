package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileSummaryDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import alatoo.edu.edtechmentorshipplatform.repo.MentorProfileRepo;
import alatoo.edu.edtechmentorshipplatform.services.impl.MentorSearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorSearchServiceImplTest {

    @Mock
    private MentorProfileRepo repo;

    @InjectMocks
    private MentorSearchServiceImpl service;

    @Test
    void search_withEmptyResults_returnsEmptyPage() {
        Pageable pageable = Pageable.unpaged();
        when(repo.findAll(any(Specification.class), eq(pageable)))
            .thenReturn(Page.empty());

        Page<MentorProfileSummaryDto> result =
            service.search(/*categoryId*/null, /*lang*/null, /*minExp*/null, pageable);

        assertThat(result).isEmpty();
        verify(repo).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void search_withOneResult_mapsToSummaryDto() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                        .id(UUID.randomUUID())
                        .fullName("Alice Smith")
                        .build();
        MentorProfile profile = MentorProfile.builder()
                .id(id)
                .user(user)
                .headline("Java Guru")
                .yearsExperience(8)
                .averageRating(4.7)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("yearsExperience").descending());
        Page<MentorProfile> page = new PageImpl<>(List.of(profile), pageable, 1);

        when(repo.findAll(any(Specification.class), eq(pageable)))
            .thenReturn(page);

        Page<MentorProfileSummaryDto> dtoPage =
            service.search(/*categoryId*/123L, /*lang*/Lang.EN, /*minExp*/5, pageable);

        assertThat(dtoPage).hasSize(1);
        MentorProfileSummaryDto dto = dtoPage.getContent().get(0);

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getFullName()).isEqualTo("Alice Smith");
        assertThat(dto.getHeadline()).isEqualTo("Java Guru");
        assertThat(dto.getYearsExperience()).isEqualTo(8);
        assertThat(dto.getAverageRating()).isEqualTo(4.7);

        verify(repo).findAll(any(Specification.class), eq(pageable));
    }
}
