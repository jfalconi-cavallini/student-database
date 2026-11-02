CREATE TABLE homework (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id      UUID          NOT NULL REFERENCES students (id),
    tutor_id        UUID          NOT NULL REFERENCES tutors (id),
    task            TEXT          NOT NULL,
    assigned_date   DATE          NOT NULL,
    due_date        DATE,
    status          VARCHAR(20)   NOT NULL DEFAULT 'PENDING'
                    CHECK (status IN ('PENDING', 'SUBMITTED', 'COMPLETED')),
    submission_url  VARCHAR(2048),
    grade           VARCHAR(20),
    feedback        TEXT,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT chk_homework_due_after_assigned
        CHECK (due_date IS NULL OR due_date >= assigned_date)
);

-- Main query: "this student's homework, optionally filtered by status"
CREATE INDEX idx_homework_student_status ON homework (student_id, status);
