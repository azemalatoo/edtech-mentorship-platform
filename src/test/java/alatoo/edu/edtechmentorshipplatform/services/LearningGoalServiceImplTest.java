package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.LearningGoal;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.LearningGoalMapper;
import alatoo.edu.edtechmentorshipplatform.repo.LearningGoalRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.impl.LearningGoalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningGoalServiceImplTest {

    @Mock private LearningGoalRepo goalRepo;
    @Mock private UserRepo userRepo;
    @Mock private LearningGoalMapper mapper;

    @InjectMocks private LearningGoalServiceImpl service;

    private UUID menteeId;
    private UUID mentorId;
    private User mentee;
    private User mentor;
    private LearningGoalRequestDto requestDto;

    @BeforeEach
    void setUp() {
        menteeId = UUID.randomUUID();
        mentorId = UUID.randomUUID();
        mentee   = User.builder().id(menteeId).build();
        mentor   = User.builder().id(mentorId).build();

        requestDto = new LearningGoalRequestDto();
        requestDto.setMenteeId(menteeId);
        requestDto.setMentorId(mentorId);
        requestDto.setGoalTitle("Title");
        requestDto.setDescription("Desc");
        requestDto.setTargetDate(LocalDate.now().plusDays(7));
    }

    @Test
    void createGoal_withValidMenteeAndMentor_returnsMappedDto() {
        when(userRepo.findById(menteeId)).thenReturn(Optional.of(mentee));
        when(userRepo.findById(mentorId)).thenReturn(Optional.of(mentor));

        LearningGoal toSave = LearningGoal.builder().build();
        when(mapper.toEntity(requestDto, mentee, mentor)).thenReturn(toSave);

        LearningGoal saved = LearningGoal.builder()
                .id(42L)
                .mentee(mentee)
                .mentor(mentor)
                .status(GoalStatus.IN_PROGRESS)
                .build();
        when(goalRepo.save(toSave)).thenReturn(saved);

        LearningGoalResponseDto dto = new LearningGoalResponseDto();
        dto.setId(42L);
        when(mapper.toDto(saved)).thenReturn(dto);

        LearningGoalResponseDto result = service.createGoal(requestDto);

        assertThat(result).isSameAs(dto);
        assertThat(toSave.getStatus()).isEqualTo(GoalStatus.IN_PROGRESS);
    }

    @Test
    void createGoal_withoutMentee_throwsNotFoundException() {
        when(userRepo.findById(menteeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createGoal(requestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Mentee not found");
    }

    @Test
    void updateGoal_whenExisting_updatesOnlySuppliedFields() {
        LearningGoal existing = LearningGoal.builder()
                .id(5L)
                .goalTitle("Old")
                .description("OldDesc")
                .targetDate(LocalDate.now().plusDays(1))
                .progressPercentage(10)
                .progressNotes("n")
                .status(GoalStatus.IN_PROGRESS)
                .build();
        when(goalRepo.findById(5L)).thenReturn(Optional.of(existing));

        LearningGoalRequestDto update = new LearningGoalRequestDto();
        update.setMenteeId(menteeId); // ignored
        update.setGoalTitle("New");
        update.setProgressPercentage(50);
        update.setProgressNotes("note");
        update.setStatus(GoalStatus.PENDING);

        LearningGoal saved = LearningGoal.builder().id(5L).build();
        when(goalRepo.save(existing)).thenReturn(saved);
        LearningGoalResponseDto outDto = new LearningGoalResponseDto();
        when(mapper.toDto(saved)).thenReturn(outDto);

        LearningGoalResponseDto result = service.updateGoal(5L, update);

        assertThat(result).isSameAs(outDto);
        assertThat(existing.getGoalTitle()).isEqualTo("New");
        assertThat(existing.getProgressPercentage()).isEqualTo(50);
        assertThat(existing.getStatus()).isEqualTo(GoalStatus.PENDING);
    }

    @Test
    void updateGoal_withoutExisting_throwsNotFoundException() {
        when(goalRepo.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateGoal(5L, requestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Goal not found");
    }

    @Test
    void markAsAchieved_forExistingGoal_setsAchievedFields() {
        LearningGoal goal = LearningGoal.builder().id(7L).build();
        when(goalRepo.findById(7L)).thenReturn(Optional.of(goal));

        LearningGoal saved = LearningGoal.builder().id(7L).build();
        when(goalRepo.save(goal)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(new LearningGoalResponseDto());

        service.markAsAchieved(7L, "well done");

        assertThat(goal.getIsAchieved()).isTrue();
        assertThat(goal.getStatus()).isEqualTo(GoalStatus.ACHIEVED);
        assertThat(goal.getFeedback()).isEqualTo("well done");
        assertThat(goal.getAchievedAt()).isNotNull();
    }

    @Test
    void markAsAchieved_withoutExisting_throwsNotFoundException() {
        when(goalRepo.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markAsAchieved(7L, "x"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Goal not found");
    }


    @Test
    void updateProgress_atOrAbove100_marksAchieved() {
        LearningGoal goal = LearningGoal.builder().id(9L).build();
        when(goalRepo.findById(9L)).thenReturn(Optional.of(goal));
        when(goalRepo.save(goal)).thenReturn(goal);
        when(mapper.toDto(goal)).thenReturn(new LearningGoalResponseDto());

        service.updateProgress(9L, 100, "done");

        assertThat(goal.getIsAchieved()).isTrue();
        assertThat(goal.getStatus()).isEqualTo(GoalStatus.ACHIEVED);
    }

    @Test
    void updateProgress_withoutExisting_throwsNotFoundException() {
        when(goalRepo.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProgress(10L, 10, ""))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Goal not found");
    }

    @Test
    void changeStatus_toAchieved_onPreviouslyUnachieved_setsAchieved() {
        LearningGoal goal = LearningGoal.builder().id(11L).isAchieved(false).build();
        when(goalRepo.findById(11L)).thenReturn(Optional.of(goal));
        when(goalRepo.save(goal)).thenReturn(goal);
        when(mapper.toDto(goal)).thenReturn(new LearningGoalResponseDto());

        service.changeStatus(11L, GoalStatus.ACHIEVED);

        assertThat(goal.getIsAchieved()).isTrue();
        assertThat(goal.getStatus()).isEqualTo(GoalStatus.ACHIEVED);
        assertThat(goal.getAchievedAt()).isNotNull();
    }

    @Test
    void changeStatus_withoutExisting_throwsNotFoundException() {
        when(goalRepo.findById(12L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.changeStatus(12L, GoalStatus.PENDING))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Goal not found");
    }

    @Test
    void getGoalById_withExisting_returnsDto() {
        LearningGoal goal = new LearningGoal();
        when(goalRepo.findById(13L)).thenReturn(Optional.of(goal));
        LearningGoalResponseDto dto = new LearningGoalResponseDto();
        when(mapper.toDto(goal)).thenReturn(dto);

        assertThat(service.getGoalById(13L)).isSameAs(dto);
    }

    @Test
    void getGoalById_withoutExisting_throwsNotFoundException() {
        when(goalRepo.findById(14L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getGoalById(14L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Goal not found");
    }

    @Test
    void getGoalsByMentee_withResults_returnsAllMappedDtos() {
        UUID mid = UUID.randomUUID();
        LearningGoal g1 = new LearningGoal();
        LearningGoal g2 = new LearningGoal();
        when(goalRepo.findByMentee_Id(mid)).thenReturn(List.of(g1, g2));
        LearningGoalResponseDto d1 = new LearningGoalResponseDto();
        LearningGoalResponseDto d2 = new LearningGoalResponseDto();
        when(mapper.toDto(g1)).thenReturn(d1);
        when(mapper.toDto(g2)).thenReturn(d2);

        assertThat(service.getGoalsByMentee(mid)).containsExactly(d1, d2);
    }

    @Test
    void deleteGoal_whenExists_deletesById() {
        when(goalRepo.existsById(15L)).thenReturn(true);

        service.deleteGoal(15L);

        verify(goalRepo).deleteById(15L);
    }

    @Test
    void deleteGoal_whenNotExists_throwsNotFoundException() {
        when(goalRepo.existsById(16L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteGoal(16L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Goal not found");
    }
}
