package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MenteeProfile;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.exception.ValidationException;
import alatoo.edu.edtechmentorshipplatform.repo.MenteeProfileRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.impl.MenteeProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenteeProfileServiceImplTest {

    @Mock
    private MenteeProfileRepo menteeProfileRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private MenteeProfileServiceImpl service;

    private UUID userId;
    private User user;
    private MenteeProfileRequestDto requestDto;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder().id(userId).build();

        requestDto = new MenteeProfileRequestDto();
        requestDto.setUserID(userId);
        requestDto.setEducationLevel("Bachelor");
        requestDto.setFieldOfStudy("Computer Science");
        requestDto.setCareerGoal("Become a Software Engineer");
        requestDto.setPreferredLanguage(alatoo.edu.edtechmentorshipplatform.enums.Lang.EN);
    }

    @Test
    void createProfile_whenUserNotFound_throwsIllegalArgument() {
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createProfile(requestDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("User not found");
    }

    @Test
    void createProfile_whenProfileAlreadyExists_throwsValidationException() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        MenteeProfile existing = MenteeProfile.builder().id(UUID.randomUUID()).user(user).build();
        when(menteeProfileRepo.findByUserId(userId)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.createProfile(requestDto))
            .isInstanceOf(ValidationException.class)
            .hasMessage("Mentee with userId " + userId + " already exists");
    }

    @Test
    void createProfile_success_persistsAndReturnsDto() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(menteeProfileRepo.findByUserId(userId)).thenReturn(Optional.empty());

        ArgumentCaptor<MenteeProfile> captor = ArgumentCaptor.forClass(MenteeProfile.class);

        UUID generatedId = UUID.randomUUID();
        MenteeProfile savedEntity = MenteeProfile.builder()
            .id(generatedId)
            .user(user)
            .educationLevel(requestDto.getEducationLevel())
            .fieldOfStudy(requestDto.getFieldOfStudy())
            .careerGoal(requestDto.getCareerGoal())
            .preferredLanguage(requestDto.getPreferredLanguage())
            .build();

        when(menteeProfileRepo.save(any())).thenReturn(savedEntity);

        MenteeProfileResponseDto dto = service.createProfile(requestDto);

        verify(menteeProfileRepo).save(captor.capture());
        MenteeProfile toSave = captor.getValue();
        assertThat(toSave.getUser()).isEqualTo(user);
        assertThat(toSave.getEducationLevel()).isEqualTo("Bachelor");
        assertThat(toSave.getFieldOfStudy()).isEqualTo("Computer Science");
        assertThat(toSave.getCareerGoal()).isEqualTo("Become a Software Engineer");
        assertThat(toSave.getPreferredLanguage())
            .isEqualTo(alatoo.edu.edtechmentorshipplatform.enums.Lang.EN);

        assertThat(dto.getId()).isEqualTo(generatedId);
        assertThat(dto.getEducationLevel()).isEqualTo("Bachelor");
        assertThat(dto.getFieldOfStudy()).isEqualTo("Computer Science");
        assertThat(dto.getCareerGoal()).isEqualTo("Become a Software Engineer");
        assertThat(dto.getPreferredLanguage())
            .isEqualTo(alatoo.edu.edtechmentorshipplatform.enums.Lang.EN);
    }

    @Test
    void updateProfile_whenNotFound_throwsIllegalArgument() {
        UUID profileId = UUID.randomUUID();
        when(menteeProfileRepo.findById(profileId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProfile(profileId, requestDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Profile not found");
    }

    @Test
    void updateProfile_success_persistsAndReturnsDto() {
        UUID profileId = UUID.randomUUID();
        MenteeProfile existing = MenteeProfile.builder()
            .id(profileId)
            .user(user)
            .educationLevel("OldLevel")
            .fieldOfStudy("OldField")
            .careerGoal("OldGoal")
            .preferredLanguage(alatoo.edu.edtechmentorshipplatform.enums.Lang.KY)
            .build();

        when(menteeProfileRepo.findById(profileId)).thenReturn(Optional.of(existing));

        MenteeProfile updatedEntity = MenteeProfile.builder()
            .id(profileId)
            .user(user)
            .educationLevel(requestDto.getEducationLevel())
            .fieldOfStudy(requestDto.getFieldOfStudy())
            .careerGoal(requestDto.getCareerGoal())
            .preferredLanguage(requestDto.getPreferredLanguage())
            .build();
        when(menteeProfileRepo.save(existing)).thenReturn(updatedEntity);

        MenteeProfileResponseDto dto = service.updateProfile(profileId, requestDto);

        assertThat(existing.getEducationLevel()).isEqualTo("Bachelor");
        assertThat(existing.getFieldOfStudy()).isEqualTo("Computer Science");
        assertThat(existing.getCareerGoal()).isEqualTo("Become a Software Engineer");
        assertThat(existing.getPreferredLanguage())
            .isEqualTo(alatoo.edu.edtechmentorshipplatform.enums.Lang.EN);

        assertThat(dto.getId()).isEqualTo(profileId);
        assertThat(dto.getEducationLevel()).isEqualTo("Bachelor");
        assertThat(dto.getFieldOfStudy()).isEqualTo("Computer Science");
        assertThat(dto.getCareerGoal()).isEqualTo("Become a Software Engineer");
        assertThat(dto.getPreferredLanguage())
            .isEqualTo(alatoo.edu.edtechmentorshipplatform.enums.Lang.EN);
    }

    @Test
    void getProfileById_whenNotFound_throwsIllegalArgument() {
        UUID profileId = UUID.randomUUID();
        when(menteeProfileRepo.findById(profileId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProfileById(profileId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Profile not found");
    }

    @Test
    void getProfileById_success_returnsDto() {
        UUID profileId = UUID.randomUUID();
        MenteeProfile entity = MenteeProfile.builder()
            .id(profileId)
            .user(user)
            .educationLevel("L")
            .fieldOfStudy("F")
            .careerGoal("G")
            .preferredLanguage(alatoo.edu.edtechmentorshipplatform.enums.Lang.RU)
            .build();

        when(menteeProfileRepo.findById(profileId)).thenReturn(Optional.of(entity));

        MenteeProfileResponseDto dto = service.getProfileById(profileId);

        assertThat(dto.getId()).isEqualTo(profileId);
        assertThat(dto.getEducationLevel()).isEqualTo("L");
        assertThat(dto.getFieldOfStudy()).isEqualTo("F");
        assertThat(dto.getCareerGoal()).isEqualTo("G");
        assertThat(dto.getPreferredLanguage())
            .isEqualTo(alatoo.edu.edtechmentorshipplatform.enums.Lang.RU);
    }

    @Test
    void deleteProfile_whenNotFound_throwsIllegalArgument() {
        UUID profileId = UUID.randomUUID();
        when(menteeProfileRepo.findById(profileId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteProfile(profileId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Profile not found");
    }

    @Test
    void deleteProfile_success_deletesEntity() {
        UUID profileId = UUID.randomUUID();
        MenteeProfile entity = MenteeProfile.builder().id(profileId).build();
        when(menteeProfileRepo.findById(profileId)).thenReturn(Optional.of(entity));

        service.deleteProfile(profileId);

        verify(menteeProfileRepo).delete(entity);
    }

    @Test
    void getAllProfiles_returnsAllMapped() {
        MenteeProfile p1 = MenteeProfile.builder()
            .id(UUID.randomUUID())
            .educationLevel("E1")
            .fieldOfStudy("FS1")
            .careerGoal("CG1")
            .preferredLanguage(alatoo.edu.edtechmentorshipplatform.enums.Lang.EN)
            .build();

        MenteeProfile p2 = MenteeProfile.builder()
            .id(UUID.randomUUID())
            .educationLevel("E2")
            .fieldOfStudy("FS2")
            .careerGoal("CG2")
            .preferredLanguage(alatoo.edu.edtechmentorshipplatform.enums.Lang.RU)
            .build();

        when(menteeProfileRepo.findAll()).thenReturn(List.of(p1, p2));

        List<MenteeProfileResponseDto> all = service.getAllProfiles();

        assertThat(all).hasSize(2);

        assertThat(all).extracting(MenteeProfileResponseDto::getId)
                       .containsExactly(p1.getId(), p2.getId());
        assertThat(all).extracting(MenteeProfileResponseDto::getEducationLevel)
                       .containsExactly("E1", "E2");
        assertThat(all).extracting(MenteeProfileResponseDto::getFieldOfStudy)
                       .containsExactly("FS1", "FS2");
        assertThat(all).extracting(MenteeProfileResponseDto::getCareerGoal)
                       .containsExactly("CG1", "CG2");
        assertThat(all).extracting(MenteeProfileResponseDto::getPreferredLanguage)
                       .containsExactly(alatoo.edu.edtechmentorshipplatform.enums.Lang.EN,
                                        alatoo.edu.edtechmentorshipplatform.enums.Lang.RU);
    }
}
