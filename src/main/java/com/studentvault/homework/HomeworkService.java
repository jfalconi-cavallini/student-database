package com.studentvault.homework;

import com.studentvault.common.error.BadRequestException;
import com.studentvault.common.error.NotFoundException;
import com.studentvault.student.Student;
import com.studentvault.student.StudentRepository;
import com.studentvault.tutor.Tutor;
import com.studentvault.tutor.TutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    public HomeworkService(HomeworkRepository homeworkRepository,
                           StudentRepository studentRepository,
                           TutorRepository tutorRepository) {
        this.homeworkRepository = homeworkRepository;
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
    }

    @Transactional
    public HomeworkResponse assign(UUID studentId, HomeworkRequest request) {
        Student student = findStudentOrThrow(studentId);
        if (student.isArchived()) {
            throw new BadRequestException("Cannot assign homework to an archived student");
        }
        Tutor tutor = tutorRepository.findById(request.tutorId())
                .orElseThrow(() -> new BadRequestException("Tutor does not exist"));
        if (tutor.isArchived()) {
            throw new BadRequestException("Archived tutors cannot assign homework");
        }

        LocalDate assigned = request.assignedDate() != null
                ? request.assignedDate()
                : LocalDate.now(ZoneId.of(student.getTimezone()));
        validateDates(assigned, request.dueDate());

        Homework homework = new Homework(student, tutor, request.task().trim(), assigned, request.dueDate());
        return HomeworkResponse.from(homeworkRepository.save(homework));
    }

    @Transactional(readOnly = true)
    public Page<HomeworkResponse> list(UUID studentId, HomeworkStatus status, Pageable pageable) {
        findStudentOrThrow(studentId);
        Page<Homework> page = (status == null)
                ? homeworkRepository.findByStudentId(studentId, pageable)
                : homeworkRepository.findByStudentIdAndStatus(studentId, status, pageable);
        return page.map(HomeworkResponse::from);
    }

    @Transactional(readOnly = true)
    public HomeworkResponse get(UUID studentId, UUID homeworkId) {
        return HomeworkResponse.from(findOrThrow(studentId, homeworkId));
    }

    @Transactional
    public HomeworkResponse update(UUID studentId, UUID homeworkId, HomeworkUpdateRequest request) {
        validateDates(request.assignedDate(), request.dueDate());
        Homework homework = findOrThrow(studentId, homeworkId);
        homework.updateDetails(request.task().trim(), request.assignedDate(), request.dueDate());
        return HomeworkResponse.from(homework);
    }

    @Transactional
    public HomeworkResponse submit(UUID studentId, UUID homeworkId, HomeworkSubmitRequest request) {
        Homework homework = findOrThrow(studentId, homeworkId);
        homework.submit(request.submissionUrl().trim());
        return HomeworkResponse.from(homework);
    }

    @Transactional
    public HomeworkResponse grade(UUID studentId, UUID homeworkId, HomeworkGradeRequest request) {
        Homework homework = findOrThrow(studentId, homeworkId);
        homework.grade(request.grade().trim(), request.feedback());
        return HomeworkResponse.from(homework);
    }

    @Transactional
    public void delete(UUID studentId, UUID homeworkId) {
        homeworkRepository.delete(findOrThrow(studentId, homeworkId));
    }

    private static void validateDates(LocalDate assigned, LocalDate due) {
        if (due != null && due.isBefore(assigned)) {
            throw new BadRequestException("Due date cannot be before the assigned date");
        }
    }

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    private Homework findOrThrow(UUID studentId, UUID homeworkId) {
        return homeworkRepository.findByIdAndStudentId(homeworkId, studentId)
                .orElseThrow(() -> new NotFoundException("Homework not found"));
    }
}
