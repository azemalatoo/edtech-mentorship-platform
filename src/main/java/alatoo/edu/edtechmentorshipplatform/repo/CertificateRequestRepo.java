package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.CertificateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRequestRepo extends JpaRepository<CertificateRequest, Long> {
}
