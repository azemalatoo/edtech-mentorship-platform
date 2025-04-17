package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.TutoringPackage;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.TutoringPackageMapper;
import alatoo.edu.edtechmentorshipplatform.repo.TutoringPackageRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.TutoringPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutoringPackageServiceImpl implements TutoringPackageService {

    private final TutoringPackageRepo repository;
    private final UserRepo userRepository;

    @Override
    public TutoringPackageResponseDto create(TutoringPackageRequestDto dto) {
        User mentor = userRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new NotFoundException("Mentor not found"));

        TutoringPackage tutoringPackage = TutoringPackage.builder()
                .mentor(mentor)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .durationDays(dto.getDurationDays())
                .sessionLimit(dto.getSessionLimit())
                .supportIncluded(dto.isSupportIncluded())
                .status(dto.getStatus())
                .isActive(true)
                .createdAt(java.time.LocalDateTime.now())
                .build();

        TutoringPackage savedPackage = repository.save(tutoringPackage);
        return TutoringPackageMapper.toDto(savedPackage);
    }

    @Override
    public TutoringPackageResponseDto update(UUID id, TutoringPackageRequestDto dto) {
        TutoringPackage existingPackage = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tutoring Package not found"));

        User mentor = userRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new NotFoundException("Mentor not found"));

        existingPackage.setMentor(mentor);
        existingPackage.setTitle(dto.getTitle());
        existingPackage.setDescription(dto.getDescription());
        existingPackage.setPrice(dto.getPrice());
        existingPackage.setDurationDays(dto.getDurationDays());
        existingPackage.setSessionLimit(dto.getSessionLimit());
        existingPackage.setSupportIncluded(dto.isSupportIncluded());
        existingPackage.setStatus(dto.getStatus());
        existingPackage.setIsActive(true); // Active by default on update

        TutoringPackage updatedPackage = repository.save(existingPackage);
        return TutoringPackageMapper.toDto(updatedPackage);
    }

    @Override
    public void delete(UUID id) {
        TutoringPackage existingPackage = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tutoring Package not found"));

        repository.delete(existingPackage);
    }

    @Override
    public TutoringPackageResponseDto getById(UUID id) {
        TutoringPackage tutoringPackage = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tutoring Package not found"));

        return TutoringPackageMapper.toDto(tutoringPackage);
    }

    @Override
    public List<TutoringPackageResponseDto> getAll() {
        List<TutoringPackage> packages = repository.findAll();
        return packages.stream()
                .map(TutoringPackageMapper::toDto)
                .collect(Collectors.toList());
    }
}
