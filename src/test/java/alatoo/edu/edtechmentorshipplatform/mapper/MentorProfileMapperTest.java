package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.category.CategoryDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.Category;
import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MentorProfileMapperTest {

    @Test
    void toEntity_allFieldsProvided_shouldMapDirectly() {
        // given
        MentorProfileRequestDto dto = new MentorProfileRequestDto();
        dto.setBio("Expert in Java");
        dto.setHeadline("Java Guru");
        dto.setYearsExperience(7);
        dto.setLinkedinUrl("https://linkedin.com/in/test");
        dto.setCertifications(List.of("Cert1", "Cert2"));
        dto.setLanguages(List.of(Lang.EN, Lang.KY));
        dto.setAverageRating(4.5);

        User user = User.builder()
            .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
            .build();
        Category category = Category.builder()
            .id(10L)
            .name("Programming")
            .description("All about code")
            .build();

        // when
        MentorProfile entity = MentorProfileMapper.toEntity(dto, user, category);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getUser()).isSameAs(user);
        assertThat(entity.getBio()).isEqualTo("Expert in Java");
        assertThat(entity.getHeadline()).isEqualTo("Java Guru");
        assertThat(entity.getYearsExperience()).isEqualTo(7);
        assertThat(entity.getLinkedinUrl()).isEqualTo("https://linkedin.com/in/test");
        assertThat(entity.getCertifications()).containsExactly("Cert1", "Cert2");
        assertThat(entity.getLanguages()).containsExactly(Lang.EN, Lang.KY);
        assertThat(entity.getExpertiseCategory()).isSameAs(category);
        assertThat(entity.getAverageRating()).isEqualTo(4.5);
    }

    @Test
    void toEntity_nullDto_shouldThrowNPE() {
        UUID randomUserId = UUID.randomUUID();
        User user = User.builder().id(randomUserId).build();
        Category category = Category.builder().id(1L).build();
        assertThrows(NullPointerException.class,
            () -> MentorProfileMapper.toEntity(null, user, category));
    }

    @Test
    void toDto_fullEntity_shouldMapAllFields() {
        // given
        UUID profileId = UUID.randomUUID();
        User user = User.builder().id(UUID.randomUUID()).build();
        Category category = Category.builder()
                .id(20L)
                .name("Data")
                .description("Data category")
                .build();

        MentorProfile entity = MentorProfile.builder()
                .id(profileId)
                .user(user)
                .bio("Bio here")
                .headline("Mr. Mentor")
                .yearsExperience(3)
                .linkedinUrl("https://linkedin.com/in/mentor")
                .certifications(List.of("CertA"))
                .languages(List.of(Lang.EN))
                .expertiseCategory(category)
                .averageRating(3.8)
                .build();

        // when
        MentorProfileResponseDto dto = MentorProfileMapper.toDto(entity);
        CategoryDto catDto = dto.getExpertiseCategory();

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(profileId);
        assertThat(dto.getBio()).isEqualTo("Bio here");
        assertThat(dto.getHeadline()).isEqualTo("Mr. Mentor");
        assertThat(dto.getYearsExperience()).isEqualTo(3);
        assertThat(dto.getLinkedinUrl()).isEqualTo("https://linkedin.com/in/mentor");
        assertThat(dto.getCertifications()).containsExactly("CertA");
        assertThat(dto.getLanguages()).containsExactly(Lang.EN);
        assertThat(catDto).isNotNull();
        assertThat(catDto.getId()).isEqualTo(20L);
        assertThat(catDto.getName()).isEqualTo("Data");
        assertThat(catDto.getDescription()).isEqualTo("Data category");
        assertThat(dto.getAverageRating()).isEqualTo(3.8);
    }


    @Test
    void toDto_nullEntity_shouldThrowNPE() {
        assertThrows(NullPointerException.class,
            () -> MentorProfileMapper.toDto(null));
    }
}
