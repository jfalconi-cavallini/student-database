package com.studentvault.homework;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record HomeworkUpdateRequest(
        @NotBlank @Size(max = 5_000) String task,
        @NotNull LocalDate assignedDate,
        LocalDate dueDate
) {}
