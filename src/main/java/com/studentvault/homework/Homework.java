package com.studentvault.homework;

import com.studentvault.common.error.ConflictException;
import com.studentvault.student.Student;
import com.studentvault.tutor.Tutor;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "homework")
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @Column(nullable = false, columnDefinition = "text")
    private String task;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HomeworkStatus status = HomeworkStatus.PENDING;

    @Column(name = "submission_url", length = 2048)
    private String submissionUrl;

    @Column(length = 20)
    private String grade;

    @Column(columnDefinition = "text")
    private String feedback;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Homework() {}

    public Homework(Student student, Tutor tutor, String task, LocalDate assignedDate, LocalDate dueDate) {
        this.student = student;
        this.tutor = tutor;
        this.task = task;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
    }

    // ---- state transitions: the only way to change status ----

    public void updateDetails(String task, LocalDate assignedDate, LocalDate dueDate) {
        if (status != HomeworkStatus.PENDING) {
            throw new ConflictException("Only pending homework can be edited");
        }
        this.task = task;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
    }

    public void submit(String submissionUrl) {
        if (status == HomeworkStatus.COMPLETED) {
            throw new ConflictException("Homework has already been graded");
        }
        this.submissionUrl = submissionUrl;
        this.status = HomeworkStatus.SUBMITTED;
    }

    public void grade(String grade, String feedback) {
        if (status != HomeworkStatus.SUBMITTED) {
            throw new ConflictException("Only submitted homework can be graded");
        }
        this.grade = grade;
        this.feedback = feedback;
        this.status = HomeworkStatus.COMPLETED;
    }

    // ---- getters only: no setStatus() ----

    public UUID getId() { return id; }
    public Student getStudent() { return student; }
    public Tutor getTutor() { return tutor; }
    public String getTask() { return task; }
    public LocalDate getAssignedDate() { return assignedDate; }
    public LocalDate getDueDate() { return dueDate; }
    public HomeworkStatus getStatus() { return status; }
    public String getSubmissionUrl() { return submissionUrl; }
    public String getGrade() { return grade; }
    public String getFeedback() { return feedback; }
    public Instant getCreatedAt() { return createdAt; }
}
