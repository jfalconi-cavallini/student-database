package com.studentvault.homework;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HomeworkRepository extends JpaRepository<Homework, UUID> {

    Page<Homework> findByStudentId(UUID studentId, Pageable pageable);

    Page<Homework> findByStudentIdAndStatus(UUID studentId, HomeworkStatus status, Pageable pageable);

    Optional<Homework> findByIdAndStudentId(UUID id, UUID studentId);
}
