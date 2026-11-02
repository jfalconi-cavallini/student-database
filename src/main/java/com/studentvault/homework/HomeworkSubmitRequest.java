package com.studentvault.homework;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HomeworkSubmitRequest(
        @NotBlank
        @Size(max = 2048)
        @Pattern(regexp = "^https?://\\S+$", message = "must be an http(s) URL")
        String submissionUrl
) {}
