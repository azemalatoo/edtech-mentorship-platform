package org.example.edtechmentorshipplatform.repo;

import org.example.edtechmentorshipplatform.entity.CertificateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRequestRepo extends JpaRepository<CertificateRequest, Long> {
}
