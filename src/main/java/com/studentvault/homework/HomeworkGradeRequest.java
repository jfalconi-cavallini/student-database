package com.studentvault.homework;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HomeworkGradeRequest(
        @NotBlank @Size(max = 20) String grade,
        @Size(max = 5_000) String feedback
) {}
