package com.studentvault.homework;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/students/{studentId}/homework")
public class HomeworkController {

    private final HomeworkService homeworkService;

    public HomeworkController(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    @PostMapping
    public ResponseEntity<HomeworkResponse> assign(@PathVariable UUID studentId,
                                                   @Valid @RequestBody HomeworkRequest request) {
        HomeworkResponse created = homeworkService.assign(studentId, request);
        return ResponseEntity
                .created(URI.create("/api/students/" + studentId + "/homework/" + created.id()))
                .body(created);
    }

    @GetMapping
    public PagedModel<HomeworkResponse> list(
            @PathVariable UUID studentId,
            @RequestParam(required = false) HomeworkStatus status,
            @PageableDefault(size = 20, sort = "dueDate") Pageable pageable) {
        return new PagedModel<>(homeworkService.list(studentId, status, pageable));
    }

    @GetMapping("/{homeworkId}")
    public HomeworkResponse get(@PathVariable UUID studentId, @PathVariable UUID homeworkId) {
        return homeworkService.get(studentId, homeworkId);
    }

    @PutMapping("/{homeworkId}")
    public HomeworkResponse update(@PathVariable UUID studentId, @PathVariable UUID homeworkId,
                                   @Valid @RequestBody HomeworkUpdateRequest request) {
        return homeworkService.update(studentId, homeworkId, request);
    }

    @PostMapping("/{homeworkId}/submit")
    public HomeworkResponse submit(@PathVariable UUID studentId, @PathVariable UUID homeworkId,
                                   @Valid @RequestBody HomeworkSubmitRequest request) {
        return homeworkService.submit(studentId, homeworkId, request);
    }

    @PostMapping("/{homeworkId}/grade")
    public HomeworkResponse grade(@PathVariable UUID studentId, @PathVariable UUID homeworkId,
                                  @Valid @RequestBody HomeworkGradeRequest request) {
        return homeworkService.grade(studentId, homeworkId, request);
    }

    @DeleteMapping("/{homeworkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID studentId, @PathVariable UUID homeworkId) {
        homeworkService.delete(studentId, homeworkId);
    }
}
