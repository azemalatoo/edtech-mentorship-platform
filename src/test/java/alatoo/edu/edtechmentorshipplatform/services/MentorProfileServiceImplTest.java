package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.Category;
import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import alatoo.edu.edtechmentorshipplatform.enums.ProfileStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.exception.ValidationException;
import alatoo.edu.edtechmentorshipplatform.repo.CategoryRepo;
import alatoo.edu.edtechmentorshipplatform.repo.MentorProfileRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.impl.MentorProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorProfileServiceImplTest {

    @Mock
    private MentorProfileRepo mentorProfileRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private MentorProfileServiceImpl service;

    private UUID userId;
    private UUID profileId;
    private User user;
    private Category category;
    private MentorProfileRequestDto request;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        profileId = UUID.randomUUID();
        user = User.builder().id(userId).build();
        category = Category.builder()
                .id(99L)
                .name("Data")
                .description("desc")
                .build();

        request = new MentorProfileRequestDto();
        request.setUserID(userId);
        request.setBio("My bio");
        request.setHeadline("Expert");
        request.setYearsExperience(5);
        request.setLinkedinUrl("https://linkedin.com/in/expert");
        request.setCertifications(List.of("Cert A", "Cert B"));
        request.setLanguages(List.of(Lang.EN, Lang.RU));
        request.setExpertiseCategoryId(category.getId());
        request.setAverageRating(4.2);
        request.setTotalMentees(10);
    }

    @Test
    void getMentorProfileByUserId_whenNotFound_throws() {
        when(mentorProfileRepo.findByUserId(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getMentorProfileByUserId(userId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Mentor profile not found for user id " + userId);
    }

    @Test
    void getMentorProfileByUserId_whenFound_returnsDto() {
        MentorProfile entity = MentorProfile.builder()
                .id(profileId)
                .user(user)
                .bio("Bio")
                .headline("Head")
                .yearsExperience(3)
                .linkedinUrl("lnk.com")
                .certifications(List.of("C1"))
                .languages(List.of(Lang.KY))
                .expertiseCategory(category)
                .averageRating(3.3)
                .profileStatus(ProfileStatus.APPROVED)
                .build();
        when(mentorProfileRepo.findByUserId(userId)).thenReturn(Optional.of(entity));

        MentorProfileResponseDto dto = service.getMentorProfileByUserId(userId);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(profileId);
        assertThat(dto.getBio()).isEqualTo("Bio");
        assertThat(dto.getHeadline()).isEqualTo("Head");
        assertThat(dto.getYearsExperience()).isEqualTo(3);
        assertThat(dto.getLinkedinUrl()).isEqualTo("lnk.com");
        assertThat(dto.getCertifications()).containsExactly("C1");
        assertThat(dto.getLanguages()).containsExactly(Lang.KY);
        assertThat(dto.getExpertiseCategory().getId()).isEqualTo(category.getId());
        assertThat(dto.getAverageRating()).isEqualTo(3.3);
    }

    @Test
    void createMentorProfile_whenUserNotFound_throws() {
        when(userRepo.findById(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.createMentorProfile(request))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("User not found with ID: " + userId);
    }

    @Test
    void createMentorProfile_whenCategoryNotFound_throws() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(categoryRepo.findById(category.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.createMentorProfile(request))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Category not found with ID: " + category.getId());
    }

    @Test
    void createMentorProfile_whenAlreadyExists_throws() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(categoryRepo.findById(category.getId())).thenReturn(Optional.of(category));
        when(mentorProfileRepo.findByUserId(userId))
            .thenReturn(Optional.of(new MentorProfile()));
        assertThatThrownBy(() -> service.createMentorProfile(request))
            .isInstanceOf(ValidationException.class)
            .hasMessage("Mentor with userId " + userId + " already exists");
    }

    @Test
    void createMentorProfile_success_setsPendingAndReturnsDto() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(categoryRepo.findById(category.getId())).thenReturn(Optional.of(category));
        when(mentorProfileRepo.findByUserId(userId)).thenReturn(Optional.empty());

        ArgumentCaptor<MentorProfile> captor = ArgumentCaptor.forClass(MentorProfile.class);

        MentorProfile saved = MentorProfile.builder()
                .id(profileId)
                .user(user)
                .bio(request.getBio())
                .headline(request.getHeadline())
                .yearsExperience(request.getYearsExperience())
                .linkedinUrl(request.getLinkedinUrl())
                .certifications(request.getCertifications())
                .languages(request.getLanguages())
                .expertiseCategory(category)
                .averageRating(request.getAverageRating())
                .profileStatus(ProfileStatus.PENDING)
                .build();
        when(mentorProfileRepo.save(any())).thenReturn(saved);

        MentorProfileResponseDto dto = service.createMentorProfile(request);

        verify(mentorProfileRepo).save(captor.capture());
        MentorProfile toSave = captor.getValue();
        assertThat(toSave.getProfileStatus()).isEqualTo(ProfileStatus.PENDING);
        assertThat(toSave.getUser()).isEqualTo(user);
        assertThat(toSave.getExpertiseCategory()).isEqualTo(category);

        assertThat(dto.getId()).isEqualTo(profileId);
        assertThat(dto.getBio()).isEqualTo(request.getBio());
        assertThat(dto.getHeadline()).isEqualTo(request.getHeadline());
        assertThat(dto.getYearsExperience()).isEqualTo(request.getYearsExperience());
        assertThat(dto.getAverageRating()).isEqualTo(request.getAverageRating());
    }

    @Test
    void approveMentorProfile_whenNotFound_throws() {
        when(mentorProfileRepo.findById(profileId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.approveMentorProfile(profileId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Mentor Profile not found with ID: " + profileId);
    }

    @Test
    void approveMentorProfile_success_setsApproved() {
        MentorProfile existing = MentorProfile.builder()
                .id(profileId)
                .profileStatus(ProfileStatus.PENDING)
                .build();
        when(mentorProfileRepo.findById(profileId)).thenReturn(Optional.of(existing));

        MentorProfile updated = MentorProfile.builder()
                .id(profileId)
                .profileStatus(ProfileStatus.APPROVED)
                .build();
        when(mentorProfileRepo.save(existing)).thenReturn(updated);

        MentorProfileResponseDto dto = service.approveMentorProfile(profileId);

        assertThat(existing.getProfileStatus()).isEqualTo(ProfileStatus.APPROVED);
        assertThat(dto.getId()).isEqualTo(profileId);
    }

    @Test
    void getAllMentorProfiles_returnsMappedList() {
        MentorProfile m1 = MentorProfile.builder()
                .id(UUID.randomUUID())
                .headline("A")
                .yearsExperience(request.getYearsExperience())
                .averageRating(request.getAverageRating()).build();
        MentorProfile m2 = MentorProfile.builder()
                .id(UUID.randomUUID())
                .headline("B")
                .yearsExperience(request.getYearsExperience())
                .averageRating(request.getAverageRating())
                .build();
        when(mentorProfileRepo.findAll()).thenReturn(List.of(m1, m2));

        var list = service.getAllMentorProfiles();

        assertThat(list).extracting(MentorProfileResponseDto::getId)
                        .containsExactly(m1.getId(), m2.getId());
        assertThat(list).extracting(MentorProfileResponseDto::getHeadline)
                        .containsExactly("A", "B");
    }

    @Test
    void deleteMentorProfile_whenExists_deletes() {
        MentorProfile existing = MentorProfile.builder()
                .id(profileId)
                .yearsExperience(request.getYearsExperience())
                .averageRating(request.getAverageRating())
                .build();
        when(mentorProfileRepo.findById(profileId)).thenReturn(Optional.of(existing));

        service.deleteMentorProfile(profileId);

        verify(mentorProfileRepo).delete(existing);
    }

    @Test
    void updateProfile_whenProfileNotFound_throws() {
        when(mentorProfileRepo.findById(profileId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateProfile(profileId, request))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Mentor profile not found");
    }

    @Test
    void updateProfile_whenUserNotFound_throws() {
        MentorProfile existing = MentorProfile.builder()
                .id(profileId)
                .user(user)
                .build();
        when(mentorProfileRepo.findById(profileId)).thenReturn(Optional.of(existing));
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProfile(profileId, request))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("User not found");
    }

    @Test
    void updateProfile_whenCategoryNotFound_throws() {
        MentorProfile existing = MentorProfile.builder()
                .id(profileId)
                .user(user)
                .build();
        when(mentorProfileRepo.findById(profileId)).thenReturn(Optional.of(existing));
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(categoryRepo.findById(category.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProfile(profileId, request))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Category not found");
    }

    @Test
    void updateProfile_success_savesNewMappedEntity() {
        MentorProfile old = MentorProfile.builder()
                .id(profileId)
                .user(user)
                .build();
        when(mentorProfileRepo.findById(profileId)).thenReturn(Optional.of(old));
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(categoryRepo.findById(category.getId())).thenReturn(Optional.of(category));

        ArgumentCaptor<MentorProfile> captor = ArgumentCaptor.forClass(MentorProfile.class);
        MentorProfile saved = MentorProfile.builder()
                .id(profileId)
                .user(user)
                .bio(request.getBio())
                .headline(request.getHeadline())
                .yearsExperience(request.getYearsExperience())
                .linkedinUrl(request.getLinkedinUrl())
                .certifications(request.getCertifications())
                .languages(request.getLanguages())
                .expertiseCategory(category)
                .averageRating(request.getAverageRating())
                .build();
        when(mentorProfileRepo.save(any())).thenReturn(saved);

        MentorProfileResponseDto dto = service.updateProfile(profileId, request);

        verify(mentorProfileRepo).save(captor.capture());
        MentorProfile toSave = captor.getValue();
        assertThat(toSave.getId()).isEqualTo(profileId);
        assertThat(toSave.getBio()).isEqualTo(request.getBio());
        assertThat(toSave.getExpertiseCategory()).isEqualTo(category);

        assertThat(dto.getId()).isEqualTo(profileId);
        assertThat(dto.getBio()).isEqualTo(request.getBio());
    }

    @Test
    void getProfileById_whenNotFound_throws() {
        when(mentorProfileRepo.findById(profileId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getProfileById(profileId))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Mentor profile not found");
    }

    @Test
    void getProfileById_whenFound_returnsDto() {
        MentorProfile entity = MentorProfile.builder()
                .id(profileId)
                .bio("bbb")
                .headline("hhh")
                .yearsExperience(2)
                .linkedinUrl("u")
                .certifications(List.of("X"))
                .languages(List.of(Lang.KY))
                .expertiseCategory(category)
                .averageRating(1.1)
                .build();
        when(mentorProfileRepo.findById(profileId)).thenReturn(Optional.of(entity));

        MentorProfileResponseDto dto = service.getProfileById(profileId);

        assertThat(dto.getId()).isEqualTo(profileId);
        assertThat(dto.getBio()).isEqualTo("bbb");
        assertThat(dto.getHeadline()).isEqualTo("hhh");
        assertThat(dto.getLanguages()).containsExactly(Lang.KY);
    }
}
