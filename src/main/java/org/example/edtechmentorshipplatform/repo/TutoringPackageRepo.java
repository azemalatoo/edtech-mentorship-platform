package org.example.edtechmentorshipplatform.repo;

import org.example.edtechmentorshipplatform.entity.TutoringPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutoringPackageRepo extends JpaRepository<TutoringPackage, Long> {
}
