package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.session.MentorshipSessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionSlotRequestDto;
import alatoo.edu.edtechmentorshipplatform.entity.MenteeProfile;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.exception.EntityInUseException;
import alatoo.edu.edtechmentorshipplatform.mapper.MentorshipSessionMapper;
import alatoo.edu.edtechmentorshipplatform.repo.MenteeProfileRepo;
import alatoo.edu.edtechmentorshipplatform.repo.MentorshipSessionRepo;
import alatoo.edu.edtechmentorshipplatform.services.MentorshipSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MentorshipSessionServiceImpl implements MentorshipSessionService {
    private final MentorshipSessionRepo repo;
    private final MentorshipSessionMapper mapper;
    private final MenteeProfileRepo menteeProfileRepo;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((alatoo.edu.edtechmentorshipplatform.security.UserDetailsImpl)auth.getPrincipal()).getUser();
    }

    @Override
    public MentorshipSessionResponseDto createSlot(SessionSlotRequestDto dto) {
        User mentor = getCurrentUser();
        MentorshipSession slot = mapper.toSlotEntity(dto, mentor);
        return mapper.toDto(repo.save(slot));
    }

    @Override
    public MentorshipSessionResponseDto updateSlot(Long slotId, SessionSlotRequestDto dto) {
        MentorshipSession slot = repo.findById(slotId)
                .orElseThrow(() -> new NotFoundException("Slot not found with id " + slotId));
        User current = getCurrentUser();
        if (!slot.getMentor().getId().equals(current.getId())) {
            throw new EntityInUseException("Not the owner of this slot");
        }
        if (slot.getStatus() != SessionStatus.AVAILABLE) {
            throw new EntityInUseException("Only available slots can be updated");
        }
        slot.setAvailableFrom(dto.getAvailableFrom());
        slot.setAvailableTo(dto.getAvailableTo());
        slot.setProviderType(dto.getProviderType());
        slot.setMeetingLink(dto.getMeetingLink());
        slot.setNotes(dto.getNotes());
        return mapper.toDto(repo.save(slot));
    }

    @Override
    public List<MentorshipSessionResponseDto> getSlotsByMentorAndStatus(UUID mentorId, SessionStatus status) {
        return repo.findByMentorIdAndStatus(mentorId, status)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MentorshipSessionResponseDto bookSlot(Long slotId) {
        MentorshipSession slot = repo.findById(slotId)
                .orElseThrow(() -> new NotFoundException("Slot not found with id " + slotId));
        if (slot.getStatus() != SessionStatus.AVAILABLE) throw new EntityInUseException("Slot not available");
        User mentee = getCurrentUser();
        slot.setMentee(mentee);
        slot.setStatus(SessionStatus.BOOKED);
        return mapper.toDto(repo.save(slot));
    }

    @Override
    public MentorshipSessionResponseDto startSession(Long sessionId) {
        MentorshipSession s = repo.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found with id " + sessionId));
        if (!getCurrentUser().getId().equals(s.getMentor().getId())) throw new EntityInUseException("Not mentor of this session");
        if (s.getStatus() != SessionStatus.BOOKED) throw new EntityInUseException("Session not booked");
        s.setStartedAt(LocalDateTime.now());
        s.setStatus(SessionStatus.IN_PROGRESS);
        return mapper.toDto(repo.save(s));
    }

    @Override
    public MentorshipSessionResponseDto completeSession(Long sessionId) {
        MentorshipSession s = repo.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found with id " + sessionId));
        if (!getCurrentUser().getId().equals(s.getMentor().getId())) throw new EntityInUseException("Not mentor of this session");
        if (s.getStatus() != SessionStatus.IN_PROGRESS) throw new EntityInUseException("Session not in progress");
        s.setEndedAt(LocalDateTime.now());
        s.setStatus(SessionStatus.COMPLETED);
        return mapper.toDto(repo.save(s));
    }

    @Override
    public MentorshipSessionResponseDto cancelSession(Long sessionId) {
        MentorshipSession s = repo.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found with id " + sessionId));
        if (s.getStatus() == SessionStatus.IN_PROGRESS || s.getStatus() == SessionStatus.COMPLETED)
            throw new EntityInUseException("Cannot cancel after start");
        User u = getCurrentUser();
        if (!u.getId().equals(s.getMentor().getId()) && (s.getMentee() == null || !u.getId().equals(s.getMentee().getId())))
            throw new EntityInUseException("Not participant");
        s.setStatus(SessionStatus.CANCELLED);
        return mapper.toDto(repo.save(s));
    }

    @Override
    public List<MentorshipSessionResponseDto> getSessionsByMentee(SessionStatus status) {
        User user = getCurrentUser();

        MenteeProfile menteeProfile = menteeProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Mentee profile with id %s not found", user.getId())));

        if (status != null) {
            return repo.findByMenteeIdAndStatus(menteeProfile.getId(), status)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }

        return repo.findByMenteeId(menteeProfile.getId())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}