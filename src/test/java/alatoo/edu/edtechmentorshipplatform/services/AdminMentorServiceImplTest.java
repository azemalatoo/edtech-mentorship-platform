package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.enums.ProfileStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.repo.MentorProfileRepo;
import alatoo.edu.edtechmentorshipplatform.services.impl.AdminMentorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminMentorServiceImplTest {

    @Mock
    private MentorProfileRepo repo;

    @InjectMocks
    private AdminMentorServiceImpl service;

    private MentorProfile profile;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        profile = MentorProfile.builder()
                .id(id)
                .profileStatus(ProfileStatus.PENDING)
                .build();
    }

    @Test
    void listPending_shouldReturnPendingProfiles() {
        List<MentorProfile> pending = List.of(profile);
        when(repo.findByProfileStatus(ProfileStatus.PENDING)).thenReturn(pending);

        var result = service.listPending();

        assertThat(result).isSameAs(pending);
        verify(repo).findByProfileStatus(ProfileStatus.PENDING);
    }

    @Test
    void getById_whenFound_shouldReturnProfile() {
        when(repo.findById(id)).thenReturn(Optional.of(profile));

        var result = service.getById(id);

        assertThat(result).isSameAs(profile);
        verify(repo).findById(id);
    }

    @Test
    void getById_whenNotFound_shouldThrowNotFound() {
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Mentor profile not found");

        verify(repo).findById(id);
    }

    @Test
    void approve_shouldSetStatusToApproved_andSave() {
        when(repo.findById(id)).thenReturn(Optional.of(profile));
        when(repo.save(any(MentorProfile.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.approve(id);

        assertThat(result.getProfileStatus()).isEqualTo(ProfileStatus.APPROVED);
        ArgumentCaptor<MentorProfile> captor = ArgumentCaptor.forClass(MentorProfile.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().getProfileStatus()).isEqualTo(ProfileStatus.APPROVED);
    }

    @Test
    void reject_shouldSetStatusToRejected_andSave() {
        when(repo.findById(id)).thenReturn(Optional.of(profile));
        when(repo.save(any(MentorProfile.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.reject(id, "Reason");

        assertThat(result.getProfileStatus()).isEqualTo(ProfileStatus.REJECTED);
        ArgumentCaptor<MentorProfile> captor = ArgumentCaptor.forClass(MentorProfile.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().getProfileStatus()).isEqualTo(ProfileStatus.REJECTED);
    }

    @Test
    void approve_whenNotFound_shouldThrowNotFound() {
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.approve(id))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Mentor profile not found");
        verify(repo).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void reject_whenNotFound_shouldThrowNotFound() {
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.reject(id, "Reason"))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Mentor profile not found");
        verify(repo).findById(id);
        verify(repo, never()).save(any());
    }
}
