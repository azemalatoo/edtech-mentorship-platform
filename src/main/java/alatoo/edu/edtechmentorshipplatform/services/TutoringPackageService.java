package alatoo.edu.edtechmentorshipplatform.services;


import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageResponseDto;

import java.util.List;
import java.util.UUID;

public interface TutoringPackageService {
    TutoringPackageResponseDto create(TutoringPackageRequestDto dto);
    TutoringPackageResponseDto update(UUID id, TutoringPackageRequestDto dto);
    void delete(UUID id);
    TutoringPackageResponseDto getById(UUID id);
    List<TutoringPackageResponseDto> getAll();
}
