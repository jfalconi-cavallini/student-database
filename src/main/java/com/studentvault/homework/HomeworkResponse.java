package com.studentvault.homework;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record HomeworkResponse(
        UUID id,
        UUID studentId,
        UUID tutorId,
        String task,
        LocalDate assignedDate,
        LocalDate dueDate,
        HomeworkStatus status,
        String submissionUrl,
        String grade,
        String feedback,
        Instant createdAt
) {
    public static HomeworkResponse from(Homework h) {
        return new HomeworkResponse(
                h.getId(),
                h.getStudent().getId(),
                h.getTutor().getId(),
                h.getTask(),
                h.getAssignedDate(),
                h.getDueDate(),
                h.getStatus(),
                h.getSubmissionUrl(),
                h.getGrade(),
                h.getFeedback(),
                h.getCreatedAt());
    }
}
