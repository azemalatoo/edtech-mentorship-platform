package alatoo.edu.edtechmentorshipplatform.services;


import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageResponseDto;

import java.util.List;

public interface TutoringPackageService {
    TutoringPackageResponseDto create(TutoringPackageRequestDto dto);
    TutoringPackageResponseDto update(Long id, TutoringPackageRequestDto dto);
    void delete(Long id);
    TutoringPackageResponseDto getById(Long id);
    List<TutoringPackageResponseDto> getAll();
}
