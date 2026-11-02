package com.studentvault.homework;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record HomeworkRequest(
        @NotNull UUID tutorId,
        @NotBlank @Size(max = 5_000) String task,
        LocalDate assignedDate,
        LocalDate dueDate
) {}
