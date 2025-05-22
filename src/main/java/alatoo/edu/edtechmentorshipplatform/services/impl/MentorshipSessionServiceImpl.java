package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.TutoringPackage;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import alatoo.edu.edtechmentorshipplatform.mapper.MentorshipSessionMapper;
import alatoo.edu.edtechmentorshipplatform.repo.MentorshipSessionRepo;
import alatoo.edu.edtechmentorshipplatform.repo.TutoringPackageRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.MentorshipSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MentorshipSessionServiceImpl implements MentorshipSessionService {

    private final MentorshipSessionRepo mentorshipSessionRepository;
    private final UserRepo userRepository;
    private final TutoringPackageRepo tutoringPackageRepository;

    @Autowired
    public MentorshipSessionServiceImpl(MentorshipSessionRepo mentorshipSessionRepository,
                                        UserRepo userRepository,
                                        TutoringPackageRepo tutoringPackageRepository) {
        this.mentorshipSessionRepository = mentorshipSessionRepository;
        this.userRepository = userRepository;
        this.tutoringPackageRepository = tutoringPackageRepository;
    }

    @Override
    public SessionResponseDto createSession(SessionRequestDto createSessionRequestDto) {
        User mentor = userRepository.findById(createSessionRequestDto.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        User mentee = userRepository.findById(createSessionRequestDto.getMenteeId())
                .orElseThrow(() -> new RuntimeException("Mentee not found"));
        TutoringPackage tutoringPackage = tutoringPackageRepository.findById(createSessionRequestDto.getTutoringPackageId())
                .orElseThrow(() -> new RuntimeException("Tutoring package not found"));

        MentorshipSession session = MentorshipSession.builder()
                .mentor(mentor)
                .mentee(mentee)
                .scheduledAt(createSessionRequestDto.getScheduledAt())
                .status(SessionStatus.SCHEDULED)
                .notes(createSessionRequestDto.getNotes())
                .tutoringPackage(tutoringPackage)
                .build();

        MentorshipSession savedSession = mentorshipSessionRepository.save(session);
        return MentorshipSessionMapper.toDto(savedSession);
    }

    @Override
    public SessionResponseDto getSessionById(Long sessionId) {
        MentorshipSession session = mentorshipSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return MentorshipSessionMapper.toDto(session);
    }

    @Override
    public List<SessionResponseDto> getSessionsByMentor(UUID mentorId) {
        List<MentorshipSession> sessions = mentorshipSessionRepository.findByMentorId(mentorId);
        return sessions.stream()
                .map(MentorshipSessionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionResponseDto> getSessionsByMentee(UUID menteeId) {
        List<MentorshipSession> sessions = mentorshipSessionRepository.findByMenteeId(menteeId);
        return sessions.stream()
                .map(MentorshipSessionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SessionResponseDto updateSessionStatus(Long sessionId, String status) {
        MentorshipSession session = mentorshipSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus(SessionStatus.valueOf(status));
        MentorshipSession updatedSession = mentorshipSessionRepository.save(session);
        return MentorshipSessionMapper.toDto(updatedSession);
    }

    @Override
    public void deleteSession(Long sessionId) {
        mentorshipSessionRepository.deleteById(sessionId);
    }
}
