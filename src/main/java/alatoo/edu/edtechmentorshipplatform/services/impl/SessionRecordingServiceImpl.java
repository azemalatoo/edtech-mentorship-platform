package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.SessionRecording;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.SessionRecordingMapper;
import alatoo.edu.edtechmentorshipplatform.repo.MentorshipSessionRepo;
import alatoo.edu.edtechmentorshipplatform.repo.SessionRecordingRepo;
import alatoo.edu.edtechmentorshipplatform.security.UserDetailsImpl;
import alatoo.edu.edtechmentorshipplatform.services.SessionRecordingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionRecordingServiceImpl implements SessionRecordingService {
    private final SessionRecordingRepo recordingRepo;
    private final MentorshipSessionRepo sessionRepo;
    private final SessionRecordingMapper mapper;

    private UUID getCurrentUserId() {
        UserDetailsImpl userDetails = (UserDetailsImpl)
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser().getId();
    }

    @Override
    public SessionRecordingResponseDto uploadRecording(SessionRecordingRequestDto dto) {
        MentorshipSession session = sessionRepo.findById(dto.getSessionId())
            .orElseThrow(() -> new NotFoundException(
                "Session not found with id " + dto.getSessionId()
            ));
        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot upload recording: session not completed");
        }
        UUID userId = getCurrentUserId();
        if (!session.getMentor().getId().equals(userId)) {
            throw new AccessDeniedException("Only the mentor can upload recordings");
        }
        SessionRecording saved = recordingRepo.save(mapper.toEntity(dto, session));
        return mapper.toDto(saved);
    }

    @Override
    public List<SessionRecordingResponseDto> getRecordingsBySession(Long sessionId) {
        sessionRepo.findById(sessionId)
            .orElseThrow(() -> new NotFoundException(
                "Session not found with id " + sessionId
            ));
        return recordingRepo.findBySessionId(sessionId).stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SessionRecordingResponseDto> getRecordingsForCurrentUser() {
        UUID userId = getCurrentUserId();
        List<SessionRecording> asMentee = recordingRepo.findAllBySession_MenteeId(userId);
        List<SessionRecording> asMentor = recordingRepo.findAllBySession_MentorId(userId);
        return Stream.concat(asMentee.stream(), asMentor.stream())
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteRecording(Long recordingId) {
        SessionRecording rec = recordingRepo.findById(recordingId)
                .orElseThrow(() -> new NotFoundException("Recording not found with id " + recordingId));
        UUID userId = getCurrentUserId();
        if (!rec.getSession().getMentor().getId().equals(userId)) {
            throw new AccessDeniedException("Only the mentor can delete recordings");
        }
        recordingRepo.delete(rec);
    }
}