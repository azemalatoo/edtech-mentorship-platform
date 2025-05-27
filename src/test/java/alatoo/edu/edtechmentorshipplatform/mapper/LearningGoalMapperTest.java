package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.LearningGoal;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LearningGoalMapperTest {

    private final LearningGoalMapper mapper = new LearningGoalMapper();

    @Test
    void toEntity_allFieldsProvided_shouldMapDirectly() {
        LearningGoalRequestDto dto = new LearningGoalRequestDto();
        dto.setGoalTitle("Learn Spring");
        dto.setDescription("Deep dive into Spring Boot");
        dto.setTargetDate(LocalDate.of(2025, 6, 30));
        dto.setProgressPercentage(45);
        dto.setProgressNotes("Started modules");
        dto.setStatus(GoalStatus.IN_PROGRESS);
        dto.setFeedback("Keep going!");

        User mentee = User.builder().id(UUID.fromString("00000000-0000-0000-0000-000000000001")).build();
        User mentor = User.builder().id(UUID.fromString("00000000-0000-0000-0000-000000000002")).build();

        LearningGoal entity = mapper.toEntity(dto, mentee, mentor);


        assertThat(entity).isNotNull();
        assertThat(entity.getMentee()).isSameAs(mentee);
        assertThat(entity.getMentor()).isSameAs(mentor);
        assertThat(entity.getGoalTitle()).isEqualTo("Learn Spring");
        assertThat(entity.getDescription()).isEqualTo("Deep dive into Spring Boot");
        assertThat(entity.getTargetDate()).isEqualTo(LocalDate.of(2025, 6, 30));
        assertThat(entity.getProgressPercentage()).isEqualTo(45);
        assertThat(entity.getProgressNotes()).isEqualTo("Started modules");
        assertThat(entity.getStatus()).isEqualTo(GoalStatus.IN_PROGRESS);
        assertThat(entity.getFeedback()).isEqualTo("Keep going!");
    }

    @Test
    void toEntity_nullProgressAndStatus_shouldUseDefaults() {
        LearningGoalRequestDto dto = new LearningGoalRequestDto();
        dto.setGoalTitle("Finish Thesis");
        dto.setDescription("Complete by mid-year");
        dto.setTargetDate(LocalDate.of(2025, 7, 15));

        User mentee = User.builder().id(UUID.fromString("00000000-0000-0000-0000-000000000003")).build();
        User mentor = User.builder().id(UUID.fromString("00000000-0000-0000-0000-000000000004")).build();

        LearningGoal entity = mapper.toEntity(dto, mentee, mentor);

        assertThat(entity.getProgressPercentage()).isZero();
        assertThat(entity.getStatus()).isEqualTo(GoalStatus.IN_PROGRESS);
    }

    @Test
    void toDto_fullEntity_shouldMapAllFields() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 27, 9, 0);
        LocalDateTime achievedAt = createdAt.plusDays(2);
        LocalDate targetDate = achievedAt.plusWeeks(1).toLocalDate(); // 2025-06-06

        UUID menteeId = UUID.fromString("00000000-0000-0000-0000-000000000005");
        UUID mentorId = UUID.fromString("00000000-0000-0000-0000-000000000006");

        LearningGoal entity = LearningGoal.builder()
                .id(123L)
                .mentee(User.builder().id(menteeId).build())
                .mentor(User.builder().id(mentorId).build())
                .goalTitle("Read Papers")
                .description("Read 10 research papers")
                .createdAt(createdAt)
                .achievedAt(achievedAt)
                .targetDate(targetDate)
                .progressPercentage(100)
                .progressNotes("Done")
                .status(GoalStatus.ACHIEVED)
                .feedback("Excellent work")
                .build();

        LearningGoalResponseDto dto = mapper.toDto(entity);

        assertThat(dto.getId()).isEqualTo(123L);
        assertThat(dto.getMenteeId()).isEqualTo(menteeId);
        assertThat(dto.getMentorId()).isEqualTo(mentorId);
        assertThat(dto.getGoalTitle()).isEqualTo("Read Papers");
        assertThat(dto.getDescription()).isEqualTo("Read 10 research papers");
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(dto.getAchievedAt()).isEqualTo(achievedAt);
        assertThat(dto.getTargetDate()).isEqualTo(targetDate);
        assertThat(dto.getProgressPercentage()).isEqualTo(100);
        assertThat(dto.getProgressNotes()).isEqualTo("Done");
        assertThat(dto.getStatus()).isEqualTo(GoalStatus.ACHIEVED);
        assertThat(dto.getFeedback()).isEqualTo("Excellent work");
    }

    @Test
    void toDto_nullMentor_shouldMapMentorIdToNull() {
        UUID menteeId = UUID.fromString("00000000-0000-0000-0000-000000000007");
        LearningGoal entity = LearningGoal.builder()
                .id(456L)
                .mentee(User.builder().id(menteeId).build())
                .mentor(null)
                .goalTitle("Plan Project")
                .description("Outline project steps")
                .createdAt(LocalDateTime.of(2025, 5, 27, 10, 0))
                .build();

        LearningGoalResponseDto dto = mapper.toDto(entity);

        assertThat(dto.getMentorId()).isNull();
        assertThat(dto.getMenteeId()).isEqualTo(menteeId);
        assertThat(dto.getGoalTitle()).isEqualTo("Plan Project");
        assertThat(dto.getDescription()).isEqualTo("Outline project steps");
    }
}
