package com.docMind.repository;

import com.docMind.model.AllowedExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllowedExtensionRepository extends JpaRepository<AllowedExtension, Long> {

  List<AllowedExtension> findByActiveTrue();

  Optional<AllowedExtension> findByExtensionAndActiveTrue(String extension);
}
