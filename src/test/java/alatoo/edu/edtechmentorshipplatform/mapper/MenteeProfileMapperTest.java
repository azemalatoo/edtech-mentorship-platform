package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MenteeProfile;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenteeProfileMapperTest {

    @Test
    void toEntity_shouldMapAllFields() {
        // given
        UUID profileId = UUID.fromString("00000000-0000-0000-0000-00000000000A");
        MenteeProfileResponseDto dto = MenteeProfileResponseDto.builder()
                .id(profileId)
                .educationLevel("Bachelor's")
                .fieldOfStudy("Computer Science")
                .careerGoal("Software Engineer")
                .preferredLanguage(Lang.EN)
                .build();

        UUID userId = UUID.fromString("00000000-0000-0000-0000-00000000000B");
        User user = User.builder()
                .id(userId)
                .build();

        // when
        MenteeProfile entity = MenteeProfileMapper.toEntity(dto, user);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(profileId);
        assertThat(entity.getUser()).isSameAs(user);
        assertThat(entity.getEducationLevel()).isEqualTo("Bachelor's");
        assertThat(entity.getFieldOfStudy()).isEqualTo("Computer Science");
        assertThat(entity.getCareerGoal()).isEqualTo("Software Engineer");
        assertThat(entity.getPreferredLanguage()).isEqualTo(Lang.EN);
    }

    @Test
    void toEntity_nullDto_shouldThrowNPE() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        assertThrows(NullPointerException.class,
                () -> MenteeProfileMapper.toEntity(null, user));
    }

    @Test
    void toDto_shouldMapAllFields() {
        // given
        UUID profileId = UUID.fromString("00000000-0000-0000-0000-00000000000C");
        UUID userId    = UUID.fromString("00000000-0000-0000-0000-00000000000D");

        MenteeProfile entity = MenteeProfile.builder()
                .id(profileId)
                .user(User.builder().id(userId).build())
                .educationLevel("Master's")
                .fieldOfStudy("Data Science")
                .careerGoal("Data Analyst")
                .preferredLanguage(Lang.KY)
                .build();

        // when
        MenteeProfileResponseDto dto = MenteeProfileMapper.toDto(entity);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(profileId);
        assertThat(dto.getEducationLevel()).isEqualTo("Master's");
        assertThat(dto.getFieldOfStudy()).isEqualTo("Data Science");
        assertThat(dto.getCareerGoal()).isEqualTo("Data Analyst");
        assertThat(dto.getPreferredLanguage()).isEqualTo(Lang.KY);
    }

    @Test
    void toDto_nullEntity_shouldThrowNPE() {
        assertThrows(NullPointerException.class,
                () -> MenteeProfileMapper.toDto(null));
    }
}
